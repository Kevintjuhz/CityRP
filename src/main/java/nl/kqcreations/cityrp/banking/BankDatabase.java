package nl.kqcreations.cityrp.banking;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nl.kqcreations.cityrp.CityRPPlugin;
import nl.kqcreations.cityrp.api.banking.Bank;
import nl.kqcreations.cityrp.util.JsonSerializable;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.database.SimpleDatabase;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BankDatabase extends SimpleDatabase {

    private Map<String, Bank> banks = new ConcurrentHashMap<>();

    @Getter
    private static final BankDatabase instance = new BankDatabase();

    public BukkitTask loadBankAccounts(boolean async) {
        Runnable task = () -> {
            Common.log("&b[Banking] &aLoading bank data from disk...");
            long time = System.currentTimeMillis();
            try {
                String sql = "SELECT * FROM Banks";
                ResultSet rs = query(sql);
                while (rs.next()) {
                    String json = rs.getString("JsonData");
                    String rawClass = rs.getString("Class");
                    Class<?> unknown = Class.forName(rawClass);
                    if (!Bank.class.isAssignableFrom(unknown)) {
                        throw new IllegalArgumentException("Class " + rawClass + "is not a type of Bank!");
                    }
                    Bank bank = JsonSerializable.gson.fromJson(json, unknown.asSubclass(Bank.class));
                    registerBank(bank);
                }
            } catch (SQLException | IllegalArgumentException ex) {
                Common.log("&eError occurred whilst trying to load Bank Account Data: " + ex.getMessage());
            } catch (ReflectiveOperationException ex) {
                Common.log("&c[SEVERE] Unable to reconstruct bank instance! ");
                ex.printStackTrace();
            } finally {
                Common.log("&b[Banking] &aTime Taken: " + (System.currentTimeMillis() - time) + "ms.");
            }
        };
        return async ? Bukkit.getScheduler().runTaskAsynchronously(CityRPPlugin.getInstance(), task) : Bukkit.getScheduler().runTask(CityRPPlugin.getInstance(), task);
    }


    /**
     * Schedules a task which will update all the bank accounts.
     *
     * @param async Whether the updates should be done asynchronously.
     * @return Returns a BukkitTask which represents the execution of submitting
     * the accounts to be saved.
     */
    public BukkitTask saveBankAccounts(boolean async) {
        BukkitScheduler scheduler = Bukkit.getScheduler();
        Collection<String> sqls = banks.values().stream().map(this::generateSaveSQL).collect(Collectors.toSet());
        Runnable task = () -> sqls.forEach(this::update);
        return async ? scheduler.runTaskAsynchronously(CityRPPlugin.getInstance(), task) : scheduler.runTask(CityRPPlugin.getInstance(), task);
    }

    public void saveBankData(final Bank bank) {
        update(generateSaveSQL(bank));
    }

    private String generateSaveSQL(Bank bank) {
        String json = bank.toJson();
        String clazz = bank.getClass().getCanonicalName();
        String name = bank.getName();
        return "INSERT INTO Banks ('Name', 'JsonData', 'Class'), values(" + name + "," + json + "," + clazz + ") " +
                "ON DUPLICATE KEY UPDATE JsonData=" + json + ", class=" + clazz;
    }

    public void loadBankData(Bank bank) {

    }

    public void registerBank(Bank bank) {
        if (banks.containsKey(bank.getName())) {
            throw new IllegalArgumentException("Bank already registered!");
        }
        banks.put(bank.getName(), bank);
    }

    public void unregisterBank(Bank bank, boolean saveData) {
        if (banks.containsKey(bank.getName()) && saveData) {
            saveBankData(bank);
        }
        banks.remove(bank.getName());
    }


}
