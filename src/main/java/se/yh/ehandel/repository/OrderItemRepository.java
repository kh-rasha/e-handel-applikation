package se.yh.ehandel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se.yh.ehandel.domain.entity.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}

