package nl.kqcreations.cityrp.api;

import nl.kqcreations.cityrp.banking.BankDatabase;

public class API {

    public void loadBanking() {
        BankDatabase.getInstance().loadBankAccounts(true);
    }

}
