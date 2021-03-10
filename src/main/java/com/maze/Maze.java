package com.maze;

import java.util.ArrayList;
import java.util.List;

public class Maze {

    private int mazeHeight;
    private int mazeWidth;
    private int graphHeight;
    private int graphWidth;
    private Node[][] maze;
    private AdjacencyMatrix adjacencyMatrix;
    private List<Edge> edges;
    private List<int[][]> edgesCoordinates;
    private Node mazeEntry;
    private Node mazeExit;


    public Maze(int mazeHeight, int mazeWidth) {
        this.mazeHeight = mazeHeight;
        this.mazeWidth = mazeWidth;
        this.maze = new Node[this.mazeHeight][this.mazeWidth];
        this.graphHeight = mazeHeight % 2 == 0 ? (mazeHeight - 1) / 2 : mazeHeight / 2;
        this.graphWidth = mazeWidth % 2 == 0 ? (mazeWidth - 1) / 2 : mazeWidth / 2;
        this.adjacencyMatrix = new AdjacencyMatrix(graphHeight, graphWidth);
        this.edges = new ArrayList<>();
        this.edgesCoordinates = new ArrayList<>();
    }

    public Maze() {
    }

    public int getMazeHeight() {
        return mazeHeight;
    }

    public int getMazeWidth() {
        return mazeWidth;
    }

    public Node[][] getMaze() {
        return maze;
    }

    public Node getMazeEntry() {
        return mazeEntry;
    }

    public Node getMazeExit() {
        return mazeExit;
    }

    public void build() {
        adjacencyMatrix.generateAdjacencyMatrix();
        primsAlgorithm();
        changeWeightOfEdgesToOne();
        mapEdgesToCoordinates();
        generateMazeFullOfWalls();
        createTunnels();
        createEntranceAndExit();
    }

    private void primsAlgorithm() {
        boolean[] inMST = new boolean[adjacencyMatrix.getNumberOfNodes()];
        inMST[0] = true;

        while (edges.size() < adjacencyMatrix.getNumberOfNodes() - 1) {
            int min = Integer.MAX_VALUE;
            int rowWithMin = -1;
            int columnWithMin = -1;

            for (int row = 0; row < adjacencyMatrix.getNumberOfNodes(); row++) {
                for (int column = 0; column < adjacencyMatrix.getNumberOfNodes(); column++) {
                    if (adjacencyMatrix.getMatrix()[row][column] < min && adjacencyMatrix.getMatrix()[row][column] != 0 && isValidEdge(row, column, inMST)) {
                        min = adjacencyMatrix.getMatrix()[row][column];
                        rowWithMin = row;
                        columnWithMin = column;
                    }
                }
            }

            if (rowWithMin != -1 && columnWithMin != -1) {
                edges.add(new Edge(rowWithMin, columnWithMin));
                inMST[columnWithMin] = inMST[rowWithMin] = true;
            }
        }
    }

    private boolean isValidEdge(int node1, int node2, boolean[] inMST) {
        if (node1 == node2)
            return false;
        if (!inMST[node1] && !inMST[node2])
            return false;
        else return !inMST[node1] || !inMST[node2];
    }

    private void changeWeightOfEdgesToOne() {
        for (int row = 0; row < adjacencyMatrix.getNumberOfNodes(); row++) {
            for (int column = 0; column < adjacencyMatrix.getNumberOfNodes(); column++) {
                if (adjacencyMatrix.getMatrix()[row][column] != 0) {
                    adjacencyMatrix.getMatrix()[row][column] = 1;
                }
            }
        }
    }

    private void mapEdgesToCoordinates() {
        for (Edge edge : edges) {
            int rowCoordinateNode1 = edge.node1 / adjacencyMatrix.getGraphWidth();
            int columnCoordinateNode1 = edge.node1 % adjacencyMatrix.getGraphWidth();
            int rowCoordinateNode2 = edge.node2 / adjacencyMatrix.getGraphWidth();
            int columnCoordinateNode2 = edge.node2 % adjacencyMatrix.getGraphWidth();
            edgesCoordinates.add(new int[][]{{rowCoordinateNode1, columnCoordinateNode1}, {rowCoordinateNode2, columnCoordinateNode2}});
        }
    }

    private void generateMazeFullOfWalls() {
        for (int i = 0; i < mazeHeight; i++) {
            for (int j = 0; j < mazeWidth; j++) {
                maze[i][j] = new Node(i, j);
            }
        }
    }

    private void createTunnels() {
        for (int[][] edgeCoordinate : edgesCoordinates) {
            int rowCoordinateNode1 = 2 * edgeCoordinate[0][0] + 1 ;
            int columnCoordinateNode1 = 2 * edgeCoordinate[0][1] + 1;
            int rowEdgeCoordinate = edgeCoordinate[0][0] + edgeCoordinate[1][0] + 1;
            int columnEdgeCoordinate = edgeCoordinate[0][1] + edgeCoordinate[1][1] + 1;
            int rowCoordinateNode2 = 2 * edgeCoordinate[1][0] + 1;
            int columnCoordinateNode2 = 2 * edgeCoordinate[1][1] + 1;
            maze[rowCoordinateNode1][columnCoordinateNode1].weight = 1;
            maze[rowEdgeCoordinate][columnEdgeCoordinate].weight = 1;
            maze[rowCoordinateNode2][columnCoordinateNode2].weight = 1;
        }

        if (mazeWidth % 2 == 0) {
            for (int i = 0; i < mazeHeight; i++) {
                maze[i][mazeHeight - 2].weight = maze[i][mazeHeight - 3].weight;
            }
        }
        if (mazeHeight % 2 == 0) {
            for (int i = 0; i < mazeWidth; i++) {
                maze[mazeHeight - 2][i].weight = maze[mazeHeight - 3][i].weight;
            }
        }
    }

    private void createEntranceAndExit() {
        for (int i = 2; i < mazeHeight; i++) {
            if (maze[i][1].weight == 1) {
                mazeEntry = maze[i][0];
                mazeEntry.weight = 1;
                break;
            }
        }

        for (int i = mazeHeight - 1; i > 0; i--) {
            if (maze[i][mazeWidth - 2].weight == 1) {
                mazeExit = maze[i][mazeWidth - 1];
                mazeExit.weight = 1;
                break;
            }
        }
    }

    public void initializeMazeFromFile(String importedMaze) {
        String[] importedMazeArray = importedMaze.split("");
        this.mazeHeight = (int) Math.sqrt(importedMaze.length());
        this.mazeWidth = (int) Math.sqrt(importedMaze.length());
        maze = new Node[mazeHeight][mazeWidth];
        generateMazeFullOfWalls();

        int iterator = 0;
        for (int row = 0; row < mazeHeight; row++) {
            for (int column = 0; column < mazeWidth; column++) {
                maze[row][column].weight = Integer.parseInt(importedMazeArray[iterator++]);
            }
        }

        createEntranceAndExit();
    }

    public void displayMaze() {
        for (int row = 0; row < mazeHeight; row++) {
            for (int column = 0; column < mazeWidth; column++) {
                switch(maze[row][column].weight) {
                    case 0:
                        System.out.print("\u2588\u2588");
                        break;
                    case 1 :
                        System.out.print("  ");
                        break;
                    default:
                        System.out.print("//");
                }
            }
            System.out.println();
        }
    }

}
