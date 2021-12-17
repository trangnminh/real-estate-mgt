package eeet2582.realestatemgt.service;

import eeet2582.realestatemgt.helper.UserHouse;
import eeet2582.realestatemgt.model.Payment;
import eeet2582.realestatemgt.model.Rental;
import eeet2582.realestatemgt.repository.PaymentRepository;
import eeet2582.realestatemgt.repository.RentalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

// Handle Rental and Payment operations
@Service
public class RentalService {

    @Autowired
    private final RentalRepository rentalRepository;

    @Autowired
    private final PaymentRepository paymentRepository;

    public RentalService(RentalRepository rentalRepository, PaymentRepository paymentRepository) {
        this.rentalRepository = rentalRepository;
        this.paymentRepository = paymentRepository;
    }

    public List<Rental> getAllRentals() {
        return rentalRepository.findAll();
    }

    // Return paginated rentals
    public Page<Rental> getFilteredRentals(int pageNo, int pageSize, String sortBy, String orderBy) {
        Pageable pageable;
        if (orderBy.equals("asc")) {
            pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).ascending());
        } else {
            pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending());
        }
        return rentalRepository.findAll(pageable);
    }

    // Get one by ID, try to reuse the exception
    public Rental getRentalById(Long rentalId) {
        return rentalRepository.findById(rentalId).orElseThrow(() -> new IllegalStateException("Rental with rentalId=" + rentalId + " does not exist!"));
    }

    // Transactional means "all or nothing", if the transaction fails midway nothing is saved
    @Transactional
    public void saveRentalById(Long rentalId, Long userId, Long houseId, String startDate, String endDate, Double depositAmount, Double monthlyFee, Double payableFee) {
        // If ID is provided, try to find the current item, else make new one
        Rental rental = (rentalId != null) ? getRentalById(rentalId) : new Rental();

        // Do input checking here

        // Save the cleaned item
        rental.setUserHouse(new UserHouse(userId, houseId));
        rental.setStartDate(LocalDate.parse(startDate, DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        rental.setEndDate(LocalDate.parse(endDate, DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        rental.setDepositAmount(depositAmount);
        rental.setMonthlyFee(monthlyFee);
        rental.setPayableFee(payableFee);
        rentalRepository.save(rental);
    }

    @Transactional
    public void deleteRentalById(Long rentalId) {
        Rental rental = getRentalById(rentalId);

        // With orphan remover, no need to explicitly delete child Payments
        rentalRepository.delete(rental);
    }

    // Delete all rentals having the same userId
    public void deleteRentalsByUserId(Long userId) {
        rentalRepository.deleteByUserHouse_UserId(userId);
    }

    // Delete all rentals having the same houseId
    public void deleteRentalsByHouseId(Long houseId) {
        rentalRepository.deleteByUserHouse_HouseId(houseId);
    }

    // --- PAYMENT --- //
    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    // Return paginated payments of the provided rental
    public Page<Payment> getFilteredPaymentsByRentalId(Long rentalId, int pageNo, int pageSize, String sortBy, String orderBy) {
        Pageable pageable;
        if (orderBy.equals("asc")) {
            pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).ascending());
        } else {
            pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending());
        }
        return paymentRepository.findByRental_RentalId(rentalId, pageable);
    }

    // Get one by ID, try to reuse the exception
    public Payment getPaymentById(Long paymentId) {
        if (!paymentRepository.existsById(paymentId))
            throw new IllegalStateException("Payment with paymentId=" + paymentId + " does not exist!");

        return paymentRepository.getById(paymentId);
    }

    // Transactional means "all or nothing", if the transaction fails midway nothing is saved
    @Transactional
    public void savePaymentById(Long rentalId, Long paymentId, Double amount, String date, String time, String note) {
        // Find the associated rental
        Rental rental = getRentalById(rentalId);

        // If ID is provided, try to find the current item, else make new one
        Payment payment = (paymentId != null) ? getPaymentById(paymentId) : new Payment();

        // Do input checking here

        // Save the cleaned item
        payment.setAmount(amount);
        payment.setDate(LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        payment.setTime(LocalTime.parse(time, DateTimeFormatter.ofPattern("HH:mm")));
        payment.setNote(note);

        payment.setRental(rental);
        paymentRepository.save(payment);
    }

    public void deletePaymentById(Long paymentId) {
        Payment payment = getPaymentById(paymentId);
        paymentRepository.delete(payment);
    }
}