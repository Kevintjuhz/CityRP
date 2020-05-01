package nl.kqcreations.cityrp.command;

import nl.kqcreations.cityrp.data.mongo_data.PlayerData;
import org.bukkit.entity.Player;
import org.mineacademy.fo.command.SimpleCommand;

import java.util.UUID;

public class SetLevelCommand extends SimpleCommand {
	public SetLevelCommand() {
		super("setlevel");
		setMinArguments(1);
		setUsage("<level>");
	}

	@Override
	protected void onCommand() {
		checkConsole();

		final int level = findNumber(0, "&cPlease specify a valid number");

		Player player = getPlayer();
		UUID uuid = player.getUniqueId();
		PlayerData data = PlayerData.getPlayerData(uuid);
		if (data == null) {
			return;
		}

		data.setLevel(level);
		tell("&aSet your level to " + level);
	}
}
