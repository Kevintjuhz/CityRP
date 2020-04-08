package nl.kqcreations.cityrp.command.plot;

import nl.kqcreations.cityrp.tool.PlotWand;
import org.bukkit.entity.Player;
import org.mineacademy.fo.command.SimpleCommand;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static nl.kqcreations.cityrp.util.PlotUtil.PLOT_UTIL;

public class PlotWandCommand extends SimpleCommand {
	public PlotWandCommand() {
		super("plotwand|pw");
		setDescription("Get a plotwand to set a plot selection!");
		setUsage("<get|pos1|pos2>");
		setMinArguments(1);
	}

	@Override
	protected void onCommand() {
		checkConsole();

		final Player player = getPlayer();
		final UUID uuid = player.getUniqueId();

		switch (args[0]) {
			case "get":
				player.getInventory().addItem(new PlotWand().getItem());
				tell("&aYou were given a plotwand!");
				break;
			case "pos1":
				PLOT_UTIL.setPlotWandPos(uuid, 1, player.getLocation());
				tell("&aFirst Postion set!");
				break;
			case "pos2":
				PLOT_UTIL.setPlotWandPos(uuid, 2, player.getLocation());
				tell("&aSecond Position set!");
				break;
		}

	}

	@Override
	protected List<String> tabComplete() {
		if (args.length == 1)
			return Arrays.asList("get", "pos1", "pos2");

		return null;
	}
}
