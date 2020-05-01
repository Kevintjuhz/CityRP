package nl.kqcreations.cityrp.data.mongo_data.bank;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Sorts;
import nl.kqcreations.cityrp.data.mongo_data.MongoConnector;
import nl.kqcreations.cityrp.data.mongo_data.SimpleDocumentGenerator;
import nl.kqcreations.cityrp.data.mongo_data.bank.transaction.Transaction;
import nl.kqcreations.cityrp.settings.Settings;
import org.bson.Document;
import org.mineacademy.fo.Common;

import java.util.*;

import static com.mongodb.client.model.Filters.eq;

public class BankAccountData {

	private static MongoCollection<Document> bankAccountCollection = MongoConnector.getInstance().getCollection("bank-accounts");

	private static Map<Integer, BankAccount> bankAccountMap = new HashMap<>();

	private SimpleDocumentGenerator<BankAccount> documentGenerator = new BankDocumentGenerator();

	/**
	 * Loads all the bankaccounts and puts them in a bankAccountMap, with the bank-id as the key
	 */
	public static void LoadBankAccounts() {
		for (Document document : bankAccountCollection.find()) {
			bankAccountMap.put(document.getInteger("account-id"), getBankAccountFromDocument(document));
		}
	}

	/**
	 * Saves all the bank accounts to the mongo collection
	 */
	public static void saveBankAccounts() {
		Common.runLaterAsync(() -> {
			System.out.println("Saving bank accounts");
			for (BankAccount bankAccount : bankAccountMap.values()) {
				updateBankAccount(bankAccount.getAccountId(), getDocumentFromAccount(bankAccount));
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
			bankAccountCollection.insertOne(getDocumentFromAccount(bankAccount));
		});

		return bankAccount;
	}

	/**
	 * Creates the main player bank account.
	 * This method should only be called if the player doesn't have any previous
	 * bank accounts.
	 *
	 * @param uuid
	 * @return
	 */
	public static int createMainPlayerBankAccount(UUID uuid) {
		final int bankAccountId = getNewId();
		BankAccount bankAccount = new BankAccount(bankAccountId);
		bankAccount.setOwner(uuid);
		bankAccount.setMain(true);
		bankAccount.setBalance(Settings.STARTING_BALANCE);
		bankAccount.setType(BankAccount.AccountType.PRIVATE_ACCOUNT);
		bankAccount.setName("Main");

		Document document = getDocumentFromAccount(bankAccount);
		bankAccountCollection.insertOne(document);
		bankAccountMap.put(bankAccountId, getBankAccountFromDocument(document));
		return bankAccount.getAccountId();
	}

	/**
	 * Gets a bank account from the given document
	 *
	 * @param document
	 * @return
	 */
	private static BankAccount getBankAccountFromDocument(Document document) {
		BankAccount bankAccount = new BankAccount(
				document.getInteger("account-id"),
				BankAccount.AccountType.valueOf(document.getString("type")),
				document.getBoolean("is-main"));
		bankAccount.setName(document.getString("name"));
		bankAccount.setBalance(document.getDouble("balance"));
		bankAccount.setFrozen(document.getBoolean("is-frozen", false));
		bankAccount.setOwner(UUID.fromString(document.getString("owner")));

		String pin = document.getString("pin");
		if (pin != null && !pin.equals(""))
			bankAccount.setPin(pin);

		if (document.get("users") != null) {
			List<Document> users = (List<Document>) document.get("users");
			Map<UUID, BankAccount.BankUser> accessLevelMap = new HashMap<>();

			for (Document user : users) {
				accessLevelMap.put(UUID.fromString(user.getString("uuid")),
						new BankAccount.BankUser(user.getString("name"),
								UUID.fromString(user.getString("uuid")),
								BankAccount.AccessLevel.valueOf(user.getString("access"))));
			}

			bankAccount.setUsers(accessLevelMap);
		}

		if (document.get("transactions") != null) {
			List<Transaction> transactions = new ArrayList<>();

			for (String transactionId : document.getList("transactions", String.class)) {
				Transaction transaction = Transaction.fromTransactionId(transactionId);

				transactions.add(transaction);
			}

			bankAccount.setTransactions(transactions);
		}

		return bankAccount;
	}

	/**
	 * Create a document from the given bank account
	 *
	 * @param bankAccount
	 * @return
	 */
	private static Document getDocumentFromAccount(BankAccount bankAccount) {
		List<Document> users = new ArrayList<>();
		for (BankAccount.BankUser bankUser : bankAccount.getUsers().values()) {
			users.add(new Document()
					.append("uuid", bankUser.getUuid().toString())
					.append("access", bankUser.getAccessLevel().toString())
					.append("name", bankUser.getName())
			);
		}

		List<String> transactions = new ArrayList<>();
		for (Transaction transaction : bankAccount.getTransactions()) {
			transactions.add(transaction.getTransactionId().toString());
		}

		Document document = new Document("account-id", bankAccount.getAccountId())
				.append("owner", bankAccount.getOwner() != null ? bankAccount.getOwner().toString() : "")
				.append("is-main", bankAccount.isMain())
				.append("type", bankAccount.getType().toString())
				.append("balance", bankAccount.getBalance())
				.append("users", users)
				.append("name", bankAccount.getName())
				.append("is-frozen", bankAccount.isFrozen())
				.append("transactions", transactions)
				.append("pin", bankAccount.getPin());

		return document;
	}

	/**
	 * Update bankaccount into the collection
	 *
	 * @param accountId
	 * @param document
	 */
	private static void updateBankAccount(int accountId, Document document) {

		Document document1 = new Document("account-id", accountId);
		if (bankAccountCollection.find(document1).first() == null) {
			bankAccountCollection.insertOne(document);
		} else {
			bankAccountCollection.replaceOne(
					eq("account-id", accountId),
					document
			);
		}
	}

	/**
	 * Generates a new bank account id.
	 *
	 * @return
	 */
	private static int getNewId() {
		Document document = bankAccountCollection.find().sort(Sorts.descending("account-id")).first();

		if (document == null)
			return 1;

		return document.getInteger("account-id") + 1;
	}
}
