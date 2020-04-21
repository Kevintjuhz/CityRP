package nl.kqcreations.cityrp.command.cityrp;

import org.mineacademy.fo.command.SimpleCommandGroup;

public class CityRPCommandGroup extends SimpleCommandGroup {
	@Override
	protected void registerSubcommands() {
		registerSubcommand(new CityRPSaveCommand(this));
	}
}
