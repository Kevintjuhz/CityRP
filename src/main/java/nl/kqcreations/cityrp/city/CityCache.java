package nl.kqcreations.cityrp.city;

import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.mineacademy.fo.settings.YamlSectionConfig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class CityCache extends YamlSectionConfig {

	private static final Map<World, CityCache> cacheMap = new HashMap<>();

	private final World world;
	private String name;
	private String regionName;
	private ChatColor color;

	public CityCache(final World world) {
		super(world.getName());
		this.world = world;

		loadConfiguration(NO_DEFAULT, "data/cities.yml");
		cacheMap.put(world, this);
	}

	@Override
	protected void onLoadFinish() {
		if (isSet("Name"))
			name = getString("Name");

		if (isSet("Region_Name"))
			regionName = getString("Region_Name");

		color = get("Color", ChatColor.class);
	}

	public void setName(final String name) {
		this.name = name;

		save("Name", name);
	}

	public void setColor(final ChatColor color) {
		this.color = color;

		save("Color", color.name());
	}

	public void setRegionName(final String regionName) {
		this.regionName = regionName;

		save("Region_Name", regionName);
	}

//	public void addCity(final String name, final World world) {
//		addCity(name, "", world);
//	}
//
//	public void addCity(final String name, final String region_name, final World world) {
//		Valid.checkNotNull(name, "City name cannot be null");
//
//		CityCache cache = new CityCache(world);
//
//	}

	// --------------------------------------------------------------------------------------------------------------
	// Static methods below
	// --------------------------------------------------------------------------------------------------------------

	public static void addCity(final World world, final String name) {
		addCity(world, name, ChatColor.WHITE);
	}

	public static void addCity(final World world, final String name, final ChatColor color) {
		addCity(world, name, color, "");
	}

	public static void addCity(final World world, final String name, final ChatColor color, final String wg_region) {
		CityCache cache = cacheMap.get(world);

		if (cache != null)
			return;

		cache = new CityCache(world);
		cache.setName(name);
		cache.setColor(color);
		cache.setRegionName(wg_region);

		cacheMap.put(world, cache);

	}

	public static CityCache getCityCache(final World world) {
		CityCache cache = cacheMap.get(world);

		if (cache == null) {
			cache = new CityCache(world);

			cacheMap.put(world, cache);
		}

		return cache;
	}

	public static CityCache getByName(final String name) {
		for (final CityCache loadedClass : cacheMap.values())
			if (loadedClass.getName().equals(name))
				return loadedClass;

		return null;
	}

	public static List<CityCache> getCities() {
		return new ArrayList<>(cacheMap.values());
	}
}
