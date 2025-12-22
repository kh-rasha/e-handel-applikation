package se.yh.ehandel.ehandelapplikation.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import se.yh.ehandel.ehandelapplikation.testdata.TestData;
import se.yh.ehandel.repository.ProductRepository;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;


    @Test
    void findBySku_returns_product_when_exists() {
        var product = TestData.product("BDSG", "Testproduct");
        var save = productRepository.save(product);

        var found = productRepository.findBySku("BDSG");

        assertThat(found).isPresent();
        assertThat(found.get().getSku()).isEqualTo("BDSG");
        assertThat(found.get().getName()).isEqualTo("Testproduct");
    }
}