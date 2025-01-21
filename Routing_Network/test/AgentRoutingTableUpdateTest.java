import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AgentRoutingTableUpdateTest {
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
    public void testAgentUpdatesNodeRoutingTableWithMultipleEvents() {
        // Create multiple events
        EventInfo event1 = new EventInfo(1, "event1", node1, null, 0);
        EventInfo event2 = new EventInfo(2, "event2", node3, null, 0);
        EventInfo event3 = new EventInfo(3, "event3", node5, null, 0);

        node1.addEvent(event1);
        node3.addEvent(event2);
        node5.addEvent(event3);

        // Create and send agents from each node
        Agent agent1 = new Agent(event1, 20, node1);
        node1.receiveMessage(agent1);
        environment.getAllMessages().add(agent1);

        Agent agent2 = new Agent(event2, 20, node3);
        node3.receiveMessage(agent2);
        environment.getAllMessages().add(agent2);

        Agent agent3 = new Agent(event3, 20, node5);
        node5.receiveMessage(agent3);
        environment.getAllMessages().add(agent3);

        // Simulate a few steps
        for (int i = 0; i < 20; i++) {
            environment.getNodes().forEach(node -> node.processMessages());
            environment.getAllMessages().forEach(m -> m.setMovedThisTimeStep(false));
        }

        printRoutingTables();

        // Check if each node has updated routing tables for all events
        assertRoutingTable(node1, event1, event2, event3);
        assertRoutingTable(node2, event1, event2, event3);
        assertRoutingTable(node3, event1, event2, event3);
        assertRoutingTable(node4, event1, event2, event3);
        assertRoutingTable(node5, event1, event2, event3);
    }

    @Test
    public void testAgentUpdatesDistancesCorrectlyInLine() {
        // Create an event at node1
        EventInfo event = new EventInfo(1, "event1", node1, null, 0);
        node1.addEvent(event);

        // Create and send an agent from node1
        Agent agent = new Agent(event, 20, node1);
        node1.receiveMessage(agent);
        environment.getAllMessages().add(agent);

        // Simulate a few steps
        for (int i = 0; i < 10; i++) {
            environment.getNodes().forEach(node -> node.processMessages());
            environment.getAllMessages().forEach(m -> m.setMovedThisTimeStep(false));
        }

        // Check if the last node has the correct distance
        EventInfo node5EventInfo = node5.getEventInfo(event.getId());
        assertNotNull(node5EventInfo, "Node 5 should have event1 in its routing table");
        assertEquals(4, node5EventInfo.getDistance(), "Node 5's event1 should have a distance of 4");
    }

    private void assertRoutingTable(SensorNode node, EventInfo... events) {
        for (EventInfo event : events) {
            EventInfo nodeEventInfo = node.getEventInfo(event.getId());
            assertNotNull(nodeEventInfo, "Node " + node.getId() + " should have " + event.getId() + " in its routing table");
            assertEquals(event.getOriginNode(), nodeEventInfo.getOriginNode(), "Node " + node.getId() + "'s " + event.getId() + " origin should match");
            assertTrue(nodeEventInfo.getDistance() >= 0, "Node " + node.getId() + "'s " + event.getId() + " distance should be >= 0");
            if (nodeEventInfo.getDistance() > 0) {
                assertNotNull(nodeEventInfo.getNextNode(), "Node " + node.getId() + "'s " + event.getId() + " should have a next node if distance > 0");
            }
        }
    }

    private void printRoutingTables() {
        System.out.println("Node1 Routing Table:");
        node1.getRoutingTable().forEach((eventId, eventInfo) -> {
            System.out.println("Event ID: " + eventId + ", Distance: " + eventInfo.getDistance() + ", Next Node: " + eventInfo.getNextNode() + ", Origin Node: " + eventInfo.getOriginNode());
        });

        System.out.println("Node2 Routing Table:");
        node2.getRoutingTable().forEach((eventId, eventInfo) -> {
            System.out.println("Event ID: " + eventId + ", Distance: " + eventInfo.getDistance() + ", Next Node: " + eventInfo.getNextNode() + ", Origin Node: " + eventInfo.getOriginNode());
        });

        System.out.println("Node3 Routing Table:");
        node3.getRoutingTable().forEach((eventId, eventInfo) -> {
            System.out.println("Event ID: " + eventId + ", Distance: " + eventInfo.getDistance() + ", Next Node: " + eventInfo.getNextNode() + ", Origin Node: " + eventInfo.getOriginNode());
        });

        System.out.println("Node4 Routing Table:");
        node4.getRoutingTable().forEach((eventId, eventInfo) -> {
            System.out.println("Event ID: " + eventId + ", Distance: " + eventInfo.getDistance() + ", Next Node: " + eventInfo.getNextNode() + ", Origin Node: " + eventInfo.getOriginNode());
        });

        System.out.println("Node5 Routing Table:");
        node5.getRoutingTable().forEach((eventId, eventInfo) -> {
            System.out.println("Event ID: " + eventId + ", Distance: " + eventInfo.getDistance() + ", Next Node: " + eventInfo.getNextNode() + ", Origin Node: " + eventInfo.getOriginNode());
        });
    }
}
