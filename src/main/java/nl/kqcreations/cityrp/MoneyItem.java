package nl.kqcreations.cityrp;

import lombok.Getter;
import org.mineacademy.fo.remain.CompMaterial;

@Getter
public enum MoneyItem {

	TWENTY_FIVE_HUNDRED_DOLLAR_BILL(2500, CompMaterial.EMERALD),
	FIVE_HUNDRED_DOLLAR_BILL(500, CompMaterial.DIAMOND),
	TWO_HUNDRED_DOLLAR_BILL(200, CompMaterial.PRISMARINE_CRYSTALS),
	HUNDRED_DOLLAR_BILL(100, CompMaterial.PRISMARINE_SHARD),
	FIFTY_DOLLAR_BILL(50, CompMaterial.GOLD_INGOT),
	TWENTY_DOLLAR_BILL(20, CompMaterial.GOLD_NUGGET),
	TEN_DOLLAR_BILL(10, CompMaterial.QUARTZ),
	FIVE_DOLLAR_BILL(5, CompMaterial.IRON_NUGGET),
	ONE_DOLLAR_BILL(1, CompMaterial.COAL);


	public final int price;
	public final CompMaterial item;

	MoneyItem(int price, CompMaterial item) {
		this.price = price;
		this.item = item;
	}

	public static MoneyItem FromCompMaterial(CompMaterial compMaterial) {
		for (MoneyItem moneyItem : MoneyItem.values()) {
			if (moneyItem.getItem().getMaterial().equals(compMaterial.getMaterial()))
				return moneyItem;
		}

		return null;
	}

}
