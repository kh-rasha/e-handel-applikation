package se.yh.ehandel.ehandelapplikation.testdata;

import se.yh.ehandel.domain.entity.Customer;
import se.yh.ehandel.domain.entity.Order;
import se.yh.ehandel.domain.enums.OrderStatus;

public class TestData {

    public static Customer validCustomer(){
        return new Customer("test@live.se", "Testsson larsson");
    }


    public static Order orderWithStatus(Customer customer, OrderStatus status){
        Order order = new Order(customer);
        order.setStatus(status);
        return order;
    }


}
