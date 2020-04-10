package nl.kqcreations.cityrp.event;

import nl.kqcreations.cityrp.cache.WorldCache;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.mineacademy.fo.Common;

import static nl.kqcreations.cityrp.PlayerCityTracker.CITY_TRACKER;

public class PlayerListener implements Listener {

	@EventHandler
	public void onJoin(PlayerJoinEvent event) {

		final Player player = event.getPlayer();

		/*
		 *  Checks if the player is op and if there are any cities registered
		 *  if not it will send a message to the op and ask if he want to register a new city
		 * */
		if (player.isOp()) {
			Common.logFramed("Player is op");
			if (!WorldCache.isAnyWorldRegistered()) {
				Common.tellLater(30, player,
						"&3" + Common.chatLineSmooth(),
						"   ",
						"&3You have not yet setup any world to be an cityrp world! do:",
						"&b/worldadd &3- To setup this world",
						"   ",
						"&3" + Common.chatLineSmooth()
				);
			}
		}

		CITY_TRACKER.updatePlayerCityTracker(player);

	}

}
