package nl.kqcreations.cityrp.command.bank;

import nl.kqcreations.cityrp.data.mongo_data.PlayerData;
import nl.kqcreations.cityrp.data.mongo_data.bank.BankAccount;
import nl.kqcreations.cityrp.data.mongo_data.bank.BankAccountData;
import nl.kqcreations.cityrp.event.BankAccountAddUserEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.mineacademy.fo.Valid;
import org.mineacademy.fo.command.SimpleCommandGroup;
import org.mineacademy.fo.command.SimpleSubCommand;

import java.util.UUID;

public class BankSetOwnerCommand extends SimpleSubCommand {
	protected BankSetOwnerCommand(SimpleCommandGroup parent) {
		super(parent, "setowner");
		setMinArguments(2);
		setUsage("<id> <player>");
	}

	@Override
	protected void onCommand() {
		checkConsole();

		if (!Valid.isInteger(args[0])) {
			tell("&cPlease specify a valid id");
			return;
		}

		final BankAccount bankAccount = BankAccountData.getBankAccount(Integer.parseInt(args[0]));

		if (bankAccount == null) {
			tell("&cCould not find a bank account with id: " + args[0]);
			return;
		}

		final Player target = findPlayer(args[1]);
		final UUID uuid = target.getUniqueId();

		bankAccount.setOwner(uuid);

		final PlayerData data = PlayerData.getPlayerData(uuid);
		data.addBankAccount(bankAccount);


		tell("&aSuccessfully added player " + target.getName() + " to account " + bankAccount.getName());


		BankAccountAddUserEvent event = new BankAccountAddUserEvent(bankAccount, uuid);
		Bukkit.getServer().getPluginManager().callEvent(event);
	}
}
