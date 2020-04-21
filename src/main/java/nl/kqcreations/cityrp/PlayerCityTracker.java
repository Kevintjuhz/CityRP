package nl.kqcreations.cityrp;

import nl.kqcreations.cityrp.data.CityData;
import nl.kqcreations.cityrp.data.WorldData;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.mineacademy.fo.model.HookManager;
import org.mineacademy.fo.remain.Remain;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class PlayerCityTracker {

	private static final Map<UUID, String> playersCurrentCity = new HashMap<>();

	public static void updatePlayerCityTracker(Player player) {
		final World world = player.getWorld();
		final String worldName = world.getName();

		WorldData cache = WorldData.getWorldCache(worldName);

		if (cache == null)
			return;

		final Location location = player.getLocation();
		final UUID uuid = player.getUniqueId();

		final String region = getPlayerCurrentRegion(location);

		if (!containsPlayer(uuid)) {
			if (region != null)
				setCurrentPlayerCity(uuid, region);
			else
				setCurrentPlayerCity(uuid);

			sendPlayerTitle(player);
		} else {
			if (region != null && !region.equals(getCurrentCityOfPlayer(uuid))) {
				setCurrentPlayerCity(uuid, region);
				sendPlayerTitle(player);
			} else if (!getCurrentCityOfPlayer(uuid).equals("__global__") && region == null) {
				setCurrentPlayerCity(uuid);
				sendPlayerTitle(player);
			}
		}
	}

	private static boolean containsPlayer(UUID uuid) {
		return playersCurrentCity.containsKey(uuid);
	}

	private static String getPlayerCurrentRegion(Location location) {
		List<String> regions = HookManager.getRegions(location);
		for (String region : regions) {
			CityData cache = CityData.getCityCache(region);
			if (cache != null) {
				return region;
			}
		}

		return null;
	}

	public static String getCurrentCityOfPlayer(UUID uuid) {
		return playersCurrentCity.get(uuid);
	}

	private static void setCurrentPlayerCity(UUID uuid) {
		setCurrentPlayerCity(uuid, "__global__");
	}

	private static void setCurrentPlayerCity(UUID uuid, String city) {
		playersCurrentCity.remove(uuid);
		playersCurrentCity.put(uuid, city);
	}

	private static void sendPlayerTitle(Player player) {
		String city = playersCurrentCity.get(player.getUniqueId());

		if (city.equals("__global__")) {
			Remain.sendTitle(player, "&cWelcome to", "&cNO MANS LAND".toUpperCase());
		} else {
			CityData cache = CityData.getCityCache(city);
			String color = cache.getColor();
			Remain.sendTitle(player, color + "Welcome to", color + cache.getTitle().toUpperCase());
		}
	}
}
