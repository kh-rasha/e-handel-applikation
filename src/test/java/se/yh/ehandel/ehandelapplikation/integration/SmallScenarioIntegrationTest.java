package se.yh.ehandel.ehandelapplikation.integration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.context.ActiveProfiles;

import se.yh.ehandel.domain.enums.OrderStatus;
import se.yh.ehandel.domain.enums.PaymentMethod;
import se.yh.ehandel.ehandelapplikation.testdata.TestData;
import se.yh.ehandel.repository.*;
import se.yh.ehandel.service.CheckoutService;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest(classes = SmallScenarioIntegrationTest.TestApp.class)
@ActiveProfiles("test")
class SmallScenarioIntegrationTest {


    @SpringBootApplication
    @ComponentScan(
            basePackages = "se.yh.ehandel",
            excludeFilters = @ComponentScan.Filter(
                    type = FilterType.REGEX,
                    pattern = "se\\.yh\\.ehandel\\.cli\\..*"
            )
    )
    static class TestApp { }

    @Autowired private CheckoutService checkoutService;

    @Autowired private CustomerRepository customerRepository;
    @Autowired private ProductRepository productRepository;
    @Autowired private InventoryRepository inventoryRepository;
    @Autowired private OrderRepository orderRepository;
    @Autowired private OrderItemRepository orderItemRepository;
    @Autowired private PaymentRepository paymentRepository;
    @Autowired private CategoryRepository categoryRepository;

    @BeforeEach
    void cleanDb() {
        paymentRepository.deleteAll();
        orderItemRepository.deleteAll();
        orderRepository.deleteAll();

        inventoryRepository.deleteAll();
        productRepository.deleteAll();
        categoryRepository.deleteAll();
        customerRepository.deleteAll();
    }

    @Test
    void small_seed_creates_expected_counts() {
        TestData.seedSmall(customerRepository, productRepository, inventoryRepository);

        assertThat(customerRepository.count()).isEqualTo(5);
        assertThat(productRepository.count()).isEqualTo(10);
        assertThat(inventoryRepository.count()).isEqualTo(10);
    }

    @Test
    void checkout_small_updates_stock_or_rolls_back() {
        TestData.seedSmall(customerRepository, productRepository, inventoryRepository);

        var sku = "SKU-1";
        var qty = 2;

        var product = productRepository.findBySku(sku).orElseThrow();
        var invBefore = inventoryRepository.findByProduct_Id(product.getId()).orElseThrow();
        assertThat(invBefore.getInStock()).isEqualTo(10);

        var order = checkoutService.checkout(
                "test1@live.se",
                TestData.skuQty(sku, qty),
                PaymentMethod.CARD
        );

        assertThat(order.getStatus()).isIn(OrderStatus.PAID, OrderStatus.CANCELLED);

        var invAfter = inventoryRepository.findByProduct_Id(product.getId()).orElseThrow();
        if (order.getStatus() == OrderStatus.PAID) {
            assertThat(invAfter.getInStock()).isEqualTo(10 - qty);
        } else {
            assertThat(invAfter.getInStock()).isEqualTo(10);
        }
    }

    @Test
    void checkout_more_than_stock_throws_and_creates_no_order() {
        TestData.seedSmall(customerRepository, productRepository, inventoryRepository);

        assertThatThrownBy(() -> checkoutService.checkout(
                "test1@live.se",
                TestData.skuQty("SKU-1", 999),
                PaymentMethod.CARD
        )).isInstanceOf(IllegalArgumentException.class);

        assertThat(orderRepository.count()).isZero();
    }
}