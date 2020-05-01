package nl.kqcreations.cityrp.command.business;

import nl.kqcreations.cityrp.conversation.business.CreateNewBusinessConversation;
import org.mineacademy.fo.command.SimpleCommandGroup;
import org.mineacademy.fo.command.SimpleSubCommand;

public class BusinessCreateCommand extends SimpleSubCommand {
	protected BusinessCreateCommand(SimpleCommandGroup parent) {
		super(parent, "create");
	}

	@Override
	protected void onCommand() {
		checkConsole();

		new CreateNewBusinessConversation().start(getPlayer());
	}
}
