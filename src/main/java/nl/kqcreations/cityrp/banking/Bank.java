package nl.kqcreations.cityrp.banking;

import java.util.Optional;
import java.util.UUID;

public interface Bank {

    String getName();

    Currency getPrimaryCurrency();

    boolean isCurrencyConversionSupported(Currency currency);

    double getConversionRateFor(Currency currency) throws IllegalArgumentException;

    double convertToPrimary(double sum, Currency original);

    Optional<BankAccount> getAccountById(int Id);

    Optional<Integer> getIdByAccount(BankAccount bankAccount);

    default BankAccount getOrCreateAccountFor(UUID player) {
        return getAccountFor(player).orElse(createAccount(player));
    }

    Optional<BankAccount> getAccountFor(UUID player);

    BankAccount createAccount(UUID player);

}
