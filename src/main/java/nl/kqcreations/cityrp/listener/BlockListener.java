package nl.kqcreations.cityrp.listener;

import nl.kqcreations.cityrp.menu.ATM.ATMMenu;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.mineacademy.fo.remain.CompMaterial;

public class BlockListener implements Listener {

	@EventHandler
	public void onBlockInteract(PlayerInteractEvent event) {
		Block block = event.getClickedBlock();

		if (event.getAction() != Action.RIGHT_CLICK_BLOCK)
			return;

		if (!event.getClickedBlock().getType().equals(CompMaterial.RED_SANDSTONE_STAIRS.getMaterial()))
			return;

		Player player = event.getPlayer();
		new ATMMenu(player).displayTo(player);
	}

}
