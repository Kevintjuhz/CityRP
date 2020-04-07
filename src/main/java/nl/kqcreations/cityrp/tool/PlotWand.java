package nl.kqcreations.cityrp.tool;

import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.mineacademy.fo.menu.model.ItemCreator;
import org.mineacademy.fo.menu.tool.Tool;
import org.mineacademy.fo.remain.CompMaterial;

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

    }
}
