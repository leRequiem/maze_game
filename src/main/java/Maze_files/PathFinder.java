package Maze_files;

import java.util.LinkedList;
import java.util.Queue;

public class PathFinder {

    public int hasPathToExit(int[][] matrix, int startX, int startY, int endX, int endY) {

        int rows = matrix.length;
        int cols = matrix[0].length;
        boolean[][] visited = new boolean[rows][cols];
        int steps = 0;

        // Начало BFS
        Queue<int[]> queue = new LinkedList<>();
        queue.offer(new int[] {startX, startY});
        visited[startX][startY] = true;


        int[][] directions = { {-1, 0}, {1, 0}, {0, -1}, {0, 1} };

        while (!queue.isEmpty()) {
            int[] current = queue.poll();
            int x = current[0];
            int y = current[1];

            if (x == endX && y == endY) {
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
}
