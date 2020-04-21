package nl.kqcreations.cityrp.command.plot;

import nl.kqcreations.cityrp.util.PlotUtil;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.mineacademy.fo.command.SimpleCommandGroup;
import org.mineacademy.fo.command.SimpleSubCommand;

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

		String[] message = PlotUtil.getPlotInfoMessage(location);

		tell(message);
	}
}
