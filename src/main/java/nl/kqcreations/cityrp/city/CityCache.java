package nl.kqcreations.cityrp.city;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.ChatColor;
import org.mineacademy.fo.Valid;
import org.mineacademy.fo.collection.SerializedMap;
import org.mineacademy.fo.model.ConfigSerializable;
import org.mineacademy.fo.settings.YamlSectionConfig;

import java.util.*;

@Getter
public class CityCache extends YamlSectionConfig {

	private static final Map<String, CityCache> cacheMap = new HashMap<>();

	private final String world;
	private Collection<City> cities = new HashSet<>();


	public CityCache(final String world) {
		super(world);
		this.world = world;

		loadConfiguration(NO_DEFAULT, "data/cities.yml");
		cacheMap.put(world, this);
	}

	@Override
	protected void onLoadFinish() {
		cities = getSetSafe("Cities", City.class);
	}


	// --------------------------------------------------------------------------------------------------------------
	// Static methods below
	// --------------------------------------------------------------------------------------------------------------

//	public static void addCity(final World world, final String name) {
//		addCity(world, name, ChatColor.WHITE);
//	}
//
//	public static void addCity(final World world, final String name, final ChatColor color) {
//		addCity(world, name, color, "");
//	}

	public static boolean CityExists(final String world, final String name) {
		CityCache cache = cacheMap.get(world);
		Valid.checkNotNull(cache, "World named " + name + "does not exist!");
		return cache.getCities().stream().allMatch((city) -> city.getName().equals(name) || city.getWgRegion().equals(name));
	}

	public static void addCity(final String world, final String name, final String wg_region) {
		addCity(world, name, wg_region, ChatColor.WHITE);
	}

	public static void addCity(final String world, final String name, final String wg_region, final ChatColor color) {
		CityCache cache = getCityCache(world);

		if (cache == null) {
			cache = new CityCache(world);
			cacheMap.put(world, cache);
		}

		boolean notContainsCity = cache.getCities().stream().noneMatch((city) -> city.getWgRegion().equals(wg_region) || city.getName().equals(name));

		if (notContainsCity) {
			cache.cities.add(new City(name, wg_region, color));
			cache.save("Cities", cache.cities);
		}
	}

	public static CityCache getCityCache(final String world) {
		CityCache cache = cacheMap.get(world);

		if (cache == null) {
			cache = new CityCache(world);

			cacheMap.put(world, cache);
		}

		return cache;
	}

	public static Optional<City> getCityByRegion(final String world, final String region) {
		CityCache cache = getCityCache(world);

		for (final City city : cache.getCities())
			if (city.getWgRegion().equals(region)) {
				return Optional.of(city);
			}

		return Optional.empty();
	}

	public static Optional<City> getCityByName(final String world, final String name) {
		CityCache cache = getCityCache(world);

		for (final City city : cache.getCities())
			if (city.getName().equals(name)) {
				return Optional.of(city);
			}
		return Optional.empty();
	}

	// --------------------------------------------------------------------------------------------------------------
	// Own classes
	// --------------------------------------------------------------------------------------------------------------

	@Getter
	@AllArgsConstructor
	public final static class City implements ConfigSerializable {

		private String name;
		private String wgRegion;
		private ChatColor color;

		@Override
		public SerializedMap serialize() {
			final SerializedMap map = new SerializedMap();

			map.put("Name", name);
			map.put("WG_Region", wgRegion);
			map.put("Color", color.name());

			return map;
		}

		public static City deserialize(final SerializedMap map) {
			final String name = map.getString("Name");
			final String wg_region = map.getString("WG_Region");
			final ChatColor color = map.get("Color", ChatColor.class);

			return new City(name, wg_region, color);
		}

	}

}
