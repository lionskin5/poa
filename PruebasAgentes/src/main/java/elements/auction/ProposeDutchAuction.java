package elements.auction;

import jade.content.AgentAction;
import jade.content.onto.annotations.Slot;

@SuppressWarnings("serial")
public class ProposeDutchAuction implements AgentAction {
	
	private LoteVacio lot;
	private int price;	
	
	@Slot(mandatory = true)
	public LoteVacio getLot() {
		return lot;
	}
	public void setLot(LoteVacio lot) {
		this.lot = lot;
	}
	@Slot(mandatory = true)
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}

}
