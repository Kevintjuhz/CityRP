package nl.kqcreations.cityrp.command.bank;

import nl.kqcreations.cityrp.data.bank.BankAccount;
import nl.kqcreations.cityrp.data.bank.BankAccountData;
import nl.kqcreations.cityrp.event.BankAccountCreateEvent;
import org.mineacademy.fo.command.SimpleCommandGroup;
import org.mineacademy.fo.command.SimpleSubCommand;

import java.util.ArrayList;
import java.util.List;

public class CreateBankCommand extends SimpleSubCommand {
	protected CreateBankCommand(SimpleCommandGroup parent) {
		super(parent, "create");
		setDescription("Lets you create a new bank account");
		setUsage("[name] [type]");
	}

	@Override
	protected void onCommand() {
		checkConsole();

		BankAccount.AccountType type = null;

		if (args.length > 0) {
			type = BankAccount.AccountType.valueOf(args[0].toUpperCase());
		}

		String name = args.length > 1 ? args[1] : "";

		if (name.length() > 10) {
			tell("&cPlease pick a bank account name not longer then 10!");
			return;
		}

		BankAccount bankAccount = BankAccountData.createNewBankAccount(name, type != null ? type : BankAccount.AccountType.BUSINESS_ACCOUNT);
		tell("&aYou successfully created a bank account with id: " + bankAccount.getAccountId() + " and name: " + bankAccount.getName());

		BankAccountCreateEvent event = new BankAccountCreateEvent(bankAccount, getPlayer());
	}

	@Override
	protected List<String> tabComplete() {
		if (args.length == 1)
			return completeLastWord(BankAccount.AccountType.values());

		return new ArrayList<>();
	}
}
