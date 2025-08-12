package breeder;

import java.awt.*;
import java.util.*;
import java.util.List;
import javax.swing.*;

public class DecaySpectrumPlot extends JPanel {
    private final Map<Double, Double> spectrumData;

    public DecaySpectrumPlot(Map<Double, Double> spectrumData) {
        this.spectrumData = spectrumData;
        setBackground(Color.WHITE); // White background
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (spectrumData.isEmpty()) return;

        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(1.0f));

        // Find min/max for X and Y
        // Dynamically
        // double minX = Double.MAX_VALUE, maxX = -Double.MAX_VALUE;
        // double maxY = -Double.MAX_VALUE;
        // Or fixed minimum values
        double minX = 0.0, maxX = -Double.MAX_VALUE;
        double maxY = 0.0;

        for (var entry : spectrumData.entrySet()) {
            double x = entry.getKey();
            double y = entry.getValue();
            if (x < minX) minX = x;
            if (x > maxX) maxX = x;
            if (y > maxY) maxY = y;
        }

        if (maxY <= 0) return; // Nothing to plot
        
        // Smart scaling: Set a reasonable cap based on the energy distribution
        // Find peaks in different energy regions to avoid one peak dominating
        double energyRange = maxX - minX;
        double regionSize = energyRange / 4; // Divide into 4 energy regions
        
        List<Double> regionPeaks = new ArrayList<>();
        
        for (int region = 0; region < 4; region++) {
            double regionMin = minX + region * regionSize;
            double regionMaxEnergy = minX + (region + 1) * regionSize;
            
            // Find the maximum value in this energy region
            double regionMaxValue = spectrumData.entrySet().stream()
                .filter(entry -> entry.getKey() >= regionMin && entry.getKey() <= regionMaxEnergy)
                .mapToDouble(Map.Entry::getValue)
                .max()
                .orElse(0.0);
            
            if (regionMaxValue > 0) {
                regionPeaks.add(regionMaxValue);
            }
        }
        
        // Calculate a reasonable cap based on the median of region peaks
        if (!regionPeaks.isEmpty()) {
            regionPeaks.sort(Double::compareTo);
            double medianPeak = regionPeaks.get(regionPeaks.size() / 2);
            
            // Cap at 5x the median peak value, but not less than 100 counts
            double suggestedCap = Math.max(medianPeak * 5, 100.0);
            
            if (maxY > suggestedCap) {
                maxY = suggestedCap;
            }
        }

        int w = getWidth();
        int h = getHeight();
        int margin = 50;

        double xScale = (w - 2.0 * margin) / (maxX - minX);
        double yScale = (h - 2.0 * margin) / maxY;

        // Draw axes
        g2.setColor(Color.BLACK);
        g2.drawLine(margin, h - margin + 1, w - margin, h - margin + 1); // X axis, keep columns above the axis
        g2.drawLine(margin, margin, margin, h - margin); // Y axis

        g2.setColor(Color.BLACK);
        g2.drawString("Energy (keV)", w / 2 - 30, h - 5); // X-axis label
        g2.rotate(-Math.PI / 2);
        g2.drawString("Counts", -h / 2 - 20, 15); // Y-axis label
        g2.rotate(Math.PI / 2);
        
        // Add title indicating if scaling is capped
        // double originalMax = spectrumData.values().stream().mapToDouble(Double::doubleValue).max().orElse(0.0);
        // if (originalMax > maxY) {
        //     g2.setColor(Color.RED);
        //     g2.drawString("Y-axis capped at " + String.format("%.0f", maxY) + " for visibility", 10, 20);
        //     g2.setColor(Color.BLACK); // Reset color for tick labels
        // }

        // ===== Draw X-axis ticks & labels (adaptive spacing) =====
        double xTickStep = 1.0; // start at 1 keV
        double minXPixelSpacing = 50; // min pixel gap between ticks

