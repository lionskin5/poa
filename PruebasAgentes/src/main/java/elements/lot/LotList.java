package elements.lot;

import java.util.List;

import jade.content.Concept;
import jade.content.onto.annotations.AggregateSlot;
import jade.content.onto.annotations.Slot;

@SuppressWarnings("serial")
public class LotList implements Concept {
	
	private List<Lot> sellerLots;

	public LotList(List<Lot> sellerLots) {
		this.sellerLots = sellerLots;
	}
	
	@Slot(mandatory = true)
	@AggregateSlot(cardMin = 1)
	public List<Lot> getSellerLots() {
		return sellerLots;
	}
	public void setSellerLots(List<Lot> sellerLots) {
		this.sellerLots = sellerLots;
	}

}
