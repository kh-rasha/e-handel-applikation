package se.yh.ehandel.cli;

import java.util.Scanner;

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
