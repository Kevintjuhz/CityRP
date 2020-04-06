package nl.kqcreations.cityrp;

import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.flags.registry.FlagConflictException;
import com.sk89q.worldguard.protection.flags.registry.FlagRegistry;
import nl.kqcreations.cityrp.city.CityCache;
import nl.kqcreations.cityrp.command.city.CityCommandGroup;
import nl.kqcreations.cityrp.event.PlayerListener;
import nl.kqcreations.cityrp.task.CityChangeTask;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.model.HookManager;
import org.mineacademy.fo.plugin.SimplePlugin;

public class CityRPPlugin extends SimplePlugin {

    public static StateFlag CITY_FLAG;
    public static StateFlag PLOT_FLAG;
//	public static StateFlag SUB_PLOT_FLAG;

    private CityChangeTask cityChangeTask;

    @Override
    protected void onPluginStart() {
		if (HookManager.isWorldGuardLoaded()) {

			CITY_FLAG = AddWorldGuardFlag("city-flag");
			PLOT_FLAG = AddWorldGuardFlag("plot-flag");

//		if (!HookManager.isWorldGuardLoaded()) {
//			Common.logFramed("&cCityRP could not be loaded, Please make sure you have WorldGuard installed");
//			Bukkit.getPluginManager().disablePlugin(this);
//			return;
//		}


			cityChangeTask = new CityChangeTask();
			cityChangeTask.runTaskTimer(this, 0, 2 * 20);

			// Loading all the worlds cache
			for (World world : Bukkit.getServer().getWorlds()) {
				CityCache cache = CityCache.getCityCache(world.getName());
			}

			registerCommands("city", new CityCommandGroup());

			registerEvents(new PlayerListener());
		}
	}

        @Override
        protected void onPluginStop () {
            cleanBeforeReload();
        }

        @Override
        protected void onPluginLoad () {
            cleanBeforeReload();
        }

        // For bukkit runnables etc.
        private void cleanBeforeReload () {
            stopTaskIfRunning(cityChangeTask);
        }

        // Call this to stop task if running from within the cleanBeforeReload method
        private void stopTaskIfRunning ( final BukkitRunnable task){
            if (task != null) {
                try {
                    task.cancel();
                } catch (final IllegalStateException ex) {
                }
            }
        }

        /**
         * Adds a worldguard flag to worldguard, can only be called from main plugin class
         *
         * @param flagName
         * @return
         */
        private StateFlag AddWorldGuardFlag (String flagName){
            FlagRegistry registry = WorldGuard.getInstance().getFlagRegistry();

            try {
                // create a flag with the name "my-custom-flag", defaulting to true
                StateFlag flag = new StateFlag(flagName, false);
                registry.register(flag);
                return flag;
            } catch (FlagConflictException e) {
                // some other plugin registered a flag by the same name already.
                // you can use the existing flag, but this may cause conflicts - be sure to check type
                Flag<?> existing = registry.get(flagName);
                if (existing instanceof StateFlag) {
                    return (StateFlag) existing;
                } else {
                    // types don't match - this is bad news! some other plugin conflicts with you
                    // hopefully this never actually happens
                    Common.log("Types don't match, the flag name: " + flagName + " you specified already exists");
                }
            }
            return null;
        }
    }
