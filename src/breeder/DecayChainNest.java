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
        this.decayChains.add(decayChain);

        for (DecayChain decayChainEl : decayChains) {
            for (Isotope isotope : decayChainEl.getDecayChain()) {
                // Check if the isotope decays to or can transmutate to any isotope in the chain
                boolean hasDecayProduct = hasDecayChain(isotope.getDecayIsotope());
                boolean isAlphaTransmutation = hasDecayChain(isotope.getAlphaCaptureProduct(0.0f));
                boolean isNeutronTransmutation = hasDecayChain(isotope.getNeutronCaptureProduct(0.0f));

                if (hasDecayProduct || isAlphaTransmutation || isNeutronTransmutation) {
                    isotopeToChainMap.put(isotope, Map.of(decayChainEl, decayChainEl.getIsotopeIndex(isotope)));
                }
            }
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

    /**
     * Get total activity (Bq) of all chains.
     */
    public double getTotalActivity() {
        double totalActivity = 0.0;
        for (DecayChain chain : decayChains) {
            for (int i = 0; i < chain.getDecayChainLength(); i++) {
                double mass = chain.getIsotopeMass(i);
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
                    double mass = chain.getIsotopeMass(chain.getIsotopeIndex(isotope));
                    totalActivity += isotope.getActivityPerGram() * mass;
                }
            }
        }
        return totalActivity;
    }

    public double getIsotopeActivity(Isotope isotope) {
        Map<DecayChain, Integer> chainInfo = isotopeToChainMap.get(isotope);
        if (chainInfo == null) return 0.0;

        // Get the first entry (assuming each isotope only appears in one chain)
        Map.Entry<DecayChain, Integer> entry = chainInfo.entrySet().iterator().next();
        DecayChain chain = entry.getKey();
        int index = entry.getValue();
        
        if (index == -1) return 0.0;

        double mass = chain.getIsotopeMass(index);
        return isotope.getActivityPerGram() * mass;
    }
}