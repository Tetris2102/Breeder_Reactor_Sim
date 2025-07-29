package breeder;
import java.util.TreeMap;

public class Isotope {
    private final String name;  // e.g. "U-238"
    private final short massMolar;  // g/mol
    private float halfLife = Float.POSITIVE_INFINITY;  // in seconds
    private Isotope decayIsotope;
    private DecayType decayType;  // ALPHA, BETA, GAMMA, NEUTRON
    private float decayEnergy;  // eV
    private float density;  // g/cm^3
    private TreeMap<Float, CaptureData> neutronCaptureXS;  // Neutron capture cross section, cm^2
    private TreeMap<Float, CaptureData> alphaCaptureXS;  // Alpha capture cross section, cm^2
    private TreeMap<Float, CaptureDatum> fissionXS;  // Fission cross section, cm^2

    public Isotope(String name, short massMolar) {  // Stable isotope by default
        this.name = name;
        this.massMolar = massMolar;
        neutronCaptureXS = new TreeMap<>();
        alphaCaptureXS = new TreeMap<>();
    }

    public void setDecayProperties(float halfLife, Isotope decayIsotope, DecayType decayType, float decayEnergyEV) {
        this.halfLife = halfLife;
        this.decayIsotope = decayIsotope;
        this.decayType = decayType;
        this.decayEnergy = decayEnergyEV;
    }

    public float getDecayEnergy() {
        return decayEnergy;
    }

    public Isotope getDecayIsotope() {
        return decayIsotope;
    }

    public DecayType getDecayType() {
        return decayType;
    }

    // public void setFissionProperties(float energyEV, float xs, Isotope[] fissionProducts) {}

    // Store cross section and resulting isotope for each energy
    private static class CaptureData {
        public float crossSection;
        public Isotope productIsotope;

        CaptureData(float crossSection, Isotope productIsotope) {
            this.crossSection = crossSection;
            this.productIsotope = productIsotope;
        }
    }

    public void setAlphaCapture(float energyEV, float xs, Isotope productIsotope) {
        alphaCaptureXS.put(energyEV, new CaptureData(xs, productIsotope));
    }

    // Get cross section for alpha capture
    public float getAlphaCaptureXS(float energyEV) {
        if (alphaCaptureXS.isEmpty()) {
            return 0.0f;
        }
        
        Float floorKey = alphaCaptureXS.floorKey(energyEV);
        Float ceilingKey = alphaCaptureXS.ceilingKey(energyEV);

        if (floorKey == null) return alphaCaptureXS.get(ceilingKey).crossSection;
        if (ceilingKey == null) return alphaCaptureXS.get(floorKey).crossSection;

        float floorDiff = energyEV - floorKey;
        float ceilingDiff = ceilingKey - energyEV;

        return (float) ((floorDiff <= ceilingDiff)
            ? alphaCaptureXS.get(floorKey).crossSection
            : alphaCaptureXS.get(ceilingKey).crossSection);
    }

    // Get product isotope for alpha capture
    public Isotope getAlphaCaptureProduct(float energyEV) {
        if (alphaCaptureXS.isEmpty()) {
            return null;
        }

        Float floorKey = alphaCaptureXS.floorKey(energyEV);
        Float ceilingKey = alphaCaptureXS.ceilingKey(energyEV);

        if (floorKey == null) return alphaCaptureXS.get(ceilingKey).productIsotope;
        if (ceilingKey == null) return alphaCaptureXS.get(floorKey).productIsotope;

        float floorDiff = energyEV - floorKey;
        float ceilingDiff = ceilingKey - energyEV;

        return (floorDiff <= ceilingDiff)
            ? alphaCaptureXS.get(floorKey).productIsotope
            : alphaCaptureXS.get(ceilingKey).productIsotope;
    }

    public void setNeutronCapture(float energyEV, float xs, Isotope productIsotope) {
        neutronCaptureXS.put(energyEV, new CaptureData(xs, productIsotope));
    }

