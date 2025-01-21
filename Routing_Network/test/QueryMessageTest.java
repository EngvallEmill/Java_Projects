import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

public class QueryMessageTest {
    private Environment environment;
    private SensorNode node1;
    private SensorNode node2;
    private SensorNode node3;
    private SensorNode node4;
    private SensorNode node5;
    @BeforeEach
    public void setup() {

        environment = new Environment();
        node1 = new SensorNode(1, 0, 0);
        node2 = new SensorNode(2, 0, 10);
        node3 = new SensorNode(3, 0, 20);
        node4 = new SensorNode(4, 0, 30);
        node5 = new SensorNode(5, 0, 40);

        node1.addNeighbor(node2);
        node2.addNeighbor(node1);
        node2.addNeighbor(node3);
        node3.addNeighbor(node2);
        node3.addNeighbor(node4);
        node4.addNeighbor(node3);
        node4.addNeighbor(node5);
        node5.addNeighbor(node4);

        environment.addNode(node1);
        environment.addNode(node2);
        environment.addNode(node3);
        environment.addNode(node4);
        environment.addNode(node5);
    }

    @Test
    public void testQueryMessageRetries() {
        // Create an event at node3
        EventInfo event = new EventInfo(1, "event1", node3, null, 0);
        node3.addEvent(event);

        // Create and send a query message from node1
        QueryMessage query = new QueryMessage("event1", 1, node1, environment);  // Skicka Environment som parameter
        node1.receiveMessage(query);
        environment.getAllMessages().add(query);

        // Simulate a few steps
        for (int i = 0; i < 5; i++) {
            environment.getNodes().forEach(node -> node.processMessages());
            environment.getAllMessages().forEach(m -> m.setMovedThisTimeStep(false));
        }

        // Verify query message retries and updates hops count
        assertEquals(1, query.getNumHops(), "QueryMessage should have retried and reset hops count");
    }


    @Test
    public void DataRetrievalTest(){
        QueryMessage qmessage = new QueryMessage("Q1", 10, node1,environment);
        assertTrue(qmessage.hasVisited(node1));
        assertEquals(qmessage.getMaxHop(), 10);
        assertEquals(qmessage.getPreviousNode(), node1);
        assertEquals(qmessage.getEventId(), "Q1");

    }
}
