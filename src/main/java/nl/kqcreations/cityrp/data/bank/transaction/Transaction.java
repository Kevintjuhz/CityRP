package nl.kqcreations.cityrp.data.bank.transaction;

import com.mongodb.client.MongoCollection;
import lombok.Getter;
import lombok.Setter;
import nl.kqcreations.cityrp.data.MongoConnector;
import org.bson.Document;
import org.mineacademy.fo.Common;

import java.util.UUID;

@Getter
public final class Transaction {

	private static MongoCollection<Document> collection = MongoConnector.getInstance().getCollection("transactions");

	@Setter
	// Random Transaction id
	private UUID transactionId;

	// Bank account id
	private int invokerId;

	// Amount
	private double amount;

	// New balance
	private double newBalance;

	// Transaction Type
	private TransactionType type;

	@Setter
	// Player UUID
	private UUID executor;

	@Setter
	// Receiver Bank account ID
	private int receiverId;

	@Setter
	private long date;

	/**
	 * Create a new Transaction object
	 *
	 * @param bankAccountId The bank account id from the invoker
	 * @param amount        The amount of the transaction
	 * @param newBalance    The new balance of the bank account
	 * @param type          The transaction type
	 */
	public Transaction(int bankAccountId, int amount, double newBalance, TransactionType type) {
		this.invokerId = bankAccountId;
		this.amount = amount;
		this.newBalance = newBalance;
		this.type = type;
		this.date = System.currentTimeMillis();
	}

	public void generateUUID() {
		UUID uuid;

		while (true) {
			uuid = UUID.randomUUID();

			if (collection.find(new Document("transaction-id", uuid)).first() == null) {
				transactionId = uuid;
				return;
			}
		}
	}

	/**
	 * Saves the transaction to the transactions collection
	 */
	public void save() {
		Common.runLaterAsync(() -> {
			collection.insertOne(this.toDocument());
		});
	}

	private Document toDocument() {
		final Document document = new Document("transaction-id", transactionId)
				.append("invoker", invokerId)
				.append("amount", amount)
				.append("type", type.toString())
				.append("date", date);

		if (executor != null)
			document.append("executor", executor.toString());

		if (receiverId <= 0)
			document.append("receiver", receiverId);

		return document;
	}

	// ----------------------------
	// Static methods
	// ----------------------------

	/**
	 * Gets the transaction from a transactionId by
	 * going through the transactions collection
	 * this method should only be used on startup of the plugin.
	 * To get the transactions for each bank account.
	 *
	 * @param transactionId
	 * @return
	 */
	public static Transaction fromTransactionId(String transactionId) {
		Document document = new Document("transaction-id", transactionId);

		document = collection.find(document).first();

		return fromDocument(document);
	}

	/**
	 * Creates a transaction from the given document.
	 * This transaction is not meant to be edited
	 *
	 * @param document
	 * @return
	 */
	public static Transaction fromDocument(Document document) {
		final String transactionId = document.getString("transaction-id");
		final int amount = document.getInteger("amount");
		final String type = document.getString("type");
		final int newBalance = document.getInteger("new-balance");
		final int invokerId = document.getInteger("invoker");

		if (transactionId == null || amount <= 0 || type == null || newBalance <= 0) {
			return null;
		}

		final TransactionType transactionType;

		try {
			transactionType = TransactionType.valueOf(type);
		} catch (IllegalArgumentException e) {
			Common.logFramed("Could not find Transaction type of: " + type + " from bank account id: " + invokerId);
			return null;
		}

		final Transaction transaction = new Transaction(invokerId, amount, newBalance, transactionType);
		transaction.setDate(document.getInteger("date"));
		transaction.setTransactionId(UUID.fromString(document.getString("transaction-id")));

		if (document.getString("executor") != null)
			transaction.setExecutor(UUID.fromString(document.getString("executor")));

		if (document.getInteger("receiver") != null)
			transaction.setReceiverId(document.getInteger("receiver"));

		return transaction;
	}
}
