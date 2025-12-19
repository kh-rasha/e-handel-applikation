package se.yh.ehandel.cli.command;

import se.yh.ehandel.cli.ConsoleIO;
import se.yh.ehandel.domain.entity.Category;
import se.yh.ehandel.service.CategoryService;

public class CategoryAddCommand implements Command {
    private final CategoryService categoryService;
    private final ConsoleIO io;

    public CategoryAddCommand(CategoryService categoryService, ConsoleIO io) {
        this.categoryService = categoryService;
        this.io = io;
    }

    @Override
    public String name() {
        return "category add";
    }

    @Override
    public String help() {
        return "category add - create new category";
    }

    @Override
    public void execute() {
        String name = io.readLine("Category name: ").trim();
        if (name.isEmpty()) {
            io.println("Category name cannot be empty");
            return;
        }
        Category category = new Category();
        category.setName(name);
        categoryService.create(category);
        io.println("Category created: " + name);
    }
}
