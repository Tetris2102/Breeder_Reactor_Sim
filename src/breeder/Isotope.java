package breeder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Random;
import java.util.TreeMap;

public class Isotope {
    private final String name;  // e.g. "U-238"
    private final short massMolar;  // g/mol
    private float halfLife = Float.POSITIVE_INFINITY;  // in seconds
    private Isotope decayIsotope;
    private DecayType decayType;  // ALPHA, BETA, GAMMA, NEUTRON
    private float decayEnergy;  // MeV
    private float density;  // g/cm^3
    private final TreeMap<Float, CaptureData> neutronCaptureXS;  // Neutron capture cross section, barns
    private final TreeMap<Float, CaptureData> alphaCaptureXS;  // Alpha capture cross section, barns
    private final TreeMap<Float, CaptureDatum> fissionXS;  // Fission cross section, barns

    public Isotope(String name, short massMolar) {  // Stable isotope by default
        this.name = name;
        this.massMolar = massMolar;
        this.neutronCaptureXS = new TreeMap<>();
        this.alphaCaptureXS = new TreeMap<>();
        this.fissionXS = new TreeMap<>();
    }

    public void setDecayProperties(float halfLife, Isotope decayIsotope, DecayType decayType, float decayEnergy) {
        this.halfLife = halfLife;
        this.decayIsotope = decayIsotope;
        this.decayType = decayType;
        this.decayEnergy = decayEnergy;
    }

    public float getHalfLife() {
        return halfLife;
    }

    public Isotope getDecayIsotope() {
        return decayIsotope;
    }

    public DecayType getDecayType() {
        return decayType;
    }

    public float getDecayEnergy() {
        return decayEnergy;
    }

    // public void setFissionProperties(float energy, float xs, Isotope[] fissionProducts) {}

    // Store cross section and resulting isotope for each energy
    private static class CaptureData {
        public float crossSection;
        public Isotope productIsotope;

        CaptureData(float crossSection, Isotope productIsotope) {
            this.crossSection = crossSection;
            this.productIsotope = productIsotope;
        }
    }

    public void setAlphaCapture(float energy, float xs, Isotope productIsotope) {
        alphaCaptureXS.put(energy, new CaptureData(xs, productIsotope));
    }

    // Get cross section for alpha capture
    public float getAlphaCaptureXS(float energy) {
        if (alphaCaptureXS.isEmpty()) {
            return 0.0f;
        }
        
        Float floorKey = alphaCaptureXS.floorKey(energy);
        Float ceilingKey = alphaCaptureXS.ceilingKey(energy);

        if (floorKey == null) return alphaCaptureXS.get(ceilingKey).crossSection;
        if (ceilingKey == null) return alphaCaptureXS.get(floorKey).crossSection;

        float floorDiff = energy - floorKey;
        float ceilingDiff = ceilingKey - energy;

        return (float) ((floorDiff <= ceilingDiff)
            ? alphaCaptureXS.get(floorKey).crossSection
            : alphaCaptureXS.get(ceilingKey).crossSection);
    }

    // Get product isotope for alpha capture
    public Isotope getAlphaCaptureProduct(float energy) {
        if (alphaCaptureXS.isEmpty()) {
            return null;
        }

        Float floorKey = alphaCaptureXS.floorKey(energy);
        Float ceilingKey = alphaCaptureXS.ceilingKey(energy);

        if (floorKey == null) return alphaCaptureXS.get(ceilingKey).productIsotope;
        if (ceilingKey == null) return alphaCaptureXS.get(floorKey).productIsotope;

        float floorDiff = energy - floorKey;
        float ceilingDiff = ceilingKey - energy;

        return (floorDiff <= ceilingDiff)
            ? alphaCaptureXS.get(floorKey).productIsotope
            : alphaCaptureXS.get(ceilingKey).productIsotope;
    }

    public void setNeutronCapture(float energy, float xs, Isotope productIsotope) {
        neutronCaptureXS.put(energy, new CaptureData(xs, productIsotope));
    }

    // Get cross section for neutron capture
    public float getNeutronCaptureXS(float energy) {
        if (neutronCaptureXS.isEmpty()) {
            return 0.0f;
        }
        
        Float floorKey = neutronCaptureXS.floorKey(energy);
        Float ceilingKey = neutronCaptureXS.ceilingKey(energy);

        if (floorKey == null) return neutronCaptureXS.get(ceilingKey).crossSection;
        if (ceilingKey == null) return neutronCaptureXS.get(floorKey).crossSection;

        float floorDiff = energy - floorKey;
        float ceilingDiff = ceilingKey - energy;

        return (floorDiff <= ceilingDiff)
            ? neutronCaptureXS.get(floorKey).crossSection
            : neutronCaptureXS.get(ceilingKey).crossSection;
    }

