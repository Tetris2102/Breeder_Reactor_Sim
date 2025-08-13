package breeder;

import java.awt.*;
import java.util.*;
import java.util.List;
import javax.swing.*;

public class DecaySpectrumPlot extends JPanel {
    private Map<Double, Double> spectrumData;
    // private Map<Isotope, Double[]> spectrumLines;  // spectrumData without Gaussian smoothing (single peaks), key - Isotope, value - decay energy, MeV

    public DecaySpectrumPlot(Map<Double, Double> spectrumData/*, Map<Isotope, Double[]> spectrumLines*/) {
        this.spectrumData = spectrumData;
        // this.spectrumLines = spectrumLines;
        setBackground(Color.WHITE); // White background
    }
    
    public void updateSpectrum(Map<Double, Double> newSpectrumData/*, Map<Isotope, Double[]> newSpectrumLines*/) {
        this.spectrumData.clear();
        this.spectrumData.putAll(newSpectrumData);
        // this.spectrumLines.clear();
        // this.spectrumLines.putAll(newSpectrumLines);
        repaint(); // Trigger redraw
    }
    
    // Smart number formatting for compact axis labels
    private String formatCompactNumber(double value, double range) {
        if (range >= 1000000) {
            // Use scientific notation for very large ranges
            return String.format("%.1e", value);
        } else if (range >= 10000) {
            // Use K notation for thousands (e.g., 1.5K instead of 1500)
            if (value >= 1000) {
                return String.format("%.1fK", value / 1000);
            } else {
                return String.format("%.0f", value);
            }
        } else if (range >= 1000) {
            // Use compact format for hundreds
            return String.format("%.0f", value);
        } else if (range >= 100) {
            // Use 1 decimal place for tens
            return String.format("%.1f", value);
        } else if (range >= 10) {
            // Use 1 decimal place for small numbers
            return String.format("%.1f", value);
        } else {
            // Use 1 decimal place for very small numbers
            return String.format("%.1f", value);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(1.0f));
        
        if (spectrumData.isEmpty()/* || spectrumLines.isEmpty()*/) return;

        // Find min/max for X and Y
        // Dynamically
        // double minX = Double.MAX_VALUE, maxX = -Double.MAX_VALUE;
        // double maxY = -Double.MAX_VALUE;
        // Or with fixed minimum values
        double minX = 0.0, maxX = -Double.MAX_VALUE;
        double maxY = 0.0;

        for (var entry : spectrumData.entrySet()) {
            double x = entry.getKey();
            double y = entry.getValue();
            if (x < minX) minX = x;
            if (x > maxX) maxX = x;
            if (y > maxY) maxY = y;
        }

        if (maxY <= 0) {
            // Draw empty plot with minimum scale
            int w = getWidth();
            int h = getHeight();
            int margin = 50;
            g2.setColor(Color.BLACK);
            g2.drawLine(margin, h - margin + 1, w - margin, h - margin + 1); // X axis
            g2.drawLine(margin, margin, margin, h - margin); // Y axis
            g2.drawString("Energy (keV)", w / 2 - 30, h - 5); // X-axis label
            g2.rotate(-Math.PI / 2);
            g2.drawString("Counts", -h / 2 - 20, 15); // Y-axis label
            g2.rotate(Math.PI / 2);
            
            // Draw "No Data" message
            g2.setColor(Color.GRAY);
            g2.setFont(new Font("Arial", Font.ITALIC, 16));
            String noDataMsg = "No spectrum data available";
            int msgWidth = g2.getFontMetrics().stringWidth(noDataMsg);
            g2.drawString(noDataMsg, (w - msgWidth) / 2, h / 2);
            return;
        }
        
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
        
        // Ensure minimum Y-axis scale to prevent very small scales
        double minYScale = 10.0; // Minimum 10 counts on Y-axis
        if (maxY < minYScale) {
            maxY = minYScale;
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

        // ===== Draw X-axis ticks & labels (dynamic adaptive spacing) =====
        double xRange = maxX - minX;
        double minXPixelSpacing = 50; // min pixel gap between ticks
        
        // Calculate appropriate tick step based on energy range
        double xTickStep = Math.pow(10, Math.floor(Math.log10(xRange / 8))); // Start with ~8 ticks
        
        // Refine to nice round numbers
        if (xTickStep > xRange / 4) xTickStep = xRange / 4;
        if (xTickStep > xRange / 6) xTickStep = xRange / 6;
        if (xTickStep > xRange / 8) xTickStep = xRange / 8;
        if (xTickStep > xRange / 10) xTickStep = xRange / 10;
        
        // Ensure minimum tick step
        xTickStep = Math.max(xTickStep, 0.1);
        
        double xPixelPerTick = xTickStep * xScale;
        
        // If ticks are too close, increase spacing
        while (xPixelPerTick < minXPixelSpacing && xTickStep < xRange) {
            xTickStep *= 2;
            xPixelPerTick = xTickStep * xScale;
        }
        
        // Safety check: limit maximum number of ticks to prevent overcrowding
        int maxTicks = 20;
        int estimatedTicks = (int) Math.ceil(xRange / xTickStep);
        if (estimatedTicks > maxTicks) {
            xTickStep = xRange / maxTicks;
        }

        for (double xVal = Math.ceil(minX / xTickStep) * xTickStep; xVal <= maxX; xVal += xTickStep) {
            int xPix = (int) (margin + (xVal - minX) * xScale);
            g2.drawLine(xPix + 1, h - margin, xPix + 1, h - margin + 5); // tick
            
            // Smart label formatting for compact display
            String label = formatCompactNumber(xVal, xRange);
            
            int labelWidth = g2.getFontMetrics().stringWidth(label);
            g2.drawString(label, xPix - labelWidth / 2, h - margin + 20);
        }


        // ===== Draw Y-axis ticks & labels (dynamic adaptive spacing) =====
        double yRange = maxY;
        double minYPixelSpacing = 30; // min pixel gap between ticks
        
        // Calculate appropriate tick step based on the actual data range
        double yTickStep = Math.pow(10, Math.floor(Math.log10(yRange / 6))); // Start with ~6 ticks
        
        // Refine the tick step to be a nice round number
        if (yTickStep > yRange / 3) yTickStep = yRange / 3;
        if (yTickStep > yRange / 4) yTickStep = yRange / 4;
        if (yTickStep > yRange / 5) yTickStep = yRange / 5;
        if (yTickStep > yRange / 6) yTickStep = yRange / 6;
        if (yTickStep > yRange / 8) yTickStep = yRange / 8;
        
        // Ensure minimum tick step
        yTickStep = Math.max(yTickStep, 1.0); // Minimum 1 count per tick
        
        double yPixelPerTick = yTickStep * yScale;
        
        // If ticks are too close, increase spacing
        while (yPixelPerTick < minYPixelSpacing && yTickStep < yRange) {
            yTickStep *= 2;
            yPixelPerTick = yTickStep * yScale;
        }
        
        // Safety check: limit maximum number of ticks to prevent overcrowding
        int maxYTicks = 15;
        int estimatedYTicks = (int) Math.ceil(yRange / yTickStep);
        if (estimatedYTicks > maxYTicks) {
            yTickStep = yRange / maxYTicks;
        }

        for (double yVal = 0; yVal <= maxY; yVal += yTickStep) {
            int yPix = (int) (h - margin - yVal * yScale);
            g2.drawLine(margin - 5, yPix, margin, yPix); // tick
            
            // Smart label formatting for compact display
            String label = formatCompactNumber(yVal, yRange);
            
            int labelWidth = g2.getFontMetrics().stringWidth(label);
            g2.drawString(label, margin - labelWidth - 8, yPix + 4);
        }

        FontMetrics fm = g2.getFontMetrics();  // For centering text

        // Draw the spectrum bars
        for (var entry : spectrumData.entrySet()) {
            double originalValue = entry.getValue();
            double displayValue = Math.min(originalValue, maxY); // Cap at maxY for display
            
            // Prevent first column from crossing the Y-axis
            int xPix = (int) (margin + (entry.getKey() - minX) * xScale + 1);
            int yPix = (int) (h - margin - displayValue * yScale);

            // Use different color for capped peaks
            // if (originalValue > maxY) {
            //     g2.setColor(Color.ORANGE); // Orange for peaks that are capped
            // } else {
            //     g2.setColor(Color.GREEN); // Green for normal peaks
            // }
            
            g2.setColor(Color.GREEN);
            g2.drawLine(xPix, h - margin, xPix, yPix);

            // Mark isotope peaks (not working currently)
            // ArrayList<Isotope> isotopesMarked = new ArrayList<>();
            // g2.setColor(Color.BLACK);
            // for (Isotope i : spectrumLines.keySet()) {
            //     if(spectrumLines.get(i)[1] > 5 && !isotopesMarked.contains(i)) {
            //         String iName = i.getName();
            //         int textWidth = fm.stringWidth(iName);
            //         int textHeight = fm.getAscent() + fm.getDescent();

            //         int centeredX = xPix - (textWidth / 2);
            //         int centeredY = yPix - (textHeight / 2);

            //         g2.drawString(i.getName(), centeredX, centeredY - 10);
            //         isotopesMarked.add(i);
            //     }
            // }
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
