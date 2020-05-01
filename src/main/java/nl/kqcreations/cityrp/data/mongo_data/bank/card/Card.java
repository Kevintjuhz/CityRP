package nl.kqcreations.cityrp.data.mongo_data.bank.card;

import lombok.Getter;
import nl.kqcreations.cityrp.data.mongo_data.bank.BankAccount;
import org.bukkit.inventory.ItemStack;
import org.mineacademy.fo.menu.model.ItemCreator;
import org.mineacademy.fo.remain.CompMaterial;

import java.util.UUID;

@Getter
public class Card {

	UUID cardId;

	BankAccount bankAccount;

	public Card(BankAccount bankAccount) {
		this.bankAccount = bankAccount;
		cardId = UUID.randomUUID();
	}

	public ItemStack createItem() {
		return ItemCreator.of(CompMaterial.BLAZE_ROD, "Debit Card",
				"&7Card For: &f" + bankAccount.getName(),
				"&7Account Type: &f" + bankAccount.getType().toString(),
				"&7Account ID: &f" + bankAccount.getAccountId(),
				"&7Card ID: &f" + getCardId()
		).build().make();
	}
}
