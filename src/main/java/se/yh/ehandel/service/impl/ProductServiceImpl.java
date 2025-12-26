package se.yh.ehandel.service.impl;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.yh.ehandel.domain.entity.Product;
import se.yh.ehandel.repository.ProductRepository;
import se.yh.ehandel.service.ProductService;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Product create(Product product) {
        product.setCreatedAt(LocalDateTime.now());
        product.setActive(true);
        return productRepository.save(product);
    }

    @Override
    public Product update(Long id, Product updated) {
        Product existing = findById(id);

        if (updated.getSku() != null) {
            existing.setSku(updated.getSku());
        }
        if (updated.getName() != null) {
            existing.setName(updated.getName());
        }
        if (updated.getDescription() != null) {
            existing.setDescription(updated.getDescription());
        }
        if (updated.getPrice() != null) {
            existing.setPrice(updated.getPrice());
        }
        if (updated.getCategories() != null) {
            existing.setCategories(updated.getCategories());
        }

        existing.setActive(updated.isActive());

        return productRepository.save(existing);
    }


    @Override
    @Transactional(readOnly = true)
    public Product findById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("Product not found with id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Product> searchByName(String query) {
        return productRepository.findByNameContainingIgnoreCase(query);
    }

    @Override
    public void deactivate(Long id) {
        Product product = findById(id);
        product.setActive(false);
        productRepository.save(product);
    }
}
