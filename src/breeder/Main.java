package breeder;

import java.awt.*;
import java.util.Map;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.HashMap;

public class Main {

    // MEMO: maybe add densities for all isotopes in IsotopeLibrary
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Create the main frame
            JFrame frame = new JFrame("Breeder Reactor Simulator");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1200, 800);
            
            // Create fuel with initial isotopes
            DecayChainNest fuel = IsotopeLibrary.decayTree;

            // Create neutron source
            NeutronSource neutronSource = new NeutronSource(IsotopeLibrary.Pu238, IsotopeLibrary.Be9);
            neutronSource.setAlphaSourceMass(0.0f);  // 0 grams by default

            // Create breeder
            Breeder breeder = new Breeder(fuel, neutronSource, 100.0f, 0.3f);
            
            // Create spectrum simulator
            DecaySpectrum decaySpectrum = new DecaySpectrum(breeder);
            
            // Create initial empty spectrum
            Map<Double, Double> initialSpectrum = new java.util.HashMap<>();
            // Map<Isotope, Double[]> initialSpectrumLines = new java.util.HashMap<>();
            DecaySpectrumPlot plot = new DecaySpectrumPlot(initialSpectrum/*, initialSpectrumLines*/);
            
            // Create spectrum capture panel
            JPanel capturePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
            capturePanel.setBorder(BorderFactory.createTitledBorder("Spectrum Capture"));
            
            // Decay type dropdown
            JLabel decayTypeLabel = new JLabel("Particle:");
            String[] decayTypes = {"ALPHA", "BETA", "GAMMA", "NEUTRON"};
            JComboBox<String> decayTypeCombo = new JComboBox<>(decayTypes);
            decayTypeCombo.setSelectedItem("GAMMA"); // Default to gamma
            
            // Capture button
            JButton captureButton = new JButton("Capture Spectrum");
            captureButton.setFont(new Font("Arial", Font.BOLD, 14));
            captureButton.setBackground(new Color(70, 130, 180)); // Steel blue
            captureButton.setForeground(Color.BLACK);
            captureButton.setFocusPainted(false);
            
            capturePanel.add(decayTypeLabel);
            capturePanel.add(decayTypeCombo);
            capturePanel.add(captureButton);
            
            JLabel statusLabel = new JLabel("No spectrum captured yet");
            statusLabel.setFont(new Font("Arial", Font.ITALIC, 12));
            statusLabel.setForeground(Color.GRAY);
            
            // Create isotope table
            String[] columnNames = {"Isotope", "Activity (Bq)", "Atoms"};
            DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false; // Make table read-only
                }
            };
            JTable isotopeTable = new JTable(tableModel);
            JScrollPane tableScrollPane = new JScrollPane(isotopeTable);
            tableScrollPane.setPreferredSize(new Dimension(400, 200));
            
            // Create isotope control panel
            JPanel isotopeControlPanel = new JPanel(new GridBagLayout());
            isotopeControlPanel.setBorder(BorderFactory.createTitledBorder("Isotope Control"));
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(2, 2, 2, 2);
            
            // Isotope name input
            gbc.gridx = 0; gbc.gridy = 0;
            isotopeControlPanel.add(new JLabel("Isotope:"), gbc);
            gbc.gridx = 1; gbc.gridy = 0;
            JTextField isotopeNameField = new JTextField(10);
            isotopeControlPanel.add(isotopeNameField, gbc);
            
            // Unit selection
            gbc.gridx = 0; gbc.gridy = 1;
            isotopeControlPanel.add(new JLabel("Unit:"), gbc);
            gbc.gridx = 1; gbc.gridy = 1;
            JRadioButton gramsRadio = new JRadioButton("Grams", true);
            JRadioButton atomsRadio = new JRadioButton("Atoms");
            ButtonGroup unitGroup = new ButtonGroup();
            unitGroup.add(gramsRadio);
            unitGroup.add(atomsRadio);
            JPanel unitPanel = new JPanel();
            unitPanel.add(gramsRadio);
            unitPanel.add(atomsRadio);
            isotopeControlPanel.add(unitPanel, gbc);
            
            // Value input
            gbc.gridx = 0; gbc.gridy = 2;
            isotopeControlPanel.add(new JLabel("Value:"), gbc);
            gbc.gridx = 1; gbc.gridy = 2;
            JTextField valueField = new JTextField(10);
            isotopeControlPanel.add(valueField, gbc);
            
            // Set button
            gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
            JButton setButton = new JButton("Set Isotope");
            setButton.setBackground(new Color(70, 130, 180));
            setButton.setForeground(Color.BLACK);
            isotopeControlPanel.add(setButton, gbc);
            
            // Reactor simulation panel
            JPanel reactorPanel = new JPanel(new GridBagLayout());
            reactorPanel.setBorder(BorderFactory.createTitledBorder("Reactor Simulation"));
            GridBagConstraints gbc2 = new GridBagConstraints();
            gbc2.insets = new Insets(2, 2, 2, 2);
            
            // Time input
            gbc2.gridx = 0; gbc2.gridy = 0;
            reactorPanel.add(new JLabel("Time (seconds):"), gbc2);
            gbc2.gridx = 1; gbc2.gridy = 0;
            JTextField timeField = new JTextField(10);
            timeField.setText("3600"); // Default 1 hour
            reactorPanel.add(timeField, gbc2);
            
            // Simulate button
            gbc2.gridx = 0; gbc2.gridy = 1; gbc2.gridwidth = 2;
            JButton simulateButton = new JButton("Simulate Reactor");
            simulateButton.setBackground(new Color(220, 20, 60)); // Crimson
            simulateButton.setForeground(Color.BLACK);
            reactorPanel.add(simulateButton, gbc2);
            
            // Neutron source panel
            JPanel neutronPanel = new JPanel(new GridBagLayout());
            neutronPanel.setBorder(BorderFactory.createTitledBorder("Neutron Source"));
            GridBagConstraints gbc3 = new GridBagConstraints();
            gbc3.insets = new Insets(2, 2, 2, 2);
            
            // Source isotope
            gbc3.gridx = 0; gbc3.gridy = 0;
            neutronPanel.add(new JLabel("Source Isotope:"), gbc3);
            gbc3.gridx = 1; gbc3.gridy = 0;
            JTextField sourceIsotopeField = new JTextField(10);
            sourceIsotopeField.setText("Pu-238");
            neutronPanel.add(sourceIsotopeField, gbc3);
            
            // Target isotope
            gbc3.gridx = 0; gbc3.gridy = 1;
            neutronPanel.add(new JLabel("Target Isotope:"), gbc3);
            gbc3.gridx = 1; gbc3.gridy = 1;
            JTextField targetIsotopeField = new JTextField(10);
            targetIsotopeField.setText("Be-9");
            neutronPanel.add(targetIsotopeField, gbc3);
            
            // Source mass
            gbc3.gridx = 0; gbc3.gridy = 2;
            neutronPanel.add(new JLabel("Source Mass (g):"), gbc3);
            gbc3.gridx = 1; gbc3.gridy = 2;
            JTextField sourceMassField = new JTextField(10);
            sourceMassField.setText("1.0");
            neutronPanel.add(sourceMassField, gbc3);
            
            // Set neutron source button
            gbc3.gridx = 0; gbc3.gridy = 3; gbc3.gridwidth = 2;
            JButton setNeutronButton = new JButton("Set Neutron Source");
            setNeutronButton.setBackground(new Color(255, 140, 0)); // Dark orange
            setNeutronButton.setForeground(Color.BLACK);
            neutronPanel.add(setNeutronButton, gbc3);
            
            // Helper function to find isotope by name
            java.util.function.Function<String, Isotope> findIsotopeByName = (name) -> {
                for (Isotope isotope : fuel.getIsotopesAtomsMap().keySet()) {
                    if (isotope.getName().equalsIgnoreCase(name)) {
                        return isotope;
                    }
                }
                return null;
            };
            
            // Function to update isotope table
            Runnable updateTable = () -> {
                tableModel.setRowCount(0); // Clear existing rows
                Map<Isotope, Double> isotopeMap = fuel.getIsotopesAtomsMap();
                
                isotopeMap.entrySet().stream()
                    .filter(entry -> entry.getValue() > 0)
                    .sorted((a, b) -> a.getKey().getName().compareTo(b.getKey().getName()))
                    .forEach(entry -> {
                        Isotope isotope = entry.getKey();
                        double atoms = entry.getValue();
                        double activity = isotope.getActivityPerAtom() * atoms;
                        
                        tableModel.addRow(new Object[]{
                            isotope.getName(),
                            String.format("%.2e", activity),
                            String.format("%.2e", atoms)
                        });
                    });
            };
            
            // Function to capture new spectrum
            captureButton.addActionListener(e -> {
                // Simulate decay for a short time
                fuel.simulateDecay(1.0, 0.1);
                
                // Get selected decay type
                String selectedDecayType = (String) decayTypeCombo.getSelectedItem();
                DecayType decayType;
                switch (selectedDecayType) {
                    case "ALPHA":
                        decayType = DecayType.ALPHA;
                        break;
                    case "BETA":
                        decayType = DecayType.BETA;
                        break;
                    case "GAMMA":
                        decayType = DecayType.GAMMA;
                        break;
                    case "NEUTRON":
                        decayType = DecayType.NEUTRON;
                        break;
                    default:
                        decayType = DecayType.GAMMA;
                }

                // Capture spectrum
                double sigma = 0.004;
                double backgroundCounts = 5.0;

                // Map<Double, Double> newSpectrum = null;
                // if (decayType == DecayType.NEUTRON) {
                //     newSpectrum = breeder.getNeutronSpectrum(100, 10000, 0.1);  // Raw neutron energy peaks
                //     newSpectrum = decaySpectrum.toGauss(newSpectrum, sigma);  // Gaussian smoothed
                // } else {
                //     newSpectrum = decaySpectrum.simulateRealDS(2500, 0.1, decayType, sigma, backgroundCounts);
                // }
                Map<Double, Double> newSpectrum = decaySpectrum.simulateRealDS(2500, 0.1, decayType, sigma, backgroundCounts);
                // Map<Isotope, Double[]> newSpectrumLines = decaySpectrum.captureDSIMap(2500, 0.1, decayType);
                // Update plot
                plot.updateSpectrum(newSpectrum/*, newSpectrumLines*/);
                
                // Update table
                updateTable.run();
                
                // Update status
                statusLabel.setText(selectedDecayType + " spectrum captured: " + newSpectrum.size() + " data points");
                statusLabel.setForeground(new Color(0, 128, 0)); // Green
            });
            
            // Set isotope button action
            setButton.addActionListener(e -> {
                try {
                    String isotopeName = isotopeNameField.getText().trim();
                    double value = Double.parseDouble(valueField.getText());
                    
                    Isotope isotope = findIsotopeByName.apply(isotopeName);
                    if (isotope == null) {
                        JOptionPane.showMessageDialog(frame, "Isotope '" + isotopeName + "' not found!", 
                            "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    
                    if (gramsRadio.isSelected()) {
                        // Set grams directly
                        fuel.getDecayChain(isotope).setIsotopeMassGrams(isotope, value);
                    } else {
                        // Set atoms directly
                        fuel.getDecayChain(isotope).setIsotopeMassAtoms(isotope, value);
                    }
                    
                    updateTable.run();
                    statusLabel.setText("Isotope " + isotopeName + " set successfully");
                    statusLabel.setForeground(new Color(0, 128, 0)); // Green
                    
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, "Invalid number format!", 
                        "Error", JOptionPane.ERROR_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage(), 
                        "Error", JOptionPane.ERROR_MESSAGE);
                }
            });
            
            // Simulate reactor button action
            simulateButton.addActionListener(e -> {
                try {
                    double time = Double.parseDouble(timeField.getText());
                    
                    // Simulate reactor for given time
                    breeder.simulateTime(time, Math.min(time/100, 1.0));  // Adaptive time step
                    
                    updateTable.run();
                    statusLabel.setText("Reactor simulated for " + time + " seconds");
                    statusLabel.setForeground(new Color(0, 128, 0)); // Green
                    
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, "Invalid time format!", 
                        "Error", JOptionPane.ERROR_MESSAGE);
                }
            });
            
            // Set neutron source button action
            setNeutronButton.addActionListener(e -> {
                try {
                    String sourceName = sourceIsotopeField.getText().trim();
                    String targetName = targetIsotopeField.getText().trim();
                    double sourceMass = Double.parseDouble(sourceMassField.getText());
                    
                    Isotope sourceIsotope = findIsotopeByName.apply(sourceName);
                    Isotope targetIsotope = findIsotopeByName.apply(targetName);

                    // Set alpha dource mass in fuel
                    fuel.getDecayChain(sourceIsotope).setIsotopeMassGrams(sourceIsotope, sourceMass);

                    // (alpha, n) converter is in abundance (10x the mass of alpha source)
                    fuel.getDecayChain(targetIsotope).setIsotopeMassGrams(targetIsotope, sourceMass * 10);
                    
                    if (sourceIsotope == null) {
                        JOptionPane.showMessageDialog(frame, "Source isotope '" + sourceName + "' not found!",
                            "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    if (targetIsotope == null) {
                        JOptionPane.showMessageDialog(frame, "Target isotope '" + targetName + "' not found!", 
                            "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    
                    // Create neutron source
                    neutronSource.setAlphaSource(sourceIsotope);
                    neutronSource.setTargetIsotope(targetIsotope);
                    neutronSource.setAlphaSourceMass((float) sourceMass);
                    
                    if (neutronSource.getNeutronEnergy() == 0.0f) {
                        JOptionPane.showMessageDialog(frame, "Neutron emission energy is not set for " + targetName + "! " + targetIsotope.getNEmissionEnergy(), 
                            "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    
                    statusLabel.setText("Neutron source set: " + sourceName + " + " + targetName + 
                                      " (Rate: " + String.format("%.2e", neutronSource.getNeutronRate()) + " n/s)");
                    statusLabel.setForeground(new Color(0, 128, 0)); // Green
                    
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, "Invalid mass format!", 
                        "Error", JOptionPane.ERROR_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage(), 
                        "Error", JOptionPane.ERROR_MESSAGE);
                }
            });
            
            // Initial table update
            updateTable.run();
            
            // Layout setup
            JPanel leftPanel = new JPanel(new BorderLayout());
            leftPanel.add(plot, BorderLayout.CENTER);
            
            JPanel buttonPanel = new JPanel(new BorderLayout());
            buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            buttonPanel.add(capturePanel, BorderLayout.CENTER);
            buttonPanel.add(statusLabel, BorderLayout.SOUTH);
            leftPanel.add(buttonPanel, BorderLayout.SOUTH);
            
            JPanel rightPanel = new JPanel(new BorderLayout());
            
            // Top section with isotope table
            JPanel topRightPanel = new JPanel(new BorderLayout());
            JLabel titleLabel = new JLabel("Isotope Information", SwingConstants.CENTER);
            titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
            titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
            topRightPanel.add(titleLabel, BorderLayout.NORTH);
            topRightPanel.add(tableScrollPane, BorderLayout.CENTER);
            
            // Bottom section with controls
            JPanel bottomRightPanel = new JPanel();
            bottomRightPanel.setLayout(new BoxLayout(bottomRightPanel, BoxLayout.Y_AXIS));
            bottomRightPanel.add(isotopeControlPanel);
            bottomRightPanel.add(Box.createVerticalStrut(10));
            bottomRightPanel.add(reactorPanel);
            bottomRightPanel.add(Box.createVerticalStrut(10));
            bottomRightPanel.add(neutronPanel);
            
            // Scrollable bottom panel
            JScrollPane controlScrollPane = new JScrollPane(bottomRightPanel);
            controlScrollPane.setPreferredSize(new Dimension(400, 400));
            
            rightPanel.add(topRightPanel, BorderLayout.NORTH);
            rightPanel.add(controlScrollPane, BorderLayout.CENTER);
            
            // Main layout
            JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, rightPanel);
            splitPane.setDividerLocation(800);
            splitPane.setResizeWeight(0.7);
            
            frame.add(splitPane);
            frame.setVisible(true);
        });
    }
}