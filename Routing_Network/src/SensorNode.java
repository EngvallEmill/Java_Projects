import java.util.*;

public class SensorNode {
    private final int id;
    private final Position position;
    private final List<SensorNode> neighbors;
    private final Map<String, EventInfo> routingTable;
    private final List<Message> messageQueue;
    private final List<EventInfo> events;

    /** Constructor for SensorNode.
     *
     * @param id Identifier for node.
     * @param x coordinate for node in grid.
     * @param y coordinate for node in grid.
     */
    public SensorNode(int id, double x, double y) {
        this.id = id;
        this.position = new Position(x, y);
        this.neighbors = new ArrayList<>();
        this.routingTable = new HashMap<>();
        this.messageQueue = new ArrayList<>();
        this.events = new ArrayList<>();
    }

    /** Adds a neighboring node to the nodes list.
     * @param neighbor node to be added to the list of neighbors
     */
    public void addNeighbor(SensorNode neighbor) {
        neighbors.add(neighbor);
    }

    /** Gets the identifying int from a specific node.
     * @return the identifier for the node as an int.
     */
    public int getId() {
        return id;
    }

    /** Gets the position of the node in the grid.
     * @return a position
     */
    public Position getPosition() {
        return position;
    }

    /** Gets all the neighbors from a specific node.
     * @return a list of Nodes.
     */
    public List<SensorNode> getNeighbors() {
        return neighbors;
    }

    /** Gets the RoutingTable for specified node.
     * @return a HashMap with Events.
     */
    public Map<String, EventInfo> getRoutingTable() {
        return routingTable;
    }

    /** Gets the queue for how the messages are prioritized.
     * @return a list with Messages.
     */
    public List<Message> getMessageQueue() {
        return messageQueue;
    }

    /** Adds an incoming message to the queue in the node.
     * @param message to be processed in the node.
     */
    public void receiveMessage(Message message) {
        messageQueue.add(message);
    }

    /** Adds an Event to the RoutingTable.
     * @param event to be added to the node.
     */
    public void addEvent(EventInfo event) {
        addToRoutingTable(event);
    }

    /** Private method for addEvent() method. Used to avoid confusion
     * when using the addEvent() method.
     * Adds the event to both the list of Events that the node knows
     * about and to its RoutingTable. This is how the System manges
     * its Event storing.
     *
     * @param event to be added to both the known Events and RoutingTable.
     */
    private void addToRoutingTable(EventInfo event){
        events.add(event);
        routingTable.put(event.getId(), event);
    }

    /** Gets an Event from the RoutingTable.
     * @param eventId specifies which Event is looked for
     * @return an Event. If eventId is not in the RoutingTable, the method
     * returns null. null value is a valid return value.
     */
    public EventInfo getEventInfo(String eventId) {
        return routingTable.get(eventId);
    }

    /** Gets all the known Events in a node.
     * @return a list with Events.
     */
    public List<EventInfo> getEvents() {
        return events;
    }

    /** Depending on the nature of the Message that is received,
     * the method is calling different methods.
     */
    public void processMessages() {
        List<Message> newQueue = new ArrayList<>(messageQueue); // Copy original messages
        messageQueue.clear(); // Clear original queue

        for (Message message : newQueue) {
            if (!message.hasMovedThisTimeStep()) {
                if (message instanceof Agent) {
                    processAgentMessage(message);
                } else if (message instanceof QueryMessage) {
                    processQueryMessage(message);
                } else if (message instanceof ResponseMessage) {
                    processResponseMessage(message);
                }
            }
        }
        addNotDeadMessagesBackToTheQueue(newQueue);
    }



    /** Updates the Agent's information with information from the message.
     * Then moves the Agent.
     * @param agentMessage to be processed.
     */
    private void processAgentMessage(Message agentMessage){
        Agent agent = (Agent) agentMessage;
        agent.updateRoutingTable(this);
        agent.move();
    }

    /** Moves the Query through the current node to the next node.
     * @param queryMessage to be processed.
     */
    private void processQueryMessage(Message queryMessage){
        QueryMessage query = (QueryMessage) queryMessage;
        query.move();
    }

    /** Moves the response through the current node to the next node.
     * @param responseMessage to be processed.
     */
    private void processResponseMessage(Message responseMessage){
        ResponseMessage response = (ResponseMessage) responseMessage;
        response.move();
    }

    /** Adds the non-dead messages to the node's queue.
     * @param newQueue that the non-dead messages will be added to.
     */
    private void addNotDeadMessagesBackToTheQueue(List<Message> newQueue){
        for (Message message : newQueue) {
            if (!message.isDead()) {
                messageQueue.add(message);
            }
        }

    }


}