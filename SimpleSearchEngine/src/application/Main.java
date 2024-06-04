package application;

import application.logic.Controller;

public class Main {

    public static void main(String[] args) {

        new Controller(args[0]).run();
    }
}
