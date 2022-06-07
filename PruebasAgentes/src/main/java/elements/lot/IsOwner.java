package elements.lot;

import elements.register.Register;
import jade.content.Predicate;
import jade.content.onto.annotations.Slot;
import jade.core.AID;

@SuppressWarnings("serial")
public class IsOwner implements Predicate {
	
	private Lot lot;
	private Register owner;
	
	public IsOwner(Lot lot, Register owner) {
		this.lot = lot;
		this.owner = owner;
	}
	
	@Slot(mandatory = true)
	public Lot getLot() {
		return lot;
	}
	public void setLot(Lot lot) {
		this.lot = lot;
	}
	@Slot(mandatory = true)
	public Register getOwner() {
		return owner;
	}
	public void setOwner(Register owner) {
		this.owner = owner;
	}
	
	public boolean isAgentOwner(AID sender) {
		return this.owner.isAgentMember(sender);
	}
	
}
