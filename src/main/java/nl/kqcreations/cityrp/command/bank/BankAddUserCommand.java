package nl.kqcreations.cityrp.command.bank;

import nl.kqcreations.cityrp.data.BankAccount;
import nl.kqcreations.cityrp.data.BankAccountData;
import nl.kqcreations.cityrp.data.PlayerData;
import nl.kqcreations.cityrp.event.BankAccountAddUserEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.mineacademy.fo.Valid;
import org.mineacademy.fo.command.SimpleCommandGroup;
import org.mineacademy.fo.command.SimpleSubCommand;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class BankAddUserCommand extends SimpleSubCommand {
	protected BankAddUserCommand(SimpleCommandGroup parent) {
		super(parent, "adduser");
		setDescription("Adds a user to the given bankaccount");
		setMinArguments(2);
		setUsage("<id> <player> [access-level]");
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

		bankAccount.addUser(uuid);
		if (args.length > 2) {
			try {
				BankAccount.AccessLevel level = BankAccount.AccessLevel.valueOf(args[2]);
				bankAccount.setUserAccessLevel(uuid, level);
			} catch (IllegalArgumentException e) {
				tell("&cThat is not a valid AccessLevel, Types: " + Arrays.toString(BankAccount.AccessLevel.values()));
			}
		}

		final PlayerData data = PlayerData.getPlayerData(uuid);
		boolean success = data.addBankAccount(bankAccount);

		if (!success) {
			tell("&cThis player already has access to that account");
			return;
		}

		tell("&aSuccessfully added player " + target.getName() + " to account " + bankAccount.getName());


		BankAccountAddUserEvent event = new BankAccountAddUserEvent(bankAccount, uuid);
		Bukkit.getServer().getPluginManager().callEvent(event);
	}

	@Override
	protected List<String> tabComplete() {
		if (args.length == 3) {
			return completeLastWord(BankAccount.AccessLevel.values());
		}

		return new ArrayList<>();
	}
}
