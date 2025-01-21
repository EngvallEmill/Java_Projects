import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class AgentTest {
    private SensorNode node;
    private EventInfo event;
    private Environment environment;

    @BeforeEach
    public void setUp() {
        node = new SensorNode(1, 0, 0);
        event = new EventInfo(0, "event1", node, null, 0);
        environment = new Environment();
        environment.addNode(node);
    }

    @Test
    public void testAgentInitialization() {
        Agent agent = new Agent(event, 50, node);
        assertNotNull(agent.getEventTable().get("event1"));
    }

    @Test
    public void testAgentMovement() {
        Agent agent = new Agent(event, 50, node);
        SensorNode neighbor = new SensorNode(2, 0, 1);
        node.addNeighbor(neighbor);
        environment.addNode(neighbor);

        node.receiveMessage(agent);
        environment.getAllMessages().add(agent);

        // Simulate a few steps
        for (int i = 0; i < 2; i++) {
            environment.getNodes().forEach(n -> n.processMessages());
            environment.getAllMessages().forEach(m -> m.setMovedThisTimeStep(false));
        }

        assertTrue(agent.hasVisited(neighbor));
    }
}
