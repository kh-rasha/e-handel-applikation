package se.yh.ehandel.service;

import org.junit.jupiter.api.Test;
import se.yh.ehandel.domain.entity.Customer;
import se.yh.ehandel.domain.entity.Inventory;
import se.yh.ehandel.domain.entity.Product;
import se.yh.ehandel.domain.enums.OrderStatus;
import se.yh.ehandel.domain.enums.PaymentMethod;
import se.yh.ehandel.repository.CustomerRepository;
import se.yh.ehandel.repository.InventoryRepository;
import se.yh.ehandel.repository.OrderRepository;
import se.yh.ehandel.repository.ProductRepository;
import se.yh.ehandel.service.impl.CheckoutServiceImpl;
import jakarta.persistence.EntityNotFoundException;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CartSessionServiceTest {

    @Test
    void addProduct_adds_product_to_cart() {

        CustomerRepository customerRepo = mock(CustomerRepository.class);
        ProductRepository productRepo = mock(ProductRepository.class);
        InventoryRepository inventoryRepo = mock(InventoryRepository.class);
        OrderRepository orderRepo = mock(OrderRepository.class);

            // Arrange
            CartSessionService cartService = new CartSessionService();

            String sku = "SKU1";
            int quantity = 2;

            // Act
            cartService.add(sku, quantity);

            // Assert
            assertTrue(cartService.view().containsKey(sku), "Cart should contain the product");
            assertEquals(quantity, cartService.view().get(sku).intValue(), "Quantity should match");
        }

    @Test
    void removeProduct_removes_product_from_cart() {
        // Arrange
        CartSessionService cartService = new CartSessionService();
        String sku = "SKU1";
        cartService.add(sku, 3); // lägg till först

        // Act
        cartService.remove(sku);

        // Assert
        assertFalse(cartService.view().containsKey(sku), "Cart should no longer contain the product");
        assertTrue(cartService.isEmpty(), "Cart should be empty after removal");
    }

    @Test
    void clearCart_empties_the_cart() {
        // Arrange
        CartSessionService cartService = new CartSessionService();
        cartService.add("SKU1", 2);
        cartService.add("SKU2", 1);

        // Act
        cartService.clear();

        // Assert
        assertTrue(cartService.isEmpty(), "Cart should be empty after clear");
        assertEquals(0, cartService.view().size(), "Cart size should be 0 after clear");
    }

    @Test
    void addProduct_multiple_times_sums_quantity() {
        // Arrange
        CartSessionService cartService = new CartSessionService();
        String sku = "SKU1";

        // Act
        cartService.add(sku, 2);
        cartService.add(sku, 3); // lägg till samma produkt igen

        // Assert
        assertTrue(cartService.view().containsKey(sku), "Cart should contain the product");
        assertEquals(5, cartService.view().get(sku).intValue(), "Quantity should sum up correctly");
    }


}
