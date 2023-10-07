package Maze_files;

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

    public int[][] getMaze() {
        return maze;
    }


}
