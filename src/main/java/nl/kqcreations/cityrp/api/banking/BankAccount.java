package nl.kqcreations.cityrp.api.banking;

import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.util.Map;
import java.util.UUID;

/**
 * Represents a bank account which a player or organisation can own.
 */
public interface BankAccount extends ConfigurationSerializable {

    Bank getBank();

    String getName();

    /**
     * The creator of this account.
     *
     * @return Returns the UUID of the player who created this account.
     */
    UUID getOpener();

    double getCurrentBalance();

    /**
     * Withdraw a sum from this account.
     *
     * @param targetSum The sum to withdraw.
     * @return Returns whether the operation was successful, true if enough funds are in the account.
     */
    boolean withdraw(double targetSum);

    /**
     * Deposit a sum from to account.
     *
     * @param targetSum The amount to deposit.
     * @return Returns whether the operation was successful.
     */
    boolean deposit(double targetSum);

    /**
     * Get a map representing player's access level to this account.
     *
     * @return Returns a shallow cloned map of players' access to this account.
     */
    Map<UUID, AccessLevel> getRegisteredPeers();

    //TODO
    /*
    default boolean transferSumsFrom(BankAccount other, double targetSum) {
        Objects.requireNonNull(other);
        if (!withdraw(targetSum)) {
            return false;
        }
        if (!deposit(targetSum)) {
            other.deposit(targetSum); //Give the money back if failed to deposit.
            return false;
        }
        return true;
    }
    */

    void setBalance(double balance);

    boolean isFrozen();

    void setFrozen(boolean frozen);

    AccessLevel getAccessOf(UUID player);

    void setAccessOf(UUID player, AccessLevel newLevel);

    default void promoteAccess(UUID player) {
        AccessLevel current = getAccessOf(player);
        current = current == null ? AccessLevel.VIEW : current.getNext();
        setAccessOf(player, current);
    }

    default void demoteAccess(UUID player) {
        AccessLevel current = getAccessOf(player);
        if (current != null) {
            setAccessOf(player, current.getPrevious());
        }
    }

    enum AccessLevel {

        VIEW, MANAGE, OWNER;

        public AccessLevel getNext() {
            AccessLevel[] levels = values();
            if (this.ordinal() == levels.length) {
                return this;
            }
            return levels[this.ordinal() + 1];
        }

        public AccessLevel getPrevious() {
            AccessLevel[] levels = values();
            if (this.ordinal() == 0) {
                return this;
            }
            return levels[this.ordinal() - 1];
        }
    }


}
