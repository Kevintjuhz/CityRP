package nl.kqcreations.cityrp.conversation;

import nl.kqcreations.cityrp.data.mongo_data.bank.BankAccount;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.mineacademy.fo.conversation.SimplePrompt;

public class ATMSetPinConversation extends SimplePrompt {

	private final BankAccount bankAccount;

	public ATMSetPinConversation(BankAccount bankAccount) {
		this.bankAccount = bankAccount;
	}

	@Override
	protected String getCustomPrefix() {
		return "&8[&5ATM&8]&f -";
	}

	@Override
	protected String getPrompt(ConversationContext ctx) {
		return "What do you want your pin to be";
	}

	@Override
	protected boolean isInputValid(ConversationContext context, String input) {
		if (input.length() > 10 || input.length() < 2) {
			tell(context, "&cPlease specify a password with a length between 2 and 10");
			return false;
		}

		return true;
	}

	@Override
	protected @Nullable Prompt acceptValidatedInput(@NotNull ConversationContext conversationContext, @NotNull String s) {
		bankAccount.setPin(s);
		tell(conversationContext, "&aSuccessfully set pin code to: " + s);

		return Prompt.END_OF_CONVERSATION;
	}
}
