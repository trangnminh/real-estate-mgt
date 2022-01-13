package eeet2582.realestatemgt.repository;

import eeet2582.realestatemgt.model.Payment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Page<Payment> findByRental_RentalId(Long rentalId, Pageable pageable);

    @Query("select distinct p from Payment p inner join Rental r on r.rentalId = p.rental.rentalId where r.userHouse.userId=?1")
    Page<Payment> findPaymentByUserId(Long userId, Pageable pageable);
}
