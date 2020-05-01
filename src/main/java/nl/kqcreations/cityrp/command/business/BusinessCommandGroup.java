package nl.kqcreations.cityrp.command.business;

import org.mineacademy.fo.command.SimpleCommandGroup;

public class BusinessCommandGroup extends SimpleCommandGroup {
	@Override
	protected void registerSubcommands() {
		registerSubcommand(new BusinessCreateCommand(this));
	}
}
