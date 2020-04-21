package nl.kqcreations.cityrp.command.plot;

import nl.kqcreations.cityrp.util.PlotUtil;
import org.bukkit.entity.Player;
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
		checkConsole();

		final Player player = getPlayer();
		PlotUtil.addPlot(player, args[0]);
		tell("&aSuccessfully added plot " + args[0]);
	}
}
