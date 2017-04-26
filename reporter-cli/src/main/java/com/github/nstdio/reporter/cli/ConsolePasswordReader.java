package com.github.nstdio.reporter.cli;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Scanner;

class ConsolePasswordReader {

    char[] read() {
        Scanner scanner = new Scanner(System.in);
        PrintStream out = System.out;
        System.setOut(new PrintStream(new OutputStream() {
            @Override
            public void write(int b) throws IOException {
            }

        }));

        final char[] chars = scanner.nextLine().toCharArray();
        System.setOut(out);
        return chars;
    }
}
