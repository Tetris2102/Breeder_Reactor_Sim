package breeder;

import java.util.Map;

public class Breeder {
    private final DecayChainNest fuel;
    private final NeutronSource neutronSource;
    private double neutronFlux; // Current neutron population in the reactor
    // private double alphaPopulation;   // Current alpha particle population from decays
    
    // Reactor physics parameters
    private final float neutronEscapeProbability; // Probability of neutron escaping per second
    // private final float alphaEscapeProbability; // Probability of alpha escaping per second
    
    public Breeder(DecayChainNest fuel, NeutronSource neutronSource, 
                  float initialNeutrons, float neutronEscapeProbability, float alphaEscapeProbability) {
        if (fuel == null || neutronSource == null) {
            throw new IllegalArgumentException("Fuel and neutron source cannot be null");
        }
        if (neutronEscapeProbability < 0 || neutronEscapeProbability > 1 ||
            alphaEscapeProbability < 0 || alphaEscapeProbability > 1) {
            throw new IllegalArgumentException("Escape probabilities must be between 0 and 1");
        }
        
        this.fuel = fuel;
        this.neutronSource = neutronSource;
        this.neutronFlux = Math.max(initialNeutrons, 0);
        this.neutronEscapeProbability = neutronEscapeProbability;
        // this.alphaEscapeProbability = alphaEscapeProbability;
        // this.alphaPopulation = 0;
    }

    public DecayChainNest getFuel() {
        return fuel;
    }

    public NeutronSource getNeutronSource() {
        return neutronSource;
    }

    public double getNeutronFlux() {
        return neutronFlux;
    }

    // public double getAlphaPopulation() {
    //     return alphaPopulation;
    // }

    /**
     * Update particle populations based on source and decay rates
     */
    public void updateParticlePopulations(double timeStep) {
        if (timeStep <= 0) return;
        
        // Add new neutrons from the neutron source
        double newNeutrons = neutronSource.getNeutronRate() * timeStep;
        neutronFlux += newNeutrons;
        
        // Add alpha particles from alpha decays in fuel
        // double newAlphas = fuel.getActivityByDecay(DecayType.ALPHA) * timeStep;
        // alphaPopulation += newAlphas;
        
        // Remove escaped neutrons (exponential decay model)
        double neutronEscapeLoss = neutronFlux * neutronEscapeProbability * timeStep;
        neutronFlux = Math.max(0, neutronFlux - neutronEscapeLoss);
        
        // Remove escaped alphas (exponential decay model)
        // double alphaEscapeLoss = alphaPopulation * alphaEscapeProbability * timeStep;
        // alphaPopulation = Math.max(0, alphaPopulation - alphaEscapeLoss);
    }

    /**
     * Simulate neutron capture on an isotope
     */
    public void simulateNeutronCapture(Isotope isotope, double timeStep) {
        if (isotope == null || timeStep <= 0) return;

        float xs = isotope.getNeutronCaptureXS(neutronSource.getNeutronEnergy());
        if (xs <= 0) return; // No capture possible

        DecayChain parentChain = fuel.getDecayChain(isotope);
        if (parentChain == null) return;

        int isotopeIndex = parentChain.getIsotopeIndex(isotope);
        if (isotopeIndex == -1) return;

        // Calculate reaction rate based on current neutron population
        double atoms = parentChain.getIsotopeAtoms(isotopeIndex);
        double sigma_cm2 = xs * 1e-24;
        double reactionRate = atoms * sigma_cm2 * neutronFlux; // Reactions per second
        double atomsTransformed = Math.min(atoms, reactionRate * timeStep);

        if (atomsTransformed <= 0 || atomsTransformed > atoms) return;

        // Deduct transformed atoms from parent chain
        parentChain.setIsotopeMassAtoms(atoms - atomsTransformed, isotopeIndex);

        // Get product isotope and add to its chain
        Isotope productIsotope = isotope.getNeutronCaptureProduct(neutronSource.getNeutronEnergy());
        if (productIsotope == null) return;

        DecayChain productChain = fuel.getDecayChain(productIsotope);
        if (productChain == null) return;

        int productIndex = productChain.getIsotopeIndex(productIsotope);
        if (productIndex == -1) return;

        double currentAtoms = productChain.getIsotopeAtoms(productIndex);
        productChain.setIsotopeMassAtoms(currentAtoms + atomsTransformed, productIndex);
        
        // Reduce neutron population by number of captures
        neutronFlux = neutronFlux - atomsTransformed;
    }

    /**
     * Simulate neutron irradiation on the entire fuel
     */
    public void simulateNeutronIrradiation(double timeStep) {
        if (timeStep <= 0) return;
        
        for (DecayChain chain : fuel.getDecayChains()) {
            for (Isotope isotope : chain.getDecayChain()) {
                if (isotope.getNeutronCaptureXS(neutronSource.getNeutronEnergy()) > 0) {
                    simulateNeutronCapture(isotope, timeStep);
                }
            }
        }
    }

    /**
     * Simulate alpha capture on an isotope
     */
    // public void simulateAlphaCapture(Isotope isotope, double timeStep) {
    //     if (isotope == null || timeStep <= 0) return;

