package se.yh.ehandel.ehandelapplikation.testdata;

import se.yh.ehandel.domain.entity.*;
import se.yh.ehandel.domain.enums.OrderStatus;

import java.math.BigDecimal;
import java.time.Instant;

public class TestData {




    public static Customer validCustomer(){
        return new Customer("test@live.se", "Testsson larsson");
    }


    public static Order orderWithStatus(Customer customer, OrderStatus status){
        Order order = new Order(customer);
        order.setStatus(status);
        return order;
    }
    public static Product product(String sku, String name){
        Product p = new Product();
        p.setSku(sku);
        p.setName(name);
        p.setDescription("desc");
        p.setPrice(BigDecimal.valueOf(350));
        return p;
    }

    public static Inventory inventoryByProductId(Product product, int inStock) {
        Inventory inventory = new Inventory();
        inventory.setProduct(product);
        inventory.setInStock(inStock);
        return inventory;
    }

    public static Category category(String name){
       return  new Category(name);
    }

    public static Product productWithStock(){
        long id = 100L;

        Product product = new Product("SKU-" + id,
                "Testdata",
                "desc",
                java.math.BigDecimal.valueOf(100));


        setId(product, id);
        return product;
    }
    public static java.util.Map<String, Integer> skuQty(String sku, int qty) {
        return java.util.Map.of(sku, qty);
    }

    private static void setId(Product product, long id) {
        try {
            var field = Product.class.getDeclaredField("id");
            field.setAccessible(true);
            field.set(product, id);
        } catch (Exception e) {
            throw new RuntimeException("Kunde inte sätta id på Product i test", e);
        }
    }
}




