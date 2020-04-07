package nl.kqcreations.cityrp.city;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.ChatColor;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.Valid;
import org.mineacademy.fo.collection.SerializedMap;
import org.mineacademy.fo.model.ConfigSerializable;
import org.mineacademy.fo.settings.YamlSectionConfig;

import java.util.*;

@Getter
public final class CityCache extends YamlSectionConfig {

    private static final Map<String, CityCache> cacheMap = new HashMap<>();

    private final String world;
    private Collection<City> cities = new HashSet<>();

    public CityCache(final String world) {
        super(world);
        this.world = world;

        loadConfiguration(NO_DEFAULT, "data/cities.yml");
        cacheMap.put(world, this);
    }

    /**
     * Returns the city with region __global__
     * if it exists.
     *
     * @param world
     */
    public static City getDefaultCity(final String world) {
        CityCache cache = getCityCache(world);

        for (City city : cache.getCities()) {
            Common.log("Looped city " + city);
            if (city.getWgRegion().equals("__global__")) {
                Common.logFramed("IT HAS A GLOBAL CITY");
                return city;
            }
        }

        return null;
    }

    /**
     * Gets the city cache from the given world,
     * if there is no cache one will be generated!
     *
     * @param world
     */
    public static CityCache getCityCache(final String world) {
        CityCache cache = cacheMap.get(world);

        if (cache == null) {
            cache = new CityCache(world);

            cacheMap.put(world, cache);
        }

        return cache;
    }

    // --------------------------------------------------------------------------------------------------------------
    // Static methods below
    // --------------------------------------------------------------------------------------------------------------

    public static void addCity(final String world, final String name, final String wg_region) {
        addCity(world, name, wg_region, ChatColor.WHITE);
    }

    /**
     * Adds a city to the given world, only when no other city
     * with the same name or same region exists!
     *
     * @param world
     * @param name
     * @param wg_region
     * @param color
     */
    public static void addCity(final String world, final String name, final String wg_region, final ChatColor color) {
        CityCache cache = getCityCache(world);

        if (cache == null) {
            cache = new CityCache(world);
            cacheMap.put(world, cache);
        }

//		boolean notContainsCity = cache.getCities().stream().noneMatch((city) -> city.getWgRegion().equals(wg_region) || city.getName().equals(name));

//		if (notContainsCity) {
        cache.cities.add(new City(name, wg_region, color));
        cache.save("Cities", cache.cities);
//		}
    }

    /**
     * Gets a city by region
     *
     * @param world
     * @param region
     * @return
     */
    public static City getCityByRegion(final String world, final String region) {
        CityCache cache = getCityCache(world);

        for (final City city : cache.getCities())
            if (city.getWgRegion().equals(region)) {
                return city;
            }

        return null;
    }

    /**
     * Gets a city by name
     *
     * @param world
     * @param name
     */
    public static Optional<City> getCityByName(final String world, final String name) {
        CityCache cache = getCityCache(world);

        for (final City city : cache.getCities())
            if (city.getName().equals(name)) {
                return Optional.of(city);
            }


        return Optional.empty();
    }

    @Override
    protected void onLoadFinish() {
        cities = getSetSafe("Cities", City.class);
    }

    /**
     * Returns true if a city exists in a given world
     *
     * @param world
     * @param name
     */
    public boolean CityExists(final String world, final String name) {
        CityCache cache = cacheMap.get(world);
        Valid.checkNotNull(cache, "World named " + name + "does not have any registered cities!");
        return cache.getCities().stream().allMatch((city) -> city.getName().equals(name) || city.getWgRegion().equals(name));
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

        public static City deserialize(final SerializedMap map) {
            final String name = map.getString("Name");
            final String wgRegion = map.getString("WG_Region");
            final ChatColor color = map.get("Color", ChatColor.class);

            return new City(name, wgRegion, color);
        }

        @Override
        public SerializedMap serialize() {
            final SerializedMap map = new SerializedMap();

            map.put("Name", name);
            map.put("WG_Region", wgRegion);
            map.put("Color", color.name());

            return map;
        }

    }

}
