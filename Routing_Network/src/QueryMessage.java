import java.util.*;

public class QueryMessage extends Message {
    private String eventId;
    private int maxHop;
    private int numHops;
    private int retries;
    private SensorNode startNode;
    private SensorNode previousNode;
    private Set<SensorNode> visitedNodes;
    private List<SensorNode> path;
    private boolean randomStepTaken;
    private Environment environment;

    /**
     * Constructor for QueryMessage class.
     * @param eventId the event identifier that the Query is locking for.
     * @param maxHop the maximum allowed of hops a Query can make.
     * @param startNode for the Query.
     * @param environment to be simulated.
     */
    public QueryMessage(String eventId, int maxHop, SensorNode startNode, Environment environment) {
        super();
        this.eventId = eventId;
        this.maxHop = maxHop;
        this.numHops = 0;
        this.retries = 2; // Initial number of retries
        this.startNode = startNode;
        this.previousNode = startNode;
        this.visitedNodes = new HashSet<>();
        this.path = new ArrayList<>();
        this.randomStepTaken = false;
        this.environment = environment;
        visit(startNode);
        path.add(startNode);
    }

    /**
     * Sets a node as visited.
     * @param node to be set as visited.
     */
    public void visit(SensorNode node) {
        visitedNodes.add(node);
    }

    /**
     * Checks if a specified node has been visited.
     * @param node to check.
     * @return true if a node has been visited, else false.
     */
    public boolean hasVisited(SensorNode node) {
        return visitedNodes.contains(node);
    }

    /**
     * Get the number of hops that the Query has done.
     * @return a number of hops that has been made.
     */
    public int getNumHops() {
        return numHops;
    }

    /**
     * Gets a number of the maximum allowed of hops that a Query can make.
     * @return the maximum allowed of hops that a Query can make.
     */
    public int getMaxHop() {
        return maxHop;
    }

    /**
     * Checks if the Query has exceeded its maximum allowed hops.
     * @return true if the Query has made more hops the allowed, else false.
     */
    public boolean hasExceededMaxHop() {
        return numHops >= maxHop;
    }

    /**
     * Increases the counter for how many hops a Query has made.
     */
    public void incrementHops() {
        numHops++;
    }

    /**
     * Gets the previous visited node.
     * @return the previous node.
     */
    public SensorNode getPreviousNode() {
        return previousNode;
    }

    /**
     * Sets a node as the previous visited node.
     * @param node to be set as the previous visited node.
     */
    public void setPreviousNode(SensorNode node) {
        previousNode = node;
    }

    /**
     * Checks if the query can try again to search for an Event.
     * @return true if it can try again, else false.
     */
    public boolean hasRetriesLeft() {
        return retries > 0;
    }

    /**
     * Resets the number of tries a Query can make before it gives up.
     */
    public void resetRetries() {
        retries--;
        numHops = 0;
        visitedNodes.clear();
        path.clear();
        setPreviousNode(startNode);
        visit(startNode);
        path.add(startNode);
    }

    /**
     * Sets the value of the Query if it has made a random step.
     * @param taken if it has made a random step.
     */
    public void setRandomStepTaken(boolean taken) {
        this.randomStepTaken = taken;
    }

    /**
     * Gets the eventId from Query.
     * @return a String.
     */
    public String getEventId() {
        return eventId;
    }

    /**
     * Checks if the Query has found the node were the searched for node.
     * @param currentNode to check.
     * @return true if it is the correct node, else false.
     */
    public boolean hasFound(SensorNode currentNode) {
        for (EventInfo event : currentNode.getEvents()) {
            if (event.getId().equals(eventId) && event.getDistance() == 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * Moves the Query.
     */
    @Override
    public void move() {
        if (!isDead() && !hasMovedThisTimeStep()) {
            if (hasFound(previousNode)) {
                System.out.println("QueryMessage found the event at Node " + previousNode.getPosition());
                List<SensorNode> responsePath = new ArrayList<>(path);
                environment.createResponseMessage(eventId, responsePath);
                setDead(true);
                return;
            }
            if (hasExceededMaxHop()) {
                System.out.println("QueryMessage exceeded maxHop at Node " + previousNode.getPosition());
                if (hasRetriesLeft()) {
                    resetRetries();
                    move(); // Retry with reset parameters
                } else {
                    setDead(true);
                }
                return;
            }
            SensorNode nextNode = selectNextNode(previousNode);
            setMovedThisTimeStep(true);
            if (nextNode != null) {
                visit(nextNode);
                path.add(nextNode);
                setPreviousNode(nextNode);
                if (randomStepTaken) {
                    incrementHops();
                    setRandomStepTaken(false); // Reset the flag after a random step
                }
                nextNode.receiveMessage(this);
            } else {
                System.out.println("QueryMessage has no available unvisited neighbors at Node " + previousNode.getId());
                if (hasRetriesLeft()) {
                    resetRetries();
                    move(); // Retry with reset parameters
                } else {
                    setDead(true);
                }
            }
        }
    }

    private SensorNode selectNextNode(SensorNode currentNode) {
        EventInfo eventInfo = currentNode.getRoutingTable().get(eventId);
        if (eventInfo != null && eventInfo.getNextNode() != null && !hasVisited(eventInfo.getNextNode())) {
            setRandomStepTaken(false);
            return eventInfo.getNextNode();
        }
        return selectRandomNeighbor(currentNode);
    }

    private SensorNode selectRandomNeighbor(SensorNode currentNode) {
        List<SensorNode> neighbors = currentNode.getNeighbors();
        Collections.shuffle(neighbors);
        for (SensorNode neighbor : neighbors) {
            if (!neighbor.equals(previousNode)) {
                setRandomStepTaken(true);
                return neighbor;
            }
        }
        return null;
    }
}
