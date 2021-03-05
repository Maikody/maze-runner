package com.maze;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class Maze {
    private int gridHeight;
    private int gridWidth;
    private int graphHeight;
    private int graphWidth;
    private int numberOfNodes;

    private int[][] adjacencyMatrix;
    private Node[][] maze;
    private Node src;
    private Node dest;

    private final Random random = new Random();
    private final List<Edge> edges = new ArrayList<>();
    private final List<int[][]> edgesCoordinates = new ArrayList<>();
    private final int[] adjCellRowCoords = {-1, 0, 0, 1};
    private final int[] adjCellColumnCoords = {0, -1, 1, 0};

    public Maze() {
    }

    public Maze(int height) {
        this.gridHeight = height;
        this.gridWidth = height;
        this.maze = new Node[gridHeight][gridWidth];
        this.graphHeight = height % 2 == 0 ? (this.gridHeight - 1) / 2 : this.gridHeight / 2;
        this.graphWidth = height % 2 == 0 ? (this.gridWidth - 1) / 2 : this.gridWidth / 2;
        this.numberOfNodes = graphHeight * graphWidth;
        this.adjacencyMatrix = new int[numberOfNodes][numberOfNodes];
        generateMaze();
    }

    private void generateMaze() {
        generateAdjacencyMatrix();
        primsAlgorithm();
        mapEdgesToCoordinates();
        generateMazeOfWalls();
        createTunnels();
        createEntranceAndExit();
    }

    private void generateAdjacencyMatrix() {
        for (int i = 0; i < numberOfNodes; i++) {
            int widthIterator = 1;
            for (int j = 0; j < numberOfNodes; j++) {
                if (isSameNode(i, j)) continue;
                putRandomValue(i, widthIterator, j);
                widthIterator = moveToNextLine(widthIterator);
                putMirrorValue(i, j);
            }
        }
    }

    private void putRandomValue(int i, int widthIterator, int j) {
        if ((j == i + 1 && widthIterator != graphWidth) || j == i + graphWidth) {
            adjacencyMatrix[i][j] = random.nextInt(9) + 1;
        }
    }

    private boolean isSameNode(int i, int j) {
        if (i == j) {
            adjacencyMatrix[i][j] = 0;
            return true;
        }
        return false;
    }

    private void putMirrorValue(int i, int j) {
        adjacencyMatrix[j][i] = adjacencyMatrix[i][j];
    }

    private int moveToNextLine(int widthIterator) {
        if (widthIterator == graphWidth) {
            widthIterator = 1;
        } else {
            widthIterator++;
        }
        return widthIterator;
    }

    private void primsAlgorithm() {
        boolean[] inMST = new boolean[numberOfNodes];
        inMST[0] = true;

        while (edges.size() < numberOfNodes - 1) {
            int min = Integer.MAX_VALUE;
            int rowWithMin = -1, columnWithMin = -1;

            for (int row = 0; row < numberOfNodes; row++) {
                for (int column = 0; column < numberOfNodes; column++) {
                    if (adjacencyMatrix[row][column] < min && adjacencyMatrix[row][column] != 0 && isValidEdge(row, column, inMST)) {
                        min = adjacencyMatrix[row][column];
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

        for (int row = 0; row < numberOfNodes; row++) {
            for (int column = 0; column < numberOfNodes; column++) {
                if (adjacencyMatrix[row][column] != 0) {
                    adjacencyMatrix[row][column] = 1;
                }
            }
        }
    }

    private boolean isValidEdge(int u, int v, boolean[] inMST) {
        if (u == v)
            return false;
        if (!inMST[u] && !inMST[v])
            return false;
        else return !inMST[u] || !inMST[v];
    }

    private void mapEdgesToCoordinates() {
        for (Edge edge : edges) {
            int rowCoordinateNode1 = edge.node1 / graphWidth;
            int columnCoordinateNode1 = edge.node1 % graphWidth;
            int rowCoordinateNode2 = edge.node2 / graphWidth;
            int columnCoordinateNode2 = edge.node2 % graphWidth;
            edgesCoordinates.add(new int[][]{{rowCoordinateNode1, columnCoordinateNode1}, {rowCoordinateNode2, columnCoordinateNode2}});
        }
    }

    private void generateMazeOfWalls() {
        for (int i = 0; i < gridHeight; i++) {
            for (int j = 0; j < gridWidth; j++) {
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

        if (gridWidth % 2 == 0) {
            for (int i = 0; i < gridHeight; i++) {
                maze[i][gridHeight - 2].weight = maze[i][gridHeight - 3].weight;
            }
        }
        if (gridHeight % 2 == 0) {
            for (int i = 0; i < gridWidth; i++) {
                maze[gridHeight - 2][i].weight = maze[gridHeight - 3][i].weight;
            }
        }
    }

    private void createEntranceAndExit() {
        for (int i = 2; i < gridHeight; i++) {
            if (maze[i][1].weight == 1) {
                src = maze[i][0];
                src.weight = 1;
                break;
            }
        }

        for (int i = gridHeight - 1; i > 0; i--) {
            if (maze[i][gridWidth - 2].weight == 1) {
                dest = maze[i][gridWidth - 1];
                dest.weight = 1;
                break;
            }
        }
    }

    public void drawMaze() {
        for (int row = 0; row < gridHeight; row++) {
            for (int column = 0; column < gridWidth; column++) {
                switch(maze[row][column].weight) {
                    case -1:
                        System.out.print("//");
                        break;
                    case 0:
                        System.out.print("\u2588\u2588");
                        break;
                    case 1 :
                        System.out.print("  ");
                        break;
                }
            }
            System.out.println();
        }
    }

    public String mazeToString() {
        StringBuilder stringBuilder = new StringBuilder();

        for (int row = 0; row < gridHeight; row++) {
            for (int column = 0; column < gridWidth; column++) {
                stringBuilder.append(maze[row][column].weight);
            }
            stringBuilder.append(System.lineSeparator());
        }

        return stringBuilder.toString() ;
    }

    public boolean loadMaze(String fileName) {
        File file  = new File(fileName);

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            if (!file.getName().endsWith("txt")) {
                throw new FileFormatException();
            }
            String importedMaze = br.lines().collect(Collectors.joining());
            initializeMazeFromFile(importedMaze);
            return true;
        } catch (FileNotFoundException e) {
            System.out.println("The file " + file.getName() + " does not exist\n");
        } catch (FileFormatException | IOException e) {
            System.out.println("Cannot load the maze. It has an invalid format.\n");
        }

        return false;
    }

    public void initializeMazeFromFile(String importedMaze) {
        String[] sequence = importedMaze.split("");
        this.gridHeight = (int) Math.sqrt(importedMaze.length());
        this.gridWidth = (int) Math.sqrt(importedMaze.length());
        maze = new Node[gridHeight][gridWidth];
        generateMazeOfWalls();

        int iterator = 0;
        for (int row = 0; row < gridHeight; row++) {
            for (int column = 0; column < gridWidth; column++) {
                maze[row][column].weight = Integer.parseInt(sequence[iterator++]);
            }
        }

        createEntranceAndExit();
    }

    public void saveMaze(String fileName) {
        File file  = new File(fileName);

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
            if (!file.getName().endsWith("txt")) {
                throw new FileFormatException();
            }
            bw.write(mazeToString());
        } catch (FileNotFoundException e) {
            System.out.println("The file " + file.getName() + " does not exist");
        } catch (FileFormatException | IOException e) {
            System.out.println("Cannot load the maze. It has an invalid format.");
        }

    }

    public void solveMaze() {
        boolean[][] visited = new boolean[gridHeight][gridWidth];

        int[][] distances = new int[gridHeight][];
        for (int i = 0; i < gridHeight; i++) {
            distances[i] = new int[gridWidth];
            Arrays.fill(distances[i], 0);
        }

        visited[src.x][src.y] = true;
        maze[src.x][src.y].weight = -1;

        Queue<QueueNode> q = new LinkedList<>();

        QueueNode s = new QueueNode(src, 0);
        q.add(s);

        while (!q.isEmpty()) {
            QueueNode currentNode = q.peek();
            Node pt = currentNode.pt;

            if (pt.x == dest.x && pt.y == dest.y) {
                maze[dest.x][dest.y].weight = -1;

                for (int i = 0; i < gridHeight; i++){
                    for(int j = 0; j < gridWidth; j++){
                        System.out.print(distances[i][j] + " ");
                    }
                    System.out.println();
                }

                for (int d = distances[dest.x][dest.y] - 1; d >= 0; d--) {
                    for (int i = 0; i < 4; i++) {
                        int row = pt.x + adjCellRowCoords[i];
                        int col = pt.y + adjCellColumnCoords[i];

                        if (isInRange(row, col, gridWidth, gridHeight) && distances[row][col] == d) {
                            maze[row][col].weight = -1;
                            pt = new Node(row, col);
                            break;
                        }
                    }
                }

            }

            q.remove();

            for (int i = 0; i < 4; i++) {
                int row = pt.x + adjCellRowCoords[i];
                int col = pt.y + adjCellColumnCoords[i];

                if (isInRange(row, col, gridWidth, gridHeight) && maze[row][col].weight == 1 && !visited[row][col]) {
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
        for (int i = 0; i < gridHeight; i++) {
            for (int j = 0; j < gridWidth; j++) {
                if (maze[i][j].weight == -1) {
                    maze[i][j].weight = 1;
                }
            }
        }
    }

}
