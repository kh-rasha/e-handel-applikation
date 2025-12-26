package se.yh.ehandel.cli.command;

import se.yh.ehandel.domain.entity.Order;
import org.springframework.stereotype.Component;
import se.yh.ehandel.cli.ConsoleIO;
import se.yh.ehandel.domain.enums.PaymentMethod;
import se.yh.ehandel.service.CartSessionService;
import se.yh.ehandel.service.CheckoutService;

@Component
public class CheckoutCommand implements Command {

    private final ConsoleIO io;
    private final CartSessionService cartSession;
    private final CheckoutService checkoutService;


    public CheckoutCommand(ConsoleIO io, CartSessionService cartSession, CheckoutService checkoutService) {
        this.io = io;
        this.cartSession = cartSession;
        this.checkoutService = checkoutService;
    }

    @Override
    public String name() { return "checkout"; }

    @Override
    public String help() { return "checkout - create order + reserve stock + simulate payment"; }

    @Override
    public void execute() {
        String email = cartSession.getSelectedCustomerEmail();
        if (email == null) {
            io.println("No customer selected. Run: cart select");
            return;
        }
        if (cartSession.isEmpty()) {
            io.println("Cart is empty. Run: cart add");
            return;
        }

        String methodStr = io.readLine("Payment method (CARD/INVOICE): ").trim().toUpperCase();
        PaymentMethod method;
        try {
            method = PaymentMethod.valueOf(methodStr);
        } catch (Exception e) {
            io.println("Invalid method. Use CARD or INVOICE.");
            return;
        }

        Order order = checkoutService.checkout(email, cartSession.view(), method);
        cartSession.clear();

        io.println("Order created: id=" + order.getId());
        io.println("Status: " + order.getStatus());
        io.println("Total: " + order.getTotal());
        if (!order.getPayments().isEmpty()) {
            var p = order.getPayments().get(order.getPayments().size() - 1);
            io.println("Payment: " + p.getMethod() + " / " + p.getStatus() + " / " + p.getTimestamp());
        }
    }
}