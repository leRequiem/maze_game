package Maze_work;

public class Maze {

    private int[][] maze;
    private int rowsCount;
    private int columnsCount;
    private int startX;
    private int startY;
    private int endX;
    private int endY;
    private int steps;
    private int currentX;
    private int currentY;

    public Maze(int[][] maze, int startX, int startY, int endX, int endY) {
        this.maze = maze;
        this.rowsCount = maze.length;
        this.columnsCount = maze[0].length;
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
        this.steps = 0;
        this.currentX = startX;
        this.currentY = startY;
    }

    public boolean move(String moveDirection) {
        int newRow = currentX;
        int newColumn = currentY;

        switch (moveDirection) {
            case "u" -> newRow--;
            case "r" -> newColumn++;
            case "d" -> newRow++;
            case "l" -> newColumn--;
        }

        if (moveIsPossible(newRow, newColumn)) {
            currentX = newRow;
            currentY = newColumn;
            steps++;
            return true;
        } else
            return false;
    }

    private boolean moveIsPossible(int row, int column) {
        return row >= 0 && row < rowsCount && column >= 0 && column < columnsCount && maze[row][column] == 0;
    }

    public boolean exitWasReached() {
        return currentX == endX && currentY == endY;
    }

    public int getSteps() {
        return steps;
    }

    public void drawMaze() {
        for (int i = 0; i < rowsCount; i++) {
            for (int j = 0; j < columnsCount; j++) {
                if (i == startX && j == startY) {
                    System.out.print("S");
                } else if (i == endX && j == endY) {
                    System.out.print("E");
                } else if (maze[i][j] == 0) {
                    System.out.print("0");
                } else
                    System.out.print("1");
                System.out.print(" ");
            }
            System.out.println();
        }
    }
}
