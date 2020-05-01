package nl.kqcreations.cityrp.listener;

import nl.kqcreations.cityrp.PlayerCityTracker;
import nl.kqcreations.cityrp.data.mongo_data.player.PlayerData;
import nl.kqcreations.cityrp.data.yml_data.WorldData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.mineacademy.fo.Common;

import java.util.UUID;

public class PlayerListener implements Listener {

	@EventHandler
	public void onPlayerLogin(AsyncPlayerPreLoginEvent event) {
		UUID uuid = event.getUniqueId();

		PlayerData.onPlayerLogin(uuid);
	}

	@EventHandler
	public void onPlayerLeave(PlayerQuitEvent event) {
		UUID uuid = event.getPlayer().getUniqueId();

		Common.runLaterAsync(() -> PlayerData.onPlayerLeave(uuid));
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent event) {

		final Player player = event.getPlayer();

		/*
		 *  Checks if the player is op and if there are any cities registered
		 *  if not it will send a message to the op and ask if he want to register a new city
		 * */
		if (player.isOp()) {
			Common.logFramed("Player is op");
			if (!WorldData.isAnyWorldRegistered()) {
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

		PlayerCityTracker.updatePlayerCityTracker(player);
	}
}
