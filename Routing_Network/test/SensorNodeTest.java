import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SensorNodeTest {
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
    public void testAddEvent() {
        node.addEvent(event);
        assertEquals(event, node.getEventInfo("event1"));
    }

    @Test
    public void testReceiveMessage() {
        Agent agent = new Agent(event, 50, node);
        node.receiveMessage(agent);

        assertEquals(1, node.getMessageQueue().size());
    }
}
