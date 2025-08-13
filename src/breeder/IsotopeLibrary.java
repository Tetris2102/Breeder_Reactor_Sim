package breeder;

// All data is mainly taken from ENDF/B-VIII.0 and IAEA Nuclear Data

public abstract class IsotopeLibrary {
    private static final float YEAR_TO_SECONDS = 365.25f * 24 * 3600;
    private static final float DAY_TO_SECONDS = 24 * 3600;
    private static final float HOUR_TO_SECONDS = 3600;
    private static final float MINUTE_TO_SECONDS = 60.0f;
    private static final float MILLISECOND_TO_SECONDS = 1e-3f;
    private static final float MICROSECOND_TO_SECONDS = 1e-6f;

    // Decay tree combining all decay chains with all isotopes
    public static final DecayChainNest decayTree = new DecayChainNest();

    // Pu-238 natural decay isotopes
    public static final Isotope Pu238 = new Isotope("Pu-238", (short)238);
    public static final Isotope U234 = new Isotope("U-234", (short)234);
    // U-234 decay chain ...
    public static final DecayChain Pu238NDChain = new DecayChain("Pu-238 Natural Decay Chain");
    public static final Isotope[] Pu238NDIsotopeList = {Pu238, U234};

    // Be-9 alpha capture isotopes
    public static final Isotope Be9 = new Isotope("Be-9", (short)9);
    public static final Isotope C12 = new Isotope("C-12", (short)12);  // Stable
    public static final DecayChain Be9NCChain = new DecayChain("Be-9 Neutron Capture Chain");
    public static final Isotope[] Be9NCIsotopeList = {Be9, C12};
    // No need for DecayChain, since it's a direct (alpha, n) reaction

    // Thorium 232 natural decay isotopes
    public static final Isotope Th232 = new Isotope("Th-232", (short)232);
    public static final Isotope Ra228 = new Isotope("Ra-228", (short)228);
    public static final Isotope Ac228 = new Isotope("Ac-228", (short)228);
    public static final Isotope Th228 = new Isotope("Th-228", (short)228);
    public static final Isotope Ra224 = new Isotope("Ra-224", (short)224);
    public static final Isotope Rn220 = new Isotope("Rn-220", (short)220);
    public static final Isotope Po216 = new Isotope("Po-216", (short)216);
    public static final Isotope Pb212 = new Isotope("Pb-212", (short)212);
    public static final Isotope Bi212 = new Isotope("Bi-212", (short)212);
    public static final Isotope Po212 = new Isotope("Po-212", (short)212);
    public static final Isotope Pb208 = new Isotope("Pb-208", (short)208);  // Stable

    public static final DecayChain Th232NDChain = new DecayChain("Th-232 Natural Decay Chain");
    public static final Isotope[] Th232NDIsotopeList = {Th232, Ra228, Ac228, Th228, Ra224, Rn220, Po216, Pb212, Bi212, Po212, Pb208};


    // Thorium 232 neutron capture and corresponding decay isotopes
    // Th-232
    public static final Isotope Th233 = new Isotope("Th-233", (short)233);
    public static final Isotope Pa233 = new Isotope("Pa-233", (short)233);
    public static final Isotope U233 = new Isotope("U-233", (short)233);
    public static final Isotope Th229 = new Isotope("Th-229", (short)229);
    public static final Isotope Ra225 = new Isotope("Ra-225", (short)225);
    public static final Isotope Ac225 = new Isotope("Ac-225", (short)225);
    public static final Isotope Fr221 = new Isotope("Fr-221", (short)221);
    public static final Isotope At217 = new Isotope("At-217", (short)217);
    public static final Isotope Bi213 = new Isotope("Bi-213", (short)213);
    public static final Isotope Po213 = new Isotope("Po-213", (short)213);
    public static final Isotope Pb209 = new Isotope("Pb-209", (short)209);
    public static final Isotope Bi209 = new Isotope("Bi-209", (short)209);  // Stable

    public static final DecayChain Th232NCChain = new DecayChain("Th-232 Neutron Capture Chain");
    public static final Isotope[] Th232NCIsotopeList = {Th233, Pa233, U233, Th229, Ra225, Ac225, Fr221, At217, Bi213, Po213, Pb209, Bi209};


