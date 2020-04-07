package nl.kqcreations.cityrp.api.banking;

import com.comphenix.protocol.wrappers.collection.BiFunction;
import nl.kqcreations.cityrp.util.JsonSerializable;
import org.mineacademy.fo.Valid;

import java.util.*;
import java.util.function.Function;

public interface Bank extends ICreditService, JsonSerializable, TransactionExecutor {

    String getName();

    Currency getPrimaryCurrency();

    boolean isCurrencyConversionSupported(Currency currency);

    double getConversionRateFor(Currency currency) throws IllegalArgumentException;

    double convertToPrimary(double sum, Currency original) throws IllegalArgumentException;

    Optional<BankAccount> getAccountById(int Id);

    Optional<Integer> getIdByAccount(BankAccount bankAccount);

    default BankAccount getOrCreateAccountFor(UUID player) {
        return getAccountFor(player).orElse(createAccount(player));
    }

    Optional<BankAccount> getAccountFor(UUID player);

    BankAccount createAccount(UUID player);

    static Builder builder() {
        return new Builder();
    }

    class Builder {

        private Currency primaryCurrency;
        private Map<UUID, Integer> idMap = new HashMap<>();
        private Map<Integer, BankAccount> accountMap = new HashMap<>();
        private Map<UUID, Collection<CreditCard>> creditCards = new HashMap<>();
        private Map<Currency, Double> conversionMap = new HashMap<>();
        private String name;

        public Builder() {

        }

        public Builder(Builder other) {
            Objects.requireNonNull(other);
            this.primaryCurrency = other.primaryCurrency;
            this.idMap = new HashMap<>(other.idMap);
            this.accountMap = new HashMap<>(other.accountMap);
            this.conversionMap = new HashMap<>(other.conversionMap);
            this.name = other.name;
        }

        public Builder setIdMap(Map<UUID, Integer> idMap) {
            this.idMap.clear();
            this.idMap.putAll(idMap);
            return this;
        }

        public Builder setAccountMap(Map<Integer, BankAccount> accountMap) {
            this.accountMap.clear();
            this.accountMap.putAll(accountMap);
            return this;
        }

        public Builder setConversionMap(Map<Currency, Double> conversionMap) {
            this.conversionMap.clear();
            this.conversionMap.putAll(conversionMap);
            return this;
        }

        public Builder setCreditCards(Map<UUID, Collection<CreditCard>> creditCards) {
            this.creditCards = creditCards;
            return this;
        }

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setPrimaryCurrency(Currency primaryCurrency) {
            this.primaryCurrency = primaryCurrency;
            return this;
        }

        public Builder clear() {
            this.primaryCurrency = null;
            this.name = null;
            this.conversionMap.clear();
            this.accountMap.clear();
            this.idMap.clear();
            return this;
        }

        public Bank buildAndClear() {
            BankImpl bank = new BankImpl(name, primaryCurrency, conversionMap, creditCards);
            bank.idMap = new HashMap<>(idMap);
            bank.accountMap = new HashMap<>(accountMap);
            bank.conversionMap = new HashMap<>(conversionMap);
            return bank;
        }


        private class BankImpl implements Bank {

            private Currency primaryCurrency;
            Map<UUID, Integer> idMap = new HashMap<>();
            Map<Integer, BankAccount> accountMap = new HashMap<>();
            Map<Currency, Double> conversionMap;
            Map<UUID, Collection<CreditCard>> creditCardData = new HashMap<>();
            private String name;

            public BankImpl(String json) {
                BankImpl bank = JsonSerializable.gson.fromJson(json, BankImpl.class);
                this.primaryCurrency = bank.primaryCurrency;
                this.idMap = bank.idMap;
                this.accountMap = bank.accountMap;
                this.conversionMap = bank.conversionMap;
                this.name = bank.name;
            }

