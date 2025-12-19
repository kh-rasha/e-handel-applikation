package se.yh.ehandel.service.impl;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.yh.ehandel.domain.entity.Inventory;
import se.yh.ehandel.domain.entity.Product;
import se.yh.ehandel.repository.InventoryRepository;
import se.yh.ehandel.repository.ProductRepository;
import se.yh.ehandel.service.InventoryService;

@Service
@Transactional
public class InventoryServiceImpl implements InventoryService {
    private final InventoryRepository inventoryRepository;
    private final ProductRepository productRepository;

    public InventoryServiceImpl(InventoryRepository inventoryRepository,
                                ProductRepository productRepository) {
        this.inventoryRepository = inventoryRepository;
        this.productRepository = productRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public int getStock(Long productId) {
        Inventory inventory = inventoryRepository.findByProduct_Id(productId).orElseThrow(() ->
                new EntityNotFoundException("Inventory not found for productId: " + productId));
        return inventory.getInStock();
    }

    @Override
    public void setStock(Long productId, int quantity) {
        Product product = productRepository.findById(productId).orElseThrow(() ->
                new EntityNotFoundException("Product not found with id: " + productId));

        Inventory inventory = inventoryRepository.findByProduct_Id(productId).orElseGet(() -> {
            Inventory inv = new Inventory();
            inv.setProduct(product); // MapsId sätter productId automatiskt
            return inv;
        });

        inventory.setInStock(quantity);
        inventoryRepository.save(inventory);
    }

    @Override
    public void adjustStock(Long productId, int delta) {
        Inventory inventory = inventoryRepository.findByProduct_Id(productId).orElseThrow(() ->
                new EntityNotFoundException("Inventory not found for productId: " + productId));

        int newStock = inventory.getInStock() + delta;
        if (newStock < 0) {
            throw new IllegalArgumentException("Stock cannot be negative for productId: " + productId);
        }

        inventory.setInStock(newStock);
        inventoryRepository.save(inventory);
    }
}
