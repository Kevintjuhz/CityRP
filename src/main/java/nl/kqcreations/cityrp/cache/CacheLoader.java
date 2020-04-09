package nl.kqcreations.cityrp.cache;

import lombok.Getter;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.settings.YamlConfig;

import java.io.File;

public class CacheLoader extends YamlConfig {

    @Getter
    private static final CacheLoader instance = new CacheLoader();

    public void load() {
        loadWorlds();
        loadCities();
    }

    private void loadWorlds() {
        loadConfiguration(NO_DEFAULT, "data" + File.separator + "WorldData.yml");

        Common.log("Started to load worlds");

        for (final String worldName : getMap("").keySet()) {
            Common.log("World: " + worldName);
            WorldCache cache = WorldCache.getOrCreateWorldCache(worldName);
            Common.log(cache.getTitle(), cache.getColor());

        }

        Common.log("Worlds are loaded here is the data");
    }

    private void loadCities() {
        loadConfiguration(NO_DEFAULT, "data" + File.separator + "cities.yml");

        Common.log("Started to load cities");

        for (final String region : getMap("").keySet()) {
            Common.log("CityRegion: " + region);
            CityCache cache = CityCache.getOrCreateCityCache(region);
            Common.log(cache.getTitle(), cache.getColor());
        }

        Common.log("Cities are loaded here is the data");

    }

}
