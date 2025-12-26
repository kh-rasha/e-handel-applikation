package se.yh.ehandel.cli.command;

import org.springframework.stereotype.Component;
import se.yh.ehandel.cli.ConsoleIO;
import se.yh.ehandel.service.CartSessionService;
import se.yh.ehandel.service.CustomerService;

@Component
public class CartSelectCommand implements Command {

    private final ConsoleIO io;
    private final CustomerService customerService;
    private final CartSessionService cartSession;

    public CartSelectCommand(ConsoleIO io, CustomerService customerService, CartSessionService cartSession) {
        this.io = io;
        this.customerService = customerService;
        this.cartSession = cartSession;
    }

    @Override
    public String name() { return "cart select"; }

    @Override
    public String help() { return "cart select - select customer for cart (by email)"; }

    @Override
    public void execute() {
        String email = io.readLine("Customer email: ").trim();
        customerService.findByEmail(email); // validera att kunden finns
        cartSession.selectCustomer(email);
        io.println("Selected customer for cart: " + email);
    }
}