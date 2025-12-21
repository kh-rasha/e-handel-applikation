package se.yh.ehandel.service;


import se.yh.ehandel.domain.entity.Order;
import se.yh.ehandel.domain.enums.PaymentMethod;


import java.util.Map;

public interface CheckoutService {
    Order checkout(String customerEmail, Map<String, Integer> skuQty, PaymentMethod method);
}
