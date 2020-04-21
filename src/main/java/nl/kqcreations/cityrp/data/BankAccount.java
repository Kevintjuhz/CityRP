package nl.kqcreations.cityrp.data;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Getter
public class BankAccount {

	private int accountId;

	@Setter
	private double balance = 0;

	@Setter
	private String name;

	@Setter
	private boolean isFrozen = false;

	@Setter
	private AccountType type;

	@Setter
	private boolean isMain = false;

	@Setter
	private Map<UUID, AccessLevel> users = new HashMap<>();

	public BankAccount(int accountId, UUID uuid) {
		this.accountId = accountId;
		this.name = Integer.toString(accountId);
		this.type = AccountType.PRIVATE_ACCOUNT;
		this.addUser(uuid);
		this.setOwner(uuid);
	}

	public BankAccount(int accountId) {
		this.accountId = accountId;
		this.name = Integer.toString(accountId);
		this.type = AccountType.BUSINESS_ACCOUNT;
	}

	public BankAccount(int accountId, AccountType type) {
		this.type = type;
		this.accountId = accountId;
		this.name = Integer.toString(accountId);
	}

	public BankAccount(int accountId, AccountType type, boolean isMain) {
		this.type = type;
		this.isMain = isMain;
		this.accountId = accountId;
		this.name = Integer.toString(accountId);
	}

	public AccessLevel getUserAccessLevel(UUID playeruuid) {
		return users.get(playeruuid);
	}

	/**
	 * Adds balance to the given bankaccount
	 *
	 * @param amount
	 */
	public void addBalance(double amount) {
		this.balance = this.balance + amount;
	}

	public boolean removeBalance(double amount) {
		this.balance = this.balance - amount;

		return true;
	}

	/**
	 * Adds a user to the given bankaccount
	 *
	 * @param playeruuid
	 * @return
	 */
	public boolean addUser(UUID playeruuid) {
		return addUser(playeruuid, AccessLevel.MANAGE);
	}

	public boolean addUser(UUID playeruuid, AccessLevel level) {
		users.computeIfAbsent(playeruuid, (uuid) -> users.put(playeruuid, level));
		return true;
	}


	/**
	 * Promotes a user his accesslevel for the given bankaccount
	 *
	 * @param playeruuid
	 * @return
	 */
	public boolean promoteUser(UUID playeruuid) {
		AccessLevel level = users.get(playeruuid);
		if (level == null || level.getNext() == level)
			return false;

		users.replace(playeruuid, level.getNext());
		return true;
	}

	/**
	 * Sets a users accesslevel!
	 *
	 * @param playeruuid
	 * @param accessLevel
	 * @return false if user doesn't exist
	 */
	public boolean setUserAccessLevel(UUID playeruuid, AccessLevel accessLevel) {
		if (users.get(playeruuid) == null)
			return false;

		users.replace(playeruuid, accessLevel);
		return true;
	}

	/**
	 * sets the owner for the given bankaccount
	 *
	 * @param playeruuid
	 */
	public void setOwner(UUID playeruuid) {
		if (users.get(playeruuid) == null)
			users.put(playeruuid, AccessLevel.OWNER);
		else
			users.replace(playeruuid, AccessLevel.OWNER);
	}

	/**
	 * Demotes a user to a lower accesslevel
	 *
	 * @param playeruuid
	 * @return
	 */
	public boolean demoteUser(UUID playeruuid) {
		AccessLevel level = users.get(playeruuid);
		if (level == null || level.getPrevious() == level)
			return false;

		users.replace(playeruuid, level.getPrevious());
		return true;
	}

	public enum AccessLevel {
		VIEW, MANAGE, OWNER;

		public AccessLevel getNext() {
			AccessLevel[] levels = values();
			if (this.ordinal() == levels.length) {
				return this;
			}
			return levels[this.ordinal() + 1];
		}

		public AccessLevel getPrevious() {
			AccessLevel[] levels = values();
			if (this.ordinal() == 0) {
				return this;
			}
			return levels[this.ordinal() - 1];
		}
	}

	// List of all the account type's
	public enum AccountType {
		SAVINGS_ACCOUNT, PRIVATE_ACCOUNT, BUSINESS_ACCOUNT, GOVERNMENT_ACCOUNT
	}
}
