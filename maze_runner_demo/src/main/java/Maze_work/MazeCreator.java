package Maze_work;

import lombok.Getter;

public class MazeCreator {

    @Getter
    private int[][] maze;
    private final int rowsCount;
    private final int columnsCount;

    private static int startX;
    private static int startY;
    private static int endX;
    private static int endY;

    public MazeCreator(int columnsCount, int rowsCount) {
        this.columnsCount = columnsCount;
        this.rowsCount = rowsCount;
        this.maze = generateRandomMaze();
    }

    private int[][] generateRandomMaze() {

        int[][] mazeMatrix = new int[rowsCount][columnsCount];

        for (int i = 0; i < rowsCount; i++) {
            for (int j = 0; j < columnsCount; j++) {
                if (((int) (Math.random() * 100)) <= 30) {
                    mazeMatrix[i][j] = 1;
                } else
                    mazeMatrix[i][j] = 0;
            }
        }
        // случайное число от 0 до (rowsCount - 1)
        startX = (int)(Math.random() * 1000) % 7;

        // либо 0, либо (columnsCount - 1)
        startY = (int)(Math.random() * 1000) % 2 * (columnsCount - 1);

        // случайное число от 0 до (rowsCount - 1)
        endX = (int)(Math.random() * 1000) % 7;

        // Если startY = 0, то endY = (columnsCount - 1) и наоборот
        endY = (startY == 0) ? (columnsCount - 1) : 0;

        MazeCoords start = new MazeCoords(startX, startY);
        MazeCoords exit = new MazeCoords(endX, endY);

        while (true) {
            if (PathFinder.hasExitPath(mazeMatrix, start, exit)) {
                return mazeMatrix;
            } else mazeMatrix = generateRandomMaze();
        }
    }

    public static int getStartX() {
        return startX;
    }

    public static int getStartY() {
        return startY;
    }

    public static int getEndX() {
        return endX;
    }

    public static int getEndY() {
        return endY;
    }
}
