package nl.kqcreations.cityrp.data.mongo_data.bank;

import lombok.Getter;
import lombok.Setter;
import nl.kqcreations.cityrp.data.mongo_data.bank.transaction.Transaction;

import java.util.*;

@Getter
@Setter
public class BankAccount {

	private final int accountId;

	private double balance = 0;

	private String name;

	private String pin = "0000";

	private boolean isFrozen = false;

	private AccountType type;
	private boolean isMain = false;

	private UUID owner;
	private Map<UUID, BankUser> users = new HashMap<>();

	private List<Transaction> transactions = new ArrayList<>();

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

	public List<BankUser> getUserList() {
		List<BankUser> bankUsers = new ArrayList<>();
		bankUsers.addAll(users.values());

		return bankUsers;
	}

	// --------------------------------------------
	// Transaction actions
	// --------------------------------------------

	public List<Transaction> getSortedTransactions() {
		List<Transaction> transactions = this.transactions;

		Collections.sort(transactions);
		return transactions;
	}

	public void addTransaction(Transaction transaction) {
		transactions.add(transaction);
	}

	// --------------------------------------------
	// Balance actions
	// --------------------------------------------

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

	// --------------------------------------------
	// User actions
	// --------------------------------------------

	public BankUser getUser(UUID playeruuid) {
		return users.get(playeruuid);
	}

	/**
	 * Gets the accesslevel of the given user.
	 *
	 * @param playeruuid
	 * @return
	 */
	public AccessLevel getUserAccessLevel(UUID playeruuid) {
		return users.get(playeruuid).getAccessLevel();
	}

	/**
	 * Adds a user to the given bankaccount
	 *
	 * @param playeruuid
	 * @return
	 */
	public boolean addUser(String name, UUID playeruuid) {
		return addUser(name, playeruuid, AccessLevel.MANAGE);
	}

	public boolean addUser(String name, UUID playeruuid, AccessLevel level) {
		users.computeIfAbsent(playeruuid, (uuid) -> users.put(playeruuid, new BankUser(name, playeruuid, level)));
		return true;
	}

	/**
	 * Promotes a user his accesslevel for the given bankaccount
	 *
	 * @param playeruuid
	 * @return
	 */
	public boolean promoteUser(UUID playeruuid) {
		BankUser bankUser = users.get(playeruuid);
		AccessLevel level = bankUser.getAccessLevel();
		if (level == null || level.getNext() == level)
			return false;

		bankUser.setAccessLevel(level.getNext());

		users.replace(playeruuid, bankUser);
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

		BankUser bankUser = users.get(playeruuid);
		bankUser.setAccessLevel(accessLevel);

		users.remove(playeruuid);
		users.put(playeruuid, bankUser);
		return true;
	}


	/**
	 * Demotes a user to a lower accesslevel
	 *
	 * @param playeruuid
	 * @return
	 */
	public boolean demoteUser(UUID playeruuid) {
		BankUser bankUser = users.get(playeruuid);
		AccessLevel level = bankUser.getAccessLevel();
		if (level == null || level.getPrevious() == level)
			return false;

		bankUser.setAccessLevel(level.getPrevious());

		users.replace(playeruuid, bankUser);
		return true;
	}

	public boolean removeUser(UUID playeruuid) {
		users.remove(playeruuid);
		return true;
	}

	// --------------------------------------------
	// Bank Users
	// --------------------------------------------

	@Getter
	public static class BankUser {
		String name;
		UUID uuid;
		@Setter
		AccessLevel accessLevel;

		public BankUser(String name, UUID uuid, AccessLevel accessLevel) {
			this.name = name;
			this.uuid = uuid;
			this.accessLevel = accessLevel;
		}
	}

	// --------------------------------------------
	// Enums
	// --------------------------------------------

	public enum AccessLevel {
		VIEW, MANAGE;

		public AccessLevel getNext() {
			AccessLevel[] levels = values();
			if (this.ordinal() + 1 >= levels.length)
				return this;
			else
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
