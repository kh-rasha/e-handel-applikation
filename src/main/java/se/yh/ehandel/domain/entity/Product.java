package se.yh.ehandel.domain.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String sku;

    @Column(nullable=false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable=false)
    private BigDecimal price;

    @Column(nullable=false)
    private boolean active = true;

    @Column(nullable=false)
    private Instant createdAt = Instant.now();

    @ManyToMany
    @JoinTable(
            name = "product_category",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id"),
            uniqueConstraints = @UniqueConstraint(columnNames = {"product_id","category_id"})
    )

    private Set<Category> categories = new HashSet<>();

    //Constructor
    protected Product() {}

    public Product(String sku, String name, String description, BigDecimal price) {
        this.sku = sku;
        this.name = name;
        this.description = description;
        this.price = price;
    }

    //Getters
    public Long getId() { return id; }
    public String getSku() { return sku; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public BigDecimal getPrice() { return price; }
    public boolean isActive() { return active; }
    public Instant getCreatedAt() { return createdAt; }
    public Set<Category> getCategories() { return categories; }

    //Setters
    public void setName(String name) { this.name = name; }
    public void setDescription(String description) { this.description = description; }
    public void setPrice(BigDecimal price) { this.price = price; }
    public void setActive(boolean active) { this.active = active; }
}