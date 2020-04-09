package nl.kqcreations.cityrp.tool;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.menu.model.ItemCreator;
import org.mineacademy.fo.menu.tool.Tool;
import org.mineacademy.fo.remain.CompMaterial;

import static nl.kqcreations.cityrp.util.PlotUtil.PLOT_UTIL;

public class PlotWand extends Tool {

	@Override
	public ItemStack getItem() {
		return ItemCreator.of(CompMaterial.STICK, "&bPlot Wand", "",
				"Click left to select pos1",
				"Click right to select pos2")
				.glow(true).build().make();
	}

	@Override
	protected void onBlockClick(PlayerInteractEvent event) {
		final Player player = event.getPlayer();

		int pos = 0;
		Location location = event.getClickedBlock().getLocation();

		if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
			pos = 1;
			Common.tell(player, "&bFirst position set");
		} else if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
			pos = 2;
			Common.tell(player, "&bSecond position set");
		}

		if (pos == 0)
			return;

		event.setCancelled(true);
		PLOT_UTIL.setPlotWandPos(player.getUniqueId(), pos, location);
	}
}
