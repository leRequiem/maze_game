package Maze_work;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MazeCoords {
    int x, y;

    public MazeCoords(int x, int y) {
        this.x = x;
        this.y = y;
    }
}