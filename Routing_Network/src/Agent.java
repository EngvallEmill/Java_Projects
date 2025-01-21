import java.util.*;

public class Agent extends Message {
    private EventInfo event;
    private int maxHop;
    private int numHops;
    private SensorNode previousNode;
    private Set<SensorNode> visitedNodes;
    private Map<String, EventInfo> eventTable;

    /** Constructor for EventInfo.
     *
     * @param event to keep track of which event is first discovered by the node
     * @param maxHop decides how many jumps an Agent can make before it dies.
     * @param startNode to keep track where the node started from.
     */
    public Agent(EventInfo event, int maxHop, SensorNode startNode) {
        super();
        this.event = event;
        this.maxHop = maxHop;
        this.numHops = 0;
        this.previousNode = startNode;
        this.visitedNodes = new HashSet<>();
        this.eventTable = new HashMap<>();
        this.eventTable.put(event.getId(), event);
        visit(startNode);
    }

    /** Sets the current node to seen by the Agent.
     *
     * @param node to be set as seen.
     */
    public void visit(SensorNode node) {
        visitedNodes.add(node);
    }

    /** Checks if a node has been visited.
     *
     * @param node to check.
     * @return ture if visited, false if not.
     */
    public boolean hasVisited(SensorNode node) {
        return visitedNodes.contains(node);
    }

    /** Increases the amount of steps the agent has done by one.
     */
    public void incrementHops() {
        numHops++;
    }

    /**
     * Return how may steps the agent have taken
     * @return how may steps the agent have taken
     */
    public int getNumHops(){
        return numHops;
    }

    /**
     * Sets the current node's previous node to another node.
     * @param node to be set as the current nodes previous node.
     */
    public void setPreviousNode(SensorNode node) {
        previousNode = node;
    }

    /**
     * Gets the EventTable from the current node.
     * @return a Table with Events.
     */
    public Map<String, EventInfo> getEventTable() {
        return eventTable;
    }

    /**
     * Checks if the Agent has Exceeded the maximum allowed of hops.
     * @return true if it has exceeded its limit, false if not.
     */
    public boolean hasExceededMaxHop() {
        return numHops >= maxHop;
    }

    /** Updates the RoutingTable for the current node that the Agent is visiting.
     * It will also update the Agent's RoutingTable.
     * @param currentNode to be updated.
     */
    public void updateRoutingTable(SensorNode currentNode) {
        updateAgentRoutingTableFromRoutingTableOfNode(currentNode);
        updateAgentRoutingTableIfNodeHasShorterPathToEvent(currentNode);
        updateNodeRoutingTableWithEventsFromAgentRoutingTable(currentNode);
        updateDistancesInAgentRoutingTableToReflectNewNode(currentNode);
    }

    private void updateAgentRoutingTableFromRoutingTableOfNode(SensorNode currentNode){
        for (EventInfo event : currentNode.getEvents()) {
            String eventId = event.getId();
            if (!eventTable.containsKey(eventId)) {
                eventTable.put(eventId, new EventInfo(event.getTimeStep(), eventId, event.getOriginNode(), null, 0));
            }
        }
    }
    private void updateAgentRoutingTableIfNodeHasShorterPathToEvent(SensorNode currentNode){
        for (Map.Entry<String, EventInfo> entry : currentNode.getRoutingTable().entrySet()) {
            String eventId = entry.getKey();
            EventInfo nodeEventInfo = entry.getValue();
            EventInfo agentEventInfo = eventTable.get(eventId);
            if (agentEventInfo == null || nodeEventInfo.getDistance() < agentEventInfo.getDistance()) {
                eventTable.put(eventId, new EventInfo(nodeEventInfo.getTimeStep(), eventId, nodeEventInfo.getOriginNode(), nodeEventInfo.getNextNode(), nodeEventInfo.getDistance()));
            }
        }
    }
    private void updateNodeRoutingTableWithEventsFromAgentRoutingTable(SensorNode currentNode){
        for (Map.Entry<String, EventInfo> entry : eventTable.entrySet()) {
            String eventId = entry.getKey();
            EventInfo agentEventInfo = entry.getValue();
            EventInfo nodeEventInfo = currentNode.getRoutingTable().get(eventId);
            if (nodeEventInfo == null || agentEventInfo.getDistance() < nodeEventInfo.getDistance()) {
                currentNode.getRoutingTable().put(eventId, new EventInfo(agentEventInfo.getTimeStep(), eventId, agentEventInfo.getOriginNode(), previousNode, agentEventInfo.getDistance()));
            }
        }
    }
    private void updateDistancesInAgentRoutingTableToReflectNewNode(SensorNode currentNode){
        for (Map.Entry<String, EventInfo> entry : eventTable.entrySet()) {
            EventInfo agentEventInfo = entry.getValue();
            agentEventInfo.setDistance(agentEventInfo.getDistance() + 1);
            agentEventInfo.setNextNode(currentNode);
        }
    }


    /**
     * Moves the Agent.
     */
    @Override
    public void move() {
        if (!isDead() && !hasMovedThisTimeStep()) {
            if (hasExceededMaxHop()) {
                System.out.println("Agent exceeded maxHop at Node " + previousNode.getId());
                setDead(true);
                return;
            }
            SensorNode nextNode = selectNextNode(previousNode);
            if (nextNode != null) {
                moveAgent(nextNode);
            } else {
                System.out.println("Agent has no available unvisited neighbors at Node " + previousNode.getId());
                setDead(true);
            }
        }
    }

    private void moveAgent(SensorNode nextNode){
        visit(nextNode);
        incrementHops();
        updateRoutingTable(nextNode);
        setPreviousNode(nextNode);
        nextNode.receiveMessage(this);
        setMovedThisTimeStep(true);

    }

    private SensorNode selectNextNode(SensorNode currentNode) {
        List<SensorNode> neighbors = currentNode.getNeighbors();
        Collections.shuffle(neighbors);
        for (SensorNode neighbor : neighbors) {
            if (!hasVisited(neighbor)) {
                return neighbor;
            }
        }
        return null;
    }
}