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
import lombok.Getter;
import lombok.Setter;
import nl.kqcreations.cityrp.data.yml_data.CityData;
import nl.kqcreations.cityrp.settings.Settings;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.model.HookManager;

import java.util.List;
import java.util.UUID;

public class PlotUtil {
	// ===========================================================================
	// Plot Methods
	// ===========================================================================

	/**
	 * Calculates the plot price from two BlockVector 3 points
	 *
	 * @param first
	 * @param second
	 * @return the price for the given selection
	 */
	private static double calculatePlotPrice(BlockVector3 first, BlockVector3 second) {
		int x1 = first.getBlockX();
		int z1 = first.getBlockZ();
		int x2 = second.getBlockX();
		int z2 = second.getBlockZ();

		int area = Math.abs(x1 - x2) * Math.abs(z1 - z2);

		return area * Settings.Plot.DEFAULT_SQUARE_METER_PRICE;
	}

	/**
	 * Adds a plot to the server.
	 *
	 * @param player
	 * @param name
	 * @return A message about if the method failed or succeeded
	 */
	public static String addPlot(final Player player, String name) {

		UUID uuid = player.getUniqueId();

		if (!SelectionUtil.isSelectionSet(uuid)) {
			return "&cPlease select two positions before creating a plot!";
		}

		SelectionUtil.Selection selection = SelectionUtil.getSelection(uuid);

		if (isAPlot(player.getWorld(), selection.getPlotWandPos1().toVector3()) || isAPlot(player.getWorld(), selection.getPlotWandPos2().toVector3())) {
			return "&cA plot on your location already exists";
		}

		RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
		RegionManager regions = container.get(BukkitAdapter.adapt(player.getWorld()));

		ProtectedRegion region = new ProtectedCuboidRegion(name, selection.getPlotWandPos1(), selection.getPlotWandPos2());

		region.setFlag(Flags.CHEST_ACCESS, StateFlag.State.DENY);
		region.setFlag(Flags.CHEST_ACCESS.getRegionGroupFlag(), RegionGroup.NON_MEMBERS);

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

	/**
	 * Checks if the player is currently standing in a WorldGuard region
	 * that is not a city. If so then the method will return true
	 *
	 * @param world
	 * @param vector3
	 * @return if it is a plot or not
	 */
	public static boolean isAPlot(World world, Vector3 vector3) {
		Location location = new Location(world, vector3.getX(), vector3.getY(), vector3.getZ());
		List<String> hookRegions = HookManager.getRegions(location);

		// Simple check to see if the player is standing in a region
		for (String region : hookRegions) {
			if (CityData.getCityCache(region) != null)
				continue;

			return true;
		}

		return false;
	}

	/**
	 * Checks if the player is currently standing in a WorldGuard region
	 * that is not a city. If so then the method will return true
	 *
	 * @param location
	 * @return
	 */
	public static boolean isAPlot(Location location) {
		List<String> hookRegions = HookManager.getRegions(location);

		// Simple check to see if the player is standing in a region
		for (String region : hookRegions) {
			if (CityData.getCityCache(region) != null)
				continue;

			return true;
		}

		return false;
	}

	/**
	 * This gets the plotinfo message from a location.
	 *
	 * @param location
	 * @return plot info message
	 */
	public static String[] getPlotInfoMessage(Location location) {

		String plot = getPlotId(location);

		// If player is not standing on a plot return
		if (plot == null) {
			return notOnPlotMessage();
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

	public static String[] notOnPlotMessage() {
		return new String[]{"&cYou are currently not standing on a plot"};
	}

	public static String[] plotAlreadyOwnedMessage() {
		return new String[]{"&cThis plot is already owned!"};
	}

	public static Plot getPlot(Location location) {
		final String plotId = getPlotId(location);

		final RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
		final RegionManager regions = container.get(BukkitAdapter.adapt(location.getWorld()));
		assert regions != null;

		final ProtectedRegion region = regions.getRegion(plotId);
		final double plotPrice = calculatePlotPrice(region.getMinimumPoint(), region.getMaximumPoint());

		return new Plot(plotId, plotPrice, region);
	}

	/**
	 * Gets a plot from a given location.
	 *
	 * @param location
	 * @return a region name
	 */
	private static String getPlotId(Location location) {
		List<String> hookRegions = HookManager.getRegions(location);

		for (String region : hookRegions) {
			if (CityData.getCityCache(region) != null)
				continue;

			return region;
		}

		return null;
	}

	@Getter
	@Setter
	public static class Plot {
		private String plotId;
		private double plotPrice;
		private ProtectedRegion region;

		public Plot(String plotId, double plotPrice, ProtectedRegion region) {
			this.plotId = plotId;
			this.plotPrice = plotPrice;
			this.region = region;
		}
	}
}
