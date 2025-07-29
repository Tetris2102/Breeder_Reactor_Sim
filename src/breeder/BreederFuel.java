// package breeder;
// import java.util.ArrayList;

// SHOULD BE ABLE TO REPLACE BreederFuel WITH DecayChainNest

// public class BreederFuel {
//     private final float AVOGADRO = 6.022e23f;
//     private final DecayChain decayChain;
//     private final ArrayList<Isotope> nCaptureIsotopes;  // Isotopes that capture neutrons
//     private float nPopulation;

//     public BreederFuel(DecayChain decayChain) {
//         this.decayChain = decayChain;

//         // Find isotopes that have neutron capture cross-sections
//         nCaptureIsotopes = new ArrayList<>();
//         for (Isotope isotope : decayChain.getDecayChain()) {
//             if (isotope.getNeutronCaptureXS(0.0f) > 0.0f) { // Check if it has neutron capture XS
//                 nCaptureIsotopes.add(isotope);
//             }
//         }
//     }

    // public void simulateDecayMass(float timeElapsed) {
    //     // Simulate natural decay of all fuel isotopes

    //     for (int i = 0; i < decayChain.getDecayChain().length; i++) {
    //         double exponent = timeElapsed / nCaptureIsotopes.get(i).getHalfLife();
    //         double atomsNew = decayChain.getIsotopeAtoms(i) * (float)Math.pow(0.5, exponent);
    //         decayChain.setIsotopeMass(atomsNew, i); // Update the mass for this isotope
    //     }
    // }

    // public float[] getDecayActivity(float timeElapsed, float[] masses, Isotope[] decayIsotopes) {
    //     float[] activities = new float[decayIsotopes.length];
    //     for (int i = 0; i < decayIsotopes.length; i++) {
    //         float decayConstant = (float)(Math.log(2) / decayIsotopes[i].getHalfLife());
    //         float activity = decayConstant * masses[i] * AVOGADRO / decayIsotopes[i].getMassMolar();
    //         activities[i] = activity;
    //     }

    //     return activities;
    // }

    // public double getTotalDecays(float timeElapsed, float[] masses, Isotope[] decayIsotopes) {
    //     float[] activityList = getDecayActivity(timeElapsed, masses, decayIsotopes);
    //     double totalDecays = 0.0d;

    //     for (float activityIsotope : activityList) {
    //         totalDecays += activityIsotope;
    //     }
    //     totalDecays *= timeElapsed; // Total decays over the elapsed time

    //     return totalDecays;
    // }

    // // Helper class to return multiple values in simulateNeutronCapture
    // public static class NeutronCaptureResult {
    //     public final float remainingNeutrons;
    //     public final float[] updatedMasses;
        
    //     public NeutronCaptureResult(float neutrons, float[] masses) {
    //         this.remainingNeutrons = neutrons;
    //         this.updatedMasses = masses;
    //     }
    // }

    // public void setNeutronPopulation(float nPopulation) {
    //     this.nPopulation = nPopulation;
    // }

    // public float getNeutronPopulation() {
    //     return nPopulation;
    // }

    // public void simulateNeutronCapture(
    //     float timeElapsed,       // seconds
    //     float escapeProbability, // 0-1 probability of neutron leakage
    //     float neutronEnergy      // energy of neutrons [eV]
    // ) {
    //     Isotope[] isotopes = decayChain.getDecayChain();
    //     double[] atoms = decayChain.getIsotopesAtoms();

    //     float totalNeutrons = nPopulation * timeElapsed;
    //     float neutronsLostToLeakage = totalNeutrons * escapeProbability;
    //     float neutronsAvailable = totalNeutrons - neutronsLostToLeakage;

    //     // Calculate total reaction rate for all isotopes with neutron capture XS
    //     double[] reactionRates = new double[isotopes.length];
    //     double totalReactionRate = 0.0;

    //     for (int i = 0; i < isotopes.length; i++) {
    //         if (isotopes[i].getNeutronCaptureXS(neutronEnergy) > 0.0f) {
    //             double microXS = isotopes[i].getNeutronCaptureXS(neutronEnergy); // cm^2
    //             reactionRates[i] = microXS * nPopulation * atoms[i];
    //             totalReactionRate += reactionRates[i];
    //         } else {
    //             reactionRates[i] = 0.0;
    //         }
    //     }

    //     if (totalReactionRate <= 0.0) return; // No captures

    //     // Distribute neutron captures proportionally
    //     for (int i = 0; i < isotopes.length; i++) {
    //         if (reactionRates[i] > 0.0) {
    //             double captureFraction = reactionRates[i] / totalReactionRate;
    //             double neutronsCaptured = neutronsAvailable * captureFraction;

    //             // Decrement capturing isotope
    //             atoms[i] -= neutronsCaptured;
    //             if (atoms[i] < 0) atoms[i] = 0;

    //             // Find product isotope from cross-section data
    //             Isotope product = isotopes[i].getNeutronCaptureProduct(neutronEnergy);
    //             if (product != null) {
    //                 // Find index of product isotope in decayChain
    //                 for (int j = 0; j < isotopes.length; j++) {
    //                     if (isotopes[j].equals(product)) {
    //                         atoms[j] += neutronsCaptured;
    //                         break;
    //                     }
    //                 }
    //             }
    //         }
    //     }
    //     decayChain.setIsotopesAtoms(atoms);  // Update atom count
    //     nPopulation = neutronsAvailable; // Update neutron population
    // }
}