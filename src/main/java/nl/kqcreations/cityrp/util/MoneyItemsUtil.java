package nl.kqcreations.cityrp.util;

import nl.kqcreations.cityrp.MoneyItem;
import nl.kqcreations.cityrp.settings.Settings;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.menu.model.ItemCreator;
import org.mineacademy.fo.remain.CompMaterial;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MoneyItemsUtil {

	/**
	 * This methods returns all the money notes a player should get
	 * converted from an amount.
	 * <p>
	 * For instance if amount = 10k it will return a 4x money note item of value 2500.
	 *
	 * @param amount
	 * @return
	 */
	public static List<ItemStack> createItems(double amount) {
		List<ItemStack> items = new ArrayList<>();
		List<MoneyItem> values = Arrays.asList(MoneyItem.values());
		Collections.sort(values);
		values.sort(Collections.reverseOrder());

		for (MoneyItem item : MoneyItem.values()) {
			if (amount == 0)
				break;

			int itemValue = item.getPrice();
			int fitHowManyTimes = numDiv((int) amount, itemValue);

			if (fitHowManyTimes > 64) {
				int howManyStacks = numDiv(fitHowManyTimes, 64);
				while (howManyStacks > 0) {
					items.add(createGuiItem(item.getItem(), item.getPrice(), 64));
					fitHowManyTimes = fitHowManyTimes - 64;
					amount = amount - (64 * item.getPrice());
					howManyStacks--;
				}
			}

			if (fitHowManyTimes > 0) {
				items.add(createGuiItem(item.getItem(), item.getPrice(), fitHowManyTimes));
				amount = amount - (fitHowManyTimes * item.getPrice());
			}
		}

		return items;
	}

	public static List<ItemStack> getItemsFromMoneyItems() {
		List<ItemStack> items = new ArrayList<>();
		List<MoneyItem> values = Arrays.asList(MoneyItem.values());

		for (MoneyItem item : values) {
			items.add(createGuiItem(item.getItem(), item.getPrice(), 1));
		}

		return items;
	}


	public static boolean isNoteValid(ItemStack stack) {
		ItemMeta meta = stack.getItemMeta();
		if (meta == null)
			return false;

		if (meta.getLore() == null)
			return false;

		for (String string : meta.getLore()) {
			Common.log(string);
			if (string.contains(Settings.VALID_MONEY_NOTE))
				return true;
		}

		return false;
	}

	// Nice little method to create a gui item with a custom name, and description
	private static ItemStack createGuiItem(final CompMaterial material, final int value, final int amount) {
		final ItemStack item = ItemCreator.of(material, "&a$" + value,
				"", "&5Official CityRP banknote with a value of $" + value)
				.build().make();

		item.setAmount(amount);

		return item;
	}

	private static int numDiv(int a, int b) {
		double result = a / b;
		result = Math.floor(result);

		return (int) result;
	}
}
