package nl.kqcreations.cityrp.command.cityrp;

import nl.kqcreations.cityrp.data.BankAccountData;
import org.mineacademy.fo.command.SimpleCommandGroup;
import org.mineacademy.fo.command.SimpleSubCommand;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CityRPSaveCommand extends SimpleSubCommand {
	protected CityRPSaveCommand(SimpleCommandGroup parent) {
		super(parent, "save");
		setDescription("Gives you the ability to save all data that needs to be saved");
	}

	@Override
	protected void onCommand() {
		final String param = args.length > 0 ? args[0].toLowerCase() : "";

		if ("bankdata".equals(param)) {
			BankAccountData.saveBankAccounts();
			tell("&aSaved all the bankaccounts to the database");
		}

		// TODO Save all data if no param is specified
	}

	@Override
	protected List<String> tabComplete() {
		if (args.length == 1)
			return Arrays.asList("all", "playerdata", "bankdata");

		return new ArrayList<>();
	}
}
