package com.github.nstdio.reporter.cli;

import java.util.Scanner;

class Prompt {

    static void prompt(String question, Action yesAction, Action noAction) {
        Scanner scanner = new Scanner(System.in);
        System.out.printf("%s (yes/no) ? ", question);

        String answer = scanner.next();

        switch (answer) {
            case "y":
            case "yes":
                yesAction.execute();
                break;
            case "n":
            case "no":
                noAction.execute();
                break;
            default:
                noAction.execute();
                break;
        }
    }

    interface Action {
        void execute();
    }
}