    //     float xs = isotope.getAlphaCaptureXS(neutronSource.getAlphaSource().getDecayEnergy());
    //     if (xs <= 0) return;

    //     DecayChain parentChain = fuel.getDecayChain(isotope);
    //     if (parentChain == null) return;

    //     int isotopeIndex = parentChain.getIsotopeIndex(isotope);
    //     if (isotopeIndex == -1) return;

    //     // Calculate reaction rate based on current alpha population
    //     double atoms = parentChain.getIsotopeAtoms(isotopeIndex);
    //     double reactionRate = atoms * xs * 1e-24 * alphaPopulation; // Reactions per second
    //     double atomsTransformed = reactionRate * timeStep;

    //     if (atomsTransformed <= 0 || atomsTransformed > atoms) return;

    //     parentChain.setIsotopeMassAtoms(atoms - atomsTransformed, isotopeIndex);

    //     Isotope productIsotope = isotope.getAlphaCaptureProduct(isotope.getDecayEnergy());
    //     if (productIsotope == null) return;

    //     DecayChain productChain = fuel.getDecayChain(productIsotope);
    //     if (productChain == null) return;

    //     int productIndex = productChain.getIsotopeIndex(productIsotope);
    //     if (productIndex == -1) return;

    //     double currentAtoms = productChain.getIsotopeAtoms(productIndex);
    //     productChain.setIsotopeMassAtoms(currentAtoms + atomsTransformed, productIndex);
        
    //     // Reduce alpha population by number of captures
    //     alphaPopulation = Math.max(0, alphaPopulation - atomsTransformed);
    // }

    /**
     * Simulate alpha irradiation on the entire fuel
     */
    // public void simulateAlphaIrradiation(double timeStep) {
    //     if (timeStep <= 0) return;
        
    //     for (DecayChain chain : fuel.getDecayChains()) {
    //         for (Isotope isotope : chain.getDecayChain()) {
    //             if (isotope.getAlphaCaptureXS(neutronSource.getAlphaSource().getDecayEnergy()) > 0) {
    //                 simulateAlphaCapture(isotope, timeStep);
    //             }
    //         }
    //     }
    // }

    public Map<Isotope, Double> getIsotopesAtomsMap() {
        return fuel.getIsotopesAtomsMap();
    }

    /**
     * Simulate a complete time step including:
     * 1. Particle population updates
     * 2. Neutron captures
     * 3. Alpha captures
     * 4. Decays
     */
    public void simulateTimeStep(double timeStep) {
        if (timeStep <= 0) return;
        
        // Update particle populations from sources and decays
        updateParticlePopulations(timeStep);
        
        // Simulate neutron interactions
        simulateNeutronIrradiation(timeStep);
        
        // Simulate alpha interactions
        // simulateAlphaIrradiation(timeStep);
        
        // Simulate decays
        fuel.simulateDecay(timeStep);
    }

    public void simulateTime(double time, double timeStep) {
        if (time <= 0 || timeStep <= 0) return;
        
        int steps = (int)Math.ceil(time / timeStep);
        for (int i = 0; i < steps; i++) {
            simulateTimeStep(timeStep);
        }
    }

    /**
     * Get total neutron emission rate (source + fissions - captures)
     */
    public double getNeutronEmissionRate() {
        return neutronSource.getNeutronRate() + 
               getFissionNeutronRate() - 
               getCaptureRate();
    }

    /**
     * Calculate current fission neutron production rate
     */
    public double getFissionNeutronRate() {
        double fissionRate = 0;
        for (DecayChain chain : fuel.getDecayChains()) {
            for (Isotope isotope : chain.getDecayChain()) {
                float xs = isotope.getFissionXS(neutronSource.getNeutronEnergy());
                if (xs > 0) {
                    double atoms = chain.getIsotopeAtoms(chain.getIsotopeIndex(isotope));
                    fissionRate += atoms * xs * 1e-24 * neutronFlux;
                }
            }
        }
        return fissionRate;
    }

    /**
     * Calculate current neutron capture rate
     */
    public double getCaptureRate() {
        double captureRate = 0;
        for (DecayChain chain : fuel.getDecayChains()) {
            for (Isotope isotope : chain.getDecayChain()) {
                float xs = isotope.getNeutronCaptureXS(neutronSource.getNeutronEnergy());
                if (xs > 0) {
                    double atoms = chain.getIsotopeAtoms(chain.getIsotopeIndex(isotope));
                    captureRate += atoms * xs * 1e-24 * neutronFlux;
                }
            }
        }
        return captureRate;
    }

    /**
     * Calculate the neutron multiplication factor (k-effective)
     * Defined as (fission neutrons + source neutrons) / (captured neutrons + escaped neutrons)
     */
    public double getNeutronMultiplicationFactor() {
        double totalNeutronProduction = neutronSource.getNeutronRate() + getFissionNeutronRate();
        double totalNeutronLoss = getCaptureRate() + (neutronFlux * neutronEscapeProbability);
        
        if (totalNeutronLoss == 0) return Double.POSITIVE_INFINITY;
        return totalNeutronProduction / totalNeutronLoss;
    }
}