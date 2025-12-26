package se.yh.ehandel.ehandelapplikation.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import se.yh.ehandel.domain.entity.Category;
import se.yh.ehandel.ehandelapplikation.testdata.TestData;
import se.yh.ehandel.repository.CategoryRepository;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class CategoryRepositoryTest {


    @Autowired
    CategoryRepository categoryRepository;


    @Test
    void findByCategoryIgnoreCase_returns_matching_products() {
        var category = TestData.category("Mobiler");
        var save = categoryRepository.saveAndFlush(category);
        var found = categoryRepository.findByNameIgnoreCase("mobiler");

       assertThat(found)
               .isPresent()
               .get()
               .extracting(Category::getName)
               .isEqualTo("Mobiler");
    }
}