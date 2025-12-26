package se.yh.ehandel.domain.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "order_item")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    private int qty;

    private BigDecimal unitPrice;

    private BigDecimal lineTotal;
    protected OrderItem() {
    }

    public OrderItem(Order order, Product product, int qty, BigDecimal unitPrice) {
        this.order = order;
        this.product = product;
        this.qty = qty;
        this.unitPrice = unitPrice;
        recalcLineTotal();
    }

    private void recalcLineTotal() {
        this.lineTotal = unitPrice.multiply(BigDecimal.valueOf(qty));
    }

    public Long getId() { return id; }
    public Order getOrder() { return order; }
    public Product getProduct() { return product; }
    public int getQty() { return qty; }
    public BigDecimal getUnitPrice() { return unitPrice; }
    public BigDecimal getLineTotal() { return lineTotal; }

    public void setQty(int qty) {
        this.qty = qty;
        recalcLineTotal();
    }
}
