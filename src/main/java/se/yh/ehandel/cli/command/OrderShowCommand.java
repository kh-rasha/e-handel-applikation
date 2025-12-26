package se.yh.ehandel.cli.command;

import org.springframework.stereotype.Component;
import se.yh.ehandel.cli.ConsoleIO;
import se.yh.ehandel.service.OrderService;

@Component
public class OrderShowCommand implements Command {

    private final ConsoleIO io;
    private final OrderService orderService;

    public OrderShowCommand(ConsoleIO io, OrderService orderService) {
        this.io = io;
        this.orderService = orderService;
    }

    @Override
    public String name() { return "order show"; }

    @Override
    public String help() { return "order show - show order details (asks for id)"; }

    @Override
    public void execute() {
        String input = io.readLine("Order id: ").trim();
        Long id;

        try {
            id = Long.parseLong(input);
        } catch (NumberFormatException e) {
            io.println("Invalid order id. Must be a number.");
            return;
        }

        var o = orderService.get(id);

        io.println("Order " + o.getId() + " | " + o.getCustomer().getEmail() + " | " + o.getStatus());
        io.println("Total: " + o.getTotal() + " | Created: " + o.getCreatedAt());

        io.println("Items:");
        o.getItems().forEach(i ->
                io.println("- " + i.getProduct().getSku() + " | " + i.getProduct().getName() +
                        " | qty=" + i.getQty() +
                        " | unit=" + i.getUnitPrice() +
                        " | line=" + i.getLineTotal())
        );

        io.println("Payments:");
        o.getPayments().forEach(p ->
                io.println("- " + p.getMethod() + " | " + p.getStatus() + " | " + p.getTimestamp())
        );
    }
}