package nl.kqcreations.cityrp.util;

import nl.kqcreations.cityrp.settings.Settings;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.mineacademy.fo.Common;

public class MoneyNoteUtil {

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
}
