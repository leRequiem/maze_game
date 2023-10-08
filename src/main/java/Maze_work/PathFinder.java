package Maze_work;

import java.util.LinkedList;
import java.util.Queue;

public class PathFinder {

    public static int shortestPathToExit(int[][] matrix, MazeCoords start, MazeCoords exit) {

        int rows = matrix.length;
        int cols = matrix[0].length;
        boolean[][] visited = new boolean[rows][cols];
        int steps = 0;

        // Начало BFS
        Queue<int[]> queue = new LinkedList<>();
        queue.offer(new int[] {start.x, start.y});
        visited[start.x][start.x] = true;


        int[][] directions = { {-1, 0}, {1, 0}, {0, -1}, {0, 1} };

        while (!queue.isEmpty()) {
            int[] current = queue.poll();
            int x = current[0];
            int y = current[1];

            if (x == exit.x && y == exit.y) {
                // Достигли точки выхода
                return steps;
            }

            // Проверка соседних ячеек
            for (int[] dir : directions) {
                int newX = x + dir[0];
                int newY = y + dir[1];

                if (newX >= 0 && newX < rows && newY >= 0 && newY < cols
                        && matrix[newX][newY] == 0 && !visited[newX][newY]) {
                    queue.offer(new int[] {newX, newY});
                    visited[newX][newY] = true;
                }
            }
            steps++;
        }

        // Путь не найден
        return -1;
    }

    public static boolean hasExitPath(int[][] matrix, MazeCoords start, MazeCoords exit) {
        return shortestPathToExit(matrix, start, exit) != -1;
    }
}
