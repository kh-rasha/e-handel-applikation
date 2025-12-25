package se.yh.ehandel.ehandelapplikation.service;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.junit.jupiter.MockitoExtension;
import se.yh.ehandel.domain.entity.Inventory;
import se.yh.ehandel.domain.entity.Order;
import se.yh.ehandel.domain.enums.OrderStatus;
import se.yh.ehandel.domain.enums.PaymentMethod;
import se.yh.ehandel.ehandelapplikation.testdata.TestData;
import se.yh.ehandel.repository.CustomerRepository;
import se.yh.ehandel.repository.InventoryRepository;
import se.yh.ehandel.repository.OrderRepository;
import se.yh.ehandel.repository.ProductRepository;
import se.yh.ehandel.service.CheckoutService;
import se.yh.ehandel.service.impl.CheckoutServiceImpl;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;



    @ExtendWith(MockitoExtension.class)
    class CheckoutServiceTest {

        private final CustomerRepository customerRepo = mock(CustomerRepository.class);
        private final ProductRepository productRepo = mock(ProductRepository.class);
        private final InventoryRepository inventoryRepo = mock(InventoryRepository.class);
        private final OrderRepository orderRepo = mock(OrderRepository.class);

        Random random = mock(Random.class);
        private final CheckoutService svc =
                new CheckoutServiceImpl(customerRepo, productRepo, inventoryRepo, orderRepo);


        @Test
        void createOrder_reserves_stock_and_creates_order_items() {
            var customer = TestData.validCustomer();
            var product = TestData.productWithStock();
            var inv = TestData.inventoryByProductId(product, 10);
            var skuQty = TestData.skuQty("SKU1", 2);


            when(customerRepo.findByEmailIgnoreCase("test@live.se")).thenReturn(Optional.of(customer));
            when(productRepo.findBySku("SKU1")).thenReturn(Optional.of(product));
            when(inventoryRepo.findByProduct_Id(100L)).thenReturn(Optional.of(inv));
            when(random.nextDouble()).thenReturn(0.0);
            when(orderRepo.save(any(Order.class))).thenAnswer(i -> i.getArgument(0));

            var result = svc.checkout("test@live.se", skuQty, PaymentMethod.CARD);

            assertThat(result.getItems()).hasSize(1);
            assertThat(result.getTotal()).isEqualByComparingTo(new BigDecimal("200.00"));
            assertThat(result.getStatus()).isEqualTo(OrderStatus.PAID);

            ArgumentCaptor<Inventory> invCaptor = ArgumentCaptor.forClass(Inventory.class);
            verify(inventoryRepo, atLeastOnce()).save(invCaptor.capture());
            assertThat(invCaptor.getValue().getInStock()).isEqualTo(8);

            verify(orderRepo, times(1)).save(any(Order.class));
        }
    }

