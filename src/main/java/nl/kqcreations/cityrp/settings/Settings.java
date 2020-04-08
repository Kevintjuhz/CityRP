package nl.kqcreations.cityrp.settings;

import org.mineacademy.fo.settings.SimpleSettings;

public class Settings extends SimpleSettings {
	@Override
	protected int getConfigVersion() {
		return 1;
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
