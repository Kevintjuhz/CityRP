package nl.kqcreations.cityrp.api.banking;

import java.util.UUID;

/**
 * Represents an object which can execute and handle transactions.
 */
public interface TransactionExecutor {

    /**
     * Create a fresh transaction with this executor as the invoker.
     *
     * @param invoker              The UUID of the invoker.
     * @param invokingCreditHolder The credit holder which is invoking the transaction.
     * @param targetCreditHolder   The target of the transaction.
     * @return Returns a {@link Transaction} object.
     */
    Transaction createTransaction(UUID invoker, CreditHolder invokingCreditHolder, CreditHolder targetCreditHolder);

    /**
     * Asks this executor to handle a transaction.
     *
     * @return Returns the whether this transaction was a success.
     * @see Transaction#execute()
     */
    boolean handleTransaction(Transaction transaction);

    /**
     * Check whether a transaction can be called back.
     */
    boolean canCallBack(Transaction transaction);

    /**
     * Callback (revert) a transaction.
     *
     * @param transaction The transaction to revert.
     * @throws IllegalArgumentException thrown if the transaction cannot be called back.
     * @see #canCallBack(Transaction)
     */
    void callBack(Transaction transaction) throws IllegalArgumentException;


}
