package nl.kqcreations.cityrp.settings;

import org.mineacademy.fo.settings.SimpleSettings;

public class Settings extends SimpleSettings {
	@Override
	protected int getConfigVersion() {
		return 1;
	}

	/*public static class Database {
		public static String NAME;
		public static String HOST;
		public static Integer PORT;

		private static void init() {
			pathPrefix("Database");

			NAME = getString("Name");
			HOST = getString("Host");
			PORT = getInteger("Port");
		}
	}*/

	public static class PlayerData {
		public static String DEFAULT_RANK;
		public static String DEFAULT_JOB;
		public static String DEFAULT_CITY_COLOR;

		private static void init() {
			pathPrefix("PlayerData");

			DEFAULT_RANK = getString("Default_Rank");
			DEFAULT_JOB = getString("Default_Job");
			DEFAULT_CITY_COLOR = getString("Default_City_Color");
		}
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

	public static Boolean ALLOW_CRAFTING;
	public static Double STARTING_BALANCE;
	public static String VALID_MONEY_NOTE;

	private static void init() {
		pathPrefix(null);

		ALLOW_CRAFTING = getBoolean("Allow_Crafting");
		STARTING_BALANCE = getDoubleSafe("Starting_Balance");
		VALID_MONEY_NOTE = getString("Valid_Money_Note");
	}
}
