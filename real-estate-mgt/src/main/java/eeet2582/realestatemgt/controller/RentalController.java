package eeet2582.realestatemgt.controller;

import eeet2582.realestatemgt.model.Payment;
import eeet2582.realestatemgt.model.Rental;
import eeet2582.realestatemgt.service.RentalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Handle Rental and Payment operations
// Implemented: (Rental) get all, get one, get by userHouse, add, update, delete
// Implemented: (Payment) get all, get one, get all by rentalId, add update by rentalId, delete
@RestController
@RequestMapping("api/v1")
public class RentalController {

    private final RentalService rentalService;

    @Autowired
    public RentalController(RentalService rentalService) {
        this.rentalService = rentalService;
    }

    // Just get all (for debug)
    @GetMapping("/rentals")
    public List<Rental> getAllRentals() {
        return rentalService.getAllRentals();
    }

    // Return rentals with sort, order and pagination (no query)
    // Params aren't mandatory, if not provided will use defaults
    @GetMapping("/rentals/search")
    public Page<Rental> getFilteredRentals(@RequestParam(value = "pageNo", defaultValue = "0") int pageNo,
                                           @RequestParam(value = "pageSize", defaultValue = "5") int pageSize,
                                           @RequestParam(value = "sortBy", defaultValue = "startDate") String sortBy,
                                           @RequestParam(value = "orderBy", defaultValue = "desc") String orderBy) {
        return rentalService.getFilteredRentalsAllOrByUserIdOrByHouseId(null, null, pageNo, pageSize, sortBy, orderBy);
    }

    // Return rentals with sort, order and pagination (no query) by userId
    // Params aren't mandatory, if not provided will use defaults
    @GetMapping("/rentals/search/byUser/{userId}")
    public Page<Rental> getFilteredRentalsByUserId(@PathVariable("userId") Long userId,
                                                   @RequestParam(value = "pageNo", defaultValue = "0") int pageNo,
                                                   @RequestParam(value = "pageSize", defaultValue = "5") int pageSize,
                                                   @RequestParam(value = "sortBy", defaultValue = "startDate") String sortBy,
                                                   @RequestParam(value = "orderBy", defaultValue = "desc") String orderBy) {
        return rentalService.getFilteredRentalsAllOrByUserIdOrByHouseId(userId, null, pageNo, pageSize, sortBy, orderBy);
    }

    // Return rentals with sort, order and pagination (no query) by houseId
    // Params aren't mandatory, if not provided will use defaults
    @GetMapping("/rentals/search/byHouse/{houseId}")
    public Page<Rental> getFilteredRentalsByHouseId(@PathVariable("houseId") Long houseId,
                                                    @RequestParam(value = "pageNo", defaultValue = "0") int pageNo,
                                                    @RequestParam(value = "pageSize", defaultValue = "5") int pageSize,
                                                    @RequestParam(value = "sortBy", defaultValue = "startDate") String sortBy,
                                                    @RequestParam(value = "orderBy", defaultValue = "desc") String orderBy) {
        return rentalService.getFilteredRentalsAllOrByUserIdOrByHouseId(null, houseId, pageNo, pageSize, sortBy, orderBy);
    }

    // Get one by ID
    @GetMapping("/rentals/{rentalId}")
    public Rental getRentalById(@PathVariable("rentalId") Long rentalId) {
        return rentalService.getRentalById(rentalId);
    }

    // Update one by ID or add new one
    @PostMapping("/rentals")
    public void saveRentalById(@RequestParam(value = "rentalId", required = false) Long rentalId,
                               @RequestParam Long userId,
                               @RequestParam Long houseId,
                               @RequestParam String startDate,
                               @RequestParam String endDate,
                               @RequestParam Double depositAmount,
                               @RequestParam Double monthlyFee,
                               @RequestParam Double payableFee) {
        rentalService.saveRentalById(rentalId, userId, houseId, startDate, endDate, depositAmount, monthlyFee, payableFee);
    }

    // Delete one by ID
    @DeleteMapping("/rentals/{rentalId}")
    public void deleteRentalById(@PathVariable("rentalId") Long rentalId) {
        rentalService.deleteRentalById(rentalId);
    }

    // --- PAYMENT --- //

    // Just get all (for debug)
    @GetMapping("/payments")
    public List<Payment> getAllPayments() {
        return rentalService.getAllPayments();
    }

    // Return payments with sort, order and pagination (NO rentalId)
    // Params aren't mandatory, if not provided will use defaults
    @GetMapping("/payments/search")
    public Page<Payment> getFilteredPayments(@RequestParam(value = "pageNo", defaultValue = "0") int pageNo,
                                             @RequestParam(value = "pageSize", defaultValue = "5") int pageSize,
                                             @RequestParam(value = "sortBy", defaultValue = "date") String sortBy,
                                             @RequestParam(value = "orderBy", defaultValue = "desc") String orderBy) {
        return rentalService.getFilteredPaymentsAllOrByRentalId(null, pageNo, pageSize, sortBy, orderBy);
    }

    // Return payments with sort, order and pagination (MUST HAVE rentalId)
    // Params aren't mandatory, if not provided will use defaults
    @GetMapping("/payments/search/byRental/{rentalId}")
    public Page<Payment> getFilteredPaymentsByRentalId(@PathVariable Long rentalId,
                                                       @RequestParam(value = "pageNo", defaultValue = "0") int pageNo,
                                                       @RequestParam(value = "pageSize", defaultValue = "5") int pageSize,
                                                       @RequestParam(value = "sortBy", defaultValue = "date") String sortBy,
                                                       @RequestParam(value = "orderBy", defaultValue = "desc") String orderBy) {
        return rentalService.getFilteredPaymentsAllOrByRentalId(rentalId, pageNo, pageSize, sortBy, orderBy);
    }

    // Get one by ID
    @GetMapping("/payments/{paymentId}")
    public Payment getPaymentById(@PathVariable("paymentId") Long paymentId) {
        return rentalService.getPaymentById(paymentId);
    }

    // Update one by ID or add new one (MUST HAVE rentalId)
    @PostMapping("/payments/byRental/{rentalId}")
    public void savePaymentById(@PathVariable Long rentalId,
                                @RequestParam(value = "paymentId", required = false) Long paymentId,
                                @RequestParam Double amount,
                                @RequestParam String date,
                                @RequestParam String time,
                                @RequestParam String note) {
        rentalService.savePaymentById(rentalId, paymentId, amount, date, time, note);
    }

    // Delete one by ID
    @DeleteMapping("/payments/{paymentId}")
    public void deletePaymentById(@PathVariable("paymentId") Long paymentId) {
        rentalService.deletePaymentById(paymentId);
    }
}
