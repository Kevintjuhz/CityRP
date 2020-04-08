package nl.kqcreations.cityrp.cache;

import lombok.Getter;
import nl.kqcreations.cityrp.settings.Settings;
import org.mineacademy.fo.settings.YamlSectionConfig;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

@Getter
public final class WorldCache extends YamlSectionConfig {

	private static final Map<String, WorldCache> cacheMap = new HashMap<>();

	private static String worldName;

	private String color;
	private String title;
	private String laws;

	/**
	 * Create a new section config with a section prefix,
	 * for example Players for storing player data.
	 */
	protected WorldCache(String worldName) {
		super(worldName);
		this.worldName = worldName;


		loadConfiguration(NO_DEFAULT, "data" + File.separator + "WorldData.yml");
		cacheMap.put(worldName, this);
	}

	@Override
	protected void onLoadFinish() {
		color = getString("Color");
		title = getString("Title");
		laws = getString("Laws");
	}

	public void setColor(String color) {
		this.color = color;
		save("Color", color);
	}

	public void setTitle(String title) {
		this.title = title;
		save("Title", title);
	}

	public void setLaws(String laws) {
		this.laws = laws;
		save("Laws", laws);
	}

	// --------------------------------------------
	// Static methods
	// --------------------------------------------

	public static boolean isAnyWorldRegistered() {
		return !cacheMap.isEmpty();
	}

	public static WorldCache getWorldCache(final String worldName) {
		return cacheMap.get(worldName);
	}

	public static WorldCache getOrCreateWorldCache(final String worldName) {
		return cacheMap.computeIfAbsent(worldName, WorldCache::new);
	}

	public static void addWorld(final String worldName) {
		addWorld(worldName, "&l" + worldName);
	}

	public static void addWorld(final String worldName, final String title) {
		addWorld(worldName, title, Settings.Cities.DEFAULT_COLOR, Settings.Cities.DEFAULT_LAWS);
	}

	public static void addWorld(final String worldName, final String title, final String color) {
		addWorld(worldName, title, color, Settings.Cities.DEFAULT_LAWS);
	}

	public static void addWorld(final String worldName, String title, final String color, final String laws) {
		WorldCache cache = getOrCreateWorldCache(worldName);
		cache.setTitle(title);
		cache.setColor(color);
		cache.setLaws(laws);
	}
}
