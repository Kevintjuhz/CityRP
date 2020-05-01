package nl.kqcreations.cityrp.event;

import lombok.Getter;
import nl.kqcreations.cityrp.data.mongo_data.bank.BankAccount;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

@Getter
public class BankAccountCreateEvent extends Event {
	private static final HandlerList handlers = new HandlerList();
	private int AccountId;
	private BankAccount bankAccount;
	private Player creator;

	public BankAccountCreateEvent(BankAccount bankAccount, Player creator) {
		this.AccountId = bankAccount.getAccountId();
		this.bankAccount = bankAccount;
		this.creator = creator;
	}

	@Override
	public @NotNull HandlerList getHandlers() {
		return handlers;
	}
}
