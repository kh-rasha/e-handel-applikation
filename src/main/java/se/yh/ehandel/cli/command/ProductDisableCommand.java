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
        return "product disable <sku>";
    }

    @Override
    public void execute() {
        String sku = io.readLine("SKU: ");
        productService.deactivate(sku);
        io.println("Product disabled: " + sku);
    }
}
