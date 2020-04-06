package nl.kqcreations.cityrp.event;

import nl.kqcreations.cityrp.city.CityCache;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.remain.Remain;

public class PlayerListener implements Listener {

	@EventHandler
	public void onJoin(PlayerJoinEvent event) {

		Player player = event.getPlayer();
		CityCache cache = CityCache.getCityCache(player.getWorld());

		if (cache.getName() != null) {
			ChatColor color = cache.getColor() != null ? cache.getColor() : ChatColor.WHITE;
			Remain.sendTitle(player, Common.colorize("&7Welcome in"), Common.colorize(color + cache.getName()));
			cache.getName().toUpperCase();
		}

		if (player.isOp()) {
			if (CityCache.getCities().size() == 0) {
				Common.tell(player, "&3" + Common.chatLineSmooth());
				Common.tell(player, "   ");

				Common.tell(player, "&3You have not yet setup any world to be an cityrp world! do:");
				Common.tell(player, "&b/city add {name} [wg_region] &3- To setup a cit");

				Common.tell(player, "   ");
				Common.tell(player, "&3" + Common.chatLineSmooth());
			}
		}

	}

}