    // Significant neutron capturing (or fission) isotopes from Th232NDChain and Th232NCChain
    // Th-232 - capture
    // U-233 - fission
    // Th-229 - capture

    // Neutron capture and fission products from Th-232, U-233 and Th-229
    // neutron capture (+ corresponding decay isotopes)
    // Th-233
    // U-233 neutron capture chain:
    // U-234
    public static final Isotope Th230 = new Isotope("Th-230", (short)230);
    public static final Isotope Ra226 = new Isotope("Ra-226", (short)226);
    // Ra-226 decay chain declared below
    public static final DecayChain U233NCChain = new DecayChain("U-233 Neutron Capture Chain");
    public static final Isotope[] U233NCIsotopeList = {U234, Th230, Ra226};

    // U-233 fission products and their decay products:
    public static final Isotope Xe135 = new Isotope("Xe-135", (short)135);
    public static final Isotope Cs135 = new Isotope("Cs-135", (short)135);
    public static final Isotope Ba135 = new Isotope("Ba-135", (short)135);  // Stable
    public static final DecayChain Xe135NDChain = new DecayChain("Xe-135 Natural Decay Chain");
    public static final Isotope[] Xe135NDIsotopeList = {Xe135, Cs135, Ba135};

    public static final Isotope Sr99 = new Isotope("Sr-99", (short)99);
    public static final Isotope Y99 = new Isotope("Y-99", (short)99);
    public static final Isotope Zr99 = new Isotope("Zr-99", (short)99);
    public static final Isotope Nb99 = new Isotope("Nb-99", (short)99);
    public static final Isotope Mo99 = new Isotope("Mo-99", (short)99);
    public static final Isotope Tc99m = new Isotope("Tc-99m", (short)99);
    public static final Isotope Tc99 = new Isotope("Tc-99", (short)99);
    public static final Isotope Ru99 = new Isotope("Ru-99", (short)99);  // Stable
    public static final DecayChain Sr99NDChain = new DecayChain("Sr-99 Natural Decay Chain");
    public static final Isotope[] Sr99NDIsotopeList = {Sr99, Y99, Zr99, Nb99, Mo99, Tc99m, Tc99, Ru99};

    public static final Isotope Xe143 = new Isotope("Xe-143", (short)143);
    public static final Isotope Cs143 = new Isotope("Cs-143", (short)143);
    public static final Isotope Ba143 = new Isotope("Ba-143", (short)143);
    public static final Isotope La143 = new Isotope("La-143", (short)143);
    public static final Isotope Ce143 = new Isotope("Ce-143", (short)143);
    public static final Isotope Pr143 = new Isotope("Pr-143", (short)143);
    public static final Isotope Nd143 = new Isotope("Nd-143", (short)143);  // Stable
    public static final DecayChain Xe143NDChain = new DecayChain("Xe-143 Natural Decay Chain");
    public static final Isotope[] Xe143NDIsotopeList = {Xe143, Cs143, Ba143, La143, Ce143, Pr143, Nd143};

    public static final Isotope Sr90 = new Isotope("Sr-90", (short)90);
    public static final Isotope Y90 = new Isotope("Y-90", (short)90);
    public static final Isotope Zr90 = new Isotope("Zr-90", (short)90);  // Stable
    public static final DecayChain Sr90NDChain = new DecayChain("Sr-90 Natural Decay Chain");
    public static final Isotope[] Sr90NDIsotopeList = {Sr90, Y90, Zr90};

    public static final Isotope Cs137 = new Isotope("Cs-137", (short)137);
    public static final Isotope Ba137m = new Isotope("Ba-137m", (short)137);
    public static final Isotope Ba137 = new Isotope("Ba-137", (short)137);  // Stable
    public static final DecayChain Cs137NDChain = new DecayChain("Cs-137 Natural Decay Chain");
    public static final Isotope[] Cs137NDIsotopeList = {Cs137, Ba137m, Ba137};

