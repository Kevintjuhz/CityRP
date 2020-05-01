package nl.kqcreations.cityrp.menu.ATM;

import nl.kqcreations.cityrp.MoneyItem;
import nl.kqcreations.cityrp.data.mongo_data.PlayerData;
import nl.kqcreations.cityrp.data.mongo_data.bank.BankAccount;
import nl.kqcreations.cityrp.data.mongo_data.bank.BankAccount.AccessLevel;
import nl.kqcreations.cityrp.data.mongo_data.bank.transaction.Transaction;
import nl.kqcreations.cityrp.data.mongo_data.bank.transaction.TransactionType;
import nl.kqcreations.cityrp.util.MoneyItemsUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.PlayerUtil;
import org.mineacademy.fo.menu.Menu;
import org.mineacademy.fo.menu.MenuPagged;
import org.mineacademy.fo.menu.button.Button;
import org.mineacademy.fo.menu.button.ButtonMenu;
import org.mineacademy.fo.menu.model.ItemCreator;
import org.mineacademy.fo.remain.CompMaterial;

import java.util.Objects;

// Main menu, where you can select whether you want to deposit or withdraw
public class ATMMenu extends Menu {

	private final Button savingAccountsButton;
	private final Button privateAccountsButton;
	private final Button businessAccountsButton;

	private PlayerData data;
	private BankAccount bankAccount;

	public ATMMenu(Player player) {
		data = PlayerData.getPlayerData(player.getUniqueId());

		setTitle("&5ATM - Select a account type");
		setSize(9 * 5);
		savingAccountsButton = new ButtonMenu(new AccountSelectionMenu(BankAccount.AccountType.SAVINGS_ACCOUNT), CompMaterial.IRON_BLOCK,
				"&7Savings Accounts");

		privateAccountsButton = new ButtonMenu(new AccountSelectionMenu(BankAccount.AccountType.PRIVATE_ACCOUNT), CompMaterial.GOLD_BLOCK,
				"&6Private Accounts");

		businessAccountsButton = new ButtonMenu(new AccountSelectionMenu(BankAccount.AccountType.BUSINESS_ACCOUNT), CompMaterial.DIAMOND_BLOCK,
				"&3Business Accounts");
	}

	@Override
	public ItemStack getItemAt(int slot) {

		switch (slot) {
			case 9 * 2 + 2:
				return savingAccountsButton.getItem();
			case 9 * 2 + 4:
				return privateAccountsButton.getItem();
			case 9 * 2 + 6:
				return businessAccountsButton.getItem();
		}

		return ItemCreator.of(CompMaterial.GRAY_STAINED_GLASS_PANE, "", "").clearFlags().hideTags(true).build().make();
	}

	private final class AccountSelectionMenu extends MenuPagged<BankAccount> {

		protected AccountSelectionMenu(BankAccount.AccountType accountType) {
			super(9 * 4, ATMMenu.this, data.getFilteredBankAccounts(accountType));
			setTitle("&5ATM - Select a account");
		}

		@Override
		protected ItemStack convertToItemStack(BankAccount bankAccount) {
			return ItemCreator.of(CompMaterial.IRON_BLOCK, bankAccount.getName(),
					"&7Account ID: &f" + bankAccount.getAccountId(),
					"&7Balance: &f" + bankAccount.getBalance()
			).glow(bankAccount.isMain()).build().make();
		}

		@Override
		protected void onPageClick(Player player, BankAccount bankAccount, ClickType click) {
			if (bankAccount.isFrozen()) {
				Common.tell(player, "&cThis account has been frozen, please visit your local government!");
				return;
			}

			ATMMenu.this.bankAccount = bankAccount;
			new AccountSelectActionMenu(this, bankAccount).displayTo(player);
		}
	}

	private final class AccountSelectActionMenu extends Menu {

		private final Button depositButton;
		private final Button withdrawButton;
		private final Button settingsButton;

		public AccountSelectActionMenu(final Menu parent, final BankAccount bankAccount) {
			super(parent);
			setSize(9 * 5);
			setTitle("&5ATM $" + bankAccount.getBalance() + " - Choose Action");

			depositButton = new ButtonMenu(new AccountDepositMenu(this, bankAccount), CompMaterial.EMERALD_BLOCK, "&aDeposit Menu");
			withdrawButton = new ButtonMenu(new AccountWithdrawMenu(this, bankAccount), CompMaterial.REDSTONE_BLOCK, "&cWithdraw Menu");
			settingsButton = new ButtonMenu(new ATMSettingsMenu(this, bankAccount), CompMaterial.COAL_BLOCK, "&6Settings Menu");
		}

		@Override
		protected void onMenuClick(Player player, int slot, InventoryAction action, ClickType click, ItemStack cursor, ItemStack clicked, boolean cancelled) {
			AccessLevel level = bankAccount.getUserAccessLevel(player.getUniqueId());
			if (level.equals(AccessLevel.VIEW)) {
				animateTitle("&cYou don't have permission!");
				cancelled = true;
				return;
			}
		}

