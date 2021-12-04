package eeet2582.realestatemgt.controller;

import eeet2582.realestatemgt.model.Payment;
import eeet2582.realestatemgt.model.Rental;
import eeet2582.realestatemgt.service.RentalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Handle Rental and Payment operations
@RestController
@RequestMapping("api/v1")
public class RentalController {

    private final RentalService rentalService;

    @Autowired
    public RentalController(RentalService rentalService) {
        this.rentalService = rentalService;
    }

    @GetMapping("/rentals")
    public List<Rental> getAllRentals() {
        return rentalService.getAllRentals();
    }

    @GetMapping("/rental/{rentalId}")
    public Rental getRentalById(@PathVariable("rentalId") Long rentalId) {
        return rentalService.getRentalById(rentalId);
    }

    @DeleteMapping("/rental/{rentalId}")
    public void deleteRentalById(@PathVariable("rentalId") Long rentalId) { rentalService.deleteRentalById(rentalId); }

    @GetMapping("/payments")
    public List<Payment> getAllPayments() {
        return rentalService.getAllPayments();
    }

    @DeleteMapping("/payment/{paymentId}")
    public void deletePaymentById(@PathVariable("paymentId") Long paymentId) { rentalService.deletePaymentById(paymentId); }

    @GetMapping("/payments/byRental/{rentalId}")
    public List<Payment> getPaymentsByRentalId(@PathVariable("rentalId") Long rentalId) {
        return rentalService.getPaymentsByRentalId(rentalId);
    }
}
