package nl.kqcreations.cityrp.conversation;

import nl.kqcreations.cityrp.data.mongo_data.bank.BankAccount;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.conversations.ConversationAbandonedEvent;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.ConversationPrefix;
import org.bukkit.conversations.Prompt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.mineacademy.fo.Valid;
import org.mineacademy.fo.conversation.SimpleConversation;
import org.mineacademy.fo.conversation.SimplePrefix;
import org.mineacademy.fo.conversation.SimplePrompt;

public class ATMAddUserConversation extends SimpleConversation {

	private BankAccount bankAccount;

	public ATMAddUserConversation(BankAccount bankAccount) {
		this.bankAccount = bankAccount;
	}

	@Override
	protected Prompt getFirstPrompt() {
		return new NamePrompt();
	}

	@Override
	protected ConversationPrefix getPrefix() {
		return new SimplePrefix("&8[&5ATM&8]&f ");
	}

	@Override
	protected void onConversationEnd(final ConversationAbandonedEvent event) {
		// event.gracefulExit(); --> will be on true if the last prompt is null, or false if we cancelled it with getCanceller

		if (!event.gracefulExit())
			tell(event.getContext().getForWhom(), "Cancelled to add user");
	}

	private class NamePrompt extends SimplePrompt {

		@Override
		protected String getPrompt(ConversationContext ctx) {
			return "Who do you want to add";
		}

		@Override
		protected boolean isInputValid(ConversationContext context, String input) {
			final OfflinePlayer target = Bukkit.getOfflinePlayer(input);
			if (target.getFirstPlayed() <= 0) {
				tell(context, "&cCould not find Player" + input);
				return false;
			}

			if (bankAccount.getUser(target.getUniqueId()) != null) {
				tell(context, "&cThis user already has access");
				return false;
			}

			return true;
		}

		@Override
		protected @Nullable Prompt acceptValidatedInput(@NotNull ConversationContext conversationContext, @NotNull String s) {
			final OfflinePlayer player = Bukkit.getOfflinePlayer(s);
			Valid.checkBoolean(player != null, "&cPlayer " + s + " is not a valid player");

			assert player != null;
			bankAccount.addUser(player.getName(), player.getUniqueId());

			tell(conversationContext, "&aSuccessfully added player " + player.getName() + " to bankaccount " + bankAccount.getName());
			return Prompt.END_OF_CONVERSATION;
		}
	}
}
