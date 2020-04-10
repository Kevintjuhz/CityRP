package nl.kqcreations.cityrp.api;

import nl.kqcreations.cityrp.CityRPPlugin;
import nl.kqcreations.cityrp.api.banking.ATMRegistry;
import nl.kqcreations.cityrp.api.banking.Bank;
import nl.kqcreations.cityrp.api.banking.BankAccountData;
import nl.kqcreations.cityrp.api.banking.BankDatabase;
import nl.kqcreations.cityrp.banking.Banks;
import org.bukkit.Bukkit;

import java.util.UUID;

public class API {

    public static void loadAll() {
        loadBanking();
        loadATMRegistry();
    }

    public static void loadATMRegistry() {
        Bukkit.getPluginManager().registerEvents(ATMRegistry.INSTANCE, CityRPPlugin.getInstance());
    }

    public static void loadBanking() {
        BankDatabase.getInstance().loadBankAccounts(true);
    }

    public static void saveBankingData() {
        BankDatabase.getInstance().saveBankAccounts(true);
    }

    /**
     * Get the umbrella bank account data object for all
     * banks this player is registered to.
     *
     * @param player The UUID of the player.
     * @return Returns a {@link BankAccountData} object which contains all
     * bank account data for all banks this player has come in contact
     * with.
     */
    public static BankAccountData getAllDataFor(UUID player) {
        BankAccountData ret = null;
        for (Bank bank : BankDatabase.getInstance().getRegisteredBanks()) {
            BankAccountData data = bank.getAccountsFor(player);
            ret = ret == null ? data : ret.merge(data);
        }
        return ret;
    }

}
