package nl.kqcreations.cityrp.command.plot;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.mineacademy.fo.command.SimpleCommandGroup;
import org.mineacademy.fo.command.SimpleSubCommand;

import static nl.kqcreations.cityrp.util.PlotUtil.PLOT_UTIL;

public class PlotInfoCommand extends SimpleSubCommand {
	protected PlotInfoCommand(SimpleCommandGroup parent) {
		super(parent, "info|i");
		setMinArguments(0);
		setDescription("Returns plot info on the plot the player is standing on");
	}

	@Override
	protected void onCommand() {
		checkConsole();

		Player player = getPlayer();
		World world = player.getWorld();
		Location location = player.getLocation();

		String[] message = PLOT_UTIL.getPlotInfoMessage(location);

		tell(message);
	}
}
