package breeder;

// Class to simulate a neutron source itilizing (α, n) reaction

public class NeutronSource {
    private Isotope alphaSource;
    private Isotope neutronSource;
    private float neutronEnergy; // MeV
    private float alphaMass;  // g
    private final float AVOGADRO = 6.022e23f; // mol^-1

    public NeutronSource(Isotope alphaSource, Isotope neutronSource) {
        if (alphaSource.getDecayType() != DecayType.ALPHA) {
            throw new IllegalArgumentException("Alpha source must be an alpha-emitting isotope.");
        }

        this.alphaSource = alphaSource;
        this.neutronEnergy = alphaSource.getNEmissionEnergy();

        this.neutronSource = neutronSource;
    }

    public void setTargetIsotope(Isotope targetIsotope) {
        this.neutronSource = targetIsotope;
        this.neutronEnergy = targetIsotope.getNEmissionEnergy();
    }

    public void setAlphaSource(Isotope alphaSource) {
        this.alphaSource = alphaSource;
    }

    public Isotope getAlphaSource() {
        return alphaSource;
    }

    public Isotope getNeutronSource() {
        return neutronSource;
    }

    public void setAlphaSourceMass(float mass) {
        this.alphaMass = mass;
    }

    public float getNeutronRate() {
        // Ase approximation based on average distance travelled by alpha particles
        // and the reaction cross-section
        float alphaTravelDestance = 1.8e-3f; //cm
        float averageDensity = (alphaSource.getDensity() + neutronSource.getDensity()) / 2.0f;
        float alphaAtomicDensity = averageDensity * AVOGADRO / alphaSource.getMassMolar();
        float neutronRate = alphaSource.getActivityPerGram() * alphaMass *
                              alphaTravelDestance * alphaAtomicDensity * 
                              neutronSource.getAlphaCaptureXS(alphaSource.getDecayEnergy());
        return neutronRate;
    }

    public float getNeutronEnergy() {
        return neutronEnergy;
    }
}