package breeder;

public class Main {

    // MEMO: maybe add densities for all isotopes in IsotopeLibrary
    // MEMO2: add U-233 natural decay chain to IsotopeLibrary

    public static void main(String[] args) {
        // DecayChainNest myDCN = IsotopeLibrary.decayTree;
        // DecayChain myDC = myDCN.getDecayChain(IsotopeLibrary.Cs137);
        // myDC.setIsotopeMassGrams(IsotopeLibrary.Cs137, 10.0f);
        // myDCN.simulateDecay(3600 * 24 * 365, 30);
        // System.out.println("Cs-137: " + myDC.getIsotopeMassGrams(IsotopeLibrary.Cs137) + " g");
        // System.out.println("Ba-137m: " + myDC.getIsotopeMassGrams(IsotopeLibrary.Ba137m) + " g");

        // Implement Breeder functionality
        float neutronSourceEnergy = 5.0e-4f;  // Example energy, MeV
        NeutronSource neutronPuBe = new NeutronSource(IsotopeLibrary.Pu238, IsotopeLibrary.Be9, neutronSourceEnergy);
        neutronPuBe.setAlphaSourceMass(500.0f);
        System.out.println("Neutron source intensity: " + neutronPuBe.getNeutronRate() + " n/s");

        DecayChainNest myFuel = IsotopeLibrary.decayTree;
        myFuel.getDecayChain(IsotopeLibrary.Th229).setIsotopeMassGrams(IsotopeLibrary.Th229, 10.0f);
        myFuel.getDecayChain(IsotopeLibrary.Th232).setIsotopeMassGrams(IsotopeLibrary.Ra228, 10.0f);
        myFuel.getDecayChain(IsotopeLibrary.U233).setIsotopeMassGrams(IsotopeLibrary.U233, 10.0f);
        myFuel.getDecayChain(IsotopeLibrary.Pu238).setIsotopeMassGrams(IsotopeLibrary.Pu238, 0.0f);
        myFuel.simulateDecay(3600 * 24 * 365 * 100, 10);
        Breeder myBreeder = new Breeder(myFuel, neutronPuBe, 0.0f, 0.1f, 0.3f);
        System.out.println("U-233 atoms: " + myFuel.getIsotopeAtoms(IsotopeLibrary.U233));
        System.out.println("Th-229 atoms: " + myFuel.getIsotopeAtoms(IsotopeLibrary.Th229));
        myBreeder.simulateTime(3600 * 24 * 30, 1);
        System.out.println("Pu-238 atoms: " + myFuel.getIsotopeAtoms(IsotopeLibrary.Pu238));
        System.out.println("U-234 atoms: " + myFuel.getIsotopeAtoms(IsotopeLibrary.U234));
        System.out.println("Th-230 atoms: " + myFuel.getIsotopeAtoms(IsotopeLibrary.Th230));
        System.out.println("Ac-228 atoms: " + myFuel.getIsotopeAtoms(IsotopeLibrary.Ac228));
        System.out.println("Bi-212 atoms: " + myFuel.getIsotopeAtoms(IsotopeLibrary.Bi212));
        System.out.println("Pb-208 atoms: " + myFuel.getIsotopeAtoms(IsotopeLibrary.Pb208));
        System.out.println("U-233 atoms after capture: " + myFuel.getIsotopeAtoms(IsotopeLibrary.U233));
        System.out.println("Th-229 atoms: " + myFuel.getIsotopeAtoms(IsotopeLibrary.Th229));
        System.out.println("Pa-233 atoms: " + myFuel.getIsotopeAtoms(IsotopeLibrary.Pa233));
        System.out.println("Fission neutron rate: " + myBreeder.getNeutronEmissionRate());
        System.out.println(myBreeder.getNeutronPopulation());
        System.out.println("Capture rate: " + myBreeder.getCaptureRate());
    }
}