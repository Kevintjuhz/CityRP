package nl.kqcreations.cityrp.api.banking;

import java.util.Objects;
import java.util.UUID;

public class Transaction {

    public UUID invoker;
    public UUID reciever;
    private TransactionExecutor invokingExecutor, receivingExecutor;
    private double sum;

    public Transaction(UUID invoker, UUID reciever, TransactionExecutor invokingExecutor, TransactionExecutor receivingExecutor) {
        this.invoker = Objects.requireNonNull(invoker);
        this.reciever = Objects.requireNonNull(reciever);
        this.invokingExecutor = Objects.requireNonNull(invokingExecutor);
        this.receivingExecutor = Objects.requireNonNull(receivingExecutor);
    }

    public Transaction setSum(double sum) {
        this.sum = sum;
        return this;
    }

    public TransactionExecutor getInvokingExecutor() {
        return invokingExecutor;
    }

    public TransactionExecutor getReceivingExecutor() {
        return receivingExecutor;
    }

    public double getSum() {
        return sum;
    }

    /**
     * Executes this transaction by the order of,
     * invoker first, reciever next.
     *
     * @return Returns whether the transaction was successfully processed.
     * @throws IllegalStateException Thrown if an account was frozen whilst a transaction was in progress.
     */
    public boolean execute() throws IllegalStateException {
        if (!invokingExecutor.handleTransaction(this)) {
            return false;
        }
        if (!receivingExecutor.handleTransaction(this)) {
            invokingExecutor.callBack(this);
            return false;
        }
        return true;
    }
}
