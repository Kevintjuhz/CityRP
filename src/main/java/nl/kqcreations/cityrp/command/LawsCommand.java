package nl.kqcreations.cityrp.command;

import nl.kqcreations.cityrp.settings.Settings;
import org.mineacademy.fo.command.SimpleCommand;

public class LawsCommand extends SimpleCommand {
	public LawsCommand() {
		super("laws");
	}

	@Override
	protected void onCommand() {
		checkConsole();

		tell(Settings.Cities.DEFAULT_LAWS);
	}
}
