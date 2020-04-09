package nl.kqcreations.cityrp.api.banking;

import java.util.Objects;
import java.util.UUID;

public class Transaction {

    public final UUID invoker, receiver;
    public final String invokingAccountName, receivingAccountName;
    private TransactionExecutor invokingExecutor, receivingExecutor;
    private double sum;

    private Transaction(String invokingAccountName, String receivingAccountName, UUID invoker, UUID reciever, TransactionExecutor invokingExecutor, TransactionExecutor receivingExecutor) {
        this.invoker = Objects.requireNonNull(invoker);
        this.receiver = Objects.requireNonNull(reciever);
        this.invokingAccountName = Objects.requireNonNull(invokingAccountName);
        this.receivingAccountName = Objects.requireNonNull(receivingAccountName);
        this.invokingExecutor = Objects.requireNonNull(invokingExecutor);
        this.receivingExecutor = Objects.requireNonNull(receivingExecutor);
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

    public Transaction setSum(double sum) {
        this.sum = sum;
        return this;
    }

    /**
     * Executes this transaction by the order of,
     * invoker first, receiver next.
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

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        public UUID invoker, receiver;
        public String invokingAccountName, receivingAccountName;
        private TransactionExecutor invokingExecutor, receivingExecutor;
        private double sum;


        public Builder setInvoker(UUID invoker) {
            this.invoker = invoker;
            return this;
        }

        public Builder setReceiver(UUID receiver) {
            this.receiver = receiver;
            return this;
        }

        public Builder setInvokingAccountName(String invokingAccountName) {
            this.invokingAccountName = invokingAccountName;
            return this;
        }

        public Builder setReceivingAccountName(String receivingAccountName) {
            this.receivingAccountName = receivingAccountName;
            return this;
        }

        public Builder setInvokingExecutor(TransactionExecutor executor) {
            this.invokingExecutor = executor;
            return this;
        }

        public Builder setReceivingExecutor(TransactionExecutor executor) {
            this.receivingExecutor = executor;
            return this;
        }

        public Builder setSum(double sum) {
            this.sum = sum;
            return this;
        }

        public Builder clear() {
            sum = 0;
            invoker = null;
            receiver = null;
            invokingExecutor = null;
            receivingExecutor = null;
            receivingAccountName = null;
            invokingAccountName = null;
            return this;
        }

        public Transaction buildAndClear() {
            Transaction ret = new Transaction(invokingAccountName, receivingAccountName, invoker, receiver, invokingExecutor, receivingExecutor);
            clear();
            return ret;
        }

    }
}
