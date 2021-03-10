package com.maze;

import com.maze.ui.UserInterface;
import com.maze.ui.WorkMode;

import java.util.Scanner;

public class MazeController {

    private static final Scanner scanner = new Scanner(System.in);
    private static WorkMode workMode = WorkMode.SIMPLE;
    private static MazeProcessor mazeProcessor = new MazeProcessor();

    public static void run() {
        while (true) {
           switch (UserInterface.getUserSelection(workMode)) {
               case GENERATE_MAZE:
                   System.out.println("Enter the size of a new maze:");
                   mazeProcessor = new MazeProcessor(UserInterface.validateIntegerInput(scanner));
                   mazeProcessor.generateMaze();
                   workMode = WorkMode.FULL;
                   break;
               case LOAD_MAZE:
                   System.out.println("Enter the file name:");
                   if (mazeProcessor.loadMaze(scanner.nextLine()))
                        workMode = WorkMode.FULL;
                   break;
               case SAVE_MAZE:
                   System.out.println("Enter the file name:");
                   mazeProcessor.saveMaze(scanner.nextLine());
                   break;
               case DISPLAY_MAZE:
                   mazeProcessor.displayMaze();
                   break;
               case SOLVE_MAZE:
                   mazeProcessor.solveMaze();
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
