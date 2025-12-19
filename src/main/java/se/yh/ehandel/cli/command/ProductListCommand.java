package se.yh.ehandel.cli.command;

import se.yh.ehandel.cli.ConsoleIO;
import se.yh.ehandel.service.ProductService;

public class ProductListCommand implements Command {

    private final ProductService productService;
    private final ConsoleIO io;

    public ProductListCommand(ProductService productService, ConsoleIO io) {
        this.productService = productService;
        this.io = io;
    }

    @Override
    public String name() {
        return "product list";
    }

    @Override
    public String help() {
        return "product list [--q=text]";
    }

    @Override
    public void execute() {
        productService.findAll()
                .forEach(p ->
                        io.println(
                                p.getSku() + " | " +
                                        p.getName() + " | " +
                                        p.getPrice() + " | active=" + p.isActive()
                        )
                );
    }
}
