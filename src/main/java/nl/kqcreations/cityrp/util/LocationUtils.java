package nl.kqcreations.cityrp.util;

import org.bukkit.Location;

public class LocationUtils {

    public static Location toBlockLocation(Location location) {
        return new Location(location.getWorld(), location.getBlockX(), location.getBlockY(), location.getBlockZ());
    }

}
