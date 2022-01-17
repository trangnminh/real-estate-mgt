package eeet2582.realestatemgt.controller;

import eeet2582.realestatemgt.model.Payment;
import eeet2582.realestatemgt.model.Rental;
import eeet2582.realestatemgt.model.form.PaymentForm;
import eeet2582.realestatemgt.model.form.RentalForm;
import eeet2582.realestatemgt.service.RentalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Handle Rental and Payment operations
// Implemented: (Rental) get all, get one, get by userHouse, add, update, delete
// Implemented: (Payment) get all, get one, get all by rentalId, add update by rentalId, delete
/*
AUTHORIZED USER CAN:
- getFilteredRentalsByUserId : TODO: front-end need to check if current user id is the same id in the request params or not
- getRentalById : users can get rentals by id
- addNewRental : users can make a rental
- getFilteredPaymentsByRentalId : TODO: front-end need to check if current user id is the same id in the request params or not
- getPaymentById : users can get payment by id
- addNewPaymentByRentalId : users can make a payment for a rental
*/

/*
ADMIN CAN:
- getAllRentals : admin can get all rentals
- getFilteredRentals : admin can get all rentals by pagination and filters without any user id or house id
- getFilteredRentalsByHouseId : admin can get rentals by house id
- deleteRentalById : admin can delete rental by id
- getAllPayments : admin can get all payments
- getFilteredPayments : admin can get all payments by pagination and filters without any user id or house id
- deletePaymentById : admin can delete payment by id
- updateRentalById : admin can update rental by id
- updatePaymentById : admin can update payment by id
 */

@RestController
@RequestMapping("api/v1")
@CrossOrigin(origins = "*")
public class RentalController {

    private final RentalService rentalService;

    @Autowired
    public RentalController(RentalService rentalService) {
        this.rentalService = rentalService;
    }

    // Just get all (for debug)
    @GetMapping("/rentals")
    @PreAuthorize("hasAuthority('read:admin-messages')")
    public List<Rental> getAllRentals() {
        return rentalService.getAllRentals();
    }

    // Return rentals with sort, order and pagination (no query)
    // Params aren't mandatory, if not provided will use defaults
    @GetMapping("/rentals/search")
    @PreAuthorize("hasAuthority('read:admin-messages')")
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
    @PreAuthorize("hasAuthority('read:admin-messages')")
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

    // Add one
    @PostMapping("/rentals")
    public Rental addNewRental(@RequestBody RentalForm form) {
        return rentalService.addNewRental(form);
    }

    // Update one by ID
    @PutMapping("/rentals/{rentalId}")
    @PreAuthorize("hasAuthority('read:admin-messages')")
    public Rental updateRentalById(@PathVariable(value = "rentalId") Long rentalId, @RequestBody RentalForm form) {
        return rentalService.updateRentalById(rentalId, form);
    }

    // Delete one by ID
    @DeleteMapping("/rentals/{rentalId}")
    @PreAuthorize("hasAuthority('read:admin-messages')")
    public void deleteRentalById(@PathVariable("rentalId") Long rentalId) {
        rentalService.deleteRentalById(rentalId);
    }

    // --- PAYMENT --- //

    // Just get all (for debug)
//    @GetMapping("/payments")
//    @PreAuthorize("hasAuthority('read:admin-messages')")
//    public List<Payment> getAllPayments() {
//        return rentalService.getAllPayments();
//    }

    // Return payments with sort, order and pagination (by userId)
    // Params aren't mandatory, if not provided will use defaults
    @GetMapping("/payments/byUser")
    public Page<Payment> getFilteredPaymentsByUserId(@RequestParam(value = "userId", required = false) Long userId,
                                                     @RequestParam(value = "pageNo", defaultValue = "0") int pageNo,
                                                     @RequestParam(value = "pageSize", defaultValue = "5") int pageSize,
                                                     @RequestParam(value = "sortBy", defaultValue = "date") String sortBy,
                                                     @RequestParam(value = "orderBy", defaultValue = "desc") String orderBy) {
        return rentalService.getFilteredPaymentsByUserId(userId, pageNo, pageSize, sortBy, orderBy);
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

    // Add new Payment by Rental (MUST HAVE rentalId)
    @PostMapping("/payments/byRental/{rentalId}")
    public Payment addNewPaymentByRentalId(@PathVariable(value = "rentalId") Long rentalId, @RequestBody PaymentForm payment) {
        return rentalService.addNewPaymentByRentalId(rentalId, payment);
    }

    // Update one by ID
    @PutMapping("/payments/{paymentId}")
    @PreAuthorize("hasAuthority('read:admin-messages')")
    public Payment updatePaymentById(@PathVariable(value = "paymentId") Long paymentId, @RequestBody PaymentForm payment) {
        return rentalService.updatePaymentById(paymentId, payment);
    }

    // Delete one by ID
    @DeleteMapping("/payments/{paymentId}")
    @PreAuthorize("hasAuthority('read:admin-messages')")
    public void deletePaymentById(@PathVariable("paymentId") Long paymentId) {
        rentalService.deletePaymentById(paymentId);
    }
}
