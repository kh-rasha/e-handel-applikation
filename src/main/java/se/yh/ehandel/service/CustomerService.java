package se.yh.ehandel.service;
import se.yh.ehandel.domain.entity.Customer;
import java.util.List;

public interface CustomerService {
    Customer create(Customer customer);
    Customer findById(Long id);
    Customer findByEmail(String email);
    List<Customer> findAll();
}
