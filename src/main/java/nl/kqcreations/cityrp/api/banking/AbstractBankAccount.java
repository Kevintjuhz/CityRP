package nl.kqcreations.cityrp.api.banking;

import nl.kqcreations.cityrp.util.JsonSerializable;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public abstract class AbstractBankAccount implements BankAccount, JsonSerializable {

    private final Bank bank;
    private final UUID opener;
    private double balance;
    private boolean frozen = false;

    private Map<UUID, AccessLevel> accessLevelMap = new HashMap<>();

    public AbstractBankAccount(AbstractBankAccount other) {
        this.bank = other.bank;
        this.opener = other.opener;
        this.balance = other.balance;
        this.frozen = other.frozen;
    }

    protected AbstractBankAccount(String serial) throws IllegalArgumentException {
        this(JsonSerializable.gson.fromJson(serial, AbstractBankAccount.class));
    }

    public AbstractBankAccount(Bank bank, UUID opener) {
        this(bank, opener, 0D);
    }

    public AbstractBankAccount(Bank bank, UUID opener, double initialBalance) {
        super();
        this.bank = Objects.requireNonNull(bank);
        this.opener = Objects.requireNonNull(opener);
        setBalance(initialBalance);
    }

    /**
     * Force sets the balance for this Account's balance.
     * This method does not care about whether
     * the account is frozen.
     *
     * @param balance The non-negative balance.
     */
    public void setBalance(double balance) {
        if (balance < 0) {
            throw new IllegalArgumentException("Invalid Balance, cannot be negative!");
        }
        this.balance = balance;
    }

    @Override
    public boolean isFrozen() {
        return frozen;
    }

    @Override
    public void setFrozen(boolean frozen) {
        this.frozen = frozen;
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
        if (isFrozen()) {
            return false;
        }
        if (targetSum < 0) {
            return withdraw(-targetSum);
        }
        setBalance(getCurrentBalance() + targetSum);
        return true;
    }

    @Override
    public boolean withdraw(double targetSum) {
        if (isFrozen()) {
            return false;
        }
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

    public String toJson() {
        return JsonSerializable.gson.toJson(this);
    }
}
