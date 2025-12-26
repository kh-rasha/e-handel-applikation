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
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CheckoutServiceImplTest {

    @Test
    void checkout_fails_when_insufficient_stock() {
        CustomerRepository customerRepo = mock(CustomerRepository.class);
        ProductRepository productRepo = mock(ProductRepository.class);
        InventoryRepository inventoryRepo = mock(InventoryRepository.class);
        OrderRepository orderRepo = mock(OrderRepository.class);


        var svc = new se.yh.ehandel.service.impl.CheckoutServiceImpl(
                customerRepo, productRepo, inventoryRepo, orderRepo
        );

        Customer customer = new Customer("a@a.com", "A");
        when(customerRepo.findByEmailIgnoreCase("a@a.com")).thenReturn(Optional.of(customer));

        Product product = new Product();
        product.setSku("SKU1");
        product.setName("P1");
        product.setPrice(new BigDecimal("10.00"));
        product.setActive(true);
        // fejk-id via reflection är överkurs; enklast: mocka product.getId()
        Product productSpy = spy(product);
        when(productSpy.getId()).thenReturn(1L);

        when(productRepo.findBySku("SKU1")).thenReturn(Optional.of(productSpy));

        Inventory inv = new Inventory();
        inv.setProduct(productSpy);
        inv.setInStock(0);
        when(inventoryRepo.findByProduct_Id(1L)).thenReturn(Optional.of(inv));

        var ex = assertThrows(IllegalArgumentException.class,
                () -> svc.checkout("a@a.com", Map.of("SKU1", 1), PaymentMethod.CARD)
        );

        assertTrue(ex.getMessage().toLowerCase().contains("not enough stock"));
        verify(orderRepo, never()).save(any());
    }

    @Test
    void checkout_reduces_stock_on_success() throws Exception {
        CustomerRepository customerRepo = mock(CustomerRepository.class);
        ProductRepository productRepo = mock(ProductRepository.class);
        InventoryRepository inventoryRepo = mock(InventoryRepository.class);
        OrderRepository orderRepo = mock(OrderRepository.class);

        var svc = new se.yh.ehandel.service.impl.CheckoutServiceImpl(
                customerRepo, productRepo, inventoryRepo, orderRepo
        );

        Customer customer = new Customer("a@a.com", "A");
        when(customerRepo.findByEmailIgnoreCase("a@a.com")).thenReturn(Optional.of(customer));

        Product product = new Product();
        product.setSku("SKU1");
        product.setName("P1");
        product.setPrice(new BigDecimal("10.00"));
        product.setActive(true);

        // Sätt ID manuellt (reflection – precis vad Hibernate gör)
        var idField = Product.class.getDeclaredField("id");
        idField.setAccessible(true);
        idField.set(product, 1L);

        when(productRepo.findBySku("SKU1")).thenReturn(Optional.of(product));

        Inventory inv = new Inventory();
        inv.setProduct(product);
        inv.setInStock(10);
        when(inventoryRepo.findByProduct_Id(1L)).thenReturn(Optional.of(inv));

        when(orderRepo.save(any())).thenAnswer(i -> i.getArgument(0));

        // ACT
        svc.checkout("a@a.com", Map.of("SKU1", 2), PaymentMethod.CARD);

        // ASSERT
        assertEquals(8, inv.getInStock());
    }

    @Test
    void checkout_fails_when_cart_empty() {
        // Arrange - skapa mocks
        CustomerRepository customerRepo = mock(CustomerRepository.class);
        ProductRepository productRepo = mock(ProductRepository.class);
        InventoryRepository inventoryRepo = mock(InventoryRepository.class);
        OrderRepository orderRepo = mock(OrderRepository.class);
        PaymentMethod paymentMethod = mock(PaymentMethod.class);

        CheckoutServiceImpl svc = new CheckoutServiceImpl(
                customerRepo, productRepo, inventoryRepo, orderRepo
        );

        // Mocka customerRepo
        Customer customer = new Customer("a@a.com", "A");
        when(customerRepo.findByEmailIgnoreCase("a@a.com")).thenReturn(Optional.of(customer));

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> svc.checkout("a@a.com", Map.of(), PaymentMethod.SWISH));

        // Optional: kontrollera meddelande
        assertEquals("Cart is empty", exception.getMessage());
    }

    @Test
    void checkout_assigns_final_order_status() throws Exception {
        // Arrange
        CustomerRepository customerRepo = mock(CustomerRepository.class);
        ProductRepository productRepo = mock(ProductRepository.class);
        InventoryRepository inventoryRepo = mock(InventoryRepository.class);
        OrderRepository orderRepo = mock(OrderRepository.class);

        Customer customer = new Customer("a@a.com", "A");
        when(customerRepo.findByEmailIgnoreCase("a@a.com")).thenReturn(Optional.of(customer));

        CheckoutServiceImpl svc = new CheckoutServiceImpl(
                customerRepo, productRepo, inventoryRepo, orderRepo
        );

        Product product = new Product();
        product.setSku("SKU1");
        product.setName("P1");
        product.setPrice(new BigDecimal("10.00"));
        product.setActive(true);

        // Sätt ID manuellt (som Hibernate gör)
        var idField = Product.class.getDeclaredField("id");
        idField.setAccessible(true);
        idField.set(product, 1L);

        when(productRepo.findBySku("SKU1")).thenReturn(Optional.of(product));

        Inventory inv = new Inventory();
        inv.setProduct(product);
        inv.setInStock(5);
        when(inventoryRepo.findByProduct_Id(1L)).thenReturn(Optional.of(inv));

        // När order sparas, returnera objektet
        when(orderRepo.save(any())).thenAnswer(i -> i.getArgument(0));

        // Act
        var order = svc.checkout("a@a.com", Map.of("SKU1", 1), PaymentMethod.CARD);

        // Assert
        assertNotNull(order.getStatus(), "Order status should be assigned");
        assertEquals(OrderStatus.PAID, order.getStatus(), "Order status should be PAID after checkout");
    }

    @Test
    void checkout_fails_when_customer_not_found() {
        // Arrange
        CustomerRepository customerRepo = mock(CustomerRepository.class);
        ProductRepository productRepo = mock(ProductRepository.class);
        InventoryRepository inventoryRepo = mock(InventoryRepository.class);
        OrderRepository orderRepo = mock(OrderRepository.class);

        CheckoutServiceImpl svc = new CheckoutServiceImpl(
                customerRepo, productRepo, inventoryRepo, orderRepo
        );

        // Mocka att kunden inte finns
        when(customerRepo.findByEmailIgnoreCase("missing@a.com")).thenReturn(Optional.empty());

        // Act & Assert
        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class,
                () -> svc.checkout("missing@a.com", Map.of("SKU1", 1), PaymentMethod.CARD));


        assertTrue(ex.getMessage().toLowerCase().contains("customer not found"));
        verify(orderRepo, never()).save(any());
    }

    @Test
    void checkout_fails_when_product_not_found() throws Exception {
        // Arrange
        CustomerRepository customerRepo = mock(CustomerRepository.class);
        ProductRepository productRepo = mock(ProductRepository.class);
        InventoryRepository inventoryRepo = mock(InventoryRepository.class);
        OrderRepository orderRepo = mock(OrderRepository.class);

        CheckoutServiceImpl svc = new CheckoutServiceImpl(
                customerRepo, productRepo, inventoryRepo, orderRepo
        );

        Customer customer = new Customer("a@a.com", "A");
        when(customerRepo.findByEmailIgnoreCase("a@a.com")).thenReturn(Optional.of(customer));

        // Mocka att produkten inte finns
        when(productRepo.findBySku("MISSING_SKU")).thenReturn(Optional.empty());

        // Act & Assert
        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class,
                () -> svc.checkout("a@a.com", Map.of("MISSING_SKU", 1), PaymentMethod.CARD));

        assertTrue(ex.getMessage().toLowerCase().contains("product not found"));
        verify(orderRepo, never()).save(any());
    }

    @Test
    void checkout_with_multiple_products() throws Exception {
        // Arrange
        CustomerRepository customerRepo = mock(CustomerRepository.class);
        ProductRepository productRepo = mock(ProductRepository.class);
        InventoryRepository inventoryRepo = mock(InventoryRepository.class);
        OrderRepository orderRepo = mock(OrderRepository.class);

        CheckoutServiceImpl svc = new CheckoutServiceImpl(
                customerRepo, productRepo, inventoryRepo, orderRepo
        );

        Customer customer = new Customer("a@a.com", "A");
        when(customerRepo.findByEmailIgnoreCase("a@a.com")).thenReturn(Optional.of(customer));

        // Skapa produkter
        Product product1 = new Product();
        product1.setSku("SKU1");
        product1.setName("P1");
        product1.setPrice(new BigDecimal("10.00"));
        product1.setActive(true);
        var idField1 = Product.class.getDeclaredField("id");
        idField1.setAccessible(true);
        idField1.set(product1, 1L);

        Product product2 = new Product();
        product2.setSku("SKU2");
        product2.setName("P2");
        product2.setPrice(new BigDecimal("20.00"));
        product2.setActive(true);
        var idField2 = Product.class.getDeclaredField("id");
        idField2.setAccessible(true);
        idField2.set(product2, 2L);

        when(productRepo.findBySku("SKU1")).thenReturn(Optional.of(product1));
        when(productRepo.findBySku("SKU2")).thenReturn(Optional.of(product2));

        // Skapa inventory
        Inventory inv1 = new Inventory();
        inv1.setProduct(product1);
        inv1.setInStock(5);

        Inventory inv2 = new Inventory();
        inv2.setProduct(product2);
        inv2.setInStock(3);

        when(inventoryRepo.findByProduct_Id(1L)).thenReturn(Optional.of(inv1));
        when(inventoryRepo.findByProduct_Id(2L)).thenReturn(Optional.of(inv2));

        when(orderRepo.save(any())).thenAnswer(i -> i.getArgument(0));

        // Act
        var order = svc.checkout(
                "a@a.com",
                Map.of("SKU1", 2, "SKU2", 1),
                PaymentMethod.CARD
        );

        // Assert
        assertEquals(OrderStatus.PAID, order.getStatus());
        assertEquals(2, order.getItems().size(), "Order should contain 2 products");
        assertEquals(3, inv1.getInStock(), "Stock for SKU1 should be reduced by 2");
        assertEquals(2, inv2.getInStock(), "Stock for SKU2 should be reduced by 1");
    }

}