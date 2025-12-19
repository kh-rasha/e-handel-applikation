package se.yh.ehandel.cli.command;

public interface Command {
    String name();     // ex "product add"
    String help();     // visas i help-menyn
    void execute();
}
