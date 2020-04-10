package nl.kqcreations.cityrp.settings;

import org.mineacademy.fo.settings.SimpleSettings;

public class Settings extends SimpleSettings {
    @Override
    protected int getConfigVersion() {
        return 1;
    }

    public static class Plot {
        public static Double DEFAULT_SQUARE_METER_PRICE;

        private static void init() {
            pathPrefix("Plots");

            DEFAULT_SQUARE_METER_PRICE = getDoubleSafe("Default_Square_Meter_Price");
        }
    }

    public static class Cities {
        public static String DEFAULT_LAWS;
        public static String DEFAULT_COLOR;

        private static void init() {
            pathPrefix("Cities");

            DEFAULT_LAWS = getString("Default_Laws");
            DEFAULT_COLOR = getString("Default_Color");
        }
    }
}
