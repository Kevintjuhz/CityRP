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

    @Getter
    private static final BankDatabase instance = new BankDatabase();
    private Map<String, Bank> banks = new ConcurrentHashMap<>();

    /**
     * Load all bank accounts from the DB into memory.
     *
     * @param async Whether this operation should be done asynchronously.
     * @return Returns a BukkitTask representing the state of execution.
     */
    public BukkitTask loadBankAccounts(boolean async) {
        Runnable task = () -> {
            Common.log("&b[Banking] &bLoading bank data from disk...");
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
                Common.log("&b[Banking] &bTime Taken: " + (System.currentTimeMillis() - time) + "ms.");
            }
        };
        return async ? Bukkit.getScheduler().runTaskAsynchronously(CityRPPlugin.getInstance(), task) : Bukkit.getScheduler().runTask(CityRPPlugin.getInstance(), task);
    }


    /**
     * Schedules a task which will update all the bank accounts.
     *
     * @param async Whether the updates should be done asynchronously.
     * @return Returns a BukkitTask which represents the execution state
     * of saving the accounts.
     */
    public BukkitTask saveBankAccounts(boolean async) {
        BukkitScheduler scheduler = Bukkit.getScheduler();
        Collection<String> sqls = banks.values().stream().map(this::generateSaveSQL).collect(Collectors.toSet());
        Runnable task = () -> sqls.forEach(this::update);
        return async ? scheduler.runTaskAsynchronously(CityRPPlugin.getInstance(), task) : scheduler.runTask(CityRPPlugin.getInstance(), task);
    }

    public BukkitTask saveBankData(final Bank bank, boolean async) {
        String sql = generateSaveSQL(bank);
        Runnable runnable = () -> update(sql);
        return async ? Bukkit.getScheduler().runTaskAsynchronously(CityRPPlugin.getInstance(), runnable) : Bukkit.getScheduler().runTask(CityRPPlugin.getInstance(), runnable);
    }

    /**
     * Loads and registers the bank by a given name from the database.
     *
     * @param bankName The name of the bank.
     * @param async    Whether to do this operation async.
     * @return Returns a BukkitTask which represents the execution of
     * this task.
     */
    public BukkitTask loadAndRegisterBankData(String bankName, boolean async) {
        Runnable runnable = () -> {
            long time = System.currentTimeMillis();
            ResultSet rs = query("SELECT * FROM Banks WHERE Name=" + bankName);
            try {
                String clazz = rs.getString("Class");
                Class<?> unknown = Class.forName(clazz);
                if (!Bank.class.isAssignableFrom(unknown)) {
                    throw new IllegalArgumentException("Invalid Class " + clazz + " not type of Bank!");
                }
                String json = rs.getString("JsonData");
                Bank bank = JsonSerializable.gson.fromJson(json, unknown.asSubclass(Bank.class));
                registerBank(bank);
            } catch (SQLException ex) {
                Common.log("&b[Banking] &eWARN - Database error occurred, message: " + ex.getMessage());
            } catch (ReflectiveOperationException ex) {
                Common.log("&b[Banking] &cERROR - Unable to reconstruct bank instance!");
                ex.printStackTrace();
            } finally {
                Common.log("&b[Banking] Time Taken: " + (System.currentTimeMillis() - time) + "ms.");
            }
        };
        return async ? Bukkit.getScheduler().runTaskAsynchronously(CityRPPlugin.getInstance(), runnable) : Bukkit.getScheduler().runTask(CityRPPlugin.getInstance(), runnable);
    }

    /**
     * Internal method to generate a SQL statement to save to DB.
     */
    private String generateSaveSQL(Bank bank) {
        String json = bank.toJson();
        String clazz = bank.getClass().getCanonicalName();
        String name = bank.getName();
        return "INSERT INTO Banks ('Name', 'JsonData', 'Class'), values(" + name + "," + json + "," + clazz + ") " +
                "ON DUPLICATE KEY UPDATE JsonData=" + json + ", class=" + clazz;
    }


    public void registerBank(Bank bank) {
        if (banks.containsKey(bank.getName())) {
            throw new IllegalArgumentException("Bank already registered!");
        }
        banks.put(bank.getName(), bank);
    }

    public void unregisterBank(Bank bank, boolean saveData) {
        if (banks.containsKey(bank.getName()) && saveData) {
            saveBankData(bank, true);
        }
        banks.remove(bank.getName());
    }


}
