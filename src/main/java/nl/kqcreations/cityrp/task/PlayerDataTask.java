package nl.kqcreations.cityrp.task;

import nl.kqcreations.cityrp.data.mongo_data.player.PlayerData;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.mineacademy.fo.remain.Remain;

public class PlayerDataTask extends BukkitRunnable {
	@Override
	public void run() {
		for (final Player player : Remain.getOnlinePlayers()) {
			final PlayerData data = PlayerData.getPlayerData(player.getUniqueId());

			Remain.sendActionBar(player,
					"&2Level: &a" + data.getLevel()
							+ "   &2Job: &a" + data.getJob()
			);
		}
	}
}
