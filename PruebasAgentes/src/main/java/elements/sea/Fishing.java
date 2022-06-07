package elements.sea;

import elements.lot.Lot;
import jade.content.Concept;
import jade.content.onto.annotations.Slot;

@SuppressWarnings("serial")
public class Fishing implements Concept {
	
	private Lot lot;

	@Slot(mandatory = true)
	public Lot getLot() {
		return lot;
	}
	public void setLot(Lot lot) {
		this.lot = lot;
	}

}
