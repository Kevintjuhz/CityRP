package nl.kqcreations.cityrp.command.plot;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import nl.kqcreations.cityrp.data.BankAccount;
import nl.kqcreations.cityrp.data.PlayerData;
import nl.kqcreations.cityrp.util.PlotUtil;
import org.bukkit.entity.Player;
import org.mineacademy.fo.command.SimpleCommandGroup;
import org.mineacademy.fo.command.SimpleSubCommand;

public class PlotBuyCommand extends SimpleSubCommand {
	protected PlotBuyCommand(SimpleCommandGroup parent) {
		super(parent, "buy");
		setDescription("Use this command to buy a plot, it charges your main bank account!");
	}

	@Override
	protected void onCommand() {
		checkConsole();

		final Player player = getPlayer();
		// Is player standing on plot?
		if (!PlotUtil.isAPlot(player.getLocation())) {
			tell(PlotUtil.notOnPlotMessage());
			return;
		}

		final PlotUtil.Plot plot = PlotUtil.getPlot(player.getLocation());
		final ProtectedRegion region = plot.getRegion();

		// Is the region already owned?
		if (!region.getOwners().getPlayers().isEmpty()) {
			tell(PlotUtil.plotAlreadyOwnedMessage());
			return;
		}

		final BankAccount bankAccount = PlayerData.getPlayerData(player.getUniqueId()).getMainAccount();
		// Does player have enough money to buy plot?
		if (bankAccount.getBalance() < plot.getPlotPrice()) {
			tell("&cYou don't have enough money to buy this plot, missing $" + (plot.getPlotPrice() - bankAccount.getBalance()));
			return;
		}

		// Set plot owned
		final boolean success = bankAccount.removeBalance(plot.getPlotPrice());
		if (!success) {
			tell("&cPlot Buying failed, looks like you don't have enough money");
			return;
		}

		region.getOwners().addPlayer(player.getName());
		tell("&aYou bought plot " + plot.getPlotId() + " for $" + plot.getPlotPrice());
		// TODO get certificate of plot ownership
	}
}
