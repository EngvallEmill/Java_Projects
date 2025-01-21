
/**
 * Implements a robot using the left-hand rule algorithm to navigate mazes.
 * This robot prioritizes turning left at every decision point in the maze,
 * moving forward if left is not available, and turns right only as a last resort.
 *
 *  * @author ens19esm
 *  * @version 2024-04-24
 */
public class LeftHandRuleRobot extends AbstractRobot {
    private Direction facing;
    private boolean hasWallOnLeft;

    /**
     * Constructs a LeftHandRuleRobot with the maze it will navigate.
     * The robot starts facing North.
     *
     * @param maze the maze the robot will navigate
     */
    public LeftHandRuleRobot(Maze maze) {
        super(maze);
        this.facing = Direction.NORTH;
        this.hasWallOnLeft = false;
    }

    /**
     * Returns the current facing direction of the robot.
     *
     * @return the current facing direction
     */
    public Direction getFacing() { return facing; }

    /**
     * Enum representing the four cardinal directions and methods to change direction.
     */
    enum Direction {
        NORTH, EAST, SOUTH, WEST;

        /**
         * Turns left relative to the current direction.
         *
         * @return the new direction after turning left
         */
        Direction turnLeft() {
            return switch (this) {
                case NORTH -> WEST;
                case WEST -> SOUTH;
                case SOUTH -> EAST;
                case EAST -> NORTH;
            };
        }

        /**
         * Turns right relative to the current direction.
         *
         * @return the new direction after turning right
         */
        Direction turnRight() {
            return switch (this) {
                case NORTH -> EAST;
                case EAST -> SOUTH;
                case SOUTH -> WEST;
                case WEST -> NORTH;
            };
        }

        /**
         * Calculates the next position based on the current direction.
         *
         * @param current the current position of the robot
         * @return the position in the direction the robot is facing
         */
        Position getNextPosition(Position current) {
            return switch (this) {
                case NORTH -> current.getPosToNorth();
                case SOUTH -> current.getPosToSouth();
                case EAST -> current.getPosToEast();
                case WEST -> current.getPosToWest();
            };
        }
    }

    /**
     * Moves the robot based on the left-hand rule. The robot first tries to move left,
     * then forward, then right, and turns around if all other directions are blocked.
     */
    @Override
    public void move() {
        if (hasReachedGoal()) {
            return;
        }

        Position leftPosition = facing.turnLeft().getNextPosition(position);
        Position forwardPosition = facing.getNextPosition(position);
        Position rightPosition = facing.turnRight().getNextPosition(position);
        Position backPosition = facing.turnRight().turnRight().getNextPosition(position);

        if (!hasWallOnLeft) {
            // First, try to find a wall to the left
            if (!maze.isMovable(leftPosition)) {
                hasWallOnLeft = true;
            } else if (maze.isMovable(forwardPosition)) {
                position = forwardPosition;
            } else {
                facing = facing.turnRight();
                if (maze.isMovable(rightPosition)) {
                    position = rightPosition;
                } else {
                    facing = facing.turnRight();
                    position = backPosition;
                }
            }
        } else {
            // If there was a wall on the left, follow the left-hand rule
            if (maze.isMovable(leftPosition)) {
                facing = facing.turnLeft();
                position = leftPosition;
            } else if (maze.isMovable(forwardPosition)) {
                position = forwardPosition;
            } else if (maze.isMovable(rightPosition)) {
                facing = facing.turnRight();
                position = rightPosition;
            } else {
                facing = facing.turnRight().turnRight();
                position = backPosition;
            }
        }

        visited.add(position);
    }
}
