package eeet2582.realestatemgt.repository;

import eeet2582.realestatemgt.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    List<Payment> findByRental_RentalId(Long rentalId);
}
