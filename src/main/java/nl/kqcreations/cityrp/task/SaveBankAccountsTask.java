package nl.kqcreations.cityrp.task;

import nl.kqcreations.cityrp.data.mongo_data.bank.BankAccountData;
import org.bukkit.scheduler.BukkitRunnable;

public class SaveBankAccountsTask extends BukkitRunnable {
	@Override
	public void run() {
		BankAccountData.saveBankAccounts();
	}
}
