/**
 * Defines the basic functionalities for a robot navigating in a maze environment.
 * This interface ensures that all types of robots can move within a maze, 
 * report their position, and determine if they have reached the goal.
 *
 *  @author ens19esm
 *  @version 2024-04-24
 */
public interface Robot {

    /**
     * Instructs the robot to make a move in the maze. The specific movement 
     * logic is implemented by the classes that implement this interface.
     */
    void move();

    /**
     * Gets the current position of the robot in the maze.
     *
     * @return the current position of the robot
     */
    Position getPosition();

    /**
     * Checks if the robot has reached the goal within the maze.
     *
     * @return true if the robot has reached the goal, otherwise false
     */
    boolean hasReachedGoal();
}
