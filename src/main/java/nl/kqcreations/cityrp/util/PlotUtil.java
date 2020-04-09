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
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import nl.kqcreations.cityrp.cache.CityCache;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.model.HookManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public enum PlotUtil {

	PLOT_UTIL;

	private final Map<UUID, Selection> playerSelection = new HashMap<>();

	// ===========================================================================
	// Plot Wand methods
	// ===========================================================================

	public void setPlotWandPos(final UUID playerUuid, int position, Location location) {
		setPlotWandPos(playerUuid, position, location, true);
	}

	public void setPlotWandPos(final UUID playerUuid, int position, Location location, boolean isExpanded) {
		if (playerSelection.get(playerUuid) == null) {
			playerSelection.put(playerUuid, new Selection());
		}

		Selection selection = playerSelection.get(playerUuid);

		if (position == 1) {
			if (isExpanded)
				location.setY(1);
			selection.setPlotWandPos1(BukkitAdapter.asBlockVector(location));

		} else if (position == 2) {
			if (isExpanded)
				location.setY(256);
			selection.setPlotWandPos2(BukkitAdapter.asBlockVector(location));
		}
	}

	public boolean isSelectionSet(UUID playerUuid) {
		final Selection selection = playerSelection.get(playerUuid);
		boolean selectionSet = true;
		if (selection.getPlotWandPos1() == null || selection.getPlotWandPos2() == null)
			selectionSet = false;

		return selectionSet;
	}

	// ===========================================================================
	// Plot Methods
	// ===========================================================================

	public void addPlot(final Player player, String name) {
		UUID uuid = player.getUniqueId();

		if (!isSelectionSet(uuid)) {
			Common.tell(player, "&cPlease select two positions before creating a plot!");
			return;
		}

		Selection selection = playerSelection.get(uuid);

		if (isAPlot(player.getWorld(), selection.getPlotWandPos1().toVector3()) || isAPlot(player.getWorld(), selection.getPlotWandPos2().toVector3())) {
			Common.tell(player, "&cA plot on your location already exists");
			return;
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

		Common.tell(player, "&3Plot named " + name + " was successfully created");
	}

	public String getPlot(Location location) {
		List<String> hookRegions = HookManager.getRegions(location);

		for (String region : hookRegions) {
			if (CityCache.getCityCache(region) != null)
				continue;

			return region;
		}

		return null;
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

	// ===========================================================================
	// Custom Classes
	// ===========================================================================

	@Getter
	@AllArgsConstructor
	public class Selection {

		@Setter
		private BlockVector3 plotWandPos1;

		@Setter
		private BlockVector3 plotWandPos2;

		public Selection() {

		}
	}
}
