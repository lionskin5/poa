package elements.bank;


import jade.content.Predicate;
import jade.content.onto.annotations.Slot;

@SuppressWarnings("serial")
public class Transfer implements Predicate {
	
	private double deposit;
	private BankAccount from;
	private BankAccount to;

	@Slot(mandatory = true)
	public double getDeposit() {
		return deposit;
	}
	public void setDeposit(double deposit) {
		this.deposit = deposit;
	}
	@Slot(mandatory = true)
	public BankAccount getFrom() {
		return from;
	}
	public void setFrom(BankAccount from) {
		this.from = from;
	}
	@Slot(mandatory = true)
	public BankAccount getTo() {
		return to;
	}
	public void setTo(BankAccount to) {
		this.to = to;
	}

}
