package com.maze;

public class MazeProcessor {
    private final Maze maze;
    private final MazeSolver mazeSolver;
    private final MazeIO mazeIO;

    public MazeProcessor(int mazeHeight) {
        this.maze = new Maze(mazeHeight, mazeHeight);
        this.mazeSolver = new MazeSolver(maze);
        this.mazeIO = new MazeIO(maze);
    }

    public MazeProcessor() {
        this.maze = new Maze();
        this.mazeSolver = new MazeSolver(maze);
        this.mazeIO = new MazeIO(maze);
    }

    public void generateMaze() {
        maze.build();
    }

    public void displayMaze() {
        maze.displayMaze();
    }

    public boolean loadMaze(String filename) {
       return mazeIO.loadMaze(filename);
    }

    public void saveMaze(String filename) {
        mazeIO.saveMaze(filename);
    }

    public void solveMaze() {
        mazeSolver.solveMaze();
        displayMaze();
        mazeSolver.deletePathFromMaze();
    }

}
