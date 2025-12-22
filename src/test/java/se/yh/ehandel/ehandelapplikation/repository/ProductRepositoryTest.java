package se.yh.ehandel.ehandelapplikation.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import se.yh.ehandel.ehandelapplikation.testdata.TestData;
import se.yh.ehandel.repository.ProductRepository;

@Autowired
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;


    @Test
    void findBySku_returns_product_when_exists() {
        var product = TestData.validProduct();

    }
}