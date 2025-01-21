import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class EventInfoTest {

    @Test
    public void testConstructorAndGetters() {
        SensorNode origin = new SensorNode(1, 0.0, 0.0);
        SensorNode next = new SensorNode(2, 1.0, 1.0);
        EventInfo event = new EventInfo(100, "event123", origin, next, 5);

        assertEquals(100, event.getTimeStep());
        assertEquals("event123", event.getId());
        assertEquals(origin, event.getOriginNode());
        assertEquals(next, event.getNextNode());
        assertEquals(5, event.getDistance());
    }

    @Test
    public void testSetters() {
        SensorNode origin = new SensorNode(1, 0.0, 0.0);
        SensorNode next = new SensorNode(2, 1.0, 1.0);
        SensorNode newNext = new SensorNode(3, 2.0, 2.0);
        EventInfo event = new EventInfo(100, "event123", origin, next, 5);

        event.setNextNode(newNext);
        event.setDistance(10);

        assertEquals(newNext, event.getNextNode());
        assertEquals(10, event.getDistance());
    }

    @Test
    public void testToString() {
        SensorNode origin = new SensorNode(1, 0.0, 0.0);
        SensorNode next = new SensorNode(2, 1.0, 1.0);
        EventInfo event = new EventInfo(100, "event123", origin, next, 5);

        String expected = "EventInfo{timeStep=100, id='event123', originNode=" + origin.toString() + ", nextNode=" + next.toString() + ", distance=5}";
        assertEquals(expected, event.toString());
    }
}
