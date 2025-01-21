import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AgentQueryMovePerTimeStepTest {
    private Environment environment;
    private SensorNode node1;
    private SensorNode node2;
    private SensorNode node3;
    private SensorNode node4;
    private SensorNode node5;
    private SensorNetworkSimulation simulation;

    @BeforeEach
    public void setup() {
        simulation = new SensorNetworkSimulation();
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
    public void testAgentMovesOncePerTimeStep() {
        // Create an event at node1
        EventInfo event1 = new EventInfo(1, "event1", node1, null, 0);
        node1.addEvent(event1);

        // Create and send an agent from node1
        Agent agent1 = new Agent(event1, 10, node1);
        node1.receiveMessage(agent1);
        environment.getAllMessages().add(agent1);

        // Simulate 3 time steps
        for (int i = 0; i < 3; i++) {
            // Reset hasMovedThisTimeStep to false before processing messages
            environment.getAllMessages().forEach(message -> message.setMovedThisTimeStep(false));

            // Process messages
            environment.getNodes().forEach(node -> node.processMessages());

            // Verify messages have moved exactly once
            environment.getAllMessages().forEach(message -> {
                if (message instanceof Agent) {
                    assertTrue(message.hasMovedThisTimeStep(), "Agent should have moved in this time step");
                }
            });

            // Reset hasMovedThisTimeStep to false for the next iteration
            environment.getAllMessages().forEach(message -> message.setMovedThisTimeStep(false));
        }

        // Check if agent moved exactly 3 times (one per time step)
        assertEquals(3, agent1.getNumHops(), "Agent1 should have moved exactly 3 times");
    }

    @Test
    public void testQueryMovesOncePerTimeStep() {
        // Create an event at node1
        EventInfo event1 = new EventInfo(1, "event1", node1, null, 0);
        node1.addEvent(event1);

        // Create and send a query message from node5
        QueryMessage query1 = new QueryMessage("event1", 10, node5, environment);  // Skicka Environment som parameter
        node5.receiveMessage(query1);
        environment.getAllMessages().add(query1);

        // Simulate 3 time steps
        for (int i = 0; i < 3; i++) {
            // Reset hasMovedThisTimeStep to false before processing messages
            environment.getAllMessages().forEach(message -> message.setMovedThisTimeStep(false));

            // Process messages
            environment.getNodes().forEach(node -> node.processMessages());

            // Verify messages have moved exactly once
            environment.getAllMessages().forEach(message -> {
                if (message instanceof QueryMessage) {
                    assertTrue(message.hasMovedThisTimeStep(), "QueryMessage should have moved in this time step");
                }
            });

            // Reset hasMovedThisTimeStep to false for the next iteration
            environment.getAllMessages().forEach(message -> message.setMovedThisTimeStep(false));
        }

        // Check if query moved exactly 3 times (one per time step)
        assertEquals(3, query1.getNumHops(), "Query1 should have moved exactly 3 times");
    }
}
