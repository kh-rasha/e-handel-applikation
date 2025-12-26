package se.yh.ehandel.cli.command;

import org.springframework.stereotype.Component;
import se.yh.ehandel.cli.ConsoleIO;
import se.yh.ehandel.service.CartSessionService;

@Component
public class CartAddCommand implements Command {

    private final ConsoleIO io;
    private final CartSessionService cartSession;

    public CartAddCommand(ConsoleIO io, CartSessionService cartSession) {
        this.io = io;
        this.cartSession = cartSession;
    }

    @Override
    public String name() { return "cart add"; }

    @Override
    public String help() { return "cart add - add sku + qty to cart"; }

    @Override
    public void execute() {
        if (cartSession.getSelectedCustomerEmail() == null) {
            io.println("No customer selected. Run: cart select");
            return;
        }

        String sku = io.readLine("SKU: ").trim();
        int qty;
        try {
            qty = Integer.parseInt(io.readLine("Qty: ").trim());
        } catch (NumberFormatException e) {
            io.println("Qty must be a number.");
            return;
        }

        if (qty <= 0) {
            io.println("Qty must be greater than 0.");
            return;
        }

        cartSession.add(sku, qty);
        io.println("Added to cart: " + sku + " x " + qty);
    }
}
