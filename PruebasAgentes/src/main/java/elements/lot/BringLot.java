package elements.lot;

import jade.content.AgentAction;
import jade.content.onto.annotations.Slot;

@SuppressWarnings("serial")
public class BringLot implements AgentAction {
	
	private Lot lot;

	@Slot(mandatory = true)
	public Lot getLot() {
		return lot;
	}
	public void setLot(Lot lot) {
		this.lot = lot;
	}

}