    // Get cross section for neutron capture
    public float getNeutronCaptureXS(float energyEV) {
        if (neutronCaptureXS.isEmpty()) {
            return 0.0f;
        }
        
        Float floorKey = neutronCaptureXS.floorKey(energyEV);
        Float ceilingKey = neutronCaptureXS.ceilingKey(energyEV);

        if (floorKey == null) return neutronCaptureXS.get(ceilingKey).crossSection;
        if (ceilingKey == null) return neutronCaptureXS.get(floorKey).crossSection;

        float floorDiff = energyEV - floorKey;
        float ceilingDiff = ceilingKey - energyEV;

        return (floorDiff <= ceilingDiff)
            ? neutronCaptureXS.get(floorKey).crossSection
            : neutronCaptureXS.get(ceilingKey).crossSection;
    }

    // Get product isotope for neutron capture
    public Isotope getNeutronCaptureProduct(float energyEV) {
        if (neutronCaptureXS.isEmpty()) {
            return null;
        }
        
        Float floorKey = neutronCaptureXS.floorKey(energyEV);
        Float ceilingKey = neutronCaptureXS.ceilingKey(energyEV);

        if (floorKey == null) return neutronCaptureXS.get(ceilingKey).productIsotope;
        if (ceilingKey == null) return neutronCaptureXS.get(floorKey).productIsotope;

        float floorDiff = energyEV - floorKey;
        float ceilingDiff = ceilingKey - energyEV;

        return (floorDiff <= ceilingDiff)
            ? neutronCaptureXS.get(floorKey).productIsotope
            : neutronCaptureXS.get(ceilingKey).productIsotope;
    }

    public class CaptureDatum {
        public float crossSection;
        public Isotope[] productIsotope;

        CaptureDatum(float crossSection, Isotope[] productIsotope) {
            this.crossSection = crossSection;
            this.productIsotope = productIsotope;
        }
    }

    public void setFissionCapture(float energyEV, float xs, Isotope[] productIsotope) {
        fissionXS.put(energyEV, new CaptureDatum(xs, productIsotope));
    }

    public float getFissionXS(float energyEV) {
        if (fissionXS.isEmpty()) {
            return 0.0f;
        }
        
        Float floorKey = fissionXS.floorKey(energyEV);
        Float ceilingKey = fissionXS.ceilingKey(energyEV);

        if (floorKey == null) return fissionXS.get(ceilingKey).crossSection;
        if (ceilingKey == null) return fissionXS.get(floorKey).crossSection;

        float floorDiff = energyEV - floorKey;
        float ceilingDiff = ceilingKey - energyEV;

        return (floorDiff <= ceilingDiff)
            ? fissionXS.get(floorKey).crossSection
            : fissionXS.get(ceilingKey).crossSection;
    }

    public Isotope[] getFissionProducts(float energyEV) {
        if (fissionXS.isEmpty()) {
            return null;
        }
        
        Float floorKey = fissionXS.floorKey(energyEV);
        Float ceilingKey = fissionXS.ceilingKey(energyEV);

        if (floorKey == null) return fissionXS.get(ceilingKey).productIsotope;
        if (ceilingKey == null) return fissionXS.get(floorKey).productIsotope;

        float floorDiff = energyEV - floorKey;
        float ceilingDiff = ceilingKey - energyEV;

        return (floorDiff <= ceilingDiff)
            ? fissionXS.get(floorKey).productIsotope
            : fissionXS.get(ceilingKey).productIsotope;
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

    public float getHalfLife() {
        return halfLife;
    }

    public float getActivityPerGram() {
        // Activity of the isotope in Bq/g or decays/(second * gram)
        double activity = Math.log(2) * 6.022e23 / (halfLife * massMolar);
        return (float)activity;
    }

    public boolean isStable() {
        return halfLife == Float.POSITIVE_INFINITY;
    }
}