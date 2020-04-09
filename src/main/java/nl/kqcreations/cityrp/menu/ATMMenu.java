package nl.kqcreations.cityrp.menu;

import org.bukkit.inventory.ItemStack;
import org.mineacademy.fo.menu.Menu;
import org.mineacademy.fo.menu.button.Button;
import org.mineacademy.fo.menu.button.ButtonMenu;
import org.mineacademy.fo.menu.model.ItemCreator;
import org.mineacademy.fo.remain.CompMaterial;

public class ATMMenu extends Menu {

	private final Button depositButton;
	private final Button withdrawButton;

	public ATMMenu() {

		setTitle("&9ATM - Actions");
		setSize(9 * 5);

		depositButton = new ButtonMenu(new ATMAccountMenu("deposit"), CompMaterial.EMERALD_BLOCK,
				"&2Deposit money",
				"&aChoose if you want to",
				"&aDeposit money into a account."
		);

		withdrawButton = new ButtonMenu(new ATMAccountMenu("withdraw"), CompMaterial.REDSTONE_BLOCK,
				"&4Withdraw money",
				"&cChoose if you want to",
				"&cWithdraw money from a account!"
		);
	}

	@Override
	public ItemStack getItemAt(int slot) {

		switch (slot) {
			case 9 * 2 + 3:
				return depositButton.getItem();
			case 9 * 2 + 5:
				return withdrawButton.getItem();
			default:
				return ItemCreator.of(CompMaterial.GRAY_STAINED_GLASS_PANE).hideTags(true).build().make();
		}
	}

	private final class ATMAccountMenu extends Menu {

		private String action;

		private ATMAccountMenu(String action) {
			super(ATMMenu.this);
			this.action = action;
			setTitle("&9ATM - Accounts");
			setSize(9 * 5);
			getReturnButtonPosition();
		}

		@Override
		public ItemStack getItemAt(int slot) {
			switch (slot) {
				case 9 * 2 + 2:
					return ItemCreator.of(CompMaterial.IRON_BLOCK, "&8Saving Accounts", "&7This opens a list of your saving accounts!").build().make();
				case 9 * 2 + 4:
					return ItemCreator.of(CompMaterial.GOLD_BLOCK, "&6Private Accounts", "&eThis opens a list of your private accounts!").build().make();
				case 9 * 2 + 6:
					return ItemCreator.of(CompMaterial.DIAMOND_BLOCK, "&3Bussiness Accounts", "&bThis opens a list of your bank accounts!").build().make();
				default:
					return ItemCreator.of(CompMaterial.GRAY_STAINED_GLASS_PANE).hideTags(true).build().make();
//				return null;
			}
		}
	}
}
