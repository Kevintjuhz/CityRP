package nl.kqcreations.cityrp.data.mongo_data.bank;

import com.mongodb.client.MongoCollection;
import nl.kqcreations.cityrp.data.mongo_data.MongoConnector;
import nl.kqcreations.cityrp.data.mongo_data.SimpleDocumentHandler;
import nl.kqcreations.cityrp.data.mongo_data.bank.transaction.Transaction;
import nl.kqcreations.cityrp.settings.Settings;
import org.bson.Document;

import java.util.*;

import static com.mongodb.client.model.Filters.eq;

public class BankDocumentGenerator implements SimpleDocumentHandler<BankAccount> {

	@Override
	public MongoCollection<Document> getCollection() {
		return MongoConnector.getInstance().getCollection("bank-accounts");
	}

	@Override
	public Document toDocument(BankAccount bankAccount) {
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
			if (transaction != null)
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

	@Override
	public BankAccount fromDocument(Document document) {
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

	@Override
	public <V> BankAccount createDefault(V v) {
		if (!(v instanceof BankAccountData.DefaultAccount)) {
			throw new IllegalArgumentException("Please specify a valid DefaultAccount as a parameter");
		}

		BankAccountData.DefaultAccount defaultAccount = (BankAccountData.DefaultAccount) v;

		final int bankAccountId = defaultAccount.getAccountId();

		BankAccount bankAccount = new BankAccount(bankAccountId);
		bankAccount.setOwner(defaultAccount.getUuid());
		bankAccount.setMain(true);
		bankAccount.setBalance(Settings.STARTING_BALANCE);
		bankAccount.setType(BankAccount.AccountType.PRIVATE_ACCOUNT);
		bankAccount.setName("Main");

		// Insert it into mongo collection
		getCollection().insertOne(toDocument(bankAccount));

		return bankAccount;
	}

	@Override
	public Document findFirst(Document document) {
		return getCollection().find(document).first();
	}

	@Override
	public void update(BankAccount bankAccount) {
		Document document = new Document("account-id", bankAccount.getAccountId());
		Document bankDocument = toDocument(bankAccount);
		if (getCollection().find(document).first() == null) {
			getCollection().insertOne(bankDocument);
		} else {
			getCollection().replaceOne(
					eq("account-id", bankAccount.getAccountId()),
					bankDocument
			);
		}
	}
}
