package nl.kqcreations.cityrp.data.mongo_data.bank.transaction;

public enum TransactionType {

	ATM_DEPOSIT("Deposited from ATM"),
	ATM_WITHDRAW("Withdrawn from ATM"),
	RECEIVED_STATE_PAYCHECK("Received a state paycheck"),
	RECEIVED_PAYCHECK("Receive a paycheck"),
	BANK_TRANSFERRED_FROM("Received from a bank account"),
	BANK_TRANSFERRED_TO("Transferred to a bank account");

	private String label;

	TransactionType(String s) {
		this.label = s;
	}

	public String getLabel() {
		return this.label;
	}
}
