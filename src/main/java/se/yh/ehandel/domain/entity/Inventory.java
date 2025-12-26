package se.yh.ehandel.domain.entity;

import jakarta.persistence.*;


@Entity
@Table(name = "inventory")
public class Inventory {

    @Id
    @GeneratedValue
    private Long productId;


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
