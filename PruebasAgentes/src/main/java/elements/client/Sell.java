package elements.client;

import elements.lot.Lot;
import jade.content.AgentAction;
import jade.content.onto.annotations.Slot;

@SuppressWarnings("serial")
public class Sell implements AgentAction {
	
	private Lot lot;

	@Slot(mandatory = true)
	public Lot getLot() {
		return lot;
	}
	public void setLot(Lot lot) {
		this.lot = lot;
	}

}
