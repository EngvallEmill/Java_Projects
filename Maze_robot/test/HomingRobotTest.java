import org.junit.jupiter.api.Test;
import java.io.File;
import java.util.Scanner;
import static org.junit.jupiter.api.Assertions.*;

class HomingRobotTest {

    @Test
    void testPathFindingAroundWall() throws Exception {
        Scanner scanner = new Scanner(new File("test_resources/HomingRobot_Mazes/HomingRobot_maze"));
        Maze maze = new Maze(scanner);
        HomingRobot robot = new HomingRobot(maze);

        int previousDistance = maze.distanceToClosestGoal(robot.getPosition());
        boolean movedAwayTemporarily = false;

        while (!robot.hasReachedGoal()) {
            robot.move();
            Position currentPosition = robot.getPosition();
            int currentDistance = maze.distanceToClosestGoal(currentPosition);

            if (currentDistance > previousDistance) {
                movedAwayTemporarily = true; // Flag that we've moved away from the goal
            } else {
                if (movedAwayTemporarily) {
                    // Ensure that after moving away, we make progress towards the goal again
                    assertTrue(currentDistance < previousDistance,
                            "After moving away, the robot should move closer to the goal again. Failed at position: " + currentPosition);
                    movedAwayTemporarily = false;
                }
            }

            previousDistance = currentDistance;
        }

        assertTrue(robot.hasReachedGoal(), "Robot should have reached the goal.");
    }
}