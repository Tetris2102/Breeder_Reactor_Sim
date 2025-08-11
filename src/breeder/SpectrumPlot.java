import java.awt.*;
import java.util.*;
import javax.swing.*;

public class SpectrumPlot extends JPanel {
    private final Map<Double, Double> spectrumData;

    public SpectrumPlot(Map<Double, Double> spectrumData) {
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
        double minX = Double.MAX_VALUE, maxX = -Double.MAX_VALUE;
        double maxY = -Double.MAX_VALUE;

        for (var entry : spectrumData.entrySet()) {
            double x = entry.getKey();
            double y = entry.getValue();
            if (x < minX) minX = x;
            if (x > maxX) maxX = x;
            if (y > maxY) maxY = y;
        }

        if (maxY <= 0) return; // Nothing to plot

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

        // ===== Draw X-axis ticks & labels (adaptive spacing) =====
        double xTickStep = 10.0; // start at 10 keV
        double minXPixelSpacing = 50; // min pixel gap between ticks

        // Check if we need to increase spacing
        double xPixelPerTick = xTickStep * xScale;
        if (xPixelPerTick < minXPixelSpacing) {
            xTickStep *= 5; // bump to 50 keV
        }

        for (double xVal = Math.ceil(minX / xTickStep) * xTickStep; xVal <= maxX; xVal += xTickStep) {
            int xPix = (int) (margin + (xVal - minX) * xScale);
            g2.drawLine(xPix + 1, h - margin, xPix + 1, h - margin + 5); // tick
            String label = String.format("%.0f", xVal);
            int labelWidth = g2.getFontMetrics().stringWidth(label);
            g2.drawString(label, xPix - labelWidth / 2, h - margin + 20);
        }


        // ===== Draw Y-axis ticks & labels (adaptive spacing) =====
        double yTickStep = 20.0; // start at 20 counts
        double minYPixelSpacing = 30; // min pixel gap between ticks

        // Check if we need to increase spacing
        double yPixelPerTick = yTickStep * yScale;
        if (yPixelPerTick < minYPixelSpacing) {
            yTickStep *= 5; // bump to 100 counts
        }

        for (double yVal = 0; yVal <= maxY; yVal += yTickStep) {
            int yPix = (int) (h - margin - yVal * yScale);
            g2.drawLine(margin - 5, yPix, margin, yPix); // tick
            String label = String.format("%.0f", yVal);
            int labelWidth = g2.getFontMetrics().stringWidth(label);
            g2.drawString(label, margin - labelWidth - 8, yPix + 4);
        }

        g2.setColor(Color.GREEN);
        for (var entry : spectrumData.entrySet()) {
            // Prevent first colunm from crossing the Y-axis
            int xPix = (int) (margin + (entry.getKey() - minX) * xScale + 1);
            int yPix = (int) (h - margin - entry.getValue() * yScale);
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
