import java.util.List;
import java.util.Stack;

/**
 * HomingRobot is a specific type of robot that attempts to find the shortest path to the goal using a stack-based backtracking algorithm.
 *
 * @author ens19esm
 * @version 2024-04-24
 */
class HomingRobot extends AbstractRobot {
    protected Stack<Position> pathStack = new Stack<>();

    /**
     * Constructs a HomingRobot with a specific maze.
     *
     * @param maze the maze the robot will navigate
     */
    public HomingRobot(Maze maze) {
        super(maze);
        pathStack.push(position);
    }

    /**
     * Moves the robot towards the goal within the maze. The method checks if the goal is reached and stops if it is.
     * Otherwise, it checks for movable neighbors. If no neighbors are found, it backtracks.
     * If neighbors are found, it selects the optimal next move, updates the position, and tracks it.
     */
    @Override
    public void move() {
        if (hasReachedGoal()) {
            return;
        }

        List<Position> neighbors = getMovableNeighboursForward();
        if (neighbors.isEmpty()) {
            backtrack();
        } else {
            Position next = selectNextPosition(neighbors);
            this.position = next;
            visited.add(next);
            pathStack.push(next);
        }
    }

    /**
     * Selects the next position to move to based on the closest distance to the goal.
     *
     * @param neighbors list of possible next positions
     * @return the selected next position
     */
    private Position selectNextPosition(List<Position> neighbors) {
        Position next = neighbors.get(0);
        int minDistance = maze.distanceToClosestGoal(next);

        for (Position p : neighbors) {
            int distance = maze.distanceToClosestGoal(p);
            if (distance < minDistance) {
                minDistance = distance;
                next = p;
            }
        }

        return next;
    }

    /**
     * Backtracks to the previous position if no forward movement is possible.
     */
    private void backtrack() {
        if (pathStack.size() > 1) {
            pathStack.pop();
            this.position = pathStack.peek();
        }
    }
}
