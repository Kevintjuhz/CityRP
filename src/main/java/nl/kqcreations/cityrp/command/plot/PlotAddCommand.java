package nl.kqcreations.cityrp.command.plot;

import org.bukkit.entity.Player;
import org.mineacademy.fo.command.SimpleCommandGroup;
import org.mineacademy.fo.command.SimpleSubCommand;

import static nl.kqcreations.cityrp.util.PlotUtil.PLOT_UTIL;

public class PlotAddCommand extends SimpleSubCommand {
	protected PlotAddCommand(SimpleCommandGroup parent) {
		super(parent, "add|a");
		setMinArguments(1);
		setUsage("<name>");
		setDescription("Creates a new plot with the given selection you set with the plotwand");
	}

	@Override
	protected void onCommand() {
		checkConsole();

		final Player player = getPlayer();
		PLOT_UTIL.addPlot(player, args[0]);
	}
}
