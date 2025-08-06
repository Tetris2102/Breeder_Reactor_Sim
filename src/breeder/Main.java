package breeder;

public class Main {

    public static void main(String[] args) {
        // DecayChain myDecayChain = IsotopeLibrary.Sr90NDChain;
        // myDecayChain.setIsotopeMassGrams(IsotopeLibrary.Sr90, 10.0f);
        // System.out.println(IsotopeLibrary.Sr90NDChain.getName() + " decay chain sim");
        // System.out.println(Double.toString(myDecayChain.getIsotopeMassGrams(IsotopeLibrary.Sr90)));
        // myDecayChain.simulateDecay(3600 * 24 * 365 * 28);
        // System.out.println("Sr-90 half-life: " + IsotopeLibrary.Sr90.getHalfLife() / (3600 * 24 * 365) + "years");
        // System.out.println("Year elapsed");
        // System.out.println(Double.toString(myDecayChain.getIsotopeMassGrams(IsotopeLibrary.Sr90)));
        DecayChainNest myDCN = IsotopeLibrary.decayTree;
        DecayChain myDC = myDCN.getDecayChain(IsotopeLibrary.Cs137);
        myDC.setIsotopeMassGrams(IsotopeLibrary.Cs137, 10.0f);
        myDCN.simulateDecay(3600 * 24 * 365 * 60);
        System.out.println("Cs-137: " + myDC.getIsotopeMassGrams(IsotopeLibrary.Cs137) + " g");
        System.out.println("Ba-137: " + myDC.getIsotopeMassGrams(IsotopeLibrary.Ba137) + " g");
    }
}
