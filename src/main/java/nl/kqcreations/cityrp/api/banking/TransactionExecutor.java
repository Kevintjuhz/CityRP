package nl.kqcreations.cityrp.api.banking;

import java.util.UUID;

public interface TransactionExecutor {

    Transaction createTransaction(UUID invoker, UUID receiver, TransactionExecutor other);
    boolean handleTransaction(Transaction transaction);
    void callBack(Transaction transaction);


}
