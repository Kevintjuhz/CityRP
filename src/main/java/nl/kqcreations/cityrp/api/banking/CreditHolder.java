package nl.kqcreations.cityrp.api.banking;

/**
 * Represents a holder of credit (money).
 *
 * @see BankAccount
 */
public interface CreditHolder {

    /**
     * @return Returns the executor backing this credit.
     */
    TransactionExecutor getBackingExecutor();

    /**
     * @return Returns the name of this instance.
     */
    String getName();


    default boolean has(double sum) {
        if (sum < 0) {
            throw new IllegalArgumentException("Invalid Sum, must be greater than 0.");
        }
        return getCurrentBalance() >= sum;
    }

    /**
     * @return Returns the current balance of this instance.
     */
    double getCurrentBalance();

    /**
     * Withdraw a sum from this account.
     *
     * @param targetSum The sum to withdraw.
     * @return Returns whether the operation was successful, true if enough funds are in the account
     * and the account isn't frozen.
     */
    boolean withdraw(double targetSum);

    /**
     * Deposit a sum from to account.
     *
     * @param targetSum The amount to deposit.
     * @return Returns whether the operation was successful, and if the account isn't frozen.
     */
    boolean deposit(double targetSum);

    void setBalance(double balance);

    /**
     * Check if this account is frozen. If the account is froze,
     * {@link #deposit(double)} and {@link #withdraw(double)}
     * MUSt return false.
     *
     * @return Returns whether this instance is frozen.
     */
    boolean isFrozen();

    void setFrozen(boolean frozen);

}
