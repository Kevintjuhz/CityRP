package nl.kqcreations.cityrp.listener;

import nl.kqcreations.cityrp.settings.Settings;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;

public class CraftListener implements Listener {

	@EventHandler
	public void onCraft(CraftItemEvent event) {
		if (Settings.ALLOW_CRAFTING)
			return;

		event.setCancelled(true);
	}
}