    // Get product isotope for neutron capture
    public Isotope getNeutronCaptureProduct(float energy) {
        if (neutronCaptureXS.isEmpty()) {
            return null;
        }
        
        Float floorKey = neutronCaptureXS.floorKey(energy);
        Float ceilingKey = neutronCaptureXS.ceilingKey(energy);

        if (floorKey == null) return neutronCaptureXS.get(ceilingKey).productIsotope;
        if (ceilingKey == null) return neutronCaptureXS.get(floorKey).productIsotope;

        float floorDiff = energy - floorKey;
        float ceilingDiff = ceilingKey - energy;

        return (floorDiff <= ceilingDiff)
            ? neutronCaptureXS.get(floorKey).productIsotope
            : neutronCaptureXS.get(ceilingKey).productIsotope;
    }

    public class CaptureDatum {
        public float crossSection;
        public Isotope[] productIsotope;
        public float probability;

        CaptureDatum(float crossSection, Isotope[] productIsotope, float probability) {
            this.crossSection = crossSection;
            this.productIsotope = productIsotope;
            this.probability = probability;
        }
    }

    public void setFissionCapture(float energy, float xs, Isotope[] productIsotope, float probability) {
        fissionXS.put(energy, new CaptureDatum(xs, productIsotope, probability));
    }

    // Returns the TOTAL fission cross-section (sum of all channels) at given energy
    public float getFissionXS(float energy) {
        if (fissionXS.isEmpty()) return 0.0f;
        
        Float floorKey = fissionXS.floorKey(energy);
        Float ceilingKey = fissionXS.ceilingKey(energy);

        // Nearest-neighbor interpolation (unchanged)
        if (floorKey == null) return fissionXS.get(ceilingKey).crossSection;
        if (ceilingKey == null) return fissionXS.get(floorKey).crossSection;
        
        float floorDiff = energy - floorKey;
        float ceilingDiff = ceilingKey - energy;
        
        return (floorDiff <= ceilingDiff) 
            ? fissionXS.get(floorKey).crossSection 
            : fissionXS.get(ceilingKey).crossSection;
    }

    public Isotope[] getFissionProducts(float energy) {
        if (fissionXS.isEmpty()) {
            return null;
        }
        
        Float floorKey = fissionXS.floorKey(energy);
        Float ceilingKey = fissionXS.ceilingKey(energy);

        if (floorKey == null) return fissionXS.get(ceilingKey).productIsotope;
        if (ceilingKey == null) return fissionXS.get(floorKey).productIsotope;

        float floorDiff = energy - floorKey;
        float ceilingDiff = ceilingKey - energy;

        return (floorDiff <= ceilingDiff)
            ? fissionXS.get(floorKey).productIsotope
            : fissionXS.get(ceilingKey).productIsotope;
    }

    /**
     * Returns fission products weighted by their probability for a given neutron energy.
     * If multiple channels exist at the same energy, selects one probabilistically.
     * Uses the probabilities stored in CaptureDatum for weighted random selection.
     */
    public Isotope[] getWeightedFissionProducts(float energy) {
        if (fissionXS.isEmpty()) {
            return null;
        }

        // Get all fission channels at or near the requested energy
        NavigableMap<Float, CaptureDatum> candidates = fissionXS.subMap(
            fissionXS.floorKey(energy), true,
            fissionXS.ceilingKey(energy), true
        );

        if (candidates.isEmpty()) {
            return null;
        }

        // Collect all possible channels within ±10% energy window
        List<CaptureDatum> possibleChannels = new ArrayList<>();
        float energyTolerance = energy * 0.1f;  // 10% tolerance
        
        for (Map.Entry<Float, CaptureDatum> entry : candidates.entrySet()) {
            if (Math.abs(entry.getKey() - energy) <= energyTolerance) {
                possibleChannels.add(entry.getValue());
            }
        }

        if (possibleChannels.isEmpty()) {
            return null;
        }

        // If only one channel, return its products
        if (possibleChannels.size() == 1) {
            return possibleChannels.get(0).productIsotope;
        }

        // For multiple channels, perform weighted random selection
        float totalProbability = 0f;
        for (CaptureDatum channel : possibleChannels) {
            totalProbability += channel.probability;
        }

        // Normalize probabilities if they don't sum to 1
        float random = new Random().nextFloat() * totalProbability;
        float cumulativeProb = 0f;

        for (CaptureDatum channel : possibleChannels) {
            cumulativeProb += channel.probability;
            if (random <= cumulativeProb) {
                return channel.productIsotope;
            }
        }

        // Fallback to highest probability channel
        return possibleChannels.get(possibleChannels.size() - 1).productIsotope;
    }

    public String getName() {
        return name;
    }

    public short getMassMolar() {
        return massMolar;
    }

    public void setDensity(float density) {
        this.density = density;
    }

    public float getDensity() {
        return density;
    }

    public float getActivityPerGram() {
        // Activity of the isotope in Bq/g or decays/(second * gram)
        double activity = Math.log(2) * 6.022e23 / (halfLife * massMolar);
        return (float)activity;
    }

    public float getActivityPerAtom() {
        // Activity of the isotope in Bq/atom or decays/(second * atom)
        double activity = Math.log(2) / halfLife;
        return (float)activity;
    }

    public boolean isStable() {
        return halfLife == Float.POSITIVE_INFINITY;
    }
}