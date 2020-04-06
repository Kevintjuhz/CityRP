package nl.kqcreations.cityrp.command.city;

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

		CityCache.addCity(worldName, cityName, regionName);
		tell("&bSuccessfully added the " + cityName.toUpperCase());
	}
}
