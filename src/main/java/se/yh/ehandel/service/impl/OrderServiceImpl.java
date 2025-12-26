package se.yh.ehandel.service.impl;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.yh.ehandel.domain.entity.Inventory;
import se.yh.ehandel.domain.entity.Order;
import se.yh.ehandel.domain.entity.OrderItem;
import se.yh.ehandel.domain.enums.OrderStatus;
import se.yh.ehandel.repository.InventoryRepository;
import se.yh.ehandel.repository.OrderRepository;
import se.yh.ehandel.service.OrderService;

import java.util.List;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final InventoryRepository inventoryRepository;

    public OrderServiceImpl(OrderRepository orderRepository, InventoryRepository inventoryRepository) {
        this.orderRepository = orderRepository;
        this.inventoryRepository = inventoryRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Order> list(OrderStatus statusOrNull) {
        if (statusOrNull == null) return orderRepository.findAll();
        return orderRepository.findByStatus(statusOrNull);
    }

    @Override
    @Transactional(readOnly = true)
    public Order get(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Order not found: " + id));
    }

    @Override
    public Order cancel(Long id) {
        Order order = get(id);

        if (order.getStatus() != OrderStatus.NEW) {
            throw new IllegalArgumentException("Only NEW orders can be cancelled. Current=" + order.getStatus());
        }

        // återställ lager
        for (OrderItem item : order.getItems()) {
            Long productId = item.getProduct().getId();
            Inventory inv = inventoryRepository.findByProduct_Id(productId)
                    .orElseThrow(() -> new EntityNotFoundException("Inventory missing for productId: " + productId));
            inv.setInStock(inv.getInStock() + item.getQty());
            inventoryRepository.save(inv);
        }

        order.setStatus(OrderStatus.CANCELLED);
        return orderRepository.save(order);
    }
}