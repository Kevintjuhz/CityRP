package nl.kqcreations.cityrp.util;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.math.Vector3;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.flags.RegionGroup;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import nl.kqcreations.cityrp.cache.CityCache;
import nl.kqcreations.cityrp.settings.Settings;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.model.HookManager;

import java.util.List;
import java.util.UUID;

import static nl.kqcreations.cityrp.util.SelectionUtil.SELECTION_UTIL;

public enum PlotUtil {

	PLOT_UTIL;

	// ===========================================================================
	// Plot Methods
	// ===========================================================================

	private double calculatePlotPrice(BlockVector3 first, BlockVector3 second) {
		int x1 = first.getBlockX();
		int z1 = first.getBlockZ();
		int x2 = second.getBlockX();
		int z2 = second.getBlockZ();

		int area = Math.abs(x1 - x2) * Math.abs(z1 - z2);

		return area * Settings.Plot.DEFAULT_SQUARE_METER_PRICE;
	}

	public String addPlot(final Player player, String name) {

		SelectionUtil util = SELECTION_UTIL;

		UUID uuid = player.getUniqueId();

		if (!util.isSelectionSet(uuid)) {
			return "&cPlease select two positions before creating a plot!";
		}

		SelectionUtil.Selection selection = util.getSelection(uuid);

		if (isAPlot(player.getWorld(), selection.getPlotWandPos1().toVector3()) || isAPlot(player.getWorld(), selection.getPlotWandPos2().toVector3())) {
			return "&cA plot on your location already exists";
		}

		RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
		RegionManager regions = container.get(BukkitAdapter.adapt(player.getWorld()));

		ProtectedRegion region = new ProtectedCuboidRegion(name, selection.getPlotWandPos1(), selection.getPlotWandPos2());
		region.setFlag(Flags.PVP, StateFlag.State.ALLOW);

		region.setFlag(Flags.USE, StateFlag.State.DENY);
		region.setFlag(Flags.USE.getRegionGroupFlag(), RegionGroup.NON_MEMBERS);

		region.setFlag(Flags.INTERACT, StateFlag.State.DENY);
		region.setFlag(Flags.INTERACT.getRegionGroupFlag(), RegionGroup.NON_MEMBERS);

		region.setFlag(Flags.CHEST_ACCESS, StateFlag.State.DENY);
		region.setFlag(Flags.CHEST_ACCESS.getRegionGroupFlag(), RegionGroup.NON_MEMBERS);

		regions.addRegion(region);

		return "&3Plot named " + name + " was successfully created";
	}

	public boolean isAPlot(World world, Vector3 vector3) {
		Location location = new Location(world, vector3.getX(), vector3.getY(), vector3.getZ());
		List<String> hookRegions = HookManager.getRegions(location);

		// Simple check to see if the player is standing in a region
		for (String region : hookRegions) {
			if (CityCache.getCityCache(region) != null)
				continue;

			return true;
		}

		return false;
	}

	public String[] getPlotInfoMessage(Location location) {

		String plot = getPlot(location);

		// If player is not standing on a plot return
		if (plot == null) {
			return new String[]{"&cYou are currently not standing on a plot"};
		}

		RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
		RegionManager regions = container.get(BukkitAdapter.adapt(location.getWorld()));
		assert regions != null;

		ProtectedRegion region = regions.getRegion(plot);

		String[] message;

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

		return message;
	}

	private String getPlot(Location location) {
		List<String> hookRegions = HookManager.getRegions(location);

		for (String region : hookRegions) {
			if (CityCache.getCityCache(region) != null)
				continue;

			return region;
		}

		return null;
	}
}
