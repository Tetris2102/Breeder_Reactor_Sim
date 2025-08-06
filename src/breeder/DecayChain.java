package breeder;
import java.util.ArrayList;

public class DecayChain {
    private final double AVOGADRO = 6.022e23; // Avogadro's number
    private final ArrayList<Isotope> decayChain;  // Stores *references* to Isotope objects
    private ArrayList<Double> isotopesAtoms;  // Atoms of each isotope
    private String name;

    public DecayChain() {
        this.decayChain = new ArrayList<>();
        this.isotopesAtoms = new ArrayList<>();
    }

    public DecayChain(String name) {
        this.name = name;
        this.decayChain = new ArrayList<>();
        this.isotopesAtoms = new ArrayList<>();
    }

    public void addIsotope(Isotope isotope, double massGrams) {
        this.decayChain.add(isotope);
        double atoms = massGrams * AVOGADRO;
        this.isotopesAtoms.add(atoms);
    }

    public void addIsotopeList(Isotope[] isotopes, double[] massesGrams) {
        for (int i = 0; i < isotopes.length; i++) {
            this.addIsotope(isotopes[i], massesGrams[i]);
        }
    }

    // Note: when simulating long periods of time with simulateDecay() and there are short-lived isotopes,
    // e.g. Ba-137m in Cs-137 decay chain, their amount becomes 0.0 atoms due to large time step
    public void simulateDecay(double time) {  // time in seconds
        for (int i = 0; i < isotopesAtoms.size(); i++) {
            double remainingAtoms = isotopesAtoms.get(i) * Math.pow(0.5, time / decayChain.get(i).getHalfLife());
            double decays = isotopesAtoms.get(i) - remainingAtoms;
            isotopesAtoms.set(i, remainingAtoms); // Update the number of atoms
            if (i < isotopesAtoms.size() - 1) {  // In case of last element
                double nextAtoms = isotopesAtoms.get(i+1);
                isotopesAtoms.set(i+1, nextAtoms + decays);
            }
        }
    }

    public double simulateTotalDecays(double time) {
        double decayAmounts = 0;
        double decaysForIsotope;
        for (int i = 0; i < isotopesAtoms.size(); i++) {
            decaysForIsotope = isotopesAtoms.get(i) * Math.exp(-time / decayChain.get(i).getHalfLife());
            decayAmounts += decaysForIsotope;
        }
        return decayAmounts;
    }

    public double getTotalActivity(float mass) {
        double totalActivity = 0.0;
        for (Isotope isotope : decayChain) {
            double activity = isotope.getActivityPerGram() * mass;
            totalActivity += activity;
        }
        return totalActivity;
    }

    public String getName() {
        return name;
    }

    public ArrayList<Isotope> getDecayChain() {
        return decayChain;
    }

    public int getDecayChainLength() {
        return decayChain.size();
    }

    public Isotope getIsotope(int index) {
        return decayChain.get(index);
    }

    public int getIsotopeIndex(Isotope isotope) {
        for (int i = 0; i < decayChain.size(); i++) {
            if (decayChain.get(i).equals(isotope)) {
                return i;
            }
        }
        return -1; // Not found
    }

    public boolean containsIsotope(Isotope isotope) {
        for (Isotope iso : decayChain) {
            if (iso.equals(isotope)) {
                return true;
            }
        }
        return false;
    }

    public double getIsotopeAtoms(int index) {
        if (index < 0 || index >= isotopesAtoms.size()) {
            throw new IndexOutOfBoundsException("Index out of bounds for isotopes atoms.");
        }
        return isotopesAtoms.get(index);
    }

    public double getIsotopeAtoms(Isotope isotope) {
        if (this.containsIsotope(isotope)) {
            return this.getIsotopeAtoms(this.getIsotopeIndex(isotope));
        }

        return 0.0f;
    }

    public ArrayList<Double> getIsotopeAtomsList() {
        return isotopesAtoms;
    }

    public void setIsotopesAtoms(ArrayList<Double> atoms) {
        if (atoms.size() != isotopesAtoms.size()) {
            throw new IllegalArgumentException("Length of atoms array must match decay chain length.");
        }
        this.isotopesAtoms = atoms;
    }

    public double getIsotopeMassGrams(int index) {
        if (index < 0 || index >= isotopesAtoms.size()) {
            throw new IndexOutOfBoundsException("Index out of bounds for isotopes atoms.");
        }
        return isotopesAtoms.get(index) / AVOGADRO; // Convert atoms to grams
    }

    public double getIsotopeMassGrams(Isotope isotope) {
        return this.getIsotopeMassGrams(this.getIsotopeIndex(isotope));
    }

    public void setIsotopeMassAtoms(double atoms, int index) {
        if (index < 0 || index >= isotopesAtoms.size()) {
            throw new IndexOutOfBoundsException("Index out of bounds for isotopes atoms.");
        }
        isotopesAtoms.set(index, atoms); // Set the number of atoms directly
    }

    public void setIsotopeMassAtoms(Isotope isotope, double atoms) {
        int index = this.getIsotopeIndex(isotope);
        isotopesAtoms.set(index, atoms);
    }

    public void setIsotopeMassGrams(Isotope isotope, double grams) {
        int index = this.getIsotopeIndex(isotope);
        isotopesAtoms.set(index, grams * AVOGADRO);
    }

    public ArrayList<Double> getIsotopeMassesGrams() {
        ArrayList<Double> isotopeMassesGrams = new ArrayList<>();
        for (int i = 0; i < isotopesAtoms.size(); i++)
            isotopeMassesGrams.set(i, isotopesAtoms.get(i) / AVOGADRO);
        return isotopeMassesGrams;
    }
}
