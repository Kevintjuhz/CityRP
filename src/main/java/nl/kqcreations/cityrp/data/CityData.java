package nl.kqcreations.cityrp.data;

import lombok.Getter;
import nl.kqcreations.cityrp.settings.Settings;
import org.mineacademy.fo.settings.YamlSectionConfig;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

@Getter
public class CityData extends YamlSectionConfig {

	private static final Map<String, CityData> cacheMap = new HashMap<>();

	private static String regionName;

	private String title, region, color;

	/**
	 * Create a city with a region name. Should be the same as a region name
	 *
	 * @param regionName
	 */
	protected CityData(String regionName) {
		super(regionName);
		this.regionName = regionName;

		loadConfiguration(NO_DEFAULT, "data" + File.separator + "Cities.yml");

		cacheMap.put(regionName, this);
	}

	/**
	 * Gets the citycache of the given region name.
	 *
	 * @param regionName
	 * @return
	 */
	public static CityData getCityCache(final String regionName) {
		return cacheMap.get(regionName);
	}

	/**
	 * Gets or creates the citycache of the given region name.
	 *
	 * @param regionName
	 * @return
	 */
	public static CityData getOrCreateCityCache(final String regionName) {
		return cacheMap.computeIfAbsent(regionName, CityData::new);
	}

	@Override
	protected void onLoadFinish() {
		title = getString("Title");
		region = getString("Region");
		color = getString("Color");
	}

	/**
	 * Sets the title of a city
	 *
	 * @param title
	 */
	public void setTitle(String title) {
		this.title = title;
		save("Title", title);
	}

	/**
	 * Sets the region of the city
	 *
	 * @param region
	 */
	public void setRegion(String region) {
		this.region = region;
		save("Region", region);
	}

	/**
	 * Sets the color of the city
	 *
	 * @param color
	 */
	public void setColor(String color) {
		this.color = color;
		save("Color", color);
	}

	// --------------------------------------------
	// Static methods
	// --------------------------------------------

	/**
	 * Adds a city with the given region name.
	 *
	 * @param regionName
	 */
	public static void addCity(final String regionName) {
		addCity(regionName, "&l" + regionName);
	}

	/**
	 * Adds a city with the given region name and custom title.
	 *
	 * @param regionName
	 * @param title
	 */
	public static void addCity(final String regionName, final String title) {
		addCity(regionName, title, Settings.Cities.DEFAULT_COLOR);
	}

	/**
	 * Adds a city with the given region name, custom title and custom color.
	 *
	 * @param regionName
	 * @param title
	 * @param color
	 */
	public static void addCity(final String regionName, final String title, final String color) {
		CityData cache = getOrCreateCityCache(regionName);
		cache.setTitle(title);
		cache.setColor(color);
		cache.setRegion(regionName);
	}
}
