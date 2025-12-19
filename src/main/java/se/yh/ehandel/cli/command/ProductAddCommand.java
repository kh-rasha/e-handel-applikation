package se.yh.ehandel.cli.command;

import org.springframework.stereotype.Component;
import se.yh.ehandel.cli.ConsoleIO;
import se.yh.ehandel.domain.entity.Product;
import se.yh.ehandel.service.ProductService;

import java.math.BigDecimal;

@Component
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

        Product product = new Product();
        product.setSku(sku);
        product.setName(name);
        product.setDescription(desc);
        product.setPrice(price);

        productService.create(product);
        io.println("Product created: " + sku);
    }
}