    public static final Isotope Rb97 = new Isotope("Rb-97", (short)97);
    public static final Isotope Sr97 = new Isotope("Sr-97", (short)97);
    public static final Isotope Y97 = new Isotope("Y-97", (short)97);
    public static final Isotope Zr97 = new Isotope("Zr-97", (short)97);  // Stable
    public static final DecayChain Rb97NDChain = new DecayChain("Rb-97 Natural Decay Chain");
    public static final Isotope[] Rb97NDIsotopeList = {Rb97, Sr97, Y97, Zr97};

    public static final Isotope Zr93 = new Isotope("Zr-93", (short)93);
    public static final Isotope Nb93 = new Isotope("Nb-93", (short)93);  // Stable
    public static final DecayChain Zr93NDChain = new DecayChain("Zr-93 Natural Decay Chain");
    public static final Isotope[] Zr93NDIsotopeList = {Zr93, Nb93};

    public static final Isotope La135 = new Isotope("La-135", (short)135);
    // Ba-135 already defined, stable
    public static final DecayChain La135NDChain = new DecayChain("La-135 Natural Decay Chain");
    public static final Isotope[] La135NDIsotopeList = {La135, Ba135};

    public static final Isotope Pd117 = new Isotope("Pd-117", (short)117);
    public static final Isotope Ag117 = new Isotope("Ag-117", (short)117);
    public static final Isotope Cd117 = new Isotope("Cd-117", (short)117);  // Stable
    public static final DecayChain Pd117NDChain = new DecayChain("Pd-117 Natural Decay Chain");
    public static final Isotope[] Pd117NDIsotopeList = {Pd117, Ag117, Cd117};

    public static final Isotope Tc105 = new Isotope("Tc-105", (short)105);
    public static final Isotope Mo105 = new Isotope("Mo-105", (short)105);
    public static final Isotope Ru105 = new Isotope("Ru-105", (short)105);  // Stable
    public static final DecayChain Tc105NDChain = new DecayChain("Tc-105 Natural Decay Chain");
    public static final Isotope[] Tc105NDIsotopeList = {Tc105, Mo105, Ru105};

    public static final Isotope I129 = new Isotope("I-129", (short)129);
    public static final Isotope Xe129 = new Isotope("Xe-129", (short)129);  // Stable
    public static final DecayChain I129NDChain = new DecayChain("I-129 Natural Decay Chain");
    public static final Isotope[] I129NDIsotopeList = {I129, Xe129};

    public static final Isotope Ce144 = new Isotope("Ce-144", (short)144);
    public static final Isotope Pr144 = new Isotope("Pr-144", (short)144);
    public static final Isotope Nd144 = new Isotope("Nd-144", (short)144);  // Stable
    public static final DecayChain Ce144NDChain = new DecayChain("Ce-144 Natural Decay Chain");
    public static final Isotope[] Ce144NDIsotopeList = {Ce144, Pr144, Nd144};


    // Radium 226 decay isotopes
    // Ra-226
    public static final Isotope Rn222 = new Isotope("Rn-222", (short)222);
    public static final Isotope Po218 = new Isotope("Po-218", (short)218);
    public static final Isotope Pb214 = new Isotope("Pb-214", (short)214);
    public static final Isotope Bi214 = new Isotope("Bi-214", (short)214);
    public static final Isotope Po214 = new Isotope("Po-214", (short)214);
    public static final Isotope Tl210 = new Isotope("Tl-210", (short)210);
    public static final Isotope Pb210 = new Isotope("Pb-210", (short)210);
    public static final Isotope Bi210 = new Isotope("Bi-210", (short)210);
    public static final Isotope Po210 = new Isotope("Po-210", (short)210);
    public static final Isotope Pb206 = new Isotope("Pb-206", (short)206);
    public static final DecayChain Ra226NDChain = new DecayChain("Ra-226 Natural Decay Chain");
    public static final Isotope[] Ra226NDIsotopeList = {Ra226, Rn222, Po218, Pb214, Bi214, Po214, Tl210, Pb210, Bi210, Po210, Pb206};

