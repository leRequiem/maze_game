package Maze_files;

public class MazeCreator {
    PathFinder path = new PathFinder();

    public int[][] generateRandomMaze(int size) {

        int[][] mazeMatrix = new int[size][size];

        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 7; j++) {
                if (((int) (Math.random() * 100)) <= 30) {
                    mazeMatrix[i][j] = 1;
                } else
                    mazeMatrix[i][j] = 0;
            }
        }
        // случайное число от 0 до (size - 1)
        int startX = (int)(Math.random() * 1000) % 7;

        // либо 0, либо (size - 1)
        int startY = (int)(Math.random() * 1000) % 2 * (mazeMatrix.length - 1);

        // случайное число от 0 до (size - 1)
        int endX = (int)(Math.random() * 1000) % 7;

        // Если startY = 0, то endY = (size - 1) и наоборот
        int endY = (startY == 0) ? (mazeMatrix.length - 1) : 0;

        while (true) {
            if (path.hasPathToExit(mazeMatrix, startX, startY, endX, endY) > 0) {
                return mazeMatrix;
            } else generateRandomMaze(size);
        }
    }
}
