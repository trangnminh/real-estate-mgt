package eeet2582.realestatemgt.service;

import eeet2582.realestatemgt.helper.UserHouse;
import eeet2582.realestatemgt.model.Payment;
import eeet2582.realestatemgt.model.Rental;
import eeet2582.realestatemgt.repository.PaymentRepository;
import eeet2582.realestatemgt.repository.RentalRepository;
import eeet2582.realestatemgt.repository.UserRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

// Handle Rental and Payment operations
@Service
public class RentalService {

    @Autowired
    private final RentalRepository rentalRepository;

    @Autowired
    private final PaymentRepository paymentRepository;

    @Autowired
    private final UserRepository userRepository;

    public RentalService(RentalRepository rentalRepository, PaymentRepository paymentRepository, UserRepository userRepository) {
        this.rentalRepository = rentalRepository;
        this.paymentRepository = paymentRepository;
        this.userRepository = userRepository;
    }

    public List<Rental> getAllRentals() {
        return rentalRepository.findAll();
    }

    // Return paginated rentals
    public Page<Rental> getFilteredRentalsAllOrByUserIdOrByHouseId(Long userId, Long houseId, int pageNo, int pageSize, String sortBy, String orderBy) {
        // convert auth0Id user to simpler userId in database
        Long auth0Id = userRepository.checkAuthUserFound(userId);
        userId = auth0Id != null ? auth0Id : userId;

        Pageable pageable;

        if (orderBy.equals("asc")) {
            pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).ascending());
        } else {
            pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending());
        }

        if (userId != null) {
            return rentalRepository.findByUserHouse_UserId(userId, pageable);
        } else if (houseId != null) {
            return rentalRepository.findByUserHouse_HouseId(houseId, pageable);
        } else {
            return rentalRepository.findAll(pageable);
        }
    }

    // Get one by ID, try to reuse the exception
    public Rental getRentalById(Long rentalId) {
        return rentalRepository.findById(rentalId).orElseThrow(() -> new IllegalStateException("Rental with rentalId=" + rentalId + " does not exist!"));
    }

    public Rental addNewRental(Rental rental) {
        return rentalRepository.save(rental);
    }

    // Transactional means "all or nothing", if the transaction fails midway nothing is saved
    @Transactional
    public Rental updateRentalById(Long rentalId, @NotNull Rental newRental) {
        Rental oldRental = getRentalById(rentalId);

        if (newRental.getUserHouse().getHouseId() != null && newRental.getUserHouse().getUserId() != null) {
            oldRental.setUserHouse(new UserHouse(newRental.getUserHouse().getUserId(), newRental.getUserHouse().getHouseId()));
        }

        if (newRental.getStartDate() != null && !oldRental.getStartDate().isEqual(newRental.getStartDate())) {
            oldRental.setStartDate(newRental.getStartDate());
        }

        if (newRental.getEndDate() != null && !oldRental.getEndDate().isEqual(newRental.getEndDate())) {
            oldRental.setEndDate(newRental.getEndDate());
        }

        if (newRental.getDepositAmount() != null && !Objects.equals(newRental.getDepositAmount(), oldRental.getDepositAmount())) {
            oldRental.setDepositAmount(newRental.getDepositAmount());
        }

        if (newRental.getMonthlyFee() != null && !Objects.equals(newRental.getMonthlyFee(), oldRental.getMonthlyFee())) {
            oldRental.setMonthlyFee(newRental.getMonthlyFee());
        }

        if (newRental.getPayableFee() != null && !Objects.equals(newRental.getPayableFee(), oldRental.getPayableFee())) {
            oldRental.setPayableFee(newRental.getPayableFee());
        }

        return oldRental;
    }

    @Transactional
    public void deleteRentalById(Long rentalId) {
        Rental rental = getRentalById(rentalId);

        // With orphan remover, no need to explicitly delete child Payments
        rentalRepository.delete(rental);
    }

    // Delete all rentals having the same userId
    @Transactional
    public void deleteRentalsByUserId(Long userId) {
        // convert auth0Id user to simpler userId in database
        Long auth0Id = userRepository.checkAuthUserFound(userId);
        userId = auth0Id != null ? auth0Id : userId;
        rentalRepository.deleteByUserHouse_UserId(userId);
    }

    // Delete all rentals having the same houseId
    @Transactional
    public void deleteRentalsByHouseId(Long houseId) {
        rentalRepository.deleteByUserHouse_HouseId(houseId);
    }

    // --- PAYMENT --- //
    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    // Return paginated payments of the provided rental or just all payments
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
    public Page<Payment> getFilteredPaymentsByUserId(Long userId, int pageNo, int pageSize, String sortBy, @NotNull String orderBy) {
        // convert auth0Id user to simpler userId in database
        Long auth0Id = userRepository.checkAuthUserFound(userId);
        userId = auth0Id != null ? auth0Id : userId;
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

    @Transactional
    public Payment addNewPaymentByRentalId(Long rentalId, @NotNull Payment payment) {
        // Find the associated rental
        Rental rental = getRentalById(rentalId);
        payment.setRental(rental);
        return paymentRepository.save(payment);
    }

    // Transactional means "all or nothing", if the transaction fails midway nothing is saved
    @Transactional
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

    public void deletePaymentById(Long paymentId) {
        Payment payment = getPaymentById(paymentId);
        paymentRepository.delete(payment);
    }
}