package nl.kqcreations.cityrp.command.bank;

import nl.kqcreations.cityrp.data.mongo_data.bank.BankAccount;
import nl.kqcreations.cityrp.data.mongo_data.bank.BankAccountData;
import nl.kqcreations.cityrp.event.BankAccountCreateEvent;
import nl.kqcreations.cityrp.util.StringUtils;
import org.bukkit.entity.Player;
import org.mineacademy.fo.command.SimpleCommandGroup;
import org.mineacademy.fo.command.SimpleSubCommand;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CreateBankAccountCommand extends SimpleSubCommand {
	protected CreateBankAccountCommand(SimpleCommandGroup parent) {
		super(parent, "create");
		setDescription("Lets you create a new bank account");
		setUsage("<type> <owner> [name]");
		setMinArguments(3);
	}

	@Override
	protected void onCommand() {
		checkConsole();

		BankAccount.AccountType type = null;

		if (args.length > 0) {
			try {
				type = BankAccount.AccountType.valueOf(args[0].toUpperCase());
			} catch (IllegalArgumentException e) {
				tell("&cThat is noy a valid account type");
			}
		}

		final Player target = findPlayer(args[1]);
		final UUID uuid = target.getUniqueId();
		String name = "";

		if (args.length > 2) {
			name = StringUtils.builder(args, 2);

			if (name.length() > 25) {
				tell("&cPlease pick a bank account name not longer then 25 Characters!");
				return;
			}
		}


		BankAccount bankAccount = BankAccountData.createNewBankAccount(name, type != null ? type : BankAccount.AccountType.BUSINESS_ACCOUNT);
		bankAccount.setOwner(uuid);

		tell("&aYou successfully created a bank account with id: "
				+ bankAccount.getAccountId() + " and name: "
				+ bankAccount.getName() + " with owner " + target.getName());

		BankAccountCreateEvent event = new BankAccountCreateEvent(bankAccount, getPlayer());
	}

	@Override
	protected List<String> tabComplete() {
		if (args.length == 1)
			return completeLastWord(BankAccount.AccountType.values());

		return new ArrayList<>();
	}
}
