package nl.kqcreations.cityrp.command.city;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import nl.kqcreations.cityrp.city.CityCache;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.mineacademy.fo.command.SimpleCommandGroup;
import org.mineacademy.fo.command.SimpleSubCommand;

public class AddCityCommand extends SimpleSubCommand {

    protected AddCityCommand(SimpleCommandGroup parent) {
        super(parent, "add");

        setUsage("<name> [wg_region]");
        setMinArguments(1);
    }

    @Override
    protected void onCommand() {
        checkConsole();

        final Player player = getPlayer();
        final World world = player.getWorld();
        final String worldName = world.getName();

        final String cityName = args[0].toLowerCase();
        final String regionName;

        if (args.length > 1)
            regionName = args[1];
        else
            regionName = "__global__";

        final CityCache cache = CityCache.getCityCache(worldName);
        if (cache.CityExists(worldName, cityName)) {
            tell("&cA city with the name " + cityName + "or region name " + regionName + " already exists!");
            return;
        }

        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionManager regions = container.get(BukkitAdapter.adapt(world));
        ProtectedRegion region = regions.getRegion(regionName);
        if (region == null) {
            tell("&cThe region " + regionName + " you specified doesn't exist");
            return;
        }

        CityCache.addCity(worldName, cityName, regionName);
        tell("&bSuccessfully added the " + cityName.toUpperCase());
    }
}
