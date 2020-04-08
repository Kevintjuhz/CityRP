package nl.kqcreations.cityrp;

import nl.kqcreations.cityrp.cache.CityCache;
import nl.kqcreations.cityrp.cache.WorldCache;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.mineacademy.fo.model.HookManager;
import org.mineacademy.fo.remain.Remain;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public enum PlayerCityTracker {

	INSTANCE;

	private final Map<UUID, String> playersCurrentCity = new HashMap<>();

	public void updatePlayerCityTracker(Player player) {
		final UUID uuid = player.getUniqueId();
		final World world = player.getWorld();
		final String worldName = world.getName();
		final Location location = player.getLocation();

		WorldCache cache = WorldCache.getWorldCache(worldName);

		if (cache == null)
			return;

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

	private boolean containsPlayer(UUID uuid) {
		return playersCurrentCity.containsKey(uuid);
	}

	private String getPlayerCurrentRegion(Location location) {
		List<String> regions = HookManager.getRegions(location);
		for (String region : regions) {
			CityCache cache = CityCache.getCityCache(region);
			if (cache != null) {
				return region;
			}
		}

		return null;
	}

	private String getCurrentCityOfPlayer(UUID uuid) {
		return playersCurrentCity.get(uuid);
	}

	private void setCurrentPlayerCity(UUID uuid) {
		setCurrentPlayerCity(uuid, "__global__");
	}

	private void setCurrentPlayerCity(UUID uuid, String city) {
		playersCurrentCity.remove(uuid);
		playersCurrentCity.put(uuid, city);
	}

	private void sendPlayerTitle(Player player) {
		String city = playersCurrentCity.get(player.getUniqueId());

		if (city.equals("__global__")) {
			Remain.sendTitle(player, "&cWelcome to", "&cNO MANS LAND".toUpperCase());
		} else {
			CityCache cache = CityCache.getCityCache(city);
			String color = cache.getColor();
			Remain.sendTitle(player, color + "Welcome to", color + cache.getTitle().toUpperCase());
		}
	}
}