        // Check if we need to increase spacing
        double xPixelPerTick = xTickStep * xScale;
        if (xPixelPerTick < minXPixelSpacing) {
            xTickStep *= 2; // bump to 2 keV
            xPixelPerTick = xTickStep * xScale;
        }
        if (xPixelPerTick < minXPixelSpacing) {
            xTickStep *= 5; // bump to 10 keV
            xPixelPerTick = xTickStep * xScale;
        }
        if (xPixelPerTick < minXPixelSpacing) {
            xTickStep *= 5; // bump to 50 keV
            xPixelPerTick = xTickStep * xScale;
        }

        for (double xVal = Math.ceil(minX / xTickStep) * xTickStep; xVal <= maxX; xVal += xTickStep) {
            int xPix = (int) (margin + (xVal - minX) * xScale);
            g2.drawLine(xPix + 1, h - margin, xPix + 1, h - margin + 5); // tick
            String label = String.format("%.0f", xVal);
            int labelWidth = g2.getFontMetrics().stringWidth(label);
            g2.drawString(label, xPix - labelWidth / 2, h - margin + 20);
        }


        // ===== Draw Y-axis ticks & labels (adaptive spacing) =====
        // Calculate appropriate tick step based on the actual data range
        double yRange = maxY;
        double yTickStep = Math.pow(10, Math.floor(Math.log10(yRange / 5))); // Start with ~5 ticks
        
        // Refine the tick step to be a nice round number
        if (yTickStep > yRange / 2) yTickStep = yRange / 2;
        if (yTickStep > yRange / 3) yTickStep = yRange / 3;
        if (yTickStep > yRange / 4) yTickStep = yRange / 4;
        if (yTickStep > yRange / 5) yTickStep = yRange / 5;
        
        // Ensure minimum tick step
        yTickStep = Math.max(yTickStep, 1.0);
        
        double minYPixelSpacing = 30; // min pixel gap between ticks
        double yPixelPerTick = yTickStep * yScale;
        
        // If ticks are too close, increase spacing
        while (yPixelPerTick < minYPixelSpacing && yTickStep < yRange) {
            yTickStep *= 2;
            yPixelPerTick = yTickStep * yScale;
        }

        for (double yVal = 0; yVal <= maxY; yVal += yTickStep) {
            int yPix = (int) (h - margin - yVal * yScale);
            g2.drawLine(margin - 5, yPix, margin, yPix); // tick
            String label = String.format("%.0f", yVal);
            int labelWidth = g2.getFontMetrics().stringWidth(label);
            g2.drawString(label, margin - labelWidth - 8, yPix + 4);
        }

        // Draw the spectrum bars
        for (var entry : spectrumData.entrySet()) {
            double originalValue = entry.getValue();
            double displayValue = Math.min(originalValue, maxY); // Cap at maxY for display
            
            // Prevent first column from crossing the Y-axis
            int xPix = (int) (margin + (entry.getKey() - minX) * xScale + 1);
            int yPix = (int) (h - margin - displayValue * yScale);
            
            g2.setColor(Color.GREEN);

            // Use different color for capped peaks
            // if (originalValue > maxY) {
            //     g2.setColor(Color.ORANGE); // Orange for peaks that are capped
            // } else {
            //     g2.setColor(Color.GREEN); // Green for normal peaks
            // }
            
            g2.drawLine(xPix, h - margin, xPix, yPix);
        }
    }

    // Example usage inside a container
    // public static void main(String[] args) {
    //     SwingUtilities.invokeLater(() -> {
    //         Map<Double, Double> spectrumData = new HashMap<>();

    //         SpectrumPlot plot = new SpectrumPlot(spectrumData);

    //         JPanel mainPanel = new JPanel(new BorderLayout());
    //         mainPanel.add(plot, BorderLayout.CENTER);

    //         JFrame frame = new JFrame("Spectrum Viewer");
    //         frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    //         frame.setSize(850, 650);
    //         frame.add(mainPanel);
    //         frame.setVisible(true);

    //         // Simulated real-time data
    //         new javax.swing.Timer(50, e -> {
    //             Map<Double, Double> data = spectrumData;
    //             double x = Math.max(0, 500 - data.size());
    //             double y = Math.random() * 100;
    //             data.put(x, y);

    //             if (data.size() < 800) {
    //                 plot.repaint();
    //             }
    //         }).start();
    //     });
    // }
}
