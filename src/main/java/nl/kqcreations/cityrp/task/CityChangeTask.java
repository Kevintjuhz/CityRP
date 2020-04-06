package nl.kqcreations.cityrp.task;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import nl.kqcreations.cityrp.city.CityCache;
import nl.kqcreations.cityrp.city.PlayerCity;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.mineacademy.fo.remain.Remain;

import java.util.UUID;

public class CityChangeTask extends BukkitRunnable {

	@Override
	public void run() {

		RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();

		PlayerInteractEvent event;
		for (Player player : Remain.getOnlinePlayers()) {
			final UUID uuid = player.getUniqueId();
			final World world = player.getWorld();
			final Location location = player.getLocation();

			final RegionManager regions = container.get(BukkitAdapter.adapt(world));
			final RegionQuery query = container.createQuery();

			final PlayerCity playerCity = PlayerCity.getInstance();
			final CityCache.City city = playerCity.getPlayerCity(player.getUniqueId());

			if (query.getApplicableRegions(BukkitAdapter.adapt(location)).size() == 0) {
				if (!city.getWgRegion().equals("__global__")) {
					CityCache.City city2 = CityCache.getCityByRegion(world.getName(), "__global__");
					playerCity.setPlayerCity(uuid, city2);
					playerCity.sendPlayerCityTitle(player, city2);
				}
			} else {
				for (ProtectedRegion region : query.getApplicableRegions(BukkitAdapter.adapt(location))) {

					CityCache.City city2 = CityCache.getCityByRegion(world.getName(), region.getId());

					if (!city.getWgRegion().equals(region.getId()) && city2 != null) {

						playerCity.setPlayerCity(uuid, city2);
						playerCity.sendPlayerCityTitle(player, city2);
					}
				}
			}


		}
	}
}
