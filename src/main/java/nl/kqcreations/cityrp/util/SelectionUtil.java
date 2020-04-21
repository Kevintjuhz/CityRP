package nl.kqcreations.cityrp.util;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SelectionUtil {
	private static final Map<UUID, SelectionUtil.Selection> playerSelection = new HashMap<>();

	// ===========================================================================
	// Plot Wand methods
	// ===========================================================================

	/**
	 * Gets the selection from a given player by their uuid
	 *
	 * @param uuid
	 * @return a Selection object
	 */
	public static Selection getSelection(UUID uuid) {
		return playerSelection.get(uuid);
	}

	/**
	 * Sets a selection position for a player.
	 *
	 * @param playerUuid
	 * @param position
	 * @param location
	 */
	public static void setSelectionPos(final UUID playerUuid, int position, Location location) {
		setSelectionPos(playerUuid, position, location, true);
	}

	/**
	 * Sets a selection position for a player.
	 *
	 * @param playerUuid
	 * @param position
	 * @param location
	 * @param isExpanded
	 */
	public static void setSelectionPos(final UUID playerUuid, int position, Location location, boolean isExpanded) {
		if (playerSelection.get(playerUuid) == null) {
			playerSelection.put(playerUuid, new SelectionUtil.Selection());
		}

		SelectionUtil.Selection selection = playerSelection.get(playerUuid);

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

	/**
	 * Checks if a selection is set for that player
	 *
	 * @param playerUuid
	 * @return
	 */
	public static boolean isSelectionSet(UUID playerUuid) {
		final SelectionUtil.Selection selection = playerSelection.get(playerUuid);
		boolean selectionSet = true;
		if (selection.getPlotWandPos1() == null || selection.getPlotWandPos2() == null)
			selectionSet = false;

		return selectionSet;
	}

	// ===========================================================================
	// Custom Classes
	// ===========================================================================

	// Selection Class containing two BlockVector3 positions
	@Getter
	@Setter
	@AllArgsConstructor
	public static class Selection {

		private BlockVector3 plotWandPos1;
		private BlockVector3 plotWandPos2;

		public Selection() {
		}
	}

}
