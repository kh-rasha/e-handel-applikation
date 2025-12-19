package se.yh.ehandel.cli;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class CliRunner implements CommandLineRunner {
    private final Menu menu;

    public CliRunner(Menu menu) {
        this.menu = menu;
    }

    @Override
    public void run(String... args) {
        menu.start();
    }
}
