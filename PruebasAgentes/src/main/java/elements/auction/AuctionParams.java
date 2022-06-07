package elements.auction;

import jade.content.Concept;
import jade.content.onto.annotations.Slot;

@SuppressWarnings("serial")
public class AuctionParams implements Concept {
	
	private int priceDecrease;
	private int latency;
	private int oportunity;
	
	@Slot(mandatory = true)
	public int getPriceDecrease() {
		return priceDecrease;
	}
	public void setPriceDecrease(int priceDecrease) {
		this.priceDecrease = priceDecrease;
	}
	@Slot(mandatory = true)
	public int getLatency() {
		return latency;
	}
	public void setLatency(int latency) {
		this.latency = latency;
	}
	@Slot(mandatory = true)
	public int getOportunity() {
		return oportunity;
	}
	public void setOportunity(int oportunity) {
		this.oportunity = oportunity;
	}
	

}
