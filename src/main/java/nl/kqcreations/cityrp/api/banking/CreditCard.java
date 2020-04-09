package nl.kqcreations.cityrp.api.banking;

import org.bukkit.inventory.ItemStack;

import java.util.UUID;

/**
 * Represents a CreditCard.
 */
public interface CreditCard extends TransactionExecutor {

    int getNumericalID();

    String getName();

    UUID getOwner();

    Bank getBackingBank();

    double getInterest();

    boolean isEnabled();

    void setEnabled(boolean enabled);

    ItemStack getAsItem();

}
