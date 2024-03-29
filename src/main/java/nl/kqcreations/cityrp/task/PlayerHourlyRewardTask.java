package nl.kqcreations.cityrp.task;

import nl.kqcreations.cityrp.data.mongo_data.bank.BankAccount;
import nl.kqcreations.cityrp.data.mongo_data.bank.transaction.Transaction;
import nl.kqcreations.cityrp.data.mongo_data.bank.transaction.TransactionType;
import nl.kqcreations.cityrp.data.mongo_data.player.PlayerData;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.remain.Remain;

public class PlayerHourlyRewardTask extends BukkitRunnable {

	public static int calculateReward(int level) {
		int baseLoan = 100;
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

			// Generating a transaction for the payment
			Transaction transaction = new Transaction(bankAccount.getAccountId(), reward,
					bankAccount.getBalance(), TransactionType.RECEIVED_STATE_PAYCHECK);
			transaction.generateUUID();
			bankAccount.addTransaction(transaction);

			Common.runLater(() -> Common.tell(player, "&aYou got rewarded your loan of: $" + reward));
//			});
		}
	}
}
