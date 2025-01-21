import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

class MazeTest {

    Maze maze;

    @BeforeEach
    void setUp() throws IOException {
        maze = new Maze(new Scanner(new File("test_resources/MazeTest_Mazes/functional_maze.txt")));
    }

    @Test
    void testMazeConstruction() {
        assertEquals(8, maze.getNumColumns());
        assertEquals(10, maze.getNumRows());
    }

    @Test
    void testIsGoal() {
        Position goalPosition = new Position(1, 8);
        assertTrue(maze.isGoal(goalPosition));
    }

    @Test
    void testIsMovable() {
        Position wallPosition = new Position(4, 0);
        assertFalse(maze.isMovable(wallPosition));
        Position openPosition = new Position(5, 1);
        assertTrue(maze.isMovable(openPosition));
    }

    @Test
    void testFindStartPosition() {
        assertEquals(new Position(1, 1), maze.getStart());
    }

    @Test
    void testDistanceToClosestGoal() {
        Position nearGoal = new Position(2, 8);
        assertEquals(1, maze.distanceToClosestGoal(nearGoal));
    }

    @Test
    void testExceptions() {
        assertThrows(NoGoalException.class, () -> new Maze(new Scanner(new File("test_resources/MazeTest_Mazes/maze_without_goals.txt"))));
        assertThrows(StartException.class, () -> new Maze(new Scanner(new File("test_resources/MazeTest_Mazes/maze_with_multiple_starts.txt"))));
    }
}
