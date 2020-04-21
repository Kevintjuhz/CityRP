package nl.kqcreations.cityrp;

import nl.kqcreations.cityrp.command.LawsCommand;
import nl.kqcreations.cityrp.command.SetLevelCommand;
import nl.kqcreations.cityrp.command.WorldAddCommand;
import nl.kqcreations.cityrp.command.bank.BankCommandGroup;
import nl.kqcreations.cityrp.command.city.CityCommandGroup;
import nl.kqcreations.cityrp.command.cityrp.CityRPCommandGroup;
import nl.kqcreations.cityrp.command.plot.PlotCommandGroup;
import nl.kqcreations.cityrp.command.plot.PlotWandCommand;
import nl.kqcreations.cityrp.data.BankAccountData;
import nl.kqcreations.cityrp.data.DataLoader;
import nl.kqcreations.cityrp.data.MongoConnector;
import nl.kqcreations.cityrp.listener.BlockListener;
import nl.kqcreations.cityrp.listener.CraftListener;
import nl.kqcreations.cityrp.listener.PlayerListener;
import nl.kqcreations.cityrp.settings.Settings;
import nl.kqcreations.cityrp.task.CityChangeTask;
import nl.kqcreations.cityrp.task.PlayerDataTask;
import nl.kqcreations.cityrp.task.PlayerHourlyRewardTask;
import nl.kqcreations.cityrp.task.SaveBankAccountsTask;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.model.HookManager;
import org.mineacademy.fo.plugin.SimplePlugin;
import org.mineacademy.fo.settings.YamlStaticConfig;

import java.util.Arrays;
import java.util.List;

public class CityRPPlugin extends SimplePlugin {

	private static CityRPPlugin instance;

	private CityChangeTask cityChangeTask;
	private PlayerDataTask playerDataTask;
	private SaveBankAccountsTask saveBankAccountsTask;
	private PlayerHourlyRewardTask playerHourlyRewardTask;

	@Override
	protected void onPluginStart() {
		instance = this;

		if (!HookManager.isWorldGuardLoaded()) {
			Common.logFramed("&cCityRP could not be loaded, Please make sure you have WorldGuard installed");
			Bukkit.getPluginManager().disablePlugin(this);
			return;
		}

		if (HookManager.isPlaceholderAPILoaded()) {
			new CityRpExpansion(this).register();
		}

		MongoConnector.getInstance().connect();
		DataLoader.getInstance().load();

		cityChangeTask = new CityChangeTask();
		cityChangeTask.runTaskTimer(this, 0, 2 * 20);

		playerDataTask = new PlayerDataTask();
		playerDataTask.runTaskTimer(this, 0, 1 * 20);

		saveBankAccountsTask = new SaveBankAccountsTask();
		saveBankAccountsTask.runTaskTimerAsynchronously(this, 0, (60 * 20) * 5);

		playerHourlyRewardTask = new PlayerHourlyRewardTask();
		playerHourlyRewardTask.runTaskTimer(this, 0, (60 * 20) * 60);

		registerCommand(new WorldAddCommand());
		registerCommand(new PlotWandCommand());
		registerCommand(new SetLevelCommand());
		registerCommand(new LawsCommand());

		registerCommands("plot", new PlotCommandGroup());
		registerCommands("city", new CityCommandGroup());
		registerCommands("cityrp", new CityRPCommandGroup());
		registerCommands("bank", new BankCommandGroup());

		registerEvents(new PlayerListener());
		registerEvents(new BlockListener());
		registerEvents(new CraftListener());
	}


	@Override
	protected void onPluginStop() {
		Common.log("HERE");
		BankAccountData.saveBankAccounts();
		cleanBeforeReload();
	}

	@Override
	protected void onPluginLoad() {
		cleanBeforeReload();
//		BankAccountData.saveBankAccounts();
	}

	// For bukkit runnables etc.
	private void cleanBeforeReload() {
		stopTaskIfRunning(cityChangeTask);
		stopTaskIfRunning(playerDataTask);
		stopTaskIfRunning(saveBankAccountsTask);
		stopTaskIfRunning(playerHourlyRewardTask);
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

	public static CityRPPlugin getInstance() {
		return instance;
	}
}
