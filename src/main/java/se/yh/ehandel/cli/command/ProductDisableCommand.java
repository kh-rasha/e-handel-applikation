package se.yh.ehandel.cli.command;

import se.yh.ehandel.cli.ConsoleIO;
import se.yh.ehandel.service.ProductService;

public class ProductDisableCommand implements Command {

    private final ProductService productService;
    private final ConsoleIO io;

    public ProductDisableCommand(ProductService productService, ConsoleIO io) {
        this.productService = productService;
        this.io = io;
    }

    @Override
    public String name() {
        return "product disable";
    }

    @Override
    public String help() {
        return "product disable <id>";
    }

    @Override
    public void execute() {
        String idStr = io.readLine("Product ID: ").trim();

        try {
            Long id = Long.parseLong(idStr);
            productService.deactivate(id);
            io.println("Product disabled (id=" + id + ")");
        } catch (NumberFormatException e) {
            io.println("Invalid product id. Must be a number.");
        }
    }
}
