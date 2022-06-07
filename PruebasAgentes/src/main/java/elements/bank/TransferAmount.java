package elements.bank;

import jade.content.AgentAction;
import jade.content.onto.annotations.Slot;
import jade.core.AID;

@SuppressWarnings("serial")
public class TransferAmount implements AgentAction {
	
	private double deposit;
	private AID from;
	private AID to;
	
	@Slot(mandatory = true)
	public double getDeposit() {
		return deposit;
	}
	public void setDeposit(double deposit) {
		this.deposit = deposit;
	}
	@Slot(mandatory = true)
	public AID getFrom() {
		return from;
	}
	public void setFrom(AID from) {
		this.from = from;
	}
	@Slot(mandatory = true)
	public AID getTo() {
		return to;
	}
	public void setTo(AID to) {
		this.to = to;
	}
	

}
