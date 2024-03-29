package nl.kqcreations.cityrp.command.plot;

import nl.kqcreations.cityrp.tool.PlotWand;
import nl.kqcreations.cityrp.util.SelectionUtil;
import org.bukkit.entity.Player;
import org.mineacademy.fo.command.SimpleCommand;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

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
				tell("&bYou were given a plotwand!");
				break;
			case "pos1":
				SelectionUtil.setSelectionPos(uuid, 1, player.getLocation());
				tell("&bFirst Postion set!");
				break;
			case "pos2":
				SelectionUtil.setSelectionPos(uuid, 2, player.getLocation());
				tell("&bSecond Position set!");
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
