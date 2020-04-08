package nl.kqcreations.cityrp.api;

import nl.kqcreations.cityrp.CityRPPlugin;
import nl.kqcreations.cityrp.api.banking.ATMRegistry;
import nl.kqcreations.cityrp.banking.BankDatabase;
import org.bukkit.Bukkit;

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

}
