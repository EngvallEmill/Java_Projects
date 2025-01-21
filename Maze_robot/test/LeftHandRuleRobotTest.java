import org.junit.jupiter.api.Test;
import java.io.File;
import java.util.Scanner;
import static org.junit.jupiter.api.Assertions.*;

class LeftHandRuleRobotTest {

    @Test
    void testAlwaysHasWallToLeftUnlessTurningLeft() throws Exception {
        Scanner scanner = new Scanner(new File("test_resources/ LeftHandRuleRobot_Mazes/ LeftHandRuleRobot_Maze"));
        Maze maze = new Maze(scanner);
        LeftHandRuleRobot robot = new LeftHandRuleRobot(maze);

        while (!robot.hasReachedGoal()) {
            Position current = robot.getPosition();
            LeftHandRuleRobot.Direction facing = robot.getFacing();
            Position leftPosition = facing.turnLeft().getNextPosition(current);

            boolean isWallToLeft = !maze.isMovable(leftPosition);

            robot.move();
            Position newPosition = robot.getPosition();
            LeftHandRuleRobot.Direction newFacing = robot.getFacing();

            if (!isWallToLeft) {
                assertEquals(leftPosition, newPosition, "Expected to turn left at " + current + ", but did not.");
                assertEquals(facing.turnLeft(), newFacing, "Facing direction should have changed to left.");
            } else {
                assertNotEquals(leftPosition, newPosition, "Unexpected left turn when blocked at " + current);
            }
        }

        assertTrue(robot.hasReachedGoal(), "Robot should have reached the goal.");
    }
}