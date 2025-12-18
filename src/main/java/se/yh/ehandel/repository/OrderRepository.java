package se.yh.ehandel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se.yh.ehandel.domain.entity.Order;
import se.yh.ehandel.domain.enums.OrderStatus;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByStatus(OrderStatus status);
}
