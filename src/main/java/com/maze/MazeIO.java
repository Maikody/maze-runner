package com.maze;

import com.maze.exceptions.FileFormatException;

import java.io.*;
import java.util.stream.Collectors;

public class MazeIO {
    private final Maze maze;

    public MazeIO(Maze maze) {
        this.maze = maze;
    }

    public boolean loadMaze(String fileName) {
        File file = new File(fileName);

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            if (isNotTXTFile(file)) {
                throw new FileFormatException();
            }
            String importedMaze = br.lines().collect(Collectors.joining());
            maze.initializeMazeFromFile(importedMaze);
            return true;
        } catch (FileNotFoundException e) {
            System.out.println("The file " + file.getName() + " does not exist\n");
        } catch (FileFormatException | IOException e) {
            System.out.println("Cannot load the maze. It has an invalid format.\n");
        }

        return false;
    }

    private boolean isNotTXTFile(File file) {
        return !file.getName().endsWith("txt");
    }

    public void saveMaze(String fileName) {
        File file = new File(fileName);

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
            if (isNotTXTFile(file)) {
                throw new FileFormatException();
            }
            bw.write(mazeToString());
        } catch (FileNotFoundException e) {
            System.out.println("The file " + file.getName() + " does not exist");
        } catch (FileFormatException | IOException e) {
            System.out.println("Cannot load the maze. It has an invalid format.");
        }
    }

    public String mazeToString() {
        StringBuilder stringBuilder = new StringBuilder();

        for (int row = 0; row < maze.getMazeHeight(); row++) {
            for (int column = 0; column < maze.getMazeWidth(); column++) {
                stringBuilder.append(maze.getMaze()[row][column].weight);
            }
            stringBuilder.append(System.lineSeparator());
        }

        return stringBuilder.toString() ;
    }
}
