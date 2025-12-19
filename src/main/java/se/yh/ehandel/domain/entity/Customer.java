package se.yh.ehandel.domain.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "customer")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String name;

    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "customer")
    private List<Order> orders;


    public Customer() {
        // JPA
    }

    public Long getId() {return id;}
    public String getEmail() {return email;}
    public String getName() {return name;}
    public LocalDateTime getCreatedAt() {return createdAt;}
    public List<Order> getOrders() {return orders;}

    public void setEmail(String email) {this.email = email;}
    public void setName(String name) {this.name = name;}
    public void setCreatedAt(LocalDateTime createdAt) {this.createdAt = createdAt;}
}
