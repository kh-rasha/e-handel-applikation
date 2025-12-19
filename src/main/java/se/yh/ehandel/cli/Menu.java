package se.yh.ehandel.cli;

import org.springframework.stereotype.Component;
import se.yh.ehandel.cli.command.Command;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
public class Menu {
    private final ConsoleIO io;
    private final Map<String, Command> commands = new LinkedHashMap<>();

    public Menu(ConsoleIO io, List<Command> commandList) {
        this.io = io;

        // registrera alla kommandon
        for (Command c : commandList) {
            commands.put(c.name(), c);
        }
    }

    public void start() {
        io.println("E-handel CLI. Skriv 'help' för kommandon, 'exit' för att avsluta.");

        while (true) {
            String input = io.readLine("> ").trim();

            if (input.isBlank()) continue;

            if (input.equalsIgnoreCase("exit")) {
                io.println("Bye!");
                return;
            }

            if (input.equalsIgnoreCase("help")) {
                commands.values().forEach(c -> io.println(c.help()));
                continue;
            }

            Command cmd = commands.get(input);
            if (cmd == null) {
                io.println("Okänt kommando. Skriv 'help'.");
                continue;
            }

            try {
                cmd.execute();
            } catch (Exception e) {
                io.println("ERROR: " + e.getMessage());
            }
        }
    }
}
