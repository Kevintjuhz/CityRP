package nl.kqcreations.cityrp.menu;

import nl.kqcreations.cityrp.data.bank.BankAccount;
import org.mineacademy.fo.menu.Menu;

public class ATMSettingsMenu extends Menu {

	BankAccount bankAccount;

	public ATMSettingsMenu(Menu parent, BankAccount bankAccount) {
		super(parent);
		this.bankAccount = bankAccount;
		setTitle("&5ATM - Settings menu");
		setSize(9 * 5);
	}

}