            BankImpl(String name, Currency primaryCurrency, Map<Currency, Double> conversionMap, Map<UUID, Collection<CreditCard>> creditCardData) {
                this.name = name;
                this.primaryCurrency = primaryCurrency;
                this.conversionMap = new HashMap<>(conversionMap);
                this.creditCardData = creditCardData;
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
            public String getName() {
                return name;
            }

            @Override
            public Currency getPrimaryCurrency() {
                return primaryCurrency;
            }

            @Override
            public boolean isCurrencyConversionSupported(Currency currency) {
                return conversionMap.containsKey(currency);
            }

            @Override
            public double getConversionRateFor(Currency currency) throws IllegalArgumentException {
                Valid.checkBoolean(isCurrencyConversionSupported(currency), "Currency conversion unsupported");
                return conversionMap.get(currency);
            }

            @Override
            public double convertToPrimary(double sum, Currency original) throws IllegalArgumentException {
                return getConversionRateFor(original) * sum;
            }

            @Override
            public Optional<BankAccount> getAccountById(int Id) {
                if (!accountMap.containsKey(Id)) {
                    return Optional.empty();
                }
                return Optional.of(accountMap.get(Id));
            }

            @Override
            public Optional<Integer> getIdByAccount(BankAccount bankAccount) {
                for (Map.Entry<Integer, BankAccount> entry : accountMap.entrySet()) {
                    if (bankAccount.equals(entry.getValue())) {
                        return Optional.of(entry.getKey());
                    }
                }
                return Optional.empty();
            }

            @Override
            public Optional<BankAccount> getAccountFor(UUID player) {
                if (!idMap.containsKey(player)) {
                    return Optional.empty();
                }
                return getAccountById(idMap.get(player));
            }


            @Override
            public BankAccount createAccount(UUID player) {
                return new AbstractBankAccount(this, player) {
                };
            }

            @Override
            public String toJson() {
                return JsonSerializable.gson.toJson(this);
            }

            @Override
            public Transaction createTransaction(UUID invoker, UUID receiver, TransactionExecutor other) throws IllegalArgumentException {
                if (!getAccountFor(invoker).isPresent()) {
                    throw new IllegalArgumentException("No Bank Account found for invoker!");
                }
                return new Transaction(invoker, receiver, this, other);
            }

            @Override
            public boolean handleTransaction(Transaction transaction) {
                double sum = transaction.getSum();
                if (sum < 0) {
                    return false;
                }
                if (transaction.getInvokingExecutor().equals(this)) {
                    Optional<BankAccount> account = this.getAccountFor(transaction.invoker);
                    return account.map(bankAccount -> bankAccount.withdraw(sum)).orElse(false);
                } else if (transaction.getReceivingExecutor().equals(this)) {
                    Optional<BankAccount> account = this.getAccountFor(transaction.reciever);
                    return account.map(bankAccount -> bankAccount.deposit(sum)).orElse(false);
                }
                return false;
            }

            @Override
            public void callBack(Transaction transaction) {
                double sum = transaction.getSum();
                if (sum < 0) {
                    return;
                }
                if (transaction.getInvokingExecutor().equals(this)) {
                    Optional<BankAccount> account = this.getAccountFor(transaction.invoker);
                    account.map(bankAccount -> bankAccount.deposit(sum)).orElseThrow(() -> new IllegalStateException("Account is frozen! Unable to call back transaction."));
                } else if (transaction.getReceivingExecutor().equals(this)) {
                    Optional<BankAccount> account = this.getAccountFor(transaction.reciever);
                    account.map(bankAccount -> bankAccount.withdraw(sum)).orElseThrow(() -> new IllegalStateException("Account is frozen! Unable to call back transaction."));
                }
            }

            @Override
            public CreditCard getOrCreateCardFor(String name, UUID owner) {
                throw new UnsupportedOperationException("Bank does not support credit-card creation.");
            }

            @Override
            public boolean equals(Object o) {
                if (this == o) return true;
                if (o == null || getClass() != o.getClass()) return false;

                BankImpl bank = (BankImpl) o;

                if (!Objects.equals(primaryCurrency, bank.primaryCurrency))
                    return false;
                if (!Objects.equals(idMap, bank.idMap)) return false;
                if (!Objects.equals(accountMap, bank.accountMap)) return false;
                if (!Objects.equals(conversionMap, bank.conversionMap))
                    return false;
                return Objects.equals(name, bank.name);
            }

            @Override
            public int hashCode() {
                int result = primaryCurrency != null ? primaryCurrency.hashCode() : 0;
                result = 31 * result + (idMap != null ? idMap.hashCode() : 0);
                result = 31 * result + (accountMap != null ? accountMap.hashCode() : 0);
                result = 31 * result + (conversionMap != null ? conversionMap.hashCode() : 0);
                result = 31 * result + (name != null ? name.hashCode() : 0);
                return result;
            }
        }


    }

}
