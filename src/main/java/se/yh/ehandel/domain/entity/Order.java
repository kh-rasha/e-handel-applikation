package se.yh.ehandel.domain.entity;

import jakarta.persistence.*;
import se.yh.ehandel.domain.enums.OrderStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    private BigDecimal total;

    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "order")
    private List<OrderItem> items = new ArrayList<>();

    @OneToMany(mappedBy = "order")
    private List<Payment> payments = new ArrayList<>();
    protected Order() {
    }

    public Order(Customer customer) {
        this.customer = customer;
        this.status = OrderStatus.NEW;
        this.createdAt = LocalDateTime.now();
        this.items = new ArrayList<>();
        this.payments = new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public List<Payment> getPayments() {
        return payments;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public void addItem(OrderItem item) {
        items.add(item);
    }

    public void addPayment(Payment payment) {
        payments.add(payment);
    }
}
