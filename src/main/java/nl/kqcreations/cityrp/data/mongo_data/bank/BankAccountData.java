package nl.kqcreations.cityrp.data.mongo_data.bank;

import com.mongodb.client.model.Sorts;
import lombok.Getter;
import nl.kqcreations.cityrp.data.mongo_data.SimpleDocumentHandler;
import org.bson.Document;
import org.mineacademy.fo.Common;

import java.util.*;

public class BankAccountData {

	private static SimpleDocumentHandler<BankAccount> documentHandler;

	static {
		documentHandler = new BankDocumentGenerator();
	}

	private static Map<Integer, BankAccount> bankAccountMap = new HashMap<>();


	/**
	 * Loads all the bankaccounts and puts them in a bankAccountMap, with the bank-id as the key
	 */
	public static void LoadBankAccounts() {
		for (Document document : documentHandler.getCollection().find()) {
			bankAccountMap.put(document.getInteger("account-id"), documentHandler.fromDocument(document));
		}
	}

	/**
	 * Saves all the bank accounts to the mongo collection
	 */
	public static void saveBankAccounts() {
		Common.runLaterAsync(() -> {
			System.out.println("Saving bank accounts");
			for (BankAccount bankAccount : bankAccountMap.values()) {
				documentHandler.update(bankAccount);
			}
		});
	}

	/**
	 * Gets a bank account by the account id
	 *
	 * @param accountId
	 * @return
	 */
	public static BankAccount getBankAccount(int accountId) {
		BankAccount account = bankAccountMap.get(accountId);

		if (account != null)
			return account;

		return null;
	}

	/**
	 * Gets a list of bank accounts by a list of account ids.
	 *
	 * @param accountIds
	 * @return
	 */
	public static List<BankAccount> getBankAccounts(List<Integer> accountIds) {
		List<BankAccount> bankAccounts = new ArrayList<>();

		for (int accountId : accountIds) {
			bankAccounts.add(bankAccountMap.get(accountId));
		}

		return bankAccounts;
	}

	/**
	 * Create a new bank account with a specific name
	 *
	 * @param name
	 * @return
	 */
	public static BankAccount createNewBankAccount(String name) {
		return createNewBankAccount(name, BankAccount.AccountType.BUSINESS_ACCOUNT);
	}

	/**
	 * Create a new bank account with a specific name and account type
	 *
	 * @param name
	 * @param type
	 * @return
	 */
	public static BankAccount createNewBankAccount(String name, BankAccount.AccountType type) {
		final int bankAccountId = getNewId();
		final BankAccount bankAccount = new BankAccount(bankAccountId);
		bankAccount.setType(type);
		if (!name.equals(""))
			bankAccount.setName(name);

		bankAccountMap.putIfAbsent(bankAccountId, bankAccount);
		Common.runLaterAsync(() -> {
			documentHandler.getCollection().insertOne(documentHandler.toDocument(bankAccount));
		});

		return bankAccount;
	}

	/**
	 * Creates the main player bank account.
	 * This method should only be called if the player doesn't have any previous
	 * bank accounts.
	 *
	 * @param playeruuid Players uuid
	 * @return a bank account id
	 */
	public static int createMainPlayerBankAccount(UUID playeruuid) {

		final int bankAccountId = getNewId();
		BankAccount bankAccount = documentHandler.createDefault(new DefaultAccount(playeruuid, bankAccountId));

		bankAccountMap.put(bankAccountId, bankAccount);
		return bankAccount.getAccountId();
	}


	/**
	 * Generates a new bank account id.
	 *
	 * @return
	 */
	private static int getNewId() {
		Document document = documentHandler.getCollection().find().sort(Sorts.descending("account-id")).first();

		if (document == null)
			return 1;

		return document.getInteger("account-id") + 1;
	}

	@Getter
	protected static class DefaultAccount {
		private UUID uuid;
		private int accountId;

		public DefaultAccount(UUID uuid, int accountId) {
			this.uuid = uuid;
			this.accountId = accountId;
		}
	}
}
