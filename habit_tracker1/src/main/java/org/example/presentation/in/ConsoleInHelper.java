package org.example.presentation.in;

import java.util.Scanner;

public class ConsoleInHelper {
    private static final Scanner scanner = new Scanner(System.in);

    private ConsoleInHelper() {
    }

    public static int readInt() throws NumberFormatException {
        return Integer.parseInt(readLine());
    }

    public static String readLine() {
        return scanner.nextLine();
    }
}
