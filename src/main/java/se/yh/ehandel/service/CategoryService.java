package se.yh.ehandel.service;
import se.yh.ehandel.domain.entity.Category;
import java.util.List;

public interface CategoryService {
    Category create(Category category);
    Category findById(Long id);
    List<Category> findAll();
}
