package se.yh.ehandel.cli;

import org.springframework.stereotype.Component;
import se.yh.ehandel.cli.command.Command;

import java.util.*;

@Component
public class Menu {

    private final ConsoleIO io;
    private final Map<Integer, Command> byNumber = new LinkedHashMap<>();
    private final Map<String, Command> byName = new HashMap<>();

    public Menu(ConsoleIO io, List<Command> commands) {
        this.io = io;

        // Allow text commands as well (optional but useful)
        for (Command c : commands) {
            byName.put(normalize(c.name()), c);
        }

        add(1, "product add");
        add(2, "product list");
        add(3, "product update");
        add(4, "product disable");

        add(5, "category add");
        add(6, "category list");

        // Future extensions (Person 3 / 4)
        // add(7, "customer add");
        // add(8, "cart add");
        // add(9, "checkout");
    }

    public void start() {
        while (true) {
            printMenu();

            String input = io.readLine("Select: ").trim();
            if (input.isBlank()) continue;

            String norm = normalize(input);

            if (norm.equals("0") || norm.equals("exit") || norm.equals("quit")) {
                io.println("Goodbye!");
                return;
            }

            if (norm.equals("help") || norm.equals("?")) {
                printHelp();
                continue;
            }

            // 1) Number-based selection
            Integer choice = tryParseInt(norm);
            if (choice != null) {
                Command cmd = byNumber.get(choice);
                if (cmd == null) {
                    io.println("Invalid choice. Type 'help' or select a number from the menu.");
                    continue;
                }
                run(cmd);
                continue;
            }

            // 2) Text command fallback
            Command cmd = byName.get(norm);
            if (cmd != null) {
                run(cmd);
            } else {
                io.println("Unknown command. Select a number or type 'help'.");
            }
        }
    }

    private void run(Command cmd) {
        try {
            io.println("\n--- " + cmd.name() + " ---");
            cmd.execute();
        } catch (Exception e) {
            io.println("ERROR: " + e.getMessage());
        }
    }

    private void add(int number, String commandName) {
        Command cmd = byName.get(normalize(commandName));
        if (cmd == null) {
            throw new IllegalStateException(
                    "Command not found: " + commandName +
                            ". Make sure Command.name() matches exactly."
            );
        }
        byNumber.put(number, cmd);
    }

    private void printMenu() {
        io.println("\n======================================");
        io.println("            E-COMMERCE CLI");
        io.println("======================================");

        io.println("\n[Product]");
        io.println(" 1) Add product");
        io.println(" 2) List products");
        io.println(" 3) Update product");
        io.println(" 4) Disable product");

        io.println("\n[Category]");
        io.println(" 5) Add category");
        io.println(" 6) List categories");

        io.println("\n 0) Exit");
        io.println("--------------------------------------");
        io.println("Tip: type 'help' to see all commands.");
    }

    private void printHelp() {
        io.println("\nAvailable commands:");
        for (var entry : byNumber.entrySet()) {
            io.println(" " + entry.getKey() + ") " +
                    entry.getValue().name() + " - " +
                    entry.getValue().help());
        }
    }

    private String normalize(String s) {
        return s.trim().replaceAll("\\s+", " ").toLowerCase(Locale.ROOT);
    }

    private Integer tryParseInt(String s) {
        try {
            return Integer.parseInt(s);
        } catch (Exception e) {
            return null;
        }
    }
}