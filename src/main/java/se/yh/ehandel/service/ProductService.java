package se.yh.ehandel.service;
import se.yh.ehandel.domain.entity.Product;
import java.util.List;

public interface ProductService {
    Product create(Product product);
    Product update(Long id, Product updated);
    Product findById(Long id);
    List<Product> findAll();
    List<Product> searchByName(String query);
    void deactivate(Long id);
}
