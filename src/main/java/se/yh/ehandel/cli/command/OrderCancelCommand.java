package se.yh.ehandel.cli.command;

import org.springframework.stereotype.Component;
import se.yh.ehandel.cli.ConsoleIO;
import se.yh.ehandel.service.OrderService;

@Component
public class OrderCancelCommand implements Command {

    private final ConsoleIO io;
    private final OrderService orderService;

    public OrderCancelCommand(ConsoleIO io, OrderService orderService) {
        this.io = io;
        this.orderService = orderService;
    }

    @Override
    public String name() { return "order cancel"; }

    @Override
    public String help() { return "order cancel - cancel NEW order (asks for id)"; }

    @Override
    public void execute() {
        Long id = Long.parseLong(io.readLine("Order id: ").trim());
        var o = orderService.cancel(id);
        io.println("Cancelled order " + o.getId() + ". Status=" + o.getStatus());
    }
}