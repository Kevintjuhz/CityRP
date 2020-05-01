package nl.kqcreations.cityrp.command;

import nl.kqcreations.cityrp.data.yml_data.WorldData;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.mineacademy.fo.command.SimpleCommand;

public class WorldAddCommand extends SimpleCommand {
	public WorldAddCommand() {
		super("worldadd");
		setDescription("");
	}

	@Override
	protected void onCommand() {
		checkConsole();

		Player player = getPlayer();
		World world = player.getWorld();
		String worldName = world.getName();

		WorldData cache = WorldData.getWorldCache(worldName);
		if (cache != null) {
			tell("&cThe world " + worldName + " is already registered");
			return;
		}

		WorldData.addWorld(worldName);
		tell("&bYou successfully registered world " + worldName);
	}
}
