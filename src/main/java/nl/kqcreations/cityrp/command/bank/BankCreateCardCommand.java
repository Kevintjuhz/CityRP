package nl.kqcreations.cityrp.command.bank;

import nl.kqcreations.cityrp.data.mongo_data.bank.BankAccount;
import nl.kqcreations.cityrp.data.mongo_data.bank.BankAccountData;
import nl.kqcreations.cityrp.data.mongo_data.bank.card.Card;
import org.bukkit.entity.Player;
import org.mineacademy.fo.PlayerUtil;
import org.mineacademy.fo.Valid;
import org.mineacademy.fo.command.SimpleCommandGroup;
import org.mineacademy.fo.command.SimpleSubCommand;

public class BankCreateCardCommand extends SimpleSubCommand {
	protected BankCreateCardCommand(SimpleCommandGroup parent) {
		super(parent, "createcard|cc");
		setDescription("Creates a new Debit/Credit Card for the given bank account");
		setMinArguments(1);
		setUsage("<bankaccount-id>");
	}

	@Override
	protected void onCommand() {
		checkConsole();
		final Player player = getPlayer();

		if (!Valid.isInteger(args[0])) {
			tell("&cPlease specify a valid id");
			return;
		}

		final BankAccount bankAccount = BankAccountData.getBankAccount(Integer.parseInt(args[0]));

		if (bankAccount == null) {
			tell("&cCould not find a bank account with id: " + args[0]);
			return;
		}

		Card card = new Card(bankAccount);
		PlayerUtil.addItems(player.getInventory(), card.createItem());
	}
}
