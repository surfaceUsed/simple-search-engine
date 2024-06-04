package application;

import application.logic.Controller;

public class Main {

    public static void main(String[] args) {

        // Takes path to file "src/application/data/data.txt" as command line argument. 
        new Controller(args[0]).run();
    }
}
