package nl.kqcreations.cityrp.cache;

import lombok.Getter;
import nl.kqcreations.cityrp.settings.Settings;
import org.mineacademy.fo.settings.YamlSectionConfig;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

@Getter
public class CityCache extends YamlSectionConfig {

	private static final Map<String, CityCache> cacheMap = new HashMap<>();

	private static String regionName;

	private String title, region, color;

	protected CityCache(String regionName) {
		super(regionName);
		this.regionName = regionName;

		loadConfiguration(NO_DEFAULT, "data" + File.separator + "Cities.yml");

		cacheMap.put(regionName, this);
	}

	@Override
	protected void onLoadFinish() {
		title = getString("Title");
		region = getString("Region");
		color = getString("Color");
	}

	public void setTitle(String title) {
		this.title = title;
		save("Title", title);
	}

	public void setRegion(String region) {
		this.region = region;
		save("Region", region);
	}

	public void setColor(String color) {
		this.color = color;
		save("Color", color);
	}

	// --------------------------------------------
	// Static methods
	// --------------------------------------------

	public static CityCache getCityCache(final String regionName) {
		return cacheMap.get(regionName);
	}

	public static CityCache getOrCreateCityCache(final String regionName) {
		return cacheMap.computeIfAbsent(regionName, CityCache::new);
	}

	public static void addCity(final String regionName) {
		addCity(regionName, "&l" + regionName);
	}

	public static void addCity(final String regionName, final String title) {
		addCity(regionName, title, Settings.Cities.DEFAULT_COLOR);
	}

	public static void addCity(final String regionName, final String title, final String color) {
		CityCache cache = getOrCreateCityCache(regionName);
		cache.setTitle(title);
		cache.setColor(color);
		cache.setRegion(regionName);
	}
}
