package nl.kqcreations.cityrp.banking;

import nl.kqcreations.cityrp.api.banking.AbstractBankAccount;
import nl.kqcreations.cityrp.api.banking.Bank;
import nl.kqcreations.cityrp.api.banking.BankAccount;
import nl.kqcreations.cityrp.api.banking.Currency;
import org.mineacademy.fo.ChatUtil;

import java.util.*;

public enum Banks implements Bank {

    CENTRAL(Currencies.CENTRAL) {
        @Override
        public BankAccount createAccount(UUID player) {
            Banks banks = this;
            int id = banks.getOrCreateIDFor(player);
            return banks.accountMap.computeIfAbsent(id, (unused) -> new AbstractBankAccount(banks, player) {
            });
        }
    };

    private final Currency primaryCurrency;
    private Map<UUID, Integer> idMap = new HashMap<>();
    private Map<Integer, BankAccount> accountMap = new HashMap<>();
    private Map<Currency, Double> currencyConversion = new HashMap<>();

    Banks(Currency currency) {
        this.primaryCurrency = currency;
    }

    private int getOrCreateIDFor(UUID player) {

        if (idMap.containsKey(player)) {
            return idMap.get(player);
        }
        Iterator<Integer> iterator = idMap.values().stream().sorted(Integer::compareTo).iterator();
        int last = Integer.MIN_VALUE;
        while (iterator.hasNext()) {
            int num = iterator.next();
            if (last + 1 != num) {
                break;
            }
        }
        return last + 1;
    }

    @Override
    public Currency getPrimaryCurrency() {
        return primaryCurrency;
    }

    @Override
    public boolean isCurrencyConversionSupported(Currency currency) {
        return currencyConversion.containsKey(currency);
    }

    @Override
    public double convertToPrimary(double sum, Currency original) {
        if (isCurrencyConversionSupported(original)) {
            throw new IllegalArgumentException("Currency conversion not supported!");
        }
        return getConversionRateFor(original) * sum;
    }

    @Override
    public double getConversionRateFor(Currency currency) throws IllegalArgumentException {
        if (isCurrencyConversionSupported(currency)) {
            throw new IllegalArgumentException("Currency conversion not supported!");
        }
        return currencyConversion.get(currency);
    }

    @Override
    public String getName() {
        return ChatUtil.capitalize(name().toLowerCase());
    }

    @Override
    public Optional<BankAccount> getAccountById(int Id) {
        return Optional.ofNullable(accountMap.get(Id));
    }

    @Override
    public Optional<Integer> getIdByAccount(BankAccount bankAccount) {
        for (Map.Entry<Integer, BankAccount> entry : accountMap.entrySet()) {
            if (entry.getValue().equals(bankAccount)) return Optional.of(entry.getKey());
        }
        return Optional.empty();
    }

    @Override
    public Optional<BankAccount> getAccountFor(UUID player) {
        for (Map.Entry<Integer, BankAccount> entry : accountMap.entrySet()) {
            if (entry.getValue().getOpener().equals(player)) return Optional.of(entry.getValue());
        }
        return Optional.empty();
    }

    @Override
    public abstract BankAccount createAccount(UUID player);
}
