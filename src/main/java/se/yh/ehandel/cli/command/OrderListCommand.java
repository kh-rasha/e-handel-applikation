package se.yh.ehandel.cli.command;

import org.springframework.stereotype.Component;
import se.yh.ehandel.cli.ConsoleIO;
import se.yh.ehandel.domain.enums.OrderStatus;
import se.yh.ehandel.service.OrderService;

@Component
public class OrderListCommand implements Command {

    private final ConsoleIO io;
    private final OrderService orderService;

    public OrderListCommand(ConsoleIO io, OrderService orderService) {
        this.io = io;
        this.orderService = orderService;
    }

    @Override
    public String name() { return "order list"; }

    @Override
    public String help() { return "order list - list orders (optional filter asked interactively)"; }

    @Override
    public void execute() {
        String filter = io.readLine("Filter status (blank=all, NEW/PAID/CANCELLED): ").trim().toUpperCase();
        OrderStatus status = null;
        if (!filter.isBlank()) {
            try {
                status = OrderStatus.valueOf(filter);
            } catch (IllegalArgumentException e) {
                io.println("Invalid status. Use NEW, PAID or CANCELLED.");
                return;
            }
        }

        orderService.list(status).forEach(o ->
                io.println(o.getId() + " | " + o.getCustomer().getEmail() + " | " + o.getStatus() + " | " + o.getTotal())
        );
    }
}