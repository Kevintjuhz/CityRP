package nl.kqcreations.cityrp.event;

import lombok.Getter;
import lombok.Setter;
import nl.kqcreations.cityrp.data.mongo_data.bank.BankAccount;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

@Getter
public class BankAccountAddUserEvent extends Event {
	private static final HandlerList handlers = new HandlerList();
	private int AccountId;
	private UUID newUser;

	@Setter
	private BankAccount bankAccount;

	@Setter
	private BankAccount.AccessLevel accessLevel;

	public BankAccountAddUserEvent(BankAccount bankAccount, UUID newUser) {
		new BankAccountAddUserEvent(bankAccount, newUser, BankAccount.AccessLevel.VIEW);
	}

	public BankAccountAddUserEvent(BankAccount bankAccount, UUID newUser, BankAccount.AccessLevel level) {
		this.AccountId = bankAccount.getAccountId();
		this.bankAccount = bankAccount;
		this.newUser = newUser;
		this.accessLevel = level;
	}

	@Override
	public @NotNull HandlerList getHandlers() {
		return handlers;
	}

}
