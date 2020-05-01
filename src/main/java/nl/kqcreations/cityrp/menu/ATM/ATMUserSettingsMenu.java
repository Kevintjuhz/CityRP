package nl.kqcreations.cityrp.menu.ATM;

import nl.kqcreations.cityrp.conversation.ATMAddUserConversation;
import nl.kqcreations.cityrp.data.mongo_data.bank.BankAccount;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.mineacademy.fo.menu.Menu;
import org.mineacademy.fo.menu.MenuPagged;
import org.mineacademy.fo.menu.button.Button;
import org.mineacademy.fo.menu.button.ButtonConversation;
import org.mineacademy.fo.menu.button.ButtonMenu;
import org.mineacademy.fo.menu.model.ItemCreator;
import org.mineacademy.fo.remain.CompColor;
import org.mineacademy.fo.remain.CompMaterial;
import org.mineacademy.fo.remain.Remain;

public class ATMUserSettingsMenu extends Menu {

	private BankAccount bankAccount;
	private final Button userListButton;
	private final Button addUserButton;

	public ATMUserSettingsMenu(Menu parent, BankAccount bankAccount) {
		super(parent);
		setTitle("&5ATM - Users Settings menu");
		setSize(9 * 5);
		this.bankAccount = bankAccount;

		userListButton = new ButtonMenu(new ATMUserListMenu(this), CompMaterial.PLAYER_HEAD, "Users List", "Shows you the list of all the users that", "have access to this bank account");

		addUserButton = new Button() {
			@Override
			public void onClickedInMenu(Player player, Menu menu, ClickType click) {
				new ATMAddUserConversation(bankAccount).start(player);
			}

			@Override
			public ItemStack getItem() {
				return ItemCreator.of(CompMaterial.COOKIE, "Add User", "Will open up a conversation", "where you can tell who you want to add").build().make();
			}
		};
	}

	@Override
	public ItemStack getItemAt(int slot) {
		if (slot == 9 * 2 + 3)
			return userListButton.getItem();
		if (slot == 9 * 2 + 5)
			return addUserButton.getItem();

		return ItemCreator.of(CompMaterial.GRAY_STAINED_GLASS_PANE, "", "").clearFlags().hideTags(true).build().make();
	}

	public class ATMUserListMenu extends MenuPagged<BankAccount.BankUser> {

		protected ATMUserListMenu(Menu parent) {
			super(parent, bankAccount.getUserList());
		}


		@Override
		protected ItemStack convertToItemStack(BankAccount.BankUser bankUser) {
			return createUserSkull(bankUser);
		}

		@Override
		protected void onPageClick(Player player, BankAccount.BankUser item, ClickType click) {
			new ATMUserActionMenu(this, item).displayTo(player);
		}
	}

	public class ATMUserActionMenu extends Menu {

		private final Button promoteUser;
		private final Button demoteUser;
		private final Button removeUser;
		private BankAccount.BankUser user;

		public ATMUserActionMenu(Menu parent, BankAccount.BankUser user) {
			super(parent);
			this.user = user;
			setTitle("&5ATM - User Actions menu");
			setSize(9 * 5);

			promoteUser = new Button() {
				@Override
				public void onClickedInMenu(Player player, Menu menu, ClickType click) {
					bankAccount.promoteUser(user.getUuid());
					restartMenu("&aPromoted user" + user.getName() + " to " + user.getAccessLevel());
				}

				@Override
				public ItemStack getItem() {
					return ItemCreator.ofWool(CompColor.GREEN).name("&aPromote User").build().make();
				}
			};

			demoteUser = new Button() {
				@Override
				public void onClickedInMenu(Player player, Menu menu, ClickType click) {
					bankAccount.demoteUser(user.getUuid());
					restartMenu("&aDemoted user" + user.getName() + " to " + user.getAccessLevel());
				}

				@Override
				public ItemStack getItem() {
					return ItemCreator.ofWool(CompColor.RED).name("&cDemote User").build().make();
				}
			};

			removeUser = new ButtonConversation(new ATMAddUserConversation(bankAccount), CompMaterial.BARRIER, "&cRemove User");
		}

		@Override
		public ItemStack getItemAt(int slot) {

			if (slot == 9 * 2 + 2)
				return createUserSkull(user);
			if (slot == 9 * 2 + 4)
				return removeUser.getItem();
			if (slot == 9 + 6)
				return promoteUser.getItem();
			if (slot == 9 * 3 + 6)
				return demoteUser.getItem();

			return ItemCreator.of(CompMaterial.GRAY_STAINED_GLASS_PANE, "", "").clearFlags().hideTags(true).build().make();
		}
	}

	private ItemStack createUserSkull(BankAccount.BankUser bankUser) {
		ItemStack itemStack = ItemCreator.of(CompMaterial.PLAYER_HEAD, bankUser.getName()
				, "&7Player: &f" + bankUser.getName()
				, "&7AccessLevel: &f" + bankUser.getAccessLevel()).build().make();

		SkullMeta skullMeta = (SkullMeta) itemStack.getItemMeta();
		assert skullMeta != null;
		skullMeta.setOwningPlayer(Remain.getOfflinePlayerByUUID(bankUser.getUuid()).getPlayer());
		itemStack.setItemMeta(skullMeta);

		return itemStack;
	}
}
