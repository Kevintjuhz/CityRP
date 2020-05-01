package nl.kqcreations.cityrp.conversation.business;

import nl.kqcreations.cityrp.data.mongo_data.business.Business;
import nl.kqcreations.cityrp.data.mongo_data.business.BusinessData;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.ConversationPrefix;
import org.bukkit.conversations.Prompt;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.mineacademy.fo.conversation.SimpleConversation;
import org.mineacademy.fo.conversation.SimplePrefix;
import org.mineacademy.fo.conversation.SimplePrompt;

import java.util.Arrays;

public class CreateNewBusinessConversation extends SimpleConversation {

	private String name;
	private Business.BusinessType businessType;

	@Override
	protected Prompt getFirstPrompt() {
		return new BusinessNamePrompt();
	}

	@Override
	protected ConversationPrefix getPrefix() {
		return new SimplePrefix("&8[&5Business&8]&f ");
	}

	public class BusinessNamePrompt extends SimplePrompt {

		@Override
		protected String getPrompt(ConversationContext ctx) {
			return "What name do you want to give the business?";
		}

		@Override
		protected @Nullable Prompt acceptValidatedInput(@NotNull ConversationContext conversationContext, @NotNull String s) {
			name = s;

			return new BusinessCategoryPrompt();
		}
	}

	public class BusinessCategoryPrompt extends SimplePrompt {
		@Override
		protected String getPrompt(ConversationContext ctx) {
			return "What Type do you want this business to be?";
		}

		@Override
		protected boolean isInputValid(ConversationContext context, String input) {
			Business.BusinessType type = null;

			try {
				type = Business.BusinessType.valueOf(input);
			} catch (IllegalArgumentException e) {
				tell(context, "&cThat is not a valid account type. Available: ");
				tell(Arrays.toString(Business.BusinessType.values()));
				return false;
			}

			return true;
		}

		@Override
		protected @Nullable Prompt acceptValidatedInput(@NotNull ConversationContext conversationContext, @NotNull String s) {
			businessType = Business.BusinessType.valueOf(s);

			int id = createBusiness(conversationContext, getPlayer(conversationContext));

			tell(conversationContext, "&aSuccessfully created a new Business with ID: " + id);

			return Prompt.END_OF_CONVERSATION;
		}
	}

	private int createBusiness(ConversationContext context, Player player) {
		Business business = new Business(BusinessData.getNewId(), name, businessType, player.getWorld().getName());
		BusinessData.addNewBusiness(business);

		return business.getBusinessId();
	}
}
