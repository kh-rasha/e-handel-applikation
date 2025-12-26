package se.yh.ehandel.cli.command;

import org.springframework.stereotype.Component;
import se.yh.ehandel.cli.ConsoleIO;
import se.yh.ehandel.service.CartSessionService;

@Component
public class CartShowCommand implements Command {

    private final ConsoleIO io;
    private final CartSessionService cartSession;

    public CartShowCommand(ConsoleIO io, CartSessionService cartSession) {
        this.io = io;
        this.cartSession = cartSession;
    }

    @Override
    public String name() { return "cart show"; }

    @Override
    public String help() { return "cart show - show current cart"; }

    @Override
    public void execute() {
        if (cartSession.getSelectedCustomerEmail() == null) {
            io.println("No customer selected. Run: cart select");
            return;
        }

        io.println("Cart for: " + cartSession.getSelectedCustomerEmail());
        if (cartSession.isEmpty()) {
            io.println("(empty)");
            return;
        }

        cartSession.view().forEach((sku, qty) -> io.println(sku + " x " + qty));
    }
}