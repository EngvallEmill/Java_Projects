import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

public class ResponseMessageTest {
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
    public void testResponseMessageMovesOncePerTimeStep() {
        // Create a path from node5 to node1
        List<SensorNode> path = List.of(node5, node4, node3, node2, node1);
        ResponseMessage response = new ResponseMessage("event1", path);
        node5.receiveMessage(response); // Start the response message at node5
        environment.getAllMessages().add(response);

        // Simulate 5 time steps
        for (int i = 0; i < 5; i++) {
            System.out.println("Time step " + i + ":");
            // Reset hasMovedThisTimeStep to false before processing messages
            environment.getAllMessages().forEach(message -> message.setMovedThisTimeStep(false));

            // Process messages
            environment.getNodes().forEach(node -> node.processMessages());

            // Verify messages have moved exactly once
            environment.getAllMessages().forEach(message -> {
                if (message instanceof ResponseMessage) {
                    System.out.println("Checking if ResponseMessage has moved in this time step:");
                    assertTrue(message.hasMovedThisTimeStep(), "ResponseMessage should have moved in this time step");
                }
            });

            // Reset hasMovedThisTimeStep to false for the next iteration
            environment.getAllMessages().forEach(message -> message.setMovedThisTimeStep(false));
        }

        // Check if response message moved exactly 5 times (one per time step) and is now dead
        assertEquals(0, response.getCurrentIndex(), "ResponseMessage should have reached the start node");
        assertTrue(response.isDead(), "ResponseMessage should be dead after reaching the start node");
    }

    @Test
    public void testResponseMessageReachesStartNode() {
        // Create a path from node5 to node1
        List<SensorNode> path = List.of(node5, node4, node3, node2, node1);
        ResponseMessage response = new ResponseMessage("event1", path);
        node5.receiveMessage(response); // Start the response message at node5
        environment.getAllMessages().add(response);

        // Simulate 5 time steps
        for (int i = 0; i < 5; i++) {
            System.out.println("Time step " + i + ":");
            // Reset hasMovedThisTimeStep to false before processing messages
            environment.getAllMessages().forEach(message -> message.setMovedThisTimeStep(false));

            // Process messages
            environment.getNodes().forEach(node -> node.processMessages());

            // Verify the response has moved
            environment.getAllMessages().forEach(message -> {
                if (message instanceof ResponseMessage) {
                    System.out.println("Checking if ResponseMessage has moved in this time step:");
                    assertTrue(message.hasMovedThisTimeStep(), "ResponseMessage should have moved in this time step");
                }
            });

            // Check if response is at the correct node
            if (i < 4) {
                assertFalse(response.isDead(), "ResponseMessage should not be dead yet");
                assertEquals(path.get(path.size() - 2 - i).getId(), response.getCurrentNode().getId(), "ResponseMessage should be at the correct node");
            } else {
                assertTrue(response.isDead(), "ResponseMessage should be dead after reaching the start node");
                assertEquals(node5.getId(), response.getCurrentNode().getId(), "ResponseMessage should be at the start node");
            }

            // Reset hasMovedThisTimeStep to false for the next iteration
            environment.getAllMessages().forEach(message -> message.setMovedThisTimeStep(false));
        }
    }

    @Test
    public void DataRetrievalTest(){
        List<SensorNode> path = List.of(node1, node2, node3, node4, node5);
        ResponseMessage response = new ResponseMessage("event2", path);
        assertEquals(response.getPath(), path);
        assertEquals(response.getEventId(), "event2");

    }
}
