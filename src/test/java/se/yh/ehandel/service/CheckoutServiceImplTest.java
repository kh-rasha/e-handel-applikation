package se.yh.ehandel.service;

import org.junit.jupiter.api.Test;
import se.yh.ehandel.domain.entity.Customer;
import se.yh.ehandel.domain.entity.Inventory;
import se.yh.ehandel.domain.entity.Product;
import se.yh.ehandel.domain.enums.PaymentMethod;
import se.yh.ehandel.repository.CustomerRepository;
import se.yh.ehandel.repository.InventoryRepository;
import se.yh.ehandel.repository.OrderRepository;
import se.yh.ehandel.repository.ProductRepository;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
}