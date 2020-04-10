package nl.kqcreations.cityrp.api.banking;

import nl.kqcreations.cityrp.util.JsonSerializable;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Represents a data object used to keep track of a bunch of {@link BankAccount}s
 * Currently allows for any bank account to be stored with no restriction on the owners.
 */
public class BankAccountData implements JsonSerializable {

    public static final transient String DEFAULT = "MAIN";
    private Map<String, BankAccount> bankAccounts = new HashMap<>();

    public BankAccountData() {
    }

    public BankAccountData(String serial) {
        this.bankAccounts = JsonSerializable.gson.fromJson(serial, BankAccountData.class).bankAccounts;
    }

    public BankAccount getOrCreateMainAccount(UUID player, Bank bank) {
        return bankAccounts.computeIfAbsent(DEFAULT, (unused) -> bank.createAccount(DEFAULT, player));
    }

    public Collection<BankAccount> getAllAccounts() {
        return new HashSet<>(bankAccounts.values());
    }

    public Collection<BankAccount> getFilteredAccounts(Predicate<BankAccount> predicate) {
        return bankAccounts.values().stream().filter(predicate).collect(Collectors.toSet());
    }

    public void addAccount(BankAccount account) {
        removeAccount(account.getName());
        bankAccounts.put(account.getName(), account);
    }

    public void removeAccount(String name) {
        bankAccounts.remove(name);
    }

    public Optional<BankAccount> getAccount(String name, UUID owner) {
        if (!bankAccounts.containsKey(name)) {
            return Optional.empty();
        }
        BankAccount account = bankAccounts.get(name);
        return account.getOpener().equals(owner) ? Optional.of(account) : Optional.empty();
    }

    public boolean hasAccount(String name, UUID owner) {
        return getAccount(name, owner).isPresent();
    }

    public String toJson() {
        return JsonSerializable.gson.toJson(this);
    }

    public BankAccountData merge(BankAccountData other) {
        BankAccountData data = new BankAccountData();
        data.bankAccounts.putAll(this.bankAccounts);
        if (!other.equals(this)) {
            data.bankAccounts.putAll(other.bankAccounts);
        }
        return data;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BankAccountData that = (BankAccountData) o;
        return Objects.equals(bankAccounts, that.bankAccounts);
    }

    @Override
    public int hashCode() {
        return bankAccounts != null ? bankAccounts.hashCode() : 0;
    }
}
