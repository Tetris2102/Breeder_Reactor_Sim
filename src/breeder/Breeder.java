package breeder;

public class Breeder {
    private final DecayChainNest fuel;
    private final NeutronSource neutronSource;
    private float additionalNeutronsEmitted = 0.0f; // Additional neutrons emitted by fission

    public Breeder(DecayChainNest fuel, NeutronSource neutronSource) {
        this.fuel = fuel;
        this.neutronSource = neutronSource;
    }

    public DecayChainNest getFuel() {
        return fuel;
    }

    public NeutronSource getNeutronSource() {
        return neutronSource;
    }

    /**
     * Simulate neutron capture on an isotope, transitioning it to another chain if needed.
     * @param isotope The target isotope.
     * @param time Time interval (seconds).
     * @param escapeProbability Probability of neutron escaping after time seconds.
     */
    public void simulateNeutronCapture(Isotope isotope, double time, float escapeProbability) {
        if (isotope == null) return;

        float xs = isotope.getNeutronCaptureXS(neutronSource.getNeutronEnergy());
        if (xs <= 0) return; // No capture possible

        // Calculate reaction rate (atoms transformed per second)
        DecayChain parentChain = fuel.getDecayChain(isotope);
        if (parentChain == null) return;

        int isotopeIndex = parentChain.getIsotopeIndex(isotope);
        if (isotopeIndex == -1) return;

        double atoms = parentChain.getIsotopeAtoms(isotopeIndex);
        double reactionRate = atoms * xs * neutronSource.getNeutronRate() * (1.0f - escapeProbability); // Reactions per second
        double atomsTransformed = reactionRate * time;

        if (atomsTransformed <= 0) return;

        // Deduct transformed atoms from the parent chain
        parentChain.setIsotopeMass(atoms - atomsTransformed, isotopeIndex);

        // Get the product isotope and add it to its respective chain
        Isotope productIsotope = isotope.getNeutronCaptureProduct(neutronSource.getNeutronEnergy());
        if (productIsotope == null) return;

        DecayChain productChain = fuel.getDecayChain(productIsotope);
        if (productChain == null) {
            // If product is not in any chain, skip or handle (e.g., add to a new chain)
            return;
        }

        int productIndex = productChain.getIsotopeIndex(productIsotope);
        if (productIndex == -1) return;

        // Add transformed atoms to the product chain
        double currentAtoms = productChain.getIsotopeAtoms(productIndex);
        productChain.setIsotopeMass(currentAtoms + atomsTransformed, productIndex);
    }

    /**
     * Simulate neutron irradiation on the entire fuel, capturing neutrons and transforming isotopes.
     * @param time Time interval for the simulation (seconds).
     * @param escapeProbability Probability of neutron escaping without capture.
     */
    public void simulateNeutronIrradiation(double time, float escapeProbability) {
        for (DecayChain chain : fuel.getDecayChains()) {
            for (Isotope isotope : chain.getDecayChain()) {
                if (isotope.getNeutronCaptureXS(neutronSource.getNeutronEnergy()) > 0)
                    simulateNeutronCapture(isotope, time, escapeProbability);
            }
        }
    }

    /**
     * Simulate alpha capture on an isotope, transitioning it to another chain if needed.
     * @param isotope The target isotope.
     * @param time Time interval (seconds).
     */
    public void simulateAlphaCapture(Isotope isotope, double time) {
        if (isotope == null) return;

        float xs = isotope.getAlphaCaptureXS(neutronSource.getAlphaSource().getDecayEnergy());
        if (xs <= 0) return; // No capture possible

        DecayChain parentChain = fuel.getDecayChain(isotope);
        if (parentChain == null) return;

        int isotopeIndex = parentChain.getIsotopeIndex(isotope);
        if (isotopeIndex == -1) return;

        double atoms = parentChain.getIsotopeAtoms(isotopeIndex);
        double reactionRate = atoms * xs * fuel.getActivityByDecay(DecayType.ALPHA); // Reactions per second
        double atomsTransformed = reactionRate * time;

        if (atomsTransformed <= 0) return;

        parentChain.setIsotopeMass(atoms - atomsTransformed, isotopeIndex);

        Isotope productIsotope = isotope.getAlphaCaptureProduct(isotope.getDecayEnergy());
        if (productIsotope == null) return;

        DecayChain productChain = fuel.getDecayChain(isotope);
        if (productChain == null) return;

        int productIndex = parentChain.getIsotopeIndex(productIsotope);
        if (productIndex == -1) return;

        double currentAtoms = productChain.getIsotopeAtoms(productIndex);
        productChain.setIsotopeMass(currentAtoms + atomsTransformed, productIndex);
    }

    /**
     * Simulate alpha irradiation on the entire fuel, capturing alpha particles and transforming isotopes.
     * @param time Time interval for the simulation (seconds).
     */
    public void simulateAlphaIrradiation(double time) {
        for (DecayChain chain : fuel.getDecayChains()) {
            for (Isotope isotope : chain.getDecayChain()) {
                if (isotope.getAlphaCaptureXS(neutronSource.getAlphaSource().getDecayEnergy()) > 0)
                    simulateAlphaCapture(isotope, time);
            }
        }
    }

    /**
     * Get total neutrons created in breeder each second.
     * @return Total neutron emission rate (neutrons/s).
     */
    public float getNeutronEmissionRate() {
        return neutronSource.getNeutronRate() + additionalNeutronsEmitted;
    }
}