package se.yh.ehandel.cli.command;

import org.springframework.stereotype.Component;
import se.yh.ehandel.cli.ConsoleIO;
import se.yh.ehandel.service.CategoryService;

@Component
public class CategoryListCommand implements Command {

    private final CategoryService categoryService;
    private final ConsoleIO io;

    public CategoryListCommand(CategoryService categoryService, ConsoleIO io) {
        this.categoryService = categoryService;
        this.io = io;
    }

    @Override
    public String name() {
        return "category list";
    }

    @Override
    public String help() {
        return "category list - list all categories";
    }

    @Override
    public void execute() {
        categoryService.findAll()
                .forEach(c -> io.println(c.getId() + " | " + c.getName()));
    }
}