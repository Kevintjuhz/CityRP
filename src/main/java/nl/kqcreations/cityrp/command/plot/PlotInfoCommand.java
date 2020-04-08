package nl.kqcreations.cityrp.command.plot;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import nl.kqcreations.cityrp.cache.CityCache;
import nl.kqcreations.cityrp.settings.Settings;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.command.SimpleCommandGroup;
import org.mineacademy.fo.command.SimpleSubCommand;
import org.mineacademy.fo.model.HookManager;

import java.util.List;

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
//		String worldName = world.getName();
		Location location = player.getLocation();
		String plotRegion = "";

		List<String> hookRegions = HookManager.getRegions(location);

		// Simple check to see if the player is standing in a region
		for (String region : hookRegions) {
			if (CityCache.getCityCache(region) != null)
				continue;

			plotRegion = region;
			break;
		}

		// If player is not standing on a plot return
		if (hookRegions == null || plotRegion == "") {
			tell("&cYou are currently not standing on a plot");
			return;
		}

		RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
		RegionManager regions = container.get(BukkitAdapter.adapt(world));
		assert regions != null;

		ProtectedRegion region = regions.getRegion(plotRegion);
		sendPlotInfoMessage(region);
	}

	private void sendPlotInfoMessage(ProtectedRegion region) {

		String[] message;
		List<String> owners;

		if (region.getOwners().getPlayers().isEmpty()) {
			double price = calculatePlotPrice(region.getMinimumPoint(), region.getMaximumPoint());

			message = new String[]{
					"&3" + Common.chatLineSmooth(),
					" \n",
					"&3Plot information for plot: &b" + region.getId(),
					"&3Plot price: &b$" + price,
					"&3To buy this plot do: &b/plot buy",
					" \n",
					"&3" + Common.chatLineSmooth()
			};
		} else {
			message = new String[]{
					"&3" + Common.chatLineSmooth(),
					" \n",
					"&3Plot information for plot: &b" + region.getId(),
					"&3Owners: &b" + region.getOwners().getPlayers(),
					"&3Members: &b " + region.getMembers().getPlayers(),
					" \n",
					"&3" + Common.chatLineSmooth()
			};
		}

		tell(message);
	}

	private double calculatePlotPrice(BlockVector3 first, BlockVector3 second) {
		int x1 = first.getBlockX();
		int z1 = first.getBlockZ();
		int x2 = second.getBlockX();
		int z2 = second.getBlockZ();

		int area = Math.abs(x1 - x2) * Math.abs(z1 - z2);

		return area * Settings.Plot.DEFAULT_SQUARE_METER_PRICE;
	}
}
