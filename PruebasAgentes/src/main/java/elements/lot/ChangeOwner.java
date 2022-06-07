package elements.lot;

import elements.register.Register;
import jade.content.AgentAction;
import jade.content.onto.annotations.Slot;

@SuppressWarnings("serial")
public class ChangeOwner implements AgentAction {
	
	private Lot lot;
	private Register newOwner;
	
	@Slot(mandatory = true)
	public Lot getLot() {
		return lot;
	}
	public void setLot(Lot lot) {
		this.lot = lot;
	}
	@Slot(mandatory = true)
	public Register getNewOwner() {
		return newOwner;
	}
	public void setNewOwner(Register newOwner) {
		this.newOwner = newOwner;
	}
	

}
