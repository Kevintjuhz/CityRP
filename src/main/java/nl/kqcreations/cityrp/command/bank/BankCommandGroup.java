package nl.kqcreations.cityrp.command.bank;

import org.mineacademy.fo.command.SimpleCommandGroup;

public class BankCommandGroup extends SimpleCommandGroup {
	@Override
	protected void registerSubcommands() {
		registerSubcommand(new GetBankAccountCommand(this));
		registerSubcommand(new BankCreateAccountCommand(this));
		registerSubcommand(new BankAddUserCommand(this));
		registerSubcommand(new BankSetOwnerCommand(this));
		registerSubcommand(new BankCreateCardCommand(this));
		registerSubcommand(new BankGiveMoneyCommand(this));
	}
}
