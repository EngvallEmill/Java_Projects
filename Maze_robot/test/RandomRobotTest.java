import org.junit.jupiter.api.Test;
import java.io.File;
import java.util.Scanner;
import static org.junit.jupiter.api.Assertions.*;

class RandomRobotTest {

    @Test
    void testNoUnnecessaryBacktracking() throws Exception {
        Scanner scanner = new Scanner(new File("test_resources/RandomRobot_Mazes/random_robot_maze.txt"));
        Maze maze = new Maze(scanner);
        RandomRobot robot = new RandomRobot(maze);

        Position previousPosition = robot.getPosition();
        while (!robot.hasReachedGoal()) {
            robot.move();
            Position currentPosition = robot.getPosition();

            assertTrue(maze.distanceToClosestGoal(currentPosition) < maze.distanceToClosestGoal(previousPosition),
                    "Robot should move closer to the goal, not backtrack.");

            previousPosition = currentPosition;
        }

        assertTrue(robot.hasReachedGoal(), "Robot should reach the goal.");
    }

    @Test
    void testStepBack() throws Exception {
        Scanner scanner = new Scanner(new File("test_resources/RandomRobot_Mazes/RandomRobot_MazeBackstep"));
        Maze maze = new Maze(scanner);
        RandomRobot robot = new RandomRobot(maze);

        robot.move();
        robot.move();
        robot.move();  // Should trigger stepBack()
        assertEquals(new Position(2, 1), robot.getPosition(), "Robot should step back to the initial position.");
    }
}
