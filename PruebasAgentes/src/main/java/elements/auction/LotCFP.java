package elements.auction;

import elements.lot.Lot;
import jade.content.Concept;
import jade.content.onto.annotations.Slot;

@SuppressWarnings("serial")
public class LotCFP implements Concept {
	
	private Lot lot;
	private int price;	
	
	public LotCFP(Lot lot, int price) {
		this.lot = lot;
		this.price = price;
	}
	
	@Slot(mandatory = true)
	public Lot getLot() {
		return lot;
	}
	public void setLot(Lot lot) {
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
