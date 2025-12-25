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

@SpringBootTest(classes = MediumScenarioIntegrationTest.TestApp.class)
@ActiveProfiles("test")
class MediumScenarioIntegrationTest {

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
    void medium_seed_creates_expected_counts() {
        TestData.seedMedium(customerRepository, productRepository, inventoryRepository);

        assertThat(customerRepository.count()).isEqualTo(100);
        assertThat(productRepository.count()).isEqualTo(50);
        assertThat(inventoryRepository.count()).isEqualTo(50);
    }

    @Test
    void checkout_medium_updates_stock_or_rolls_back() {
        TestData.seedMedium(customerRepository, productRepository, inventoryRepository);

        var sku = "SKU-1";
        var qty = 2;

        var product = productRepository.findBySku(sku).orElseThrow();
        var invBefore = inventoryRepository.findByProduct_Id(product.getId()).orElseThrow();

        assertThat(invBefore.getInStock()).isEqualTo(25);

        var order = checkoutService.checkout(
                "test1@live.se",
                TestData.skuQty(sku, qty),
                PaymentMethod.CARD
        );

        assertThat(order.getStatus()).isIn(OrderStatus.PAID, OrderStatus.CANCELLED);

        var invAfter = inventoryRepository.findByProduct_Id(product.getId()).orElseThrow();
        if (order.getStatus() == OrderStatus.PAID) {
            assertThat(invAfter.getInStock()).isEqualTo(25 - qty);
        } else {
            assertThat(invAfter.getInStock()).isEqualTo(25);
        }
    }

    @Test
    void checkout_more_than_stock_throws_and_creates_no_order() {
        TestData.seedMedium(customerRepository, productRepository, inventoryRepository);

        assertThatThrownBy(() -> checkoutService.checkout(
                "test1@live.se",
                TestData.skuQty("SKU-1", 999),
                PaymentMethod.CARD
        )).isInstanceOf(IllegalArgumentException.class);

        assertThat(orderRepository.count()).isZero();
    }
}