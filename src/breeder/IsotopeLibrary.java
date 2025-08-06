package breeder;

public class IsotopeLibrary {
    private static final float YEAR_TO_SECONDS = 365.25f * 24 * 3600;
    private static final float DAY_TO_SECONDS = 24 * 3600;
    private static final float HOUR_TO_SECONDS = 3600;
    private static final float MINUTE_TO_SECONDS = 60.0f;
    private static final float MICROSECOND_TO_SECONDS = 1e-6f;

    // 1) Thorium 232 natural decay isotopes
    public static final Isotope Th232 = new Isotope("Th-232", (short)232);
    public static final Isotope Ra228 = new Isotope("Ra-228", (short)228);
    public static final Isotope Ac228 = new Isotope("Ac-228", (short)228);
    public static final Isotope Th228 = new Isotope("Th-228", (short)228);
    public static final Isotope Ra224 = new Isotope("Ra-224", (short)224);
    public static final Isotope Rn220 = new Isotope("Rn-220", (short)220);
    // Po-216 skipped, very short half-life
    public static final Isotope Pb212 = new Isotope("Pb-212", (short)212);
    public static final Isotope Bi212 = new Isotope("Bi-212", (short)212);
    // Po-212 skipped, very short half-life
    public static final Isotope Tl208 = new Isotope("Tl-208", (short)208);
    public static Isotope Pb208 = new Isotope("Pb-208", (short)208);  // Stable

    // 2) Thorium 232 neutron capture and corresponding decay isotopes
    // Th-232
    public static final Isotope Th233 = new Isotope("Th-233", (short)233);
    public static final Isotope Pa233 = new Isotope("Pa-233", (short)233);
    public static final Isotope U233 = new Isotope("U-233", (short)233);
    public static final Isotope Th229 = new Isotope("Th-229", (short)229);
    public static final Isotope Ra225 = new Isotope("Ra-225", (short)225);
    public static final Isotope Ac225 = new Isotope("Ac-225", (short)225);
    public static final Isotope Fr221 = new Isotope("Fr-221", (short)221);
    // At-217 skipped, very short half-life
    public static final Isotope Bi213 = new Isotope("Bi-213", (short)213);
    // Po213 skipped, very short half-life
    public static final Isotope Pb209 = new Isotope("Pb-209", (short)209);
    public static final Isotope Bi209 = new Isotope("Bi-209", (short)209);  // Stable

    // 3) Significant neutron capturing (or fission) isotopes from 1) and 2)
    // Th-232 - capture
    // U-233 - fission
    // Th-229 - capture

    // 4) Neutron capture and fission products from 3) (+ corresponding decay isotopes)
    // Th-233
    // U-233 neutron capture products:
    public static final Isotope U234 = new Isotope("U-234", (short)234);
    public static final Isotope Th230 = new Isotope("Th-230", (short)230);
    // U-233 fission products and their decay products:
    public static final Isotope Xe135 = new Isotope("Xe-135", (short)135);
    public static final Isotope Cs135 = new Isotope("Cs-135", (short)135);
    public static final Isotope Ba135 = new Isotope("Ba-135", (short)135);  // Stable

    public static final Isotope Sr90 = new Isotope("Sr-90", (short)90);
    public static final Isotope Y90 = new Isotope("Y-90", (short)90);
    public static final Isotope Zr90 = new Isotope("Zr-90", (short)90);  // Stable

    public static final Isotope Cs137 = new Isotope("Cs-137", (short)137);
    public static final Isotope Ba137m = new Isotope("Ba-137m", (short)137);
    public static final Isotope Ba137 = new Isotope("Ba-137", (short)137);  // Stable

    public static final Isotope Zr93 = new Isotope("Zr-93", (short)93);
    public static final Isotope Nb93 = new Isotope("Nb-93", (short)93);  // Stable


    // 5) Radium 226 decay isotopes
    public static final Isotope Ra226 = new Isotope("Ra-226", (short)226);
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

    static {
        // Decay properties for 1)
        Th232.setDecayProperties(1.405e10f * YEAR_TO_SECONDS, Ra228, DecayType.ALPHA, 4.081f);
        Ra228.setDecayProperties(5.75f * YEAR_TO_SECONDS, Ac228, DecayType.BETA, 0.046f);
        Ac228.setDecayProperties(6.15f * HOUR_TO_SECONDS, Th228, DecayType.BETA, 2.124f);
        Th228.setDecayProperties(1.912f * YEAR_TO_SECONDS, Ra224, DecayType.ALPHA, 5.520f);
        Ra224.setDecayProperties(3.632f * DAY_TO_SECONDS, Rn220, DecayType.ALPHA, 5.789f);
        Rn220.setDecayProperties(55.6f, Pb212, DecayType.ALPHA, 6.404f);
        Pb212.setDecayProperties(10.64f * HOUR_TO_SECONDS, Bi212, DecayType.BETA, 0.574f);
        Bi212.setDecayProperties(60.55f * MINUTE_TO_SECONDS, Tl208, DecayType.ALPHA, 6.207f);
        Tl208.setDecayProperties(3.053f * MINUTE_TO_SECONDS, Pb208, DecayType.BETA, 4.999f);
        // Pb208 is stable, no decay properties set


        // Decay properties for 2)
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


        // Decay properties for 4)
        // Th-232, U-233 and Th-229 already have decay properties set
        U234.setDecayProperties(2.455e5f * YEAR_TO_SECONDS, Th230, DecayType.ALPHA, 4.857f);
        Th230.setDecayProperties(7.538e4f * YEAR_TO_SECONDS, Ra226, DecayType.ALPHA, 4.770f);


        // Decay properties for 5)
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

        
        // Neutron capture and fission cross-sections for 3)

        // Thermal neutrons (0.0253 eV)
        Th232.setNeutronCapture(2.53e-8f, 7.4f, Th233);
        U233.setNeutronCapture(2.53e-8f, 45.1f, U234);

        Isotope[] fissionProductsU233_t1 = {Cs137, Zr93};  // Thermal fission products
        U233.setFissionCapture(2.53e-8f, 531.2f, fissionProductsU233_t1);
        Isotope[] fissionProductsU233_t2 = {Xe135, Sr90};
        U233.setFissionCapture(2.53e-8f, 531.2f, fissionProductsU233_t2);

        Th229.setNeutronCapture(2.53e-8f, 60.2f, Th230);

        // Epithermal neutrons (1 eV - 1 keV)
        Th232.setNeutronCapture(500.0f, 85.0f, Th233);

        Isotope[] fissionProductsU233_e1 = {Cs137, Zr93};  // Epithermal fission products
        U233.setFissionCapture(500.0f, 275.0f, fissionProductsU233_e1);
    }
}