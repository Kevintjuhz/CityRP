package nl.kqcreations.cityrp.data.bank.transaction;

public enum TransactionType {

	ATM_DEPOSIT("Deposited from ATM"),
	ATM_WITHDRAW("Withdrawn from ATM"),
	RECEIVED_PAYCHECK("Received your paycheck"),
	BANK_TRANSFERED_FROM("Received from a bankaccount"),
	BANK_TRANSFERED_TO("Transfered to a bankaccount");

	private String label;

	TransactionType(String s) {
		this.label = s;
	}

	public String getLabel() {
		return this.label;
	}
}
