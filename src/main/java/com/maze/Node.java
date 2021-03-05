package com.maze;

public class Node {
    int x;
    int y;
    int weight;

    public Node(int x, int y) {
        this.x = x;
        this.y = y;
        this.weight = 0;
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}
