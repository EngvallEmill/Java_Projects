import java.util.*;

public class ResponseMessage extends Message {
    private String eventId;
    private List<SensorNode> path;
    private int currentIndex;

    /**
     * Constructor for ResponseMessage class.
     * @param eventId to be sent back.
     * @param path to follow.
     */
    public ResponseMessage(String eventId, List<SensorNode> path) {
        super();
        this.eventId = eventId;
        this.path = new ArrayList<>(path);
        this.currentIndex = path.size() - 1;
    }

    /**
     * Gets the eventId that a ResponseMessage is carrying.
     * @return a string.
     */
    public String getEventId() {
        return eventId;
    }

    /**
     * Gets the path to follow.
     * @return a list with nodes.
     */
    public List<SensorNode> getPath() {
        return path;
    }

    /**
     * Gets the current node that the ResponseMessage is currently in.
     * @return the current node.
     */
    public SensorNode getCurrentNode() {
        return path.get(currentIndex);
    }

    /**
     * Gets the current index for the SensorNode in the list.
     * @return an index.
     */
    public int getCurrentIndex() {
        return currentIndex;
    }

    /**
     * Moves the ResponseMessage.
     */
    @Override
    public void move() {
        if (!isDead() && !hasMovedThisTimeStep()) {
            if (currentIndex > 0) {
                currentIndex--;
                SensorNode nextNode = path.get(currentIndex);
                nextNode.receiveMessage(this);
            } else {
                SensorNode startNode = path.get(currentIndex);
                EventInfo eventInfo = startNode.getEventInfo(eventId);
                if (eventInfo != null) {
                    System.out.println("ResponseMessage delivered to start node for Event " + eventId);
                }
                setDead(true);
            }
            setMovedThisTimeStep(true);
        }
    }
}
