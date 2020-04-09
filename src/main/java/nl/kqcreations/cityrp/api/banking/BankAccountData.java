package nl.kqcreations.cityrp.api.banking;

import nl.kqcreations.cityrp.util.JsonSerializable;

import java.util.*;

public class BankAccountData implements JsonSerializable {

    private Map<String, BankAccount> bankAccounts = new HashMap<>();

    public BankAccountData() {
    }

    public BankAccountData(String serial) {
        this.bankAccounts = JsonSerializable.gson.fromJson(serial, BankAccountData.class).bankAccounts;
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
