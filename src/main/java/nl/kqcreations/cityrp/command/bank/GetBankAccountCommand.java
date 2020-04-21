package nl.kqcreations.cityrp.command.bank;

import nl.kqcreations.cityrp.data.BankAccount;
import nl.kqcreations.cityrp.data.BankAccountData;
import org.bukkit.entity.Player;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.Valid;
import org.mineacademy.fo.command.SimpleCommandGroup;
import org.mineacademy.fo.command.SimpleSubCommand;
import org.mineacademy.fo.remain.Remain;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class GetBankAccountCommand extends SimpleSubCommand {
	protected GetBankAccountCommand(SimpleCommandGroup parent) {
		super(parent, "get|g");
		setUsage("<id>");
		setDescription("Get some bankaccount info by their id");
		setMinArguments(1);
	}

	@Override
	protected void onCommand() {
		checkConsole();

		final String id = args[0];
		final boolean isInt = Valid.isInteger(id);
		if (!isInt) {
			tell("&cPlease specify a valid number!");
			return;
		}

		final BankAccount bankAccount = BankAccountData.getBankAccount(Integer.parseInt(id));
		if (bankAccount == null) {
			tell("&cBank account not found");
			return;
		}

		List<String> usersInfo = new ArrayList<>();
		for (UUID uuid : bankAccount.getUsers().keySet()) {
			final Player player = Remain.getPlayerByUUID(uuid);
			final String name = player.getName();
			final BankAccount.AccessLevel accessLevel = bankAccount.getUserAccessLevel(uuid);

			usersInfo.add("&3" + name + ": &b" + accessLevel);
		}

		tell(
				"&3" + Common.chatLineSmooth(),
				"&3Bankaccount information for account &b" + id,
				"&3Name: &b" + bankAccount.getName(),
				"&3Balance: &b$" + bankAccount.getBalance(),
				"&3Type: &b" + bankAccount.getType(),
				"&3Users: &b" + usersInfo.toString(),
				"&3" + Common.chatLineSmooth()
		);
	}
}
