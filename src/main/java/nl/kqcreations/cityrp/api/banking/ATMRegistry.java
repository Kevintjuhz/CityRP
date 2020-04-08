package nl.kqcreations.cityrp.api.banking;

import nl.kqcreations.cityrp.util.LocationUtils;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Represents the cache of all known ATMs in the server.
 * Currently unused, only serves as an examplar for a registry.
 */
@Deprecated
public enum ATMRegistry implements Listener {

    INSTANCE;

    private Map<Location, ATM> atmMap = new HashMap<>();

    ATMRegistry() {
    }

    public Optional<ATM> getATMAt(Location location) {
        if (!atmMap.containsKey(LocationUtils.toBlockLocation(location))) {
            return Optional.empty();
        }
        return Optional.of(atmMap.get(location));
    }

    public ATM createATM(Location location) {
        return getATMAt(location).orElseGet(() -> atmMap.computeIfAbsent(location, DefaultATM::new));
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onRightClick(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }
        Block clicked = event.getClickedBlock();
        assert clicked != null;
        getATMAt(clicked.getLocation()).ifPresent(atm -> atm.accessAs(event.getPlayer()));
    }
}
