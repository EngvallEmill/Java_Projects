import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;

public class SensorNetworkSimulation {
    private JFrame frame;
    private JTextField messageField;
    private Environment environment;
    private JLabel eventCountLabel;
    private JLabel agentCountLabel;
    private JLabel queryMessageCountLabel;
    private JLabel responseMessageCountLabel;

    /**
     * Constructor for the Simulation class.
     */
    public SensorNetworkSimulation() {
        frame = setupMainWindow();
        frame.add(createTopPanel(), BorderLayout.NORTH);
        frame.add(createCenterPanel(), BorderLayout.CENTER);
        messageField = new JTextField("Status messages appear here...");
        frame.add(messageField, BorderLayout.SOUTH);
        frame.setVisible(true);
    }

    private JFrame setupMainWindow() {
        JFrame frame = new JFrame("Sensor Network Simulator");
        frame.setSize(500, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.setLocationRelativeTo(null);
        return frame;
    }

    private JPanel createTopPanel() {
        JPanel panel = new JPanel(new FlowLayout());
        JButton loadButton = new JButton("Load Network");
        JButton playButton = new JButton("Start Simulation");

        loadButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                loadNetwork(e);
            }
        });

        playButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                startSimulation();
            }
        });

        panel.add(loadButton);
        panel.add(playButton);
        return panel;
    }

    private JPanel createCenterPanel() {
        JPanel panel = new JPanel(new GridLayout(4, 1));
        eventCountLabel = new JLabel("Events: 0");
        agentCountLabel = new JLabel("Agents: 0");
        queryMessageCountLabel = new JLabel("QueryMessages: 0");
        responseMessageCountLabel = new JLabel("ResponseMessages: 0");

        panel.add(eventCountLabel);
        panel.add(agentCountLabel);
        panel.add(queryMessageCountLabel);
        panel.add(responseMessageCountLabel);

        return panel;
    }

    private void loadNetwork(ActionEvent e) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setAcceptAllFileFilterUsed(false);
        fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("Text Files", "txt"));

        if (fileChooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try {
                environment = new Environment(file.getAbsolutePath(), this);
                messageField.setText("Loaded network from " + file.getName());
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(frame, "Failed to load file: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void startSimulation() {
        if (environment != null) {
            environment.simulate();
            messageField.setText("Simulation completed.");
        } else {
            messageField.setText("Please load a network before starting the simulation.");
        }
    }

    /**
     * Updates the amount of discovered Events and prints that to the terminal.
     * @param count the updated counter of events.
     */
    public void updateEventCount(int count) {
        eventCountLabel.setText("Events: " + count);
    }

    /**
     * Updates the amount of created Agents and prints that to the terminal.
     * @param count the updated counter of Agents.
     */
    public void updateAgentCount(int count) {
        agentCountLabel.setText("Agents: " + count);
    }

    /**
     * Updates the amount of created QueryMessages and prints that to the terminal.
     * @param count the updated counter of Querys.
     */
    public void updateQueryMessageCount(int count) {
        queryMessageCountLabel.setText("QueryMessages: " + count);
    }

    /**
     * Updates the amount of created ResponseMessages and prints that to the terminal.
     * @param count the updated counter of Responses.
     */
    public void updateResponseMessageCount(int count) {
        responseMessageCountLabel.setText("ResponseMessages: " + count);
    }

    /**
     * Starts the simulation.
     * @param args a String with the name of the file to be run.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new SensorNetworkSimulation();
            }
        });
    }
}
