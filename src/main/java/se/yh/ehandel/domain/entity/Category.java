package se.yh.ehandel.domain.entity;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "category")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;

    @ManyToMany(mappedBy = "categories")
    private Set<Product> products = new HashSet<>();

    protected Category() {}
    public Category(String name) { this.name = name; }

    // Getters
    public Long getId() { return id; }
    public String getName() { return name; }

    // Setters
    public void setName(String name) { this.name = name; }
}
