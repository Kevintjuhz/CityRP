package nl.kqcreations.cityrp.command;

import nl.kqcreations.cityrp.cache.WorldCache;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.mineacademy.fo.command.SimpleCommand;

public class AddWorldCommand extends SimpleCommand {
	public AddWorldCommand() {
		super("addworld");
		setDescription("");
	}

	@Override
	protected void onCommand() {
		checkConsole();

		Player player = getPlayer();
		World world = player.getWorld();
		String worldName = world.getName();

		WorldCache cache = WorldCache.getWorldCache(worldName);
		if (cache != null) {
			tell("&cThe world " + worldName + " is already registered");
			return;
		}

		WorldCache.addWorld(worldName);
		tell("&aYou successfully registered world " + worldName);
	}
}
