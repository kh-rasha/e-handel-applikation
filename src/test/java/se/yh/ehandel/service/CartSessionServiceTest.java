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


}
