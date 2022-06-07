package elements.bank;

import elements.register.Register;
import jade.content.Concept;
import jade.content.onto.annotations.Slot;

@SuppressWarnings("serial")
public class BankAccount implements Concept {
	
	private double deposit;
	private Register owner;
	// Añadir movimientos quizás
	
	public BankAccount(double deposit, Register owner) {
		this.deposit = deposit;
		this.owner = owner;
	}

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
	
	// Al tener dos métodos separados, respetamos máss el patrón Experto
	public void deposit(double amount) {
		this.deposit+=amount;
	}
	
	public void withdraw(double amount) {
		this.deposit-=amount;
	}

}
