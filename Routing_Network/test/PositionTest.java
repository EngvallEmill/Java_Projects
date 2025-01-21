import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class PositionTest {

    @Test
    public void testConstructor() {
        Position position = new Position(5.0, 10.0);
        assertEquals(5.0, position.x);
        assertEquals(10.0, position.y);
    }

    @Test
    public void testDistanceTo() {
        Position position1 = new Position(0, 0);
        Position position2 = new Position(3, 4);
        assertEquals(5, position1.distanceTo(position2));
    }

    @Test
    public void testToString() {
        Position position = new Position(5, 10);
        assertEquals("(5.0, 10.0)", position.toString());
    }

    @Test
    public void testEquals() {
        Position position1 = new Position(5, 10);
        Position position2 = new Position(5, 10);
        Position position3 = new Position(10, 5);
        assertTrue(position1.equals(position2));
        assertFalse(position1.equals(position3));
    }

    @Test
    public void testHashCode() {
        Position position1 = new Position(5, 10);
        Position position2 = new Position(5, 10);
        assertEquals(position1.hashCode(), position2.hashCode());
    }
}
