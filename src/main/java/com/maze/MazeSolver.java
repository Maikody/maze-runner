package com.maze;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

public class MazeSolver {

    private final Maze maze;
    private final int[] adjacentNodeRowCoordinates = {-1, 0, 0, 1};
    private final int[] adjacentNodeColumnCoordinates = {0, -1, 1, 0};

    public MazeSolver(Maze maze) {
        this.maze = maze;
    }

    public void solveMaze() {
        boolean[][] visited = new boolean[maze.getMazeHeight()][maze.getMazeWidth()];

        int[][] distances = new int[maze.getMazeHeight()][];
        for (int i = 0; i < maze.getMazeHeight(); i++) {
            distances[i] = new int[maze.getMazeWidth()];
            Arrays.fill(distances[i], 0);
        }

        visited[maze.getMazeEntry().x][maze.getMazeEntry().y] = true;
        maze.getMaze()[maze.getMazeEntry().x][maze.getMazeEntry().y].weight = -1;

        Queue<QueueNode> q = new LinkedList<>();

        QueueNode s = new QueueNode(maze.getMazeEntry(), 0);
        q.add(s);

        while (!q.isEmpty()) {
            QueueNode currentNode = q.peek();
            Node pt = currentNode.pt;

            if (pt.x == maze.getMazeExit().x && pt.y == maze.getMazeExit().y) {
                maze.getMaze()[maze.getMazeExit().x][maze.getMazeExit().y].weight = -1;

                for (int d = distances[maze.getMazeExit().x][maze.getMazeExit().y] - 1; d >= 0; d--) {
                    for (int i = 0; i < 4; i++) {
                        int row = pt.x + adjacentNodeRowCoordinates[i];
                        int col = pt.y + adjacentNodeColumnCoordinates[i];

                        if (isInRange(row, col, maze.getMazeHeight(), maze.getMazeWidth()) && distances[row][col] == d) {
                            maze.getMaze()[row][col].weight = -1;
                            pt = new Node(row, col);
                            break;
                        }
                    }
                }
            }

            q.remove();

            for (int i = 0; i < 4; i++) {
                int row = pt.x + adjacentNodeRowCoordinates[i];
                int col = pt.y + adjacentNodeColumnCoordinates[i];

                if (isInRange(row, col, maze.getMazeWidth(), maze.getMazeHeight()) && maze.getMaze()[row][col].weight == 1 && !visited[row][col]) {
                    visited[row][col] = true;
                    QueueNode neighbourPoint = new QueueNode(new Node(row, col),currentNode.dist + 1);
                    q.add(neighbourPoint);
                    distances[row][col] = currentNode.dist + 1;
                }
            }
        }
    }

    private boolean isInRange(int row, int col, int maxRow, int maxCol) {
        return row >= 0 && row < maxRow && col >= 0 && col < maxCol;
    }

    public void deletePathFromMaze() {
        for (int i = 0; i < maze.getMazeHeight(); i++) {
            for (int j = 0; j < maze.getMazeWidth(); j++) {
                if (maze.getMaze()[i][j].weight == -1) {
                    maze.getMaze()[i][j].weight = 1;
                }
            }
        }
    }

}
