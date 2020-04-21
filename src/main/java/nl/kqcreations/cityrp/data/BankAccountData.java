package nl.kqcreations.cityrp.data;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Sorts;
import nl.kqcreations.cityrp.settings.Settings;
import org.bson.Document;
import org.mineacademy.fo.Common;

import java.util.*;

import static com.mongodb.client.model.Filters.eq;

public class BankAccountData {

	private static MongoCollection<Document> bankAccountCollection = MongoConnector.getInstance().getCollection("bank-accounts");

	private static Map<Integer, BankAccount> bankAccountMap = new HashMap<>();

	public static void LoadBankAccounts() {
		for (Document document : bankAccountCollection.find()) {
			bankAccountMap.put(document.getInteger("account-id"), getBankAccountFromDocument(document));
		}
	}

	public static void saveBankAccounts() {
		Common.runLaterAsync(() -> {
			System.out.println("Saving bank accounts");
			for (BankAccount bankAccount : bankAccountMap.values()) {
				updateBankAccount(bankAccount.getAccountId(), getDocumentFromAccount(bankAccount));
			}
		});
	}

	public static BankAccount getBankAccount(int accountId) {
		BankAccount account = bankAccountMap.get(accountId);

		if (account != null)
			return account;

		return null;
	}

	public static List<BankAccount> getBankAccounts(List<Integer> accountIds) {
		List<BankAccount> bankAccounts = new ArrayList<>();

		for (int accountId : accountIds) {
			bankAccounts.add(bankAccountMap.get(accountId));
		}

		return bankAccounts;
	}

	public static BankAccount createNewBankAccount() {
		return createNewBankAccount("");
	}

	public static BankAccount createNewBankAccount(String name) {
		return createNewBankAccount(name, BankAccount.AccountType.BUSINESS_ACCOUNT);
	}

	public static BankAccount createNewBankAccount(String name, BankAccount.AccountType type) {
		final int bankAccountId = getNewId();
		final BankAccount bankAccount = new BankAccount(bankAccountId);
		bankAccount.setType(type);
		if (!name.equals(""))
			bankAccount.setName(name);

		bankAccountMap.putIfAbsent(bankAccountId, bankAccount);

		return bankAccount;
	}

	public static int createDefaultPlayerBankAccount(UUID uuid) {
		final int bankAccountId = getNewId();
		BankAccount bankAccount = new BankAccount(bankAccountId, uuid);
		bankAccount.setMain(true);
		bankAccount.setBalance(Settings.STARTING_BALANCE);

		Document document = getDocumentFromAccount(bankAccount);
		bankAccountCollection.insertOne(document);
		bankAccountMap.put(bankAccountId, getBankAccountFromDocument(document));
		return bankAccount.getAccountId();
	}

	private static BankAccount getBankAccountFromDocument(Document document) {
		BankAccount bankAccount = new BankAccount(
				document.getInteger("account-id"),
				BankAccount.AccountType.valueOf(document.getString("type")),
				document.getBoolean("is-main"));
		bankAccount.setName(document.getString("name"));
		bankAccount.setBalance(document.getDouble("balance"));
		bankAccount.setFrozen(document.getBoolean("is-frozen", false));

		if (document.get("users") != null) {
			List<Document> users = (List<Document>) document.get("users");
			Map<UUID, BankAccount.AccessLevel> accessLevelMap = new HashMap<>();

			for (Document user : users) {
				accessLevelMap.put(UUID.fromString(user.getString("uuid")), BankAccount.AccessLevel.valueOf(user.getString("access")));
			}

			bankAccount.setUsers(accessLevelMap);
		}

		return bankAccount;
	}

	private static Document getDocumentFromAccount(BankAccount bankAccount) {
		List<Document> users = new ArrayList<>();
		for (UUID uuid : bankAccount.getUsers().keySet()) {
			users.add(new Document()
					.append("uuid", uuid.toString())
					.append("access", bankAccount.getUserAccessLevel(uuid).toString())
			);
		}


		Document document = new Document("account-id", bankAccount.getAccountId())
				.append("is-main", bankAccount.isMain())
				.append("type", bankAccount.getType().toString())
				.append("balance", bankAccount.getBalance())
				.append("users", users)
				.append("name", bankAccount.getName())
				.append("is-frozen", bankAccount.isFrozen());

		return document;
	}

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

	private static int getNewId() {
		Document document = bankAccountCollection.find().sort(Sorts.descending("account-id")).first();

		if (document == null)
			return 1;

		return document.getInteger("account-id") + 1;
	}
}
