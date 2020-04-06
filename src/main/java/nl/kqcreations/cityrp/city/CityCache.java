package nl.kqcreations.cityrp.city;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.mineacademy.fo.Valid;
import org.mineacademy.fo.collection.SerializedMap;
import org.mineacademy.fo.model.ConfigSerializable;
import org.mineacademy.fo.settings.YamlSectionConfig;

import java.util.*;

@Getter
public class CityCache extends YamlSectionConfig {

    private static final Map<World, CityCache> cacheMap = new HashMap<>();

    private final World world;
    private Collection<City> cities = new HashSet<>();


    public CityCache(final World world) {
        super(world.getName());
        this.world = world;

        loadConfiguration(NO_DEFAULT, "data/cities.yml");
        cacheMap.put(world, this);
    }

    @Override
    protected void onLoadFinish() {
        cities = getSetSafe("Cities", City.class);
    }

    public Collection<City> getCities() {
    	return new HashSet<>(cities);
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

    public static void addCity(final World world, final String name, final String wg_region) {
        addCity(world, name, wg_region, ChatColor.WHITE);
    }

    public static void addCity(final World world, final String name, final String wg_region, final ChatColor color) {
        CityCache cache = cacheMap.get(world);

        if (cache == null) {
            cache = new CityCache(world);
            cacheMap.put(world, cache);
        }

        cache.cities.add(new City(name, wg_region, color));
    }

    public static CityCache getCityCache(final World world) {
        CityCache cache = cacheMap.get(world);

//		if (cache == null) {
//			cache = new CityCache(world);
//
//			cacheMap.put(world, cache);
//		}

        return cache;
    }

    public static Optional<City> getCityByName(final World world, final String name) {
        CityCache cache = cacheMap.get(world);

        for (final City city : cache.getCities())
            if (city.getName().equals(name)) {
                return Optional.of(city);
            }


        return Optional.empty();
    }

    public static Collection<City> getCities(World world) {
        CityCache cache = getCityCache(world);
        Valid.checkNotNull(cache, "This world is not valid");

        return cache.getCities();
    }

    // --------------------------------------------------------------------------------------------------------------
    // Own classes
    // --------------------------------------------------------------------------------------------------------------

    @Getter
    @AllArgsConstructor
    public final static class City implements ConfigSerializable {

        private String name;
        private String wg_region;
        private ChatColor color;

        @Override
        public SerializedMap serialize() {
            final SerializedMap map = new SerializedMap();

            map.put("Name", name);
            map.put("WG_Region", wg_region);
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
