import java.util.ArrayList;
import java.util.List;

/**
 * Abstract base class for robots navigating a maze.
 * This class provides basic functionalities for robot movement and tracking within a maze environment.
 *
 * @author ens19esm
 * @version 2024-04-24
 */
public abstract class AbstractRobot implements Robot {
    protected Position position;
    protected Maze maze;
    protected List<Position> visited = new ArrayList<>();

    /**
     * Constructs a new AbstractRobot with a starting position.
     * Initializes the robot at the start position of the maze.
     *
     * @param maze the maze the robot will navigate
     */
    public AbstractRobot(Maze maze) {
        this.maze = maze;
        this.position = maze.getStart();
        visited.add(position);
    }

    /**
     * Retrieves all neighboring positions around the current robot's position.
     *
     * @return a list of neighboring positions
     */
    private ArrayList<Position> getNeighbours() {
        ArrayList<Position> neighbours = new ArrayList<>();
        neighbours.add(position.getPosToNorth());
        neighbours.add(position.getPosToSouth());
        neighbours.add(position.getPosToEast());
        neighbours.add(position.getPosToWest());
        return neighbours;
    }

    /**
     * Determines movable neighboring positions that have not yet been visited.
     *
     * @return a list of unvisited movable neighboring positions
     */
    protected ArrayList<Position> getMovableNeighboursForward() {
        ArrayList<Position> movable = new ArrayList<>();
        List<Position> neighbours = getNeighbours();
        for (Position p : neighbours) {
            if (maze.isMovable(p) && !visited.contains(p)) {
                movable.add(p);
            }
        }
        return movable;
    }

    @Override
    public Position getPosition() {
        return position;
    }

    @Override
    public boolean hasReachedGoal() {
        return maze.isGoal(position);
    }
}