    // List of decay chains for decayTree
    // Array stores references to objects, so no need to update it later
    public static final DecayChain[] decayChainList = {Pu238NDChain, Be9NCChain, Th232NDChain, Th232NCChain, U233NCChain, Xe135NDChain, Sr99NDChain, Xe143NDChain, Sr90NDChain, Cs137NDChain,
        Rb97NDChain, Zr93NDChain, La135NDChain, Pd117NDChain, Tc105NDChain, I129NDChain, Ce144NDChain, Ra226NDChain};

    static {
        // Decay properties for Pu238NDChain
        Pu238.setDecayProperties(87.7f * YEAR_TO_SECONDS, U234, DecayType.ALPHA, 5.593f);
        Pu238NDChain.addIsotopeList(Pu238NDIsotopeList, new double[Pu238NDIsotopeList.length]);

        Pu238.setDensity(19.8f); // g/cm³

        // Be-9 alpha capture properties (produces C-12 + neutron)
        Be9.setAlphaCapture(5.0f, 0.1f, C12);  // 100 mb cross-section at ~5 MeV alpha energy
        
        // Higher energy alphas (~7 MeV - typical from Po-210)
        Be9.setAlphaCapture(7.0f, 0.15f, C12);  
        
        // Lower energy alphas (~4 MeV)
        Be9.setAlphaCapture(4.0f, 0.03f, C12);

        // Neutron emission energy from Be-9 alpha capture
        Be9.setNEmissionEnergy(4.5f);  // MeV

        // Decay properties for Be9NCChain
        Be9NCChain.addIsotopeList(Be9NCIsotopeList, new double[Be9NCIsotopeList.length]);

        Be9.setDensity(1.8f);  // g/cm³

        // Decay properties for Th232NDChain
        Th232.setDecayProperties(1.405e10f * YEAR_TO_SECONDS, Ra228, DecayType.ALPHA, 4.081f);
        Ra228.setDecayProperties(5.75f * YEAR_TO_SECONDS, Ac228, DecayType.BETA, 0.046f);
        Ac228.setDecayProperties(6.15f * HOUR_TO_SECONDS, Th228, DecayType.BETA, 2.124f);
        Th228.setDecayProperties(1.912f * YEAR_TO_SECONDS, Ra224, DecayType.ALPHA, 5.520f);
        Ra224.setDecayProperties(3.632f * DAY_TO_SECONDS, Rn220, DecayType.ALPHA, 5.789f);
        Rn220.setDecayProperties(55.6f, Po216, DecayType.ALPHA, 6.404f);
        Po216.setDecayProperties(145.0f * MILLISECOND_TO_SECONDS, Pb212, DecayType.ALPHA, 6.906f);
        Pb212.setDecayProperties(10.64f * HOUR_TO_SECONDS, Bi212, DecayType.BETA, 0.574f);
        Bi212.setDecayProperties(60.55f * MINUTE_TO_SECONDS, Po212, DecayType.ALPHA, 6.207f);
        Po212.setDecayProperties(0.299f * MICROSECOND_TO_SECONDS, Pb208, DecayType.ALPHA, 8.954f);
        // Pb208 is stable, no decay properties set
        Th232NDChain.addIsotopeList(Th232NDIsotopeList, new double[Th232NDIsotopeList.length]);  // All masses are 0.0 grams by default


        // Decay properties for Th232NCChain
        Th233.setDecayProperties(21.83f * MINUTE_TO_SECONDS, Pa233, DecayType.BETA, 1.245f);
        Pa233.setDecayProperties(26.975f * DAY_TO_SECONDS, U233, DecayType.BETA, 0.571f);
        U233.setDecayProperties(1.592e5f * YEAR_TO_SECONDS, Th229, DecayType.ALPHA, 4.909f);
        Th229.setDecayProperties(7.34e3f * YEAR_TO_SECONDS, Ra225, DecayType.ALPHA, 5.168f);
        Ra225.setDecayProperties(14.9f * DAY_TO_SECONDS, Ac225, DecayType.BETA, 0.356f);
        Ac225.setDecayProperties(10.0f * DAY_TO_SECONDS, Fr221, DecayType.ALPHA, 5.935f);
        Fr221.setDecayProperties(4.8f * MINUTE_TO_SECONDS, Bi213, DecayType.ALPHA, 6.458f);
        Bi213.setDecayProperties(45.59f * MINUTE_TO_SECONDS, Pb209, DecayType.BETA, 5.988f);
        Pb209.setDecayProperties(3.253f * HOUR_TO_SECONDS, Bi209, DecayType.BETA, 0.644f);
        // Bi209 is stable, no decay properties set
        Th232NCChain.addIsotopeList(Th232NCIsotopeList, new double[Th232NCIsotopeList.length]);


        // Decay properties for Th-232, U-233 and Th-229 neutron capture products
        // Th-232, U-233 and Th-229 already have decay properties set
        U234.setDecayProperties(2.455e5f * YEAR_TO_SECONDS, Th230, DecayType.ALPHA, 4.857f);
        Th230.setDecayProperties(7.538e4f * YEAR_TO_SECONDS, Ra226, DecayType.ALPHA, 4.770f);
        // Ra-226 decay chain properties set in 5)
        U233NCChain.addIsotopeList(U233NCIsotopeList, new double[U233NCIsotopeList.length]);

        Xe135.setDecayProperties(9.14f * HOUR_TO_SECONDS, Cs135, DecayType.BETA, 1.160f);
        Cs135.setDecayProperties(2.3e6f * YEAR_TO_SECONDS, Ba135, DecayType.BETA, 0.269f);
        // Ba-135 is stable, no decay properties set
        Xe135NDChain.addIsotopeList(Xe135NDIsotopeList, new double[Xe135NDIsotopeList.length]);

        Sr99.setDecayProperties(0.269f, Y99, DecayType.BETA, 7.62f);
        Y99.setDecayProperties(1.47f, Zr99, DecayType.BETA, 5.59f);
        Zr99.setDecayProperties(2.1f, Nb99, DecayType.BETA, 2.81f);
        Nb99.setDecayProperties(15.0f, Mo99, DecayType.BETA, 2.36f);
        Mo99.setDecayProperties(65.94f * HOUR_TO_SECONDS, Tc99m, DecayType.BETA, 1.36f);
        Tc99m.setDecayProperties(6.01f * HOUR_TO_SECONDS, Tc99, DecayType.GAMMA, 0.142f);
        Tc99.setDecayProperties(2.11e5f * YEAR_TO_SECONDS, Ru99, DecayType.BETA, 0.294f);
        // Ru99 is stable, no decay properties set
        Sr99NDChain.addIsotopeList(Sr99NDIsotopeList, new double[Sr99NDIsotopeList.length]);

        Xe143.setDecayProperties(0.511f, Cs143, DecayType.BETA, 9.24f);
        Cs143.setDecayProperties(1.78f, Ba143, DecayType.BETA, 7.08f);
        Ba143.setDecayProperties(14.5f, La143, DecayType.BETA, 3.21f);
        La143.setDecayProperties(14.2f * MINUTE_TO_SECONDS, Ce143, DecayType.BETA, 2.06f);
        Ce143.setDecayProperties(33.0f * HOUR_TO_SECONDS, Pr143, DecayType.BETA, 0.465f);
        Pr143.setDecayProperties(13.57f * DAY_TO_SECONDS, Nd143, DecayType.BETA, 0.934f);
        // Nd143 is stable, no decay properties set
        Xe143NDChain.addIsotopeList(Xe143NDIsotopeList, new double[Xe143NDIsotopeList.length]);

        Sr90.setDecayProperties(28.79f * YEAR_TO_SECONDS, Y90, DecayType.BETA, 0.546f);
        Y90.setDecayProperties(64.0f * HOUR_TO_SECONDS, Zr90, DecayType.BETA, 2.280f);
        // Zr-90 is stable, no decay properties set
        Sr90NDChain.addIsotopeList(Sr90NDIsotopeList, new double[Sr90NDIsotopeList.length]);

        Cs137.setDecayProperties(30.08f * YEAR_TO_SECONDS, Ba137m, DecayType.BETA, 1.176f);
        Ba137m.setDecayProperties(2.552f * MINUTE_TO_SECONDS, Ba137, DecayType.GAMMA, 0.662f);
        // Ba-137 is stable, no decay properties set
        Cs137NDChain.addIsotopeList(Cs137NDIsotopeList, new double[Cs137NDIsotopeList.length]);

        Zr93.setDecayProperties(1.53e6f * YEAR_TO_SECONDS, Nb93, DecayType.BETA, 0.091f);
        // Nb-93 is stable, no decay properties set
        Zr93NDChain.addIsotopeList(Zr93NDIsotopeList, new double[Zr93NDIsotopeList.length]);

        Rb97.setDecayProperties(169.1f * MILLISECOND_TO_SECONDS, Sr97, DecayType.BETA, 8.3f);
        Sr97.setDecayProperties(429.0f * MILLISECOND_TO_SECONDS, Y97, DecayType.BETA, 4.7f);
        Y97.setDecayProperties(3.75f, Zr97, DecayType.BETA, 3.6f);
        // Zr-97 is stable, no decay properties set
        Rb97NDChain.addIsotopeList(Rb97NDIsotopeList, new double[Rb97NDIsotopeList.length]);

        La135.setDecayProperties(19.5f * HOUR_TO_SECONDS, Ba135, DecayType.BETA, 1.066f);  // ENSDF
        // Ba-135 is stable, no decay properties set
        La135NDChain.addIsotopeList(La135NDIsotopeList, new double[La135NDIsotopeList.length]);

        Pd117.setDecayProperties(19.3f * HOUR_TO_SECONDS, Ag117, DecayType.BETA, 1.42f);  // NNDC
        Ag117.setDecayProperties(73.6f * YEAR_TO_SECONDS, Cd117, DecayType.BETA, 1.23f);  // 66% β⁻ branch
        // Cd-117 is stable, no decay properties set
        Pd117NDChain.addIsotopeList(Pd117NDIsotopeList, new double[Pd117NDIsotopeList.length]);

        Tc105.setDecayProperties(7.6f * YEAR_TO_SECONDS, Mo105, DecayType.BETA, 3.72f);  // ENSDF
        Mo105.setDecayProperties(35.6f * YEAR_TO_SECONDS, Ru105, DecayType.BETA, 2.03f); // ENSDF
        // Ru-105 is stable, no decay properties set
        Tc105NDChain.addIsotopeList(Tc105NDIsotopeList, new double[Tc105NDIsotopeList.length]);

        I129.setDecayProperties(15.7e6f * YEAR_TO_SECONDS, Xe129, DecayType.BETA, 0.194f);  // IAEA
        // Xe-129 is stable, no decay properties set
        I129NDChain.addIsotopeList(I129NDIsotopeList, new double[I129NDIsotopeList.length]);

        Ce144.setDecayProperties(284.9f * DAY_TO_SECONDS, Pr144, DecayType.BETA, 0.319f);  // ENSDF
        Pr144.setDecayProperties(17.28f * MINUTE_TO_SECONDS, Nd144, DecayType.BETA, 3.0f); // ENSDF
        // Nd-144 is practically stable (half-life of 2.29fe15 years), so no decay properties set
        Ce144NDChain.addIsotopeList(Ce144NDIsotopeList, new double[Ce144NDIsotopeList.length]);


        // Decay properties for Ra226NDChain
        Ra226.setDecayProperties(1.6e3f * YEAR_TO_SECONDS, Rn222, DecayType.ALPHA, 4.87f);
        Rn222.setDecayProperties(3.8235f * DAY_TO_SECONDS, Po218, DecayType.ALPHA, 5.59f);
        Po218.setDecayProperties(3.10f * MINUTE_TO_SECONDS, Pb214, DecayType.ALPHA, 6.115f);
        Pb214.setDecayProperties(26.8f * MINUTE_TO_SECONDS, Bi214, DecayType.BETA, 1.024f);
        Bi214.setDecayProperties(19.9f * MINUTE_TO_SECONDS, Po214, DecayType.BETA, 3.272f);
        Po214.setDecayProperties(164.3f * MICROSECOND_TO_SECONDS, Tl210, DecayType.ALPHA, 7.835f);
        Tl210.setDecayProperties(1.3f * MINUTE_TO_SECONDS, Pb210, DecayType.BETA, 5.484f);
        Pb210.setDecayProperties(22.2f * YEAR_TO_SECONDS, Bi210, DecayType.BETA, 0.064f);
        Bi210.setDecayProperties(5.012f * DAY_TO_SECONDS, Po210, DecayType.BETA, 1.426f);
        Po210.setDecayProperties(138.376f * DAY_TO_SECONDS, Pb206, DecayType.ALPHA, 5.407f);
        // Pb206 is stable, no decay properties set
        Ra226NDChain.addIsotopeList(Ra226NDIsotopeList, new double[Ra226NDIsotopeList.length]);

        
        // Neutron capture for 3)
        // Thermal neutrons (0.0253 eV)
        // Cross-section in barns
        Th232.setNeutronCapture(2.53e-8f, 7.4f, Th233);      // ENDF/B-VIII.0
        U233.setNeutronCapture(2.53e-8f, 45.1f, U234);       // ENDF/B-VIII.0
        Th229.setNeutronCapture(2.53e-8f, 60.2f, Th230);     // IAEA Nuclear Data

        // Epithermal neutrons (1 eV - 1 keV)
        Th232.setNeutronCapture(5.0e-4f, 85.0f, Th233);       // ENDF/B-VIII.0
        U233.setNeutronCapture(5.0e-4f, 120.0f, U234);        // ENDF/B-VIII.0
        Th229.setNeutronCapture(5.0e-4f, 150.0f, Th230);      // Estimated higher resonance absorption

        // Fast neutrons (1 MeV)
        Th232.setNeutronCapture(1.0f, 0.1f, Th233);        // ENDF/B-VIII.0
        U233.setNeutronCapture(1.0f, 0.5f, U234);          // ENDF/B-VIII.0
        Th229.setNeutronCapture(1.0f, 0.3f, Th230);        // Estimated σ_fast ≈ 0.3 b

        // No need to update these isotopes, as DecayChain.decayChain stores references to them,
        // not their copies

        // Neutron fission for 3)
        // Thermal neutrons (0.0253 eV)
        Isotope[] fissionProductsU233_t1 = {Cs137, Rb97};
        U233.setFissionCapture(2.53e-8f, 531.2f, fissionProductsU233_t1, 0.062f);

        Isotope[] fissionProductsU233_t2 = {Xe135, Sr99};
        U233.setFissionCapture(2.53e-8f, 531.2f, fissionProductsU233_t2, 0.065f);

        Isotope[] fissionProductsU233_t3 = {Mo99, La135};
        U233.setFissionCapture(2.53e-8f, 531.2f, fissionProductsU233_t3, 0.061f);

        // Epithermal neutrons (1 eV - 1 keV)
        Isotope[] fissionProductsU233_e1 = {Cs137, Zr93};
        U233.setFissionCapture(5.0e-4f, 275.0f, fissionProductsU233_e1, 0.042f);

        Isotope[] fissionProductsU233_e2 = {Xe143, Sr90};
        U233.setFissionCapture(5.0e-4f, 275.0f, fissionProductsU233_e2, 0.038f);

        Isotope[] fissionProductsU233_e3 = {Pd117, Ag117};  // Symmetric fission becomes more likely
        U233.setFissionCapture(5.0e-4f, 275.0f, fissionProductsU233_e3, 0.035f);

        // Fast neutrons (1 MeV)
        Isotope[] fissionProductsU233_f1 = {Pd117, Ag117};
        U233.setFissionCapture(1.0f, 2.5f, fissionProductsU233_f1, 0.035f);

        Isotope[] fissionProductsU233_f2 = {Tc105, I129};
        U233.setFissionCapture(1.0f, 2.5f, fissionProductsU233_f2, 0.030f);

        Isotope[] fissionProductsU233_f3 = {Zr90, Ce144};  // Still some asymmetric fission
        U233.setFissionCapture(1.0f, 2.5f, fissionProductsU233_f3, 0.025f);

        decayTree.addDecayChainList(decayChainList);
    }
}