package se.yh.ehandel.cli.command;

import se.yh.ehandel.cli.ConsoleIO;
import se.yh.ehandel.service.ProductService;

import java.math.BigDecimal;

public class ProductAddCommand implements Command {

    private final ProductService productService;
    private final ConsoleIO io;

    public ProductAddCommand(ProductService productService, ConsoleIO io) {
        this.productService = productService;
        this.io = io;
    }

    @Override
    public String name() {
        return "product add";
    }

    @Override
    public String help() {
        return "product add - create new product";
    }

    @Override
    public void execute() {
        String sku = io.readLine("SKU: ");
        String name = io.readLine("Name: ");
        String desc = io.readLine("Description: ");
        BigDecimal price = new BigDecimal(io.readLine("Price: "));

        productService.create(sku, name, desc, price);
        io.println("Product created: " + sku);
    }
}
