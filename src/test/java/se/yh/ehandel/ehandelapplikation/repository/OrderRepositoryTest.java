package se.yh.ehandel.ehandelapplikation.repository;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import se.yh.ehandel.ehandelapplikation.testdata.TestData;
import se.yh.ehandel.repository.CustomerRepository;
import se.yh.ehandel.repository.OrderRepository;
import static org.assertj.core.api.Assertions.assertThat;
import static se.yh.ehandel.domain.enums.OrderStatus.*;


@DataJpaTest
class OrderRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private CustomerRepository customerRepository;


    @Test
    void findByStatus_returns_orders_with_given_status(){
        var customer = TestData.validCustomer();
        var saveCustomer = customerRepository.save(customer);
        var orderOne = TestData.orderWithStatus(customer, NEW);
        var orderTwo = TestData.orderWithStatus(customer, PAID);
        var orderThree = TestData.orderWithStatus(customer, CANCELLED);
        var savedOne = orderRepository.save(orderOne);
        var savedTwo = orderRepository.save(orderTwo);
        var savedThree = orderRepository.save(orderThree);

        var result = orderRepository.findByStatus(PAID);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getStatus()).isEqualTo(PAID);
    }




}
