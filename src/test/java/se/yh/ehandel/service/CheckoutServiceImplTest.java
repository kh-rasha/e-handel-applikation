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

import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;

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

}