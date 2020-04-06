package nl.kqcreations.cityrp.event;

import nl.kqcreations.cityrp.city.CityCache;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.mineacademy.fo.Common;

public class PlayerListener implements Listener {

	@EventHandler
	public void onJoin(PlayerJoinEvent event) {

		final Player player = event.getPlayer();
		final String world = player.getWorld().getName();

		if (player.isOp()) {
			Common.logFramed("Player is op");

			if (CityCache.getCityCache(world) == null || CityCache.getCityCache(world).getCities().size() == 0) {
				Common.logFramed("City Size == 0");
				Common.tell(player, "&3" + Common.chatLineSmooth());
				Common.tell(player, "   ");

				Common.tell(player, "&3You have not yet setup any world to be an cityrp world! do:");
				Common.tell(player, "&b/city add {name} [wg_region] &3- To setup a cit");

				Common.tell(player, "   ");
				Common.tell(player, "&3" + Common.chatLineSmooth());
			}
		}

//		final CityCache cache = CityCache.getCityCache(world);

		// Change this to get if player is in world guard region.
//		if (cache.getCities() != null) {
//			ChatColor color = cache.getColor() != null ? cache.getColor() : ChatColor.WHITE;
//			Remain.sendTitle(player, Common.colorize("&7Welcome in"), Common.colorize(color + cache.getName()));
//			cache.getName().toUpperCase();
//		}

	}

}
