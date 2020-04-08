package nl.kqcreations.cityrp;

import nl.kqcreations.cityrp.cache.CacheLoader;
import nl.kqcreations.cityrp.command.AddWorldCommand;
import nl.kqcreations.cityrp.command.city.CityCommandGroup;
import nl.kqcreations.cityrp.command.plot.PlotCommandGroup;
import nl.kqcreations.cityrp.command.plot.PlotWandCommand;
import nl.kqcreations.cityrp.event.PlayerListener;
import nl.kqcreations.cityrp.settings.Settings;
import nl.kqcreations.cityrp.task.CityChangeTask;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.model.HookManager;
import org.mineacademy.fo.plugin.SimplePlugin;
import org.mineacademy.fo.settings.YamlStaticConfig;

import java.util.Arrays;
import java.util.List;

public class CityRPPlugin extends SimplePlugin {

	private CityChangeTask cityChangeTask;

	@Override
	protected void onPluginStart() {
		if (!HookManager.isWorldGuardLoaded()) {
			Common.logFramed("&cCityRP could not be loaded, Please make sure you have WorldGuard installed");
			Bukkit.getPluginManager().disablePlugin(this);
			return;
		}

		CacheLoader.getInstance().load();

		cityChangeTask = new CityChangeTask();
		cityChangeTask.runTaskTimer(this, 0, 2 * 20);

		registerCommand(new AddWorldCommand());
		registerCommand(new PlotWandCommand());

		registerCommands("plot", new PlotCommandGroup());
		registerCommands("city", new CityCommandGroup());

		registerEvents(new PlayerListener());
	}

	@Override
	protected void onPluginStop() {
		cleanBeforeReload();
	}

	@Override
	protected void onPluginLoad() {
		cleanBeforeReload();
	}

	// For bukkit runnables etc.
	private void cleanBeforeReload() {
		stopTaskIfRunning(cityChangeTask);
	}

	// Call this to stop task if running from within the cleanBeforeReload method
	private void stopTaskIfRunning(final BukkitRunnable task) {
		if (task != null) {
			try {
				task.cancel();
			} catch (final IllegalStateException ex) {
			}
		}
	}

	@Override
	public List<Class<? extends YamlStaticConfig>> getSettings() {
		return Arrays.asList(Settings.class);
	}
}
