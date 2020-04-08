package nl.kqcreations.cityrp.api;

import nl.kqcreations.cityrp.banking.BankDatabase;

public class API {

    public static void loadBanking() {
        BankDatabase.getInstance().loadBankAccounts(true);
    }

    public static void saveBankingData() {
        BankDatabase.getInstance().saveBankAccounts(true);
    }

}
