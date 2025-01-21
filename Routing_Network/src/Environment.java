import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Environment {
    private List<SensorNode> nodes = new ArrayList<>();
    private List<EventInfo> allEvents = new ArrayList<>();
    private List<Message> allMessages = new ArrayList<>();
    private int timeStep = 0;
    private Random random = new Random();
    private SensorNetworkSimulation simulation; // Reference to GUI

    /**
     * Constructor for Environment class
     * @param filepath is the file to be loaded.
     * @param simulation is a reference to the graphical user interface,
     * which is used to start the simulation.
     * @throws IOException if the loading of the file would fail.
     */
    public Environment(String filepath, SensorNetworkSimulation simulation) throws IOException {
        this.simulation = simulation;
        loadNodes(filepath);
        connectNeighbors();
    }

    // Konstruktör för testning
    protected Environment() {}

    private void loadNodes(String filepath) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filepath))) {
            int nodeCount = Integer.parseInt(reader.readLine().trim());
            for (int i = 0; i < nodeCount; i++) {
                String[] parts = reader.readLine().split(",");
                int x = Integer.parseInt(parts[0].trim());
                int y = Integer.parseInt(parts[1].trim());
                nodes.add(new SensorNode(i, x, y));
            }
        }
    }

    protected void connectNeighbors() {
        for (SensorNode node : nodes) {
            for (SensorNode other : nodes) {
                if (node != other) {
                    double distance = node.getPosition().distanceTo(other.getPosition());
                    if (distance <= 15) {
                        node.addNeighbor(other);
                    }
                }
            }
        }
        System.out.println("All neighbors connected based on distance criteria.");
    }

    /**
     * Adds a node to the simulation.
     * @param node to be added to the simulation.
     */
    public void addNode(SensorNode node) {
        nodes.add(node);
    }

    /**
     * Method of simulating the simulation.
     */
    public void simulate() {
        while (timeStep < 10000) {
            System.out.println("Simulation step " + timeStep);
            triggerEvents();
            if (timeStep % 400 == 0 && timeStep != 0) {
                dispatchQueryMessages();
            }
            processMessages();
            timeStep++;
        }
    }

    private void triggerEvents() {
        for (SensorNode node : nodes) {
            if (random.nextDouble() < 0.0001) {
                String eventId = UUID.randomUUID().toString();
                int timeStamp = timeStep;
                EventInfo event = new EventInfo(timeStamp, eventId, node, null, 0);
                node.addEvent(event);
                allEvents.add(event);
                simulation.updateEventCount(allEvents.size()); // Update GUI
                System.out.println("Event " + eventId + " triggered at Node " + node.getPosition() + " at timestep " + timeStamp);

                if (random.nextDouble() < 0.5) {
                    Agent agent = new Agent(event, 50, node);
                    node.receiveMessage(agent);
                    allMessages.add(agent);
                    simulation.updateAgentCount((int) allMessages.stream().filter(m -> m instanceof Agent).count()); // Updates GUI
                    System.out.println("Agent dispatched from Node " + node.getPosition() + " for Event " + eventId);
                }
            }
        }
    }

    private void dispatchQueryMessages() {
        if (allEvents.isEmpty()) {
            System.out.println("No events available to initiate QueryMessages.");
            return;
        }
        for (int i = 0; i < 4; i++) {
            int nodeIndex = random.nextInt(nodes.size());
            SensorNode startNode = nodes.get(nodeIndex);
            EventInfo randomEvent = allEvents.get(random.nextInt(allEvents.size()));

            QueryMessage qm = new QueryMessage(randomEvent.getId(), 45, startNode, this);
            startNode.receiveMessage(qm);
            allMessages.add(qm);
            simulation.updateQueryMessageCount((int) allMessages.stream().filter(m -> m instanceof QueryMessage).count()); // Updates GUI
            System.out.println("QueryMessage dispatched from Node " + startNode.getPosition() + " with ID " + randomEvent.getId());
        }
    }

    /**
     * Gets all the nodes that are in the simulation.
     * @return a list with nodes.
     */
    public List<SensorNode> getNodes() {
        return nodes;
    }

    /**
     *  Gets all the Messages that are in the simulation.
     * @return a list with Messages.
     */
    public List<Message> getAllMessages() {
        return allMessages;
    }

    private void processMessages() {
        for (SensorNode node : nodes) {
            node.processMessages();
        }
        for (Message message : allMessages) {
            message.setMovedThisTimeStep(false);
        }
    }

    /**
     * Creates a Response when a Query has found a node with the searched for
     * Event.
     * @param eventId is the specific Event identifier that the response Message
     * are responsible for carrying back to the sender.
     * @param path to follow when returning to sender.
     */
    public void createResponseMessage(String eventId, List<SensorNode> path) {
        ResponseMessage responseMessage = new ResponseMessage(eventId, path);
        allMessages.add(responseMessage);
        simulation.updateResponseMessageCount((int) allMessages.stream().filter(m -> m instanceof ResponseMessage).count()); // Updates GUI
        path.get(path.size() - 1).receiveMessage(responseMessage); // Add the response message to the last node in the path
    }
}
