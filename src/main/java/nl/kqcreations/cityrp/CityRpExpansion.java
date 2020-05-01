package nl.kqcreations.cityrp;

import me.clip.placeholderapi.PlaceholderAPIPlugin;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;

public class CityRpExpansion extends PlaceholderExpansion {

	@Override
	public String getRequiredPlugin() {
		return null;
	}

	@Override
	public boolean canRegister() {
		return true;
	}

	@Override
	public String getIdentifier() {
		return "cityrp";
	}

	@Override
	public String getAuthor() {
		return "Kevintjuhz";
	}

	@Override
	public String getVersion() {
		return "1.0.0";
	}

	@Override
	public PlaceholderAPIPlugin getPlaceholderAPI() {
		return PlaceholderAPIPlugin.getInstance();
	}

	@Override
	public String onRequest(OfflinePlayer p, String params) {
		if (p == null)
			return "";

		if (params.equalsIgnoreCase("current_city"))
			return PlayerCityTracker.getCurrentCityOfPlayer(p.getUniqueId());

		return null;
	}


}
