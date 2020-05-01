package nl.kqcreations.cityrp.command.city;

import nl.kqcreations.cityrp.data.yml_data.CityData;
import nl.kqcreations.cityrp.data.yml_data.WorldData;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.mineacademy.fo.command.SimpleCommandGroup;
import org.mineacademy.fo.command.SimpleSubCommand;
import org.mineacademy.fo.model.HookManager;

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

		if (WorldData.getWorldCache(worldName) == null) {
			tell("&cThis world is not registered as a cityrp world",
					"&cType /addworld to add this world");
			return;
		}

		final String cityName = args[0].toLowerCase();
		final Location location = player.getLocation();

		if (HookManager.getRegions(location) == null) {
			tell("&cYou are not standing in any region");
			return;
		}

		CityData.addCity(HookManager.getRegions(location).get(0), cityName);
		tell("&bSuccessfully added the " + cityName.toUpperCase());
	}
}
