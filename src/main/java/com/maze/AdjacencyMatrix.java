package com.maze;

import java.util.Random;

public class AdjacencyMatrix {

    private final int graphHeight;
    private final int graphWidth;
    private final int numberOfNodes;
    private final int[][] adjacencyMatrix;
    private final Random random;

    public AdjacencyMatrix(int graphHeight, int graphWidth) {
        this.graphHeight = graphHeight;
        this.graphWidth = graphWidth;
        this.numberOfNodes = this.graphHeight * this.graphWidth;
        this.adjacencyMatrix = new int[numberOfNodes][numberOfNodes];
        this.random = new Random();
    }

    public int[][] getMatrix() {
        return adjacencyMatrix;
    }

    public int getGraphHeight() {
        return graphHeight;
    }

    public int getGraphWidth() {
        return graphWidth;
    }

    public int getNumberOfNodes() {
        return numberOfNodes;
    }

    public void generateAdjacencyMatrix() {
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

}
