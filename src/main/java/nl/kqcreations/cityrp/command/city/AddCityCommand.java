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

		final CityCache cache = CityCache.getCityCache(world);
		if (cache.getName() != null) {
			tell("&cA city in the world " + worldName + " already exists. Only one city per world can exist");
			return;
		}

		CityCache.addCity(world, cityName);
		tell("&bSuccessfully added the " + cityName.toUpperCase());
	}
}
