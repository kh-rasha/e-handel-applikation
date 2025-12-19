package se.yh.ehandel.cli;

import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
public class ConsoleIO {

    private final Scanner scanner = new Scanner(System.in);

    public String readLine(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    public void println(String text) {
        System.out.println(text);
    }
}
