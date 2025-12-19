package se.yh.ehandel.cli.command;

import se.yh.ehandel.cli.ConsoleIO;
import se.yh.ehandel.service.ProductService;

import java.math.BigDecimal;

public class ProductUpdateCommand implements Command {

    private final ProductService productService;
    private final ConsoleIO io;

    public ProductUpdateCommand(ProductService productService, ConsoleIO io) {
        this.productService = productService;
        this.io = io;
    }

    @Override
    public String name() {
        return "product update";
    }

    @Override
    public String help() {
        return "product update <sku>";
    }

    @Override
    public void execute() {
        String sku = io.readLine("SKU to update: ");
        String name = io.readLine("New name (blank = skip): ");
        String priceStr = io.readLine("New price (blank = skip): ");

        productService.update(
                sku,
                name.isBlank() ? null : name,
                null,
                priceStr.isBlank() ? null : new BigDecimal(priceStr)
        );

        io.println("Product updated: " + sku);
    }
}
