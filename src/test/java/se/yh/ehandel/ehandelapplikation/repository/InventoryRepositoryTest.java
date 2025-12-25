package se.yh.ehandel.ehandelapplikation.repository;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import se.yh.ehandel.domain.entity.Product;
import se.yh.ehandel.ehandelapplikation.testdata.TestData;
import se.yh.ehandel.repository.InventoryRepository;
import se.yh.ehandel.repository.ProductRepository;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class InventoryRepositoryTest {

    @Autowired
    InventoryRepository inventoryRepository;
    @Autowired
    ProductRepository productRepository;


    @Test
    void findByProductId_returns_inventory_when_exists(){
        Product product = TestData.product("SKU1", "test");
        Product saveProduct = productRepository.save(product);
        var inventory = TestData.inventoryByProductId(product, 5);

        var save = inventoryRepository.save(inventory);

        var found = inventoryRepository.findByProduct_Id(product.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getProduct().getId()).isEqualTo(product.getId());
        assertThat(found.get().getInStock()).isEqualTo(5);
    }
}
