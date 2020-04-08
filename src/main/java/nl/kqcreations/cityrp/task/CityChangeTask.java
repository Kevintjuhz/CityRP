package nl.kqcreations.cityrp.task;

import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.mineacademy.fo.remain.Remain;

import static nl.kqcreations.cityrp.PlayerCityTracker.INSTANCE;

public class CityChangeTask extends BukkitRunnable {

	@Override
	public void run() {

		RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();

		for (Player player : Remain.getOnlinePlayers()) {
			INSTANCE.updatePlayerCityTracker(player);
		}
	}
}
