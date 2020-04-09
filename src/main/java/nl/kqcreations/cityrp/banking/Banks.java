package nl.kqcreations.cityrp.banking;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import nl.kqcreations.cityrp.api.banking.Currency;
import nl.kqcreations.cityrp.api.banking.*;
import nl.kqcreations.cityrp.util.JsonSerializable;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.mineacademy.fo.ChatUtil;

import java.util.*;
import java.util.function.Supplier;

public enum Banks implements Economy, Bank {

    CENTRAL(Currencies.CENTRAL) {
        @Override
        public BankAccount createAccount(UUID player) {
            Banks banks = this;
            int id = banks.getOrCreateIDFor(player);
            return banks.accountMap.computeIfAbsent(id, (unused) -> new AbstractBankAccount(banks, player) {
            });
        }

        @Override
        public CreditCard getOrCreateCardFor(String name, UUID owner) {
            throw new UnsupportedOperationException("Central Bank Cannot Create Credit Cards!");
        }

        @Override
        public String toJson() {
            return JsonSerializable.gson.toJson(this);
        }
    };

    private final Currency primaryCurrency;
    private Map<UUID, Integer> idMap = new HashMap<>();
    private Map<Integer, BankAccount> accountMap = new HashMap<>();
    private Map<Currency, Double> currencyConversion = new HashMap<>();
    private Map<UUID, Collection<CreditCard>> creditCards = new HashMap<>();

    Banks(Currency currency) {
        this.primaryCurrency = currency;
    }

