public class EventInfo {

    private int timeStep;
    private String id;
    private SensorNode originNode;
    private SensorNode nextNode;
    private int distance;

    /**
     * Creates Eventinfo with 5 parameters
     * @param timeStep
     * @param id
     * @param originNode
     * @param nextNode
     * @param distance
     */

    public EventInfo(int timeStep, String id, SensorNode originNode, SensorNode nextNode, int distance) {
        this.timeStep = timeStep;
        this.id = id;
        this.originNode = originNode;
        this.nextNode = nextNode;
        this.distance = distance;
    }

    /**
     * Returns value of timeStep
     * @return
     */
    public int getTimeStep() {
        return timeStep;
    }

    /**
     * Returns value of Id
     * @return
     */
    public String getId() {
        return id;
    }

    /**
     * Returns Starting node
     * @return
     */
    public SensorNode getOriginNode() {
        return originNode;
    }

    /**
     * Returns value of the next node
     * @return
     */
    public SensorNode getNextNode() {
        return nextNode;
    }

    /**
     * Sets the value of the next node to be used
     * @param nextNode
     */

    public void setNextNode(SensorNode nextNode) {
        this.nextNode = nextNode;
    }

    /**
     *Returns value of distance
     * @return
     */
    public int getDistance() {
        return distance;
    }

    /**
     * Uses distance as a parameter and makes it retrievable
     * @param distance
     */
    public void setDistance(int distance) {
        this.distance = distance;
    }

    /**
     * Returns the values of Eventinfo as a structured string
     * @return
     */

    @Override
    public String toString() {
        return "EventInfo{" +
                "timeStep=" + timeStep +
                ", id='" + id + '\'' +
                ", originNode=" + originNode +
                ", nextNode=" + nextNode +
                ", distance=" + distance +
                '}';
    }
}
