package nl.kqcreations.cityrp.menu.ATM;

import nl.kqcreations.cityrp.conversation.atm.ATMSetPinConversation;
import nl.kqcreations.cityrp.data.mongo_data.bank.BankAccount;
import nl.kqcreations.cityrp.data.mongo_data.bank.BankAccountData;
import nl.kqcreations.cityrp.data.mongo_data.bank.transaction.Transaction;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.mineacademy.fo.menu.Menu;
import org.mineacademy.fo.menu.MenuPagged;
import org.mineacademy.fo.menu.button.Button;
import org.mineacademy.fo.menu.button.ButtonConversation;
import org.mineacademy.fo.menu.button.ButtonMenu;
import org.mineacademy.fo.menu.model.ItemCreator;
import org.mineacademy.fo.remain.CompMaterial;
import org.mineacademy.fo.remain.Remain;

import java.util.Objects;

public class ATMSettingsMenu extends Menu {

	private BankAccount bankAccount;
	private final Button auditLogButton;
	private final Button userSettingsButton;
	private final Button setPinButton;

	public ATMSettingsMenu(Menu parent, BankAccount bankAccount) {
		super(parent);
		this.bankAccount = bankAccount;
		setTitle("&5ATM - Settings menu");
		setSize(9 * 5);

		auditLogButton = new ButtonMenu(new ATMAuditLogMenu(this), CompMaterial.PAPER, "&fAudit Logs", "See the history of ", "all the transactions performed", "In the last 30 days");
		userSettingsButton = new ButtonMenu(new ATMUserSettingsMenu(this, bankAccount), CompMaterial.PLAYER_HEAD, "&fManage Users", "Lets you manage the users", "of this bank account.");
		setPinButton = new ButtonConversation(new ATMSetPinConversation(bankAccount), CompMaterial.BLAZE_ROD, "&fSet account pin code");
	}

	@Override
	public ItemStack getItemAt(int slot) {

		if (slot == 9 * 2 + 2)
			return auditLogButton.getItem();
		if (slot == 9 * 2 + 4)
			return setPinButton.getItem();
		if (slot == 9 * 2 + 6)
			return userSettingsButton.getItem();

		return ItemCreator.of(CompMaterial.GRAY_STAINED_GLASS_PANE, "", "").clearFlags().hideTags(true).build().make();
	}

	public class ATMAuditLogMenu extends MenuPagged<Transaction> {

		protected ATMAuditLogMenu(Menu parent) {
			super(9 * 5, parent, bankAccount.getSortedTransactions());
			setTitle("&5ATM - Logs");
		}

		@Override
		protected ItemStack convertToItemStack(Transaction transaction) {
			return ItemCreator.of(CompMaterial.PAPER, transaction.getType().getLabel(),
					transaction.getExecutor() != null ? "&7Executed by: &f" + Remain.getPlayerByUUID(transaction.getExecutor()).getName() : null,
					transaction.getReceiverId() > 0 ? "&7Transferred to account: &f" + Objects.requireNonNull(BankAccountData.getBankAccount(transaction.getReceiverId())).getName() : "",
					"&7Amount: &f$" + transaction.getAmount(),
					"&7Balance after: &f$" + transaction.getNewBalance(),
					"&7Date: &f" + transaction.getDateString()
			).build().make();
		}

		@Override
		protected void onPageClick(Player player, Transaction transaction, ClickType click) {
		}
	}
}
