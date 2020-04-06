package nl.kqcreations.cityrp.task;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.flags.registry.FlagRegistry;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import nl.kqcreations.cityrp.CityRPPlugin;
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

	private RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();

	@Override
	public void run() {
		PlayerInteractEvent event;
		for (Player player : Remain.getOnlinePlayers()) {
			UUID uuid = player.getUniqueId();
			World world = player.getWorld();
			Location location = player.getLocation();
			RegionManager regions = container.get(BukkitAdapter.adapt(world));

			RegionQuery query = container.createQuery();
			boolean hasChanged = false;

			PlayerCity playerCity = PlayerCity.getInstance();
			CityCache.City city = playerCity.getPlayerCity(player.getUniqueId());
			FlagRegistry registry = WorldGuard.getInstance().getFlagRegistry();

			if (query.getApplicableRegions(BukkitAdapter.adapt(location)).size() == 0) {
				if (!city.getWgRegion().equals("__global__")) {
					hasChanged = true;
					playerCity.setPlayerCity(uuid, CityCache.getCityByRegion(world.getName(), "__global__"));
					playerCity.sendPlayerCityTitle(player, city);
				}
			} else {
				for (ProtectedRegion region : query.getApplicableRegions(BukkitAdapter.adapt(location))) {
					if (region.getFlag(Flags.fuzzyMatchFlag(registry, CityRPPlugin.CITY_FLAG.getName())).equals(true)) {
						if (!city.getWgRegion().equals(region.getId())) {
							hasChanged = true;
							playerCity.setPlayerCity(uuid, CityCache.getCityByRegion(world.getName(), region.getId()));
							playerCity.sendPlayerCityTitle(player, city);
						}
					}
				}
			}


		}
	}
}
