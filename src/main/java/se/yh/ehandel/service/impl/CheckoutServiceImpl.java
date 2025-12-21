package se.yh.ehandel.service.impl;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.yh.ehandel.domain.entity.*;
import se.yh.ehandel.domain.enums.*;
import se.yh.ehandel.repository.*;
import se.yh.ehandel.service.CheckoutService;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Random;



@Service
public class CheckoutServiceImpl implements CheckoutService {

    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;
    private final InventoryRepository inventoryRepository;
    private final OrderRepository orderRepository;
    private final Random random = new Random();

    public CheckoutServiceImpl(CustomerRepository customerRepository,
                               ProductRepository productRepository,
                               InventoryRepository inventoryRepository,
                               OrderRepository orderRepository) {
        this.customerRepository = customerRepository;
        this.productRepository = productRepository;
        this.inventoryRepository = inventoryRepository;
        this.orderRepository = orderRepository;
    }

    @Override
    @Transactional
    public Order checkout(String customerEmail, Map<String, Integer> skuQty, PaymentMethod method) {
        if (skuQty == null || skuQty.isEmpty()) {
            throw new IllegalArgumentException("Cart is empty");
        }

        Customer customer = customerRepository.findByEmailIgnoreCase(customerEmail)
                .orElseThrow(() -> new EntityNotFoundException("Customer not found: " + customerEmail));

        // 1) Bygg order + items + total
        Order order = new Order(customer);

        BigDecimal total = BigDecimal.ZERO;

        for (var entry : skuQty.entrySet()) {
            String sku = entry.getKey();
            int qty = entry.getValue();

            if (qty <= 0) throw new IllegalArgumentException("Invalid qty for sku " + sku);

            Product product = productRepository.findBySku(sku)
                    .orElseThrow(() -> new EntityNotFoundException("Product not found (sku): " + sku));

            if (!product.isActive()) {
                throw new IllegalArgumentException("Product is inactive: " + sku);
            }

            // Lagerkontroll
            Inventory inv = inventoryRepository.findByProduct_Id(product.getId())
                    .orElseThrow(() -> new EntityNotFoundException("Inventory missing for productId: " + product.getId()));

            if (inv.getInStock() < qty) {
                throw new IllegalArgumentException("Not enough stock for sku " + sku + ". In stock=" + inv.getInStock());
            }

            BigDecimal unitPrice = product.getPrice();
            OrderItem item = new OrderItem(order, product, qty, unitPrice);
            order.addItem(item);

            total = total.add(item.getLineTotal());
        }

        order.setTotal(total);

        // 2) Reservera lager (minska)
        for (var entry : skuQty.entrySet()) {
            Product product = productRepository.findBySku(entry.getKey())
                    .orElseThrow(); // finns redan, men ok
            int qty = entry.getValue();

            Inventory inv = inventoryRepository.findByProduct_Id(product.getId()).orElseThrow();
            inv.setInStock(inv.getInStock() - qty);
            inventoryRepository.save(inv);
        }

        // 3) Betalningssimulering + payment
        Payment payment = new Payment(order, method);
        boolean approved = random.nextDouble() < 0.90;

        if (approved) {
            payment.setStatus(PaymentStatus.APPROVED);
            order.setStatus(OrderStatus.PAID);
        } else {
            payment.setStatus(PaymentStatus.DECLINED);

            // 4) Återställ lager vid misslyckad betalning
            for (var entry : skuQty.entrySet()) {
                Product product = productRepository.findBySku(entry.getKey()).orElseThrow();
                int qty = entry.getValue();

                Inventory inv = inventoryRepository.findByProduct_Id(product.getId()).orElseThrow();
                inv.setInStock(inv.getInStock() + qty);
                inventoryRepository.save(inv);
            }

            // Välj policy: här sätter vi CANCELLED för tydlighet
            order.setStatus(OrderStatus.CANCELLED);
        }

        order.addPayment(payment);

        // spara order (cascade sparar items + payments)
        return orderRepository.save(order);
    }
}