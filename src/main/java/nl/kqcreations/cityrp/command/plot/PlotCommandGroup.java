package nl.kqcreations.cityrp.command.plot;

import org.mineacademy.fo.command.SimpleCommandGroup;

public class PlotCommandGroup extends SimpleCommandGroup {
	@Override
	protected void registerSubcommands() {
		registerSubcommand(new PlotInfoCommand(this));
		registerSubcommand(new PlotAddCommand(this));
		registerSubcommand(new PlotBuyCommand(this));
	}
}
