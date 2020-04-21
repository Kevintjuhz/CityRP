package nl.kqcreations.cityrp.task;

import nl.kqcreations.cityrp.data.BankAccount;
import nl.kqcreations.cityrp.data.PlayerData;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.remain.Remain;

public class PlayerHourlyRewardTask extends BukkitRunnable {
	private static int baseLoan = 100;

	public static int calculateReward(int level) {
		return (int) (baseLoan * Math.pow(level, 2));
	}

	@Override
	public void run() {
		for (Player player : Remain.getOnlinePlayers()) {
//			Common.runLaterAsync(() -> {
			PlayerData data = PlayerData.getPlayerData(player.getUniqueId());
			BankAccount bankAccount = data.getMainAccount();
			int reward = calculateReward(data.getLevel());
			bankAccount.addBalance(reward);
			Common.runLater(() -> Common.tell(player, "&aYou got rewarded your loan of: $" + reward));
//			});
		}
	}
}
