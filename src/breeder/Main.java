// package breeder;

// import java.util.HashMap;
// import java.util.Map;

// public class Main {

//     // MEMO: maybe add densities for all isotopes in IsotopeLibrary
//     // MEMO2: add U-233 natural decay chain to IsotopeLibrary

//     public static void main(String[] args) {
//         // DecayChainNest myDCN = IsotopeLibrary.decayTree;
//         // DecayChain myDC = myDCN.getDecayChain(IsotopeLibrary.Cs137);
//         // myDC.setIsotopeMassGrams(IsotopeLibrary.Cs137, 10.0f);
//         // myDCN.simulateDecay(3600 * 24 * 365, 30);
//         // System.out.println("Cs-137: " + myDC.getIsotopeMassGrams(IsotopeLibrary.Cs137) + " g");
//         // System.out.println("Ba-137m: " + myDC.getIsotopeMassGrams(IsotopeLibrary.Ba137m) + " g");

//         // Implement Breeder functionality
//         float neutronSourceEnergy = 2.1f;  // Example energy, MeV
//         NeutronSource neutronPuBe = new NeutronSource(IsotopeLibrary.Pu238, IsotopeLibrary.Be9, neutronSourceEnergy);
//         neutronPuBe.setAlphaSourceMass(500.0f);
//         System.out.println("Neutron source intensity: " + neutronPuBe.getNeutronRate() + " n/s");

//         DecayChainNest myFuel = IsotopeLibrary.decayTree;
//         myFuel.getDecayChain(IsotopeLibrary.Cs137).setIsotopeMassGrams(IsotopeLibrary.Cs137, 10.0f);
//         myFuel.getDecayChain(IsotopeLibrary.Th232).setIsotopeMassGrams(IsotopeLibrary.Th232, 10.0f);
//         myFuel.getDecayChain(IsotopeLibrary.U233).setIsotopeMassGrams(IsotopeLibrary.U233, 10.0f);
//         myFuel.getDecayChain(IsotopeLibrary.Pu238).setIsotopeMassGrams(IsotopeLibrary.Pu238, 10.0f);
//         myFuel.simulateDecay(3600 * 24 * 30, 10);
//         Breeder myBreeder = new Breeder(myFuel, neutronPuBe, 1.0e26f, 0.1f, 0.3f);
//         System.out.println("U-233 atoms: " + myFuel.getIsotopeAtoms(IsotopeLibrary.U233));
//         System.out.println("Th-229 atoms: " + myFuel.getIsotopeAtoms(IsotopeLibrary.Th229));
//         myBreeder.simulateTime(3600 * 24 * 30, 1);
//         System.out.println("Pu-238 atoms: " + myFuel.getIsotopeAtoms(IsotopeLibrary.Pu238));
//         System.out.println("U-234 atoms: " + myFuel.getIsotopeAtoms(IsotopeLibrary.U234));
//         System.out.println("Th-230 atoms: " + myFuel.getIsotopeAtoms(IsotopeLibrary.Th230));
//         System.out.println("Ac-228 atoms: " + myFuel.getIsotopeAtoms(IsotopeLibrary.Ac228));
//         System.out.println("Bi-212 atoms: " + myFuel.getIsotopeAtoms(IsotopeLibrary.Bi212));
//         System.out.println("Pb-208 atoms: " + myFuel.getIsotopeAtoms(IsotopeLibrary.Pb208));
//         System.out.println("U-233 atoms after capture: " + myFuel.getIsotopeAtoms(IsotopeLibrary.U233));
//         System.out.println("Th-229 atoms: " + myFuel.getIsotopeAtoms(IsotopeLibrary.Th229));
//         System.out.println("Pa-233 atoms: " + myFuel.getIsotopeAtoms(IsotopeLibrary.Pa233));
//         System.out.println("Th-233 atoms: " + myFuel.getIsotopeAtoms(IsotopeLibrary.Th233));
//         System.out.println("Fission neutron rate: " + myBreeder.getNeutronEmissionRate());
//         System.out.println(myBreeder.getNeutronFlux());
//         System.out.println("Capture rate: " + myBreeder.getCaptureRate());
//         System.out.println(myBreeder.getFissionNeutronRate());
//         Map<Isotope, Double> isoMap = myBreeder.getIsotopesAtomsMap();
//         for (Isotope key : isoMap.keySet()) {
//             System.out.println(key.getName() + " - " + isoMap.get(key));
//         }
//         Map<Isotope, Double[]> spectrum = new HashMap<>();
//         spectrum = myBreeder.captureDSIMap(10.0, DecayType.BETA);
//         System.out.println("Spectrum:");
//         for (Isotope i : spectrum.keySet()) {
//             System.out.println(i.getName() + " - " + spectrum.get(i)[1] + " decays");
//         }
//     }
// }

package breeder;

import java.util.Map;
import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        // Create fuel with multiple isotopes for a more realistic spectrum
        DecayChainNest fuel = IsotopeLibrary.decayTree;
        fuel.getDecayChain(IsotopeLibrary.Cs137).setIsotopeMassGrams(IsotopeLibrary.Cs137, 0.00001f);
        fuel.getDecayChain(IsotopeLibrary.Bi214).setIsotopeMassGrams(IsotopeLibrary.Bi214, 0.00000001f);
        fuel.getDecayChain(IsotopeLibrary.Tl210).setIsotopeMassGrams(IsotopeLibrary.Tl210, 0.00000001f);
        fuel.getDecayChain(IsotopeLibrary.Tc99m).setIsotopeMassGrams(IsotopeLibrary.Tc99m, 0.000000001f);
        
        // Create spectrum and simulator
        DecaySpectrum decaySpectrum = new DecaySpectrum(fuel);
        
        // Set a larger sigma value for broader peaks and a background count value
        double sigma = 4.0; // This value controls the width of the peaks
        double backgroundCounts = 5.0; // This value sets the height of the background line
        
        // Simulate and get the populated spectrum map
        Map<Double, Double> spectrum = decaySpectrum.simulateRealDS(2500, 0.1, DecayType.GAMMA, sigma, backgroundCounts);
        
        // Create and display the plot with the populated map
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Multi-Isotope Gamma Spectrum");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(850, 650);
            
            DecaySpectrumPlot plot = new DecaySpectrumPlot(spectrum);
            frame.add(plot);
            
            frame.setVisible(true);
        });
        
        // Print some statistics
        System.out.println("Multi-isotope gamma spectrum simulation complete");
        System.out.println("Spectrum data points: " + spectrum.size());
        System.out.println("Energy range: " + spectrum.keySet().stream().min(Double::compare).orElse(0.0) + 
                          " to " + spectrum.keySet().stream().max(Double::compare).orElse(0.0) + " keV");
        
        // Print the top 5 peaks
        System.out.println("Top 5 peaks:");
        spectrum.entrySet().stream()
            .sorted((a, b) -> Double.compare(b.getValue(), a.getValue()))
            .limit(5)
            .forEach(entry -> System.out.printf("  %.1f keV: %.2f counts%n", entry.getKey(), entry.getValue()));
        
        double totalCounts = 0;
        for (double counts : spectrum.values()) {
            totalCounts += counts;
        }
        System.out.println("Total counts in spectrum: " + totalCounts);
    }
}