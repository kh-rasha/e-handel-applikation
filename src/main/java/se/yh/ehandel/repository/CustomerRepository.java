package se.yh.ehandel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se.yh.ehandel.domain.entity.Customer;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Optional<Customer> findByEmailIgnoreCase(String email);
}
