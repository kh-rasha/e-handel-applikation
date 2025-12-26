package se.yh.ehandel.service;
import se.yh.ehandel.domain.entity.Order;
import se.yh.ehandel.domain.enums.OrderStatus;

import java.util.List;

    public interface OrderService {
        List<Order> list(OrderStatus statusOrNull);
        Order get(Long id);
        Order cancel(Long id);
    }

