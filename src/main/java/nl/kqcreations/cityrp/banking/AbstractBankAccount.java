package nl.kqcreations.cityrp.banking;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public abstract class AbstractBankAccount implements BankAccount {

    private Bank bank;
    private UUID opener;
    private double balance;
    private Map<UUID, AccessLevel> accessLevelMap = new HashMap<>();

    public AbstractBankAccount(Bank bank, UUID opener) {
        this(bank, opener, 0D);
    }

    public AbstractBankAccount(Bank bank, UUID opener, double initialBalance) {
        this.bank = Objects.requireNonNull(bank);
        this.opener = Objects.requireNonNull(opener);
        setBalance(initialBalance);
    }

    public void setBalance(double balance) {
        if (balance < 0) {
            throw new IllegalArgumentException("Invalid Balance, cannot be negative!");
        }
        this.balance = balance;
    }

    @Override
    public UUID getOpener() {
        return opener;
    }

    @Override
    public Bank getBank() {
        return bank;
    }

    @Override
    public double getCurrentBalance() {
        return balance;
    }

    @Override
    public boolean deposit(double targetSum) {
        if (targetSum < 0) {
            return withdraw(-targetSum);
        }
        setBalance(getCurrentBalance() + targetSum);
        return true;
    }

    @Override
    public boolean withdraw(double targetSum) {
        if (targetSum < 0) {
            return deposit(-targetSum);
        }
        if (targetSum > getCurrentBalance()) {
            return false;
        }
        setBalance(getCurrentBalance() - targetSum);
        return true;
    }

    @Override
    public AccessLevel getAccessOf(UUID player) {
        return accessLevelMap.get(player);
    }

    @Override
    public void setAccessOf(UUID player, AccessLevel newLevel) {
        accessLevelMap.remove(player);
        accessLevelMap.put(player, newLevel);
    }

    @Override
    public Map<UUID, AccessLevel> getRegisteredPeers() {
        return accessLevelMap;
    }

    @Override
    public @NotNull Map<String, Object> serialize() {
        throw new UnsupportedOperationException();
    }
}
