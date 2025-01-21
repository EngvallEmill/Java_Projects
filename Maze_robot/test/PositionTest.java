import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class PositionTest {

    @Test
    public void testPositionEquality() {
        Position pos1 = new Position(1, 2);
        Position pos2 = new Position(1, 2);
        assertEquals(pos1, pos2,
                "Positions with the same coordinates should be equal.");
    }

    @Test
    public void testPositionInequality() {
        Position pos1 = new Position(1, 2);
        Position pos2 = new Position(2, 2);
        assertNotEquals(pos1, pos2,
                "Positions with different coordinates should not be equal.");
    }

    @Test
    public void testMoveNorth() {
        Position pos = new Position(1, 2);
        assertEquals(new Position(1, 1), pos.getPosToNorth(),
                "Moving North should decrease the y coordinate by 1.");
    }

    @Test
    public void testMoveSouth() {
        Position pos = new Position(1, 2);
        assertEquals(new Position(1, 3), pos.getPosToSouth(),
                "Moving South should increase the y coordinate by 1.");
    }

    @Test
    public void testMoveEast() {
        Position pos = new Position(1, 2);
        assertEquals(new Position(2, 2), pos.getPosToEast(),
                "Moving East should increase the x coordinate by 1.");
    }

    @Test
    public void testMoveWest() {
        Position pos = new Position(1, 2);
        assertEquals(new Position(0, 2), pos.getPosToWest(),
                "Moving West should decrease the x coordinate by 1.");
    }
}
