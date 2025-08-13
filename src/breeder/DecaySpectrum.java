package breeder;

import java.util.HashMap;
import java.util.Map;

public class DecaySpectrum {
    private final DecayChainNest fuel;

    public DecaySpectrum(DecayChainNest fuel) {
        this.fuel = fuel;
    }

    public Map<Double, Double> captureDS(double time, DecayType decayType) {
        return fuel.captureDS(time, decayType);
    }

    public Map<Isotope, Double[]> captureDSIMap(double time, DecayType decayType) {
        return fuel.captureDSIMap(time, decayType);
    }

    public Map<Isotope, Double[]> captureDSIMap(int countsToStop, double timeStep, DecayType decayType) {
        int counts = 0;
        Map<Isotope, Double[]> spectrumLines = new HashMap<>();

        while (true) {
            spectrumLines = fuel.captureDSIMap(timeStep, decayType);
            for (Isotope i : spectrumLines.keySet()) {
                counts += spectrumLines.get(i)[1];
            }
            if (counts >= countsToStop) {
                break;
            }
        }
        return spectrumLines;
    }

    // This method is no longer needed in its old form.
    // The background is now added directly in simulateRealDS.
    
    public Map<Double, Double> toGauss(Map<Double, Double> spectrum, double sigma) {
        if (spectrum.isEmpty()) {
            return new HashMap<>();
        }
        
        Map<Double, Double> broadenedSpectrum = new HashMap<>();
        
        double minEnergy = spectrum.keySet().stream().min(Double::compare).orElse(0.0);
        double maxEnergy = spectrum.keySet().stream().max(Double::compare).orElse(3000.0);
        
        // Extend the energy range to show full Gaussian peaks (3 sigma on each side)
        // Always start from 0 keV to ensure background radiation is visible from the beginning
        double extendedMinEnergy = 0.0;
        double extendedMaxEnergy = maxEnergy + 3 * sigma;
        
        // Use a smaller energy step to properly sample the Gaussian function
        for (double energy = extendedMinEnergy; energy <= extendedMaxEnergy; energy += 0.5) {
            double totalIntensity = 0.0;
            
            for (Map.Entry<Double, Double> entry : spectrum.entrySet()) {
                double E0 = entry.getKey();
                double intensity = entry.getValue();
                double diff = energy - E0;
                double gauss = Math.exp(-diff * diff / (2 * sigma * sigma)) / (sigma * Math.sqrt(2 * Math.PI));
                totalIntensity += intensity * gauss;
            }
            
            // Always add the point, even if intensity is 0, to ensure background is added everywhere
            broadenedSpectrum.put(energy, totalIntensity);
        }
        
        return broadenedSpectrum;
    }

    // Now takes sigma as a parameter and adds a flat background
    public Map<Double, Double> simulateRealDS(
        int countsToStop, double timeStep, DecayType decayType, double sigma, double backgroundCounts) {
        
        double totalTime = 0.0;
        Map<Double, Double> accumulatedSpectrum = new HashMap<>();
        Map<Double, Double> broadenedSpectrum = new HashMap<>();

        int totalCounts = 0;

        while (true) {
            fuel.simulateDecay(timeStep);
            totalTime += timeStep;

            Map<Double, Double> spectrum = captureDS(timeStep, decayType);
            
            // Accumulate the raw spectrum over time
            for (Map.Entry<Double, Double> entry : spectrum.entrySet()) {
                accumulatedSpectrum.merge(entry.getKey(), entry.getValue(), Double::sum);
            }
            
            // Broaden the accumulated spectrum using the provided sigma
            broadenedSpectrum = toGauss(accumulatedSpectrum, sigma);
            
            if (decayType == DecayType.GAMMA) {
                // Add a constant background to every point in the broadened spectrum
                broadenedSpectrum.replaceAll((energy, counts) -> counts + backgroundCounts);
            }

            // Increment total counts in the current broadened spectrum
            totalCounts += broadenedSpectrum.values().stream().mapToInt(Double::intValue).sum();
            
            if (totalCounts >= countsToStop) {
                break;
            }
            
            // Safety check to prevent infinite loops
            if (decayType != DecayType.GAMMA && totalTime > 10.0 && totalCounts < 10.0) {
                break;
            }
        }

        return broadenedSpectrum;
    }
}