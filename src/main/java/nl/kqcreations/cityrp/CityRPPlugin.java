package nl.kqcreations.cityrp;

import nl.kqcreations.cityrp.city.CityCache;
import nl.kqcreations.cityrp.command.city.CityCommandGroup;
import nl.kqcreations.cityrp.event.PlayerListener;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.model.HookManager;
import org.mineacademy.fo.plugin.SimplePlugin;

public class CityRPPlugin extends SimplePlugin {

	@Override
	protected void onPluginStart() {

		if (HookManager.isWorldGuardLoaded()) {

			registerCommands("city", new CityCommandGroup());

			// Loading all the worlds cache
			for (World world : Bukkit.getServer().getWorlds()) {
				CityCache cache = CityCache.getCityCache(world.getName());
			}

			registerEvents(new PlayerListener());
			Common.log("THIS");

		} else {
			Common.logFramed("&cCityRP could not be loaded, Please make sure you have WorldGuard installed");
			Bukkit.getPluginManager().disablePlugin(this);
		}
	}
}
