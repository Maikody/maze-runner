package com.maze.ui;

import java.util.Scanner;

public class UserInterface {

    public static UserSelection getUserSelection(WorkMode workMode) {

        Scanner scanner = new Scanner(System.in);

        displayOptions(workMode);

        while(true) {
            int n = validateIntegerInput(scanner);
            if (workMode == WorkMode.SIMPLE) {
                switch (n) {
                    case 1: return UserSelection.GENERATE_MAZE;
                    case 2: return UserSelection.LOAD_MAZE;
                    case 0: return UserSelection.EXIT;
                    default: System.out.println("Wrong number, try again");
                }
            } else {
                switch (n) {
                    case 3: return UserSelection.SAVE_MAZE;
                    case 4: return UserSelection.DISPLAY_MAZE;
                    case 5: return UserSelection.SOLVE_MAZE;
                    case 1: return UserSelection.GENERATE_MAZE;
                    case 2: return UserSelection.LOAD_MAZE;
                    case 0: return UserSelection.EXIT;
                    default: System.out.println("Wrong number, try again");
                }
            }
        }
    }

    public static int validateIntegerInput(Scanner scanner) {
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Enter only numbers!");
            }
        }
    }

    private static void displayOptions(WorkMode workMode) {
        if (workMode == WorkMode.SIMPLE) {
            printSimpleMenu();
        } else {
            printFullMenu();
        }
    }

    public static void printSimpleMenu() {
        String menu = "=== Menu ===\n" +
                "1. Generate a new maze\n" +
                "2. Load a maze\n" +
                "0. Exit";
        System.out.println(menu);
    }

    public static void printFullMenu() {
        String menu = "=== Menu ===\n" +
                "1. Generate a new maze\n" +
                "2. Load a maze\n" +
                "3. Save the maze\n" +
                "4. Display the maze\n" +
                "5. Find the escape\n" +
                "0. Exit";
        System.out.println(menu);
    }
}
