package breeder;

public class Main {

    public static void main(String[] args) {
        // System.out.println("Breeder Reactor Simulation");

        // // Th-232 natural decay
        // Isotope Th232 = new Isotope("Th-232", (short)232);
        // Isotope Ra228 = new Isotope("Ra-228", (short)228);
        // Isotope Ac228 = new Isotope("Ac-228", (short)228);
        // float yearsToSeconds = 365.0f * 24.0f * 3600.0f;
        // float halfLifeTh232 = 1.4e10f; // 14 billion years
        // float halfLifeRa228 = 5.7f; // 5.7 years
        // Th232.setDecayProperties(halfLifeTh232 * yearsToSeconds, Ra228, DecayType.ALPHA, 4.08e6f);
        // Ra228.setDecayProperties(halfLifeRa228 * yearsToSeconds, Ac228, DecayType.BETA, 4.6e4f);

        // // Th-232 neutron capture cross-sections
        // Isotope Th233 = new Isotope("Th-233", (short)233);
        // Th232.setNeutronCapture(1.0e6f, 5.0e-20f, Th233);
        // Th232.setNeutronCapture(2.0e6f, 7.0e-20f, Th233);
        // Th232.setNeutronCapture(5.0e5f, 1.0e-19f, Th233);
        // Th232.setNeutronCapture(1.0e7f, 1.5e-19f, Th233);

        // // Neutron source
        // Isotope Pu238 = new Isotope("Pu-238", (short)238);
        // Isotope U234 = new Isotope("U-234", (short)234);
        // Isotope Be9 = new Isotope("Be-9", (short)9);
        // Isotope C10 = new Isotope("C-10", (short)10);
        // Pu238.setDecayProperties(2765707200.0f, U234, DecayType.ALPHA, 5.59e6f);
        // Pu238.setDensity(19.8f);  // g/cm^3
        // Be9.setDensity(1.85f);  // g/cm^3
        // Be9.setAlphaCapture(2.1e6f, 5.0e-25f, C10); // Example cross-section
        // NeutronSource neutronSource = new NeutronSource(Pu238, Be9, 2.0e6f); // 2 MeV neutron energy
        // neutronSource.setAlphaSourceMass(0.01f);  // 10 mg of Pu-238
        // float neutronRate = neutronSource.getNeutronRate();
        // System.out.println("Neutron rate from (α, n) reaction: " + neutronRate + " neutrons/s");

        // // DecayChain for Th-232 (natural decay)
        // DecayChain decayChainTh232 = new DecayChain("Th-232 Chain");
        // float massTh232 = 10.0f;  // 10 grams of Th-232
        // decayChainTh232.addIsotope(Th232, massTh232);
        // decayChainTh232.addIsotope(Ra228, 0.0f);
        // decayChainTh232.addIsotope(Ac228, 0.0f);
        // double activityTh232 = decayChainTh232.getTotalActivity(massTh232);
        // System.out.println("Activity of 10.0g of Th-232 fuel: " + activityTh232 + " Bq");

        // // DecayChain for Th-232 (neutron capture)
        // Th232.setNeutronCapture(2.0e6f, 1.0e-19f, Th233); // Example neutron capture cross-section
        // // Th233 already declared
        // Isotope Pa233 = new Isotope("Pa-233", (short)233);
        // Th233.setDecayProperties(1.6e6f, Pa233, DecayType.BETA, 2.0e6f); // 1.6 million seconds
        // DecayChain nCaptureChainTh232 = new DecayChain();
        // nCaptureChainTh232.addIsotope(Th233, 0.0f);
        // nCaptureChainTh232.addIsotope(Pa233, 0.0f);

        // // DecayChainNest for Th232 (neutron source decay omitted)
        // DecayChainNest fuel = new DecayChainNest();
        // fuel.addDecayChain(decayChainTh232);  // Th-232 natural decay
        // fuel.addDecayChain(nCaptureChainTh232);  // Th-232 neutron capture

        // // Breeder reactor simulation
        // Breeder breeder = new Breeder(fuel, neutronSource);
        // double Th232Atoms = breeder.getFuel().getDecayChain(Th232).getIsotopeAtoms(0);
        // System.out.println(Th232Atoms + " atoms of Th-232 remaining.");
        // breeder.simulateNeutronIrradiation(1.0, 0.0f); // Simulate neutron irradiation for 1 minute with 1% escape probability
        // // Th232Atoms - breeder.getFuel().getIsotopeActivity(Th233)
        // DecayChain neutronCaptureChain = breeder.getFuel().getDecayChain(Th233);
        // System.out.println(neutronCaptureChain.getIsotopeAtoms(Th233) + " atoms transmutated.");
        // System.out.println(breeder.getFuel().getDecayChain(Th232).getIsotopeAtoms(0) + " atoms of Th-232 remaining.");

        DecayChain myDecayChain = null;
        myDecayChain = IsotopeLibrary.Sr90NDChain;
        myDecayChain.setIsotopeMassGrams(IsotopeLibrary.Sr90, 10.0f);
        System.out.println(IsotopeLibrary.Sr90NDChain.getName() + " decay chain sim");
        System.out.println(Double.toString(myDecayChain.getIsotopeMassGrams(IsotopeLibrary.Sr90)));
        myDecayChain.simulateDecay(3600 * 24 * 365 * 28);
        System.out.println("Sr-90 half-life: " + IsotopeLibrary.Sr90.getHalfLife() / (3600 * 24 * 365) + "years");
        System.out.println("Year elapsed");
        System.out.println(Double.toString(myDecayChain.getIsotopeMassGrams(IsotopeLibrary.Sr90)));
    }
}
