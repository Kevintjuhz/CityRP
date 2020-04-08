package nl.kqcreations.cityrp.api.banking;

import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * Represents an ATM.
 */
public interface ATM {

    /**
     * Get the location of the ATM.
     *
     * @return Returns the location of this ATM.
     */
    Location getLocation();

    /**
     * Open/Access the ATM as a given player.
     *
     * @param player The player to open the ATM as.
     */
    void accessAs(Player player);

}
