package eeet2582.realestatemgt.service;

import eeet2582.realestatemgt.model.AppUser;
import eeet2582.realestatemgt.model.House;
import eeet2582.realestatemgt.model.Payment;
import eeet2582.realestatemgt.model.Rental;
import eeet2582.realestatemgt.model.form.RentalForm;
import eeet2582.realestatemgt.repository.PaymentRepository;
import eeet2582.realestatemgt.repository.RentalRepository;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

import static eeet2582.realestatemgt.config.RedisConfig.*;

// Handle Rental and Payment operations
@Service
@RequiredArgsConstructor
public class RentalService {

    @Autowired
    private final UserHouseLocationUtil userHouseLocationUtil;

    @Autowired
    private final RentalRepository rentalRepository;

    @Autowired
    private final PaymentRepository paymentRepository;

    DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Cacheable(value = RENTALS)
    public List<Rental> getAllRentals() {
        return rentalRepository.findAll();
    }

    // Return paginated rentals
    @Cacheable(value = RENTAL_SEARCH)
    public Page<Rental> getFilteredRentalsAllOrByUserIdOrByHouseId(Long userId, Long houseId, int pageNo, int pageSize, String sortBy, String orderBy) {
        Pageable pageable;

        if (orderBy.equals("asc")) {
            pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).ascending());
        } else {
            pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending());
        }

        if (userId != null) {
            // Try to find the associated user
            AppUser user = userHouseLocationUtil.getUserById(userId);
            return rentalRepository.findByUser(user, pageable);
        } else if (houseId != null) {
            // Try to find the associated house
            House house = userHouseLocationUtil.getHouseById(houseId);
            return rentalRepository.findByHouse(house, pageable);
        } else {
            return rentalRepository.findAll(pageable);
        }
    }

    // Get one by ID, try to reuse the exception
    @Cacheable(key = RENTAL_ID, value = RENTAL)
    public Rental getRentalById(Long rentalId) {
        return rentalRepository.findById(rentalId).orElseThrow(() -> new IllegalStateException("Rental with rentalId=" + rentalId + " does not exist!"));
    }

    // Add new one
    @Caching(evict = {
            @CacheEvict(value = RENTALS, allEntries = true),
            @CacheEvict(value = RENTAL_SEARCH, allEntries = true)
    })
    public Rental addNewRental(@NotNull RentalForm form) {
        if (form.getUserId() != null && form.getHouseId() != null) {
            // Find the associated user and house
            AppUser user = userHouseLocationUtil.getUserById(form.getUserId());
            House house = userHouseLocationUtil.getHouseById(form.getHouseId());

            // Create new entity from param
            Rental rental = new Rental(
                    user,
                    house,
                    LocalDate.parse(form.getStartDate(), dateFormat),
                    LocalDate.parse(form.getEndDate(), dateFormat),
                    form.getDepositAmount(),
                    form.getMonthlyFee(),
                    form.getPayableFee()
            );
            return rentalRepository.save(rental);
        }
        return null;
    }

    // Transactional means "all or nothing", if the transaction fails midway nothing is saved
    @Transactional
    @Caching(evict = {
            @CacheEvict(value = RENTAL, key = RENTAL_ID),
            @CacheEvict(value = RENTALS, allEntries = true),
            @CacheEvict(value = RENTAL_SEARCH, allEntries = true)
    })
    public Rental updateRentalById(Long rentalId, @NotNull RentalForm form) {
        if (form.getUserId() != null && form.getHouseId() != null) {
            Rental rental = getRentalById(rentalId);

            // Find the associated user and house
            AppUser user = userHouseLocationUtil.getUserById(form.getUserId());
            House house = userHouseLocationUtil.getHouseById(form.getHouseId());
            rental.setUser(user);
            rental.setHouse(house);

            if (form.getStartDate() != null && !rental.getStartDate()
                    .isEqual(LocalDate.parse(form.getStartDate(), dateFormat))) {
                rental.setStartDate(LocalDate.parse(form.getStartDate(), dateFormat));
            }

            if (form.getEndDate() != null && !rental.getEndDate()
                    .isEqual(LocalDate.parse(form.getEndDate(), dateFormat))) {
                rental.setEndDate(LocalDate.parse(form.getEndDate(), dateFormat));
            }

            if (form.getDepositAmount() != null && !Objects.equals(form.getDepositAmount(), rental.getDepositAmount())) {
                rental.setDepositAmount(form.getDepositAmount());
            }

            if (form.getMonthlyFee() != null && !Objects.equals(form.getMonthlyFee(), rental.getMonthlyFee())) {
                rental.setMonthlyFee(form.getMonthlyFee());
            }

            if (form.getPayableFee() != null && !Objects.equals(form.getPayableFee(), rental.getPayableFee())) {
                rental.setPayableFee(form.getPayableFee());
            }
            return rentalRepository.save(rental);
        }
        return null;
    }

    // Delete one
    @Transactional
    @Caching(evict = {
            @CacheEvict(value = RENTAL, key = RENTAL_ID),
            @CacheEvict(value = RENTALS, allEntries = true),
            @CacheEvict(value = RENTAL_SEARCH, allEntries = true)
    })
    public void deleteRentalById(Long rentalId) {
        Rental rental = getRentalById(rentalId);

        // With orphan remover, no need to explicitly delete child Payments
        rentalRepository.delete(rental);
    }

    // --- PAYMENT --- //
    @Cacheable(value = PAYMENTS)
    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    // Return paginated payments of the provided rental or just all payments
    @Cacheable(value = PAYMENT_SEARCH_BY_RENTAL)
    public Page<Payment> getFilteredPaymentsAllOrByRentalId(Long rentalId, int pageNo, int pageSize, String sortBy, @NotNull String orderBy) {
        Pageable pageable;
        if (orderBy.equals("asc")) {
            pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).ascending());
        } else {
            pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending());
        }

        if (rentalId != null) {
            return paymentRepository.findByRental_RentalId(rentalId, pageable);
        } else {
            return paymentRepository.findAll(pageable);
        }
    }

    // Return paginated payments by userId
    @Cacheable(value = PAYMENT_SEARCH_BY_USER)
    public Page<Payment> getFilteredPaymentsByUserId(Long userId, int pageNo, int pageSize, String sortBy, @NotNull String orderBy) {
        Pageable pageable;
        if (orderBy.equals("asc")) {
            pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).ascending());
        } else {
            pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending());
        }
        if (userId != null) {
            return paymentRepository.findPaymentByUserId(userId, pageable);
        } else {
            return paymentRepository.findAll(pageable);
        }
    }

    // Get one by ID, try to reuse the exception
    public Payment getPaymentById(Long paymentId) {
        if (!paymentRepository.existsById(paymentId))
            throw new IllegalStateException("Payment with paymentId=" + paymentId + " does not exist!");

        return paymentRepository.getById(paymentId);
    }

    // Add new one
    @Transactional
    @Caching(evict = {
            @CacheEvict(value = PAYMENTS, allEntries = true),
            @CacheEvict(value = PAYMENT_SEARCH_BY_RENTAL, allEntries = true),
            @CacheEvict(value = PAYMENT_SEARCH_BY_USER, allEntries = true)
    })
    public Payment addNewPaymentByRentalId(Long rentalId, @NotNull Payment payment) {
        // Find the associated rental
        Rental rental = getRentalById(rentalId);
        payment.setRental(rental);
        return paymentRepository.save(payment);
    }

    // Transactional means "all or nothing", if the transaction fails midway nothing is saved
    @Transactional
    @Caching(evict = {
            @CacheEvict(value = PAYMENT, key = PAYMENT_ID),
            @CacheEvict(value = PAYMENTS, allEntries = true),
            @CacheEvict(value = PAYMENT_SEARCH_BY_RENTAL, allEntries = true),
            @CacheEvict(value = PAYMENT_SEARCH_BY_USER, allEntries = true)
    })
    public Payment updatePaymentById(Long paymentId, Payment newPayment) {
        Payment oldPayment = getPaymentById(paymentId);

        // Do input checking here
        if (oldPayment.getAmount() != null && !Objects.equals(newPayment.getAmount(), oldPayment.getAmount())) {
            oldPayment.setAmount(newPayment.getAmount());
        }

        if (newPayment.getDate() != null && !oldPayment.getDate().isEqual(newPayment.getDate())) {
            oldPayment.setDate(newPayment.getDate());
        }

        if (newPayment.getTime() != null && !oldPayment.getTime().equals(newPayment.getTime())) {
            oldPayment.setTime(newPayment.getTime());
        }

        if (newPayment.getNote() != null && !newPayment.getNote().isBlank() && !oldPayment.getNote().equals(newPayment.getNote())) {
            oldPayment.setNote(newPayment.getNote());
        }

        return oldPayment;
    }

    // Delete one
    @Caching(evict = {
            @CacheEvict(value = PAYMENT, key = PAYMENT_ID),
            @CacheEvict(value = PAYMENTS, allEntries = true),
            @CacheEvict(value = PAYMENT_SEARCH_BY_RENTAL, allEntries = true),
            @CacheEvict(value = PAYMENT_SEARCH_BY_USER, allEntries = true)
    })
    public void deletePaymentById(Long paymentId) {
        Payment payment = getPaymentById(paymentId);
        paymentRepository.delete(payment);
    }
}