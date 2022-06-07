package elements.bank;

import jade.content.AgentAction;
import jade.content.onto.annotations.Slot;
import jade.core.AID;

@SuppressWarnings("serial")
public class Deposit implements AgentAction {
	
	private double amount;
	private AID to;
	
	@Slot(mandatory = true)
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	@Slot(mandatory = true)
	public AID getTo() {
		return to;
	}
	public void setTo(AID to) {
		this.to = to;
	}
	

}
