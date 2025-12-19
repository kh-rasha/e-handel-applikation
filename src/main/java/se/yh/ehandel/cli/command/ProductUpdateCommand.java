package se.yh.ehandel.cli.command;

import se.yh.ehandel.cli.ConsoleIO;
import se.yh.ehandel.domain.entity.Product;
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
        return "product update <id>";
    }

    @Override
    public void execute() {
        String idStr = io.readLine("Product ID to update: ").trim();

        Long id;
        try {
            id = Long.parseLong(idStr);
        } catch (NumberFormatException e) {
            io.println("Invalid product id. Must be a number.");
            return;
        }

        String name = io.readLine("New name (blank = skip): ").trim();
        String priceStr = io.readLine("New price (blank = skip): ").trim();

        Product updated = new Product();

        if (!name.isBlank()) {
            updated.setName(name);
        }

        if (!priceStr.isBlank()) {
            try {
                updated.setPrice(new BigDecimal(priceStr));
            } catch (NumberFormatException e) {
                io.println("Invalid price format.");
                return;
            }
        }

        productService.update(id, updated);

        io.println("Product updated (id=" + id + ")");
    }
}
