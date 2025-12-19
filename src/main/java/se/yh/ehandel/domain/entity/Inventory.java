package se.yh.ehandel.domain.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "inventory")
public class Inventory {

    @Id
    private Long productId;

    @MapsId
    @OneToOne
    @JoinColumn(name = "product_id")
    private Product product;
    private int inStock;

    public Inventory() {
        // JPA
    }

    public Long getProductId() {return productId;}
    public Product getProduct() {return product;}
    public int getInStock() {return inStock;}

    public void setProduct(Product product) {this.product = product;}
    public void setInStock(int inStock) {this.inStock = inStock;}
}
