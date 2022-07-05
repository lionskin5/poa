package elements.auction;

import jade.content.Concept;
import jade.content.onto.annotations.Slot;

@SuppressWarnings("serial")
public class LotCFP implements Concept {
	
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
