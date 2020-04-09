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

public enum SelectionUtil {

    SELECTION_UTIL;

    private final Map<UUID, SelectionUtil.Selection> playerSelection = new HashMap<>();

    // ===========================================================================
    // Plot Wand methods
    // ===========================================================================

    public Selection getSelection(UUID uuid) {
        return playerSelection.get(uuid);
    }

    public void setSelectionPos(final UUID playerUuid, int position, Location location) {
        setSelectionPos(playerUuid, position, location, true);
    }

    public void setSelectionPos(final UUID playerUuid, int position, Location location, boolean isExpanded) {
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

    public boolean isSelectionSet(UUID playerUuid) {
        final SelectionUtil.Selection selection = playerSelection.get(playerUuid);
        boolean selectionSet = true;
        if (selection.getPlotWandPos1() == null || selection.getPlotWandPos2() == null)
            selectionSet = false;

        return selectionSet;
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
