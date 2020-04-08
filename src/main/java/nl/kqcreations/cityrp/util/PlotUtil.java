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

public enum PlotUtil {

	PLOT_UTIL;

	private static final Map<UUID, Selection> playerSelection = new HashMap<>();

	public void setPlotWandPos(UUID playerUuid, int position, Location location) {
		BlockVector3 blockVector3 = BukkitAdapter.asBlockVector(location);

		if (playerSelection.get(playerUuid) == null) {
			playerSelection.put(playerUuid, new Selection());
		}

		Selection selection = playerSelection.get(playerUuid);

		if (position == 1) {
			selection.setPlotWandPos1(blockVector3);

		} else if (position == 2) {
			selection.setPlotWandPos2(blockVector3);
		}
	}

	public boolean isSelectionSet(UUID playerUuid) {
		final Selection selection = playerSelection.get(playerUuid);
		boolean selectionSet = true;
		if (selection.getPlotWandPos1() == null || selection.getPlotWandPos2() == null)
			selectionSet = false;

		return selectionSet;
	}

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
