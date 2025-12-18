package se.yh.ehandel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se.yh.ehandel.domain.entity.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