    @Override
    public boolean isEnabled() {
        return true;
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
    public boolean hasBankSupport() {
        return true;
    }

    @Override
    public int fractionalDigits() {
        return 0;
    }

    @Override
    public String format(double v) {
        return primaryCurrency.getIdentifier() + v;
    }

    @Override
    public String currencyNamePlural() {
        return primaryCurrency.getPluralName();
    }

    @Override
    public String currencyNameSingular() {
        return primaryCurrency.getName();
    }

    @Override
    public boolean hasAccount(String s) {
        Player player = Bukkit.getPlayerExact(s);
        return player != null && getAccountFor(player.getUniqueId()).isPresent();
    }

    @Override
    public boolean hasAccount(OfflinePlayer offlinePlayer) {
        return getAccountFor(offlinePlayer.getUniqueId()).isPresent();
    }

    @Override
    public boolean hasAccount(String playerName, String worldName) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean hasAccount(OfflinePlayer offlinePlayer, String worldName) {
        throw new UnsupportedOperationException();
    }

    @Override
    @SuppressWarnings("deprecation")
    public double getBalance(String s){
        return getBalance(Bukkit.getOfflinePlayer(s));
    }

    @Override
    public double getBalance(OfflinePlayer offlinePlayer) {
        return getAccountFor(offlinePlayer.getUniqueId()).orElseThrow(IllegalArgumentException::new).getCurrentBalance();
    }

    @Override
    public double getBalance(String playerName, String worldName) {
        throw new UnsupportedOperationException();
    }

    @Override
    public double getBalance(OfflinePlayer offlinePlayer, String worldName) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean has(String s, double v) {
        return getBalance(s) >= v;
    }

    @Override
    public boolean has(OfflinePlayer offlinePlayer, double v) {
        return getBalance(offlinePlayer) >= v;
    }

    @Override
    public boolean has(String s, String s1, double v) {
        return getBalance(s, s1) >= v;
    }

    @Override
    public boolean has(OfflinePlayer offlinePlayer, String s, double v) {
        return getBalance(offlinePlayer, s) >= v;
    }

    @Override
    public EconomyResponse withdrawPlayer(String s, double v) {
        return null;
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer offlinePlayer, double v) {
        return null;
    }

    @Override
    public EconomyResponse withdrawPlayer(String s, String s1, double v) {
        return null;
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer offlinePlayer, String s, double v) {
        return null;
    }

    @Override
    public EconomyResponse depositPlayer(String s, double v) {
        return null;
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer offlinePlayer, double v) {
        return null;
    }

    @Override
    public EconomyResponse depositPlayer(String s, String s1, double v) {
        return null;
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer offlinePlayer, String s, double v) {
        return null;
    }

    @Override
    public EconomyResponse createBank(String s, String s1) {
        return null;
    }

    @Override
    public EconomyResponse createBank(String s, OfflinePlayer offlinePlayer) {
        return null;
    }

    @Override
    public EconomyResponse deleteBank(String s) {
        return null;
    }

    @Override
    public EconomyResponse bankBalance(String s) {
        return null;
    }

    @Override
    public EconomyResponse bankHas(String s, double v) {
        return null;
    }

    @Override
    public EconomyResponse bankWithdraw(String s, double v) {
        return null;
    }

    @Override
    public EconomyResponse bankDeposit(String s, double v) {
        return null;
    }

    @Override
    public EconomyResponse isBankOwner(String s, String s1) {
        return null;
    }

    @Override
    public EconomyResponse isBankOwner(String s, OfflinePlayer offlinePlayer) {
        return null;
    }

    @Override
    public EconomyResponse isBankMember(String s, String s1) {
        return null;
    }

    @Override
    public EconomyResponse isBankMember(String s, OfflinePlayer offlinePlayer) {
        return null;
    }

    @Override
    public List<String> getBanks() {
        return null;
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean createPlayerAccount(String s) {
        return createPlayerAccount(Bukkit.getOfflinePlayer(s));
    }

    @Override
    public boolean createPlayerAccount(OfflinePlayer offlinePlayer) {
        if (!offlinePlayer.hasPlayedBefore()) {
            return false;
        }
        getOrCreateAccountFor(offlinePlayer.getUniqueId());
        return true;
    }

    @Override
    public boolean createPlayerAccount(String s, String s1) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean createPlayerAccount(OfflinePlayer offlinePlayer, String s) {
        throw new UnsupportedOperationException();
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

    @Override
    public abstract CreditCard getOrCreateCardFor(String name, UUID owner);

    @Override
    public Transaction createTransaction(UUID invoker, UUID receiver, TransactionExecutor other) {
        return null;
    }

    @Override
    public boolean handleTransaction(Transaction transaction) {
        return false;
    }

    @Override
    public void callBack(Transaction transaction) {

    }

    @Override
    public String toJson() {
        return null;
    }

    private static class CreditCardImpl implements CreditCard {

        private final Supplier<ItemStack> supplier;
        private final String name;
        private final UUID owner;
        private final int numericalID;
        private final double interest;
        private final Bank bank;
        private boolean enabled;

        public CreditCardImpl(String name, Bank bank, UUID owner, int numericalID, double interest, Supplier<ItemStack> toItem) {
            this.name = name;
            this.bank = bank;
            this.owner = owner;
            this.numericalID = numericalID;
            this.interest = interest;
            this.supplier = toItem;
        }

        @Override
        public int getNumericalID() {
            return numericalID;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public UUID getOwner() {
            return owner;
        }

        @Override
        public Bank getBackingBank() {
            return bank;
        }

        @Override
        public double getInterest() {
            return interest;
        }

        @Override
        public boolean isEnabled() {
            return enabled;
        }

        @Override
        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        @Override
        public ItemStack getAsItem() {
            return supplier.get();
        }

        @Override
        public Transaction createTransaction(UUID invoker, UUID receiver, TransactionExecutor other) {
            return new Transaction(invoker, receiver, this, other);
        }

        @Override
        public boolean handleTransaction(Transaction transaction) {
            double sum = transaction.getSum();
            if (sum < 0) {
                throw new IllegalArgumentException("Invalid sum!");
            }
            if (transaction.getInvokingExecutor().equals(this)) {
                Optional<BankAccount> optionalBankAccount = bank.getAccountFor(transaction.invoker);
                if (!optionalBankAccount.isPresent()) {
                    return false;
                }
                BankAccount account = optionalBankAccount.get();
                return account.withdraw(sum);
            } else if (transaction.getReceivingExecutor().equals(this)) {
                Optional<BankAccount> optionalBankAccount = bank.getAccountFor(transaction.receiver);
                if (!optionalBankAccount.isPresent()) {
                    return false;
                }
                BankAccount account = optionalBankAccount.get();
                return account.deposit(sum);
            }
            return false;
        }

        @Override
        public void callBack(Transaction transaction) {
            if (transaction.getInvokingExecutor().equals(this)) {
                Optional<BankAccount> optionalBankAccount = bank.getAccountFor(transaction.invoker);
                if (!optionalBankAccount.isPresent()) {
                    throw new IllegalStateException("Unable to rollback changes! Original Bank Account not found!");
                }
                BankAccount account = optionalBankAccount.get();
                if (!account.deposit(transaction.getSum())) {
                    throw new IllegalStateException("Failed to deposit withdrawn sum!");
                }
            } else if (transaction.getReceivingExecutor().equals(this)) {
                Optional<BankAccount> optionalBankAccount = bank.getAccountFor(transaction.receiver);
                if (!optionalBankAccount.isPresent()) {
                    throw new IllegalStateException("Unable to rollback changes! Original Bank Account not found!");
                }
                BankAccount account = optionalBankAccount.get();
                if (!account.withdraw(transaction.getSum())) {
                    throw new IllegalStateException("Failed to withdraw deposited sum!");
                }
            }
        }
    }
}
