package breeder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DecayChainNest {
    private final List<DecayChain> decayChains;
    private final Map<Isotope, Map<DecayChain, Integer>> isotopeToChainMap; // Simplified map structure

    public DecayChainNest() {
        this.decayChains = new ArrayList<>();
        this.isotopeToChainMap = new HashMap<>();
    }

    public boolean hasDecayChain(Isotope isotope) {
        for (DecayChain chain : decayChains) {
            if (chain.containsIsotope(isotope)) {
                return true;
            }
        }
        return false;
    }

    public void addDecayChain(DecayChain decayChain) {
        decayChains.add(decayChain);
        for (int i = 0; i < decayChain.getDecayChain().size(); i++) {
            Isotope isotope = decayChain.getIsotope(i);
            isotopeToChainMap.computeIfAbsent(isotope, k -> new HashMap<>())
                            .put(decayChain, i);
        }
    }

    public void addDecayChainList(DecayChain[] decayChainList) {
        for (DecayChain dcl : decayChainList) {
            this.addDecayChain(dcl);
        }
    }

    public List<DecayChain> getDecayChains() {
        return decayChains;
    }

    public DecayChain getDecayChain(Isotope isotope) {
        for (DecayChain chain : decayChains) {
            if (chain.containsIsotope(isotope)) {
                return chain;
            }
        }
        return null; // Not found
    }

    /**
     * Simulate decay across all chains for a given time.
     * @param time Time in seconds.
     */
    public void simulateDecay(double time) {
        for (DecayChain chain : decayChains) {
            chain.simulateDecay(time);
        }
    }

    public void simulateDecay(double time, double timeStep) {
        for (DecayChain chain : decayChains) {
            chain.simulateDecay(time, timeStep);
        }
    }

    /**
     * Get total activity (Bq) of all chains.
     */
    public double getTotalActivity() {
        double totalActivity = 0.0;
        for (DecayChain chain : decayChains) {
            for (int i = 0; i < chain.getDecayChainLength(); i++) {
                double mass = chain.getIsotopeMassGrams(i);
                totalActivity += chain.getIsotope(i).getActivityPerGram() * mass;
            }
        }
        return totalActivity;
    }

    public double getActivityByDecay(DecayType decayType) {
        double totalActivity = 0.0;
        for (DecayChain chain : decayChains) {
            for (Isotope isotope : chain.getDecayChain()) {
                if (isotope.getDecayType() == decayType) {
                    double mass = chain.getIsotopeMassGrams(chain.getIsotopeIndex(isotope));
                    totalActivity += isotope.getActivityPerGram() * mass;
                }
            }
        }
        return totalActivity;
    }

    /**
     * Captures decay spectrum for given time (without decay simulation)
     * @param time Spectrum capture time, s
     * @param decayType Decay type to capture
     * @return Spectrum map (key - energy, value - decays)
     */
    public Map<Double, Double> captureDS(double time, DecayType decayType) {
        Map<Double, Double> spectrum = new HashMap<>();
        for (DecayChain dc : decayChains) {
            for (Isotope i : dc.getIsotopesByDecay(decayType)) {
                double energy = i.getDecayEnergy() * 1000.0; // Convert MeV to keV
                double decays = i.getActivityPerAtom() * dc.getIsotopeAtoms(i) * time;
                // Add to existing value if energy already exists, otherwise put new value
                spectrum.merge(energy, decays, Double::sum);
            }
        }
        return spectrum;
    }

    /**
     * Captures decay spectrum with corresponding isotopes
     * for given time (without decay simulation)
     * @param time Spectrum capture time, s
     * @param decayType Decay type to capture
     * @return Spectrum map (key - isotope, value - energy, decays)
     */
    public Map<Isotope, Double[]> captureDSIMap(double time, DecayType decayType) {
        Map<Isotope, Double[]> spectrum = new HashMap<>();
        for (DecayChain dc : decayChains) {
            for (Isotope i : dc.getIsotopesByDecay(decayType)) {
                double energy = i.getDecayEnergy() * 1000.0; // Convert MeV to keV
                double decays = i.getActivityPerAtom() * dc.getIsotopeAtoms(i) * time;
                Double[] energyDecayArr = new Double[2];
                energyDecayArr[0] = energy;
                energyDecayArr[1] = decays;
                spectrum.put(i, energyDecayArr);
            }
        }
        return spectrum;
    }

    public double getIsotopeActivity(Isotope isotope) {
        Map<DecayChain, Integer> chainInfo = isotopeToChainMap.get(isotope);
        if (chainInfo == null) return 0.0;

        // Get the first entry (assuming each isotope only appears in one chain)
        Map.Entry<DecayChain, Integer> entry = chainInfo.entrySet().iterator().next();
        DecayChain chain = entry.getKey();
        int index = entry.getValue();
        
        if (index == -1) return 0.0;

        double mass = chain.getIsotopeMassGrams(index);
        return isotope.getActivityPerGram() * mass;
    }

    public double getIsotopeAtoms(Isotope isotope) {
        return getDecayChain(isotope).getIsotopeAtoms(isotope);
    }

    public Map<Isotope, Double> getIsotopesAtomsMap() {
        Map<Isotope, Double> isotopeMap = new HashMap<>();
        for (DecayChain dc : getDecayChains()) {
            for (Isotope i : dc.getDecayChain()) {
                isotopeMap.put(i, dc.getIsotopeAtoms(i));
            }
        }
        return isotopeMap;
    }
}