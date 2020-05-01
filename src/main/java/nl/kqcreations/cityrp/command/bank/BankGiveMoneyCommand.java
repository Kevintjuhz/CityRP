package nl.kqcreations.cityrp.command.bank;

import nl.kqcreations.cityrp.util.MoneyItemsUtil;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.command.SimpleCommandGroup;
import org.mineacademy.fo.command.SimpleSubCommand;

public class BankGiveMoneyCommand extends SimpleSubCommand {
	protected BankGiveMoneyCommand(SimpleCommandGroup parent) {
		super(parent, "givemoney|gm");
		setUsage("<amount> [player]");
		setDescription("Gives the player the amount of money you specified in bills.");
		setMinArguments(1);
	}

	@Override
	protected void onCommand() {
		checkConsole();
		Player player = getPlayer();

		int amount = 0;

		try {
			amount = Integer.parseInt(args[0]);
		} catch (NumberFormatException e) {
			tell("&cPlease specify a valid number");
		}

		Player target = player;

		if (args.length > 1) {
			target = findPlayer(args[1]);
		}

		Inventory inventory = target.getInventory();

		for (ItemStack itemStack : MoneyItemsUtil.createItems(amount)) {
			inventory.addItem(itemStack);
		}

		tell("&aYou have given " + target.getName() + " $" + amount + " in bills");
		Common.tell(target, "&aYou have received $" + amount + " in bills from " + player.getName());
	}
}
