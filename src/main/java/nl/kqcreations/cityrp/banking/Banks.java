package nl.kqcreations.cityrp.banking;

import org.mineacademy.fo.ChatUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public enum Banks implements Bank {

    CENTRAL {
        @Override
        public BankAccount createAccount(UUID player) {
            return new AbstractBankAccount(this, player) {
            };
        }
    };

    @Override
    public String getName() {
        return ChatUtil.capitalize(name().toLowerCase());
    }

    private Map<Integer, BankAccount> accountMap = new HashMap<>();

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
