import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.io.*;
import java.util.*;


public class EnvironmentTest {
    private Environment environment;

    @Test
    public void testEnvironmentInitialization() {
        environment = new Environment();
        assertNotNull(environment.getNodes());
    }


    @Test
    public void testConnectNeighbors() {
        Environment env = new Environment();
        SensorNode node1 = new SensorNode(1, 0, 0);
        SensorNode node2 = new SensorNode(2, 1, 1);
        SensorNode node3 = new SensorNode(3, 2, 2);

        env.addNode(node1);
        env.addNode(node2);
        env.addNode(node3);
        env.connectNeighbors();

        assertTrue(node1.getNeighbors().contains(node2));
        assertTrue(node2.getNeighbors().contains(node3));
    }





}
