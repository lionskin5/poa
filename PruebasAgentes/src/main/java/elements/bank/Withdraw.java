package elements.bank;

import jade.content.AgentAction;
import jade.content.onto.annotations.Slot;
import jade.core.AID;

@SuppressWarnings("serial")
public class Withdraw implements AgentAction {
	
	private double amount;
	private AID from;
	
	@Slot(mandatory = true)
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	@Slot(mandatory = true)
	public AID getFrom() {
		return from;
	}
	public void setFrom(AID from) {
		this.from = from;
	}

}
