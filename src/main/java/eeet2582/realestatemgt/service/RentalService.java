package eeet2582.realestatemgt.service;

import eeet2582.realestatemgt.model.Payment;
import eeet2582.realestatemgt.model.Rental;
import eeet2582.realestatemgt.repository.PaymentRepository;
import eeet2582.realestatemgt.repository.RentalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public Rental getRentalById(Long rentalId) {
        return rentalRepository.findById(rentalId).orElseThrow(() -> new IllegalStateException("Rental with rentalId=" + rentalId + " does not exist!"));
    }

    @Transactional
    public void deleteRentalById(Long rentalId) {
        if (!rentalRepository.existsById(rentalId))
            throw new IllegalStateException("Rental with rentalId=" + rentalId + " does not exist!");

        rentalRepository.deleteById(rentalId);
    }

    public void deleteRentalsByUserId(Long userId) {
        rentalRepository.deleteByUserHouse_UserId(userId);
    }

    public void deleteRentalsByHouseId(Long houseId) {
        rentalRepository.deleteByUserHouse_HouseId(houseId);
    }

    // Payment
    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    public void deletePaymentById(Long paymentId) {
        if (!paymentRepository.existsById(paymentId))
            throw new IllegalStateException("Payment with paymentId=" + paymentId + " does not exist!");

        paymentRepository.deleteById(paymentId);
    }

    public List<Payment> getPaymentsByRentalId(Long rentalId) {
        if (!rentalRepository.existsById(rentalId))
            throw new IllegalStateException("Rental with rentalId=" + rentalId + " does not exist!");

        return paymentRepository.findByRental_RentalId(rentalId);
    }
}