		@Override
		public ItemStack getItemAt(int slot) {

			if (bankAccount.isMain() || !bankAccount.getOwner().equals(getViewer().getUniqueId())) {
				if (slot == 9 * 2 + 3)
					return depositButton.getItem();
				if (slot == 9 * 2 + 5)
					return withdrawButton.getItem();
			} else {
				if (slot == 9 * 2 + 2)
					return depositButton.getItem();

				if (slot == 9 * 2 + 4)
					return settingsButton.getItem();

				if (slot == 9 * 2 + 6)
					return withdrawButton.getItem();
			}


			return ItemCreator.of(CompMaterial.GRAY_STAINED_GLASS_PANE, "", "").clearFlags().hideTags(true).build().make();
		}
	}

	private final class AccountDepositMenu extends MenuPagged<ItemStack> {

		private final BankAccount bankAccount;

		protected AccountDepositMenu(final Menu parent, final BankAccount bankAccount) {
			super(9 * 3, parent, MoneyItemsUtil.getItemsFromMoneyItems());
			this.bankAccount = bankAccount;
			setTitle("&5ATM - Deposit");
		}

		@Override
		protected ItemStack convertToItemStack(ItemStack item) {
			return item;
		}

		@Override
		protected void onPageClick(Player player, ItemStack item, ClickType click) {
			PlayerInventory inventory = player.getInventory();

			if (item == null) {
				animateTitle("&cThat is not an item");
				return;
			}

			if (!inventory.contains(item.getType())) {
				animateTitle("&cYou don't have that bank note");
				return;
			}

			ItemStack playerItem = getItem(inventory.getStorageContents(), item.getType());
			int amount = Integer.parseInt(
					Common.stripColors(
							Objects.requireNonNull(
									item.getItemMeta()).getDisplayName().replace("$", "")));

			if (playerItem == null) {
				return;
			}

			if (!MoneyItemsUtil.isNoteValid(playerItem)) {
				animateTitle("&cFake money $" + amount + " bill!");
				return;
			}

			if (click.equals(ClickType.RIGHT)) {
				inventory.remove(playerItem);
				amount = amount * playerItem.getAmount();
			} else if (click.equals(ClickType.LEFT)) {
				PlayerUtil.takeOnePiece(player, playerItem);
			}

			bankAccount.addBalance(amount);
			Common.tell(player, "&aYou successfully deposited $" + amount);
			restartMenu();

			int finalAmount = amount;

			// Create the transaction
			Transaction transaction = new Transaction(bankAccount.getAccountId(), finalAmount, bankAccount.getBalance(), TransactionType.ATM_DEPOSIT);
			transaction.setExecutor(player.getUniqueId());
			transaction.generateUUID();
			bankAccount.addTransaction(transaction);

			Common.runLaterAsync(transaction::save);
		}

		@Override
		protected String[] getInfo() {
			return new String[]{
					"Click left to deposit 1 item",
					"Click right to deposit a whole stack",
					"Balance $" + bankAccount.getBalance()
			};
		}

		private ItemStack getItem(ItemStack[] itemStacks, Material type) {
			for (ItemStack itemStack : itemStacks) {
				if (itemStack == null)
					continue;
				if (itemStack.getType().equals(type))
					return itemStack;
			}

			return null;
		}

	}

	private static final class AccountWithdrawMenu extends MenuPagged<ItemStack> {

		private final BankAccount bankAccount;

		protected AccountWithdrawMenu(final Menu parent, final BankAccount bankAccount) {
			super(9 * 5, parent, MoneyItemsUtil.createItems(bankAccount.getBalance()));
			this.bankAccount = bankAccount;

			setTitle("&5ATM - Withdraw");
		}

		@Override
		protected ItemStack convertToItemStack(ItemStack item) {
			return item;
		}

		@Override
		protected String[] getInfo() {
			return new String[]{
					"Right click to take one item",
					"Left click to take the whole stack",
					"Balance $" + bankAccount.getBalance()
			};
		}

		@Override
		protected void onPageClick(Player player, ItemStack item, ClickType click) {

			if (bankAccount.getBalance() < 0) {
				Common.tell(player, "&cYou cannot withdraw from an account with a negative balance");
				animateTitle("&cError see message");
				return;
			}

			// Gets a money item fro comp material
			MoneyItem moneyItem = MoneyItem.FromCompMaterial(
					CompMaterial.fromMaterial(item.getType())
			);

			if (moneyItem == null)
				return;

			int withdrawn = 0;

			ItemStack newItem = new ItemStack(item);

			if (click.equals(ClickType.LEFT)) {
				withdrawn = moneyItem.getPrice();
				newItem.setAmount(1);
				item.setAmount(item.getAmount() - 1);
			} else if (click.equals(ClickType.RIGHT)) {
				withdrawn = moneyItem.getPrice() * item.getAmount();
				item.setAmount(0);
			}

			if (bankAccount.getBalance() < withdrawn) {
				Common.tell(player, "&cYou tried to remove $" + withdrawn + " while the account only has $" + bankAccount.getBalance());
				animateTitle("&cError, See message");
				return;
			}

			bankAccount.removeBalance(withdrawn);
			player.getInventory().addItem(newItem);
			Common.tell(player, "&aSuccessfully withdrawn $" + withdrawn);
			restartMenu();

			// Create the transaction
			int finalWithdrawn = withdrawn;

			Transaction transaction = new Transaction(bankAccount.getAccountId(), finalWithdrawn, bankAccount.getBalance(), TransactionType.ATM_WITHDRAW);
			transaction.setExecutor(player.getUniqueId());
			transaction.generateUUID();
			bankAccount.addTransaction(transaction);
			Common.runLaterAsync(transaction::save);
		}
	}
}
