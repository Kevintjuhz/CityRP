package nl.kqcreations.cityrp;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;

public class CityRpExpansion extends PlaceholderExpansion {

	private CityRPPlugin plugin;

	/**
	 * Since we register the expansion inside our own plugin, we
	 * can simply use this method here to get an instance of our
	 * plugin.
	 *
	 * @param plugin The instance of our plugin.
	 */
	public CityRpExpansion(CityRPPlugin plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean persist() {
		return true;
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
	public String onPlaceholderRequest(Player p, String params) {

		if (p == null)
			return "";

		switch (params) {
			case "current_city":
				return PlayerCityTracker.getCurrentCityOfPlayer(p.getUniqueId());
		}

		return "";
	}

}
