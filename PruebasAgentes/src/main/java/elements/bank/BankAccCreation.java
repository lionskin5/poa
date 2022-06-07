package elements.bank;

import elements.register.Register;
import jade.content.AgentAction;
import jade.content.onto.annotations.Slot;

@SuppressWarnings("serial")
public class BankAccCreation implements AgentAction {

	private double deposit;
	private Register owner;
	// Añadir movimientos quizásç
	
	@Slot(mandatory = true)
	public double getDeposit() {
		return deposit;
	}
	public void setDeposit(double deposit) {
		this.deposit = deposit;
	}
	@Slot(mandatory = true)
	public Register getOwner() {
		return owner;
	}
	public void setOwner(Register owner) {
		this.owner = owner;
	}
	
	
}
