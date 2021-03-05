package com.maze;

import java.util.Scanner;

public class MazeController {

    private static WorkMode workMode = WorkMode.SIMPLE;
    private static Maze maze = new Maze();
    private static final Scanner scanner = new Scanner(System.in);

    public static void run() {
        while (true) {
           switch (UserInterface.getUserSelection(workMode)) {
               case GENERATE_MAZE:
                   System.out.println("Enter the size of a new maze:");
                   maze = new Maze(UserInterface.validateIntegerInput(scanner.nextLine()));
                   workMode = WorkMode.FULL;
                   maze.drawMaze();
                   break;
               case LOAD_MAZE:
                   if (maze.loadMaze(scanner.nextLine()))
                        workMode = WorkMode.FULL;
                   break;
               case SAVE_MAZE:
                   maze.saveMaze(scanner.nextLine());
                   break;
               case DISPLAY_MAZE:
                   maze.drawMaze();
                   break;
               case SOLVE_MAZE:
                   maze.solveMaze();
                   maze.drawMaze();
                   maze.deletePathFromMaze();
                   break;
               case EXIT:
                   System.out.println("Bye!");
                   scanner.close();
                   System.exit(0);
                   break;
           }
        }
    }
}
