package nl.kqcreations.cityrp.command.plot;

import org.mineacademy.fo.command.SimpleCommandGroup;
import org.mineacademy.fo.command.SimpleSubCommand;

public class PlotAddCommand extends SimpleSubCommand {
	protected PlotAddCommand(SimpleCommandGroup parent) {
		super(parent, "add|a");
		setMinArguments(1);
		setUsage("<name>");
		setDescription("Creates a new plot with the given selection you set with the plotwand");
	}

	@Override
	protected void onCommand() {
//		ProtectedRegion region1 = new ProtectedCuboidRegion()
	}
}
