package elements.client;

import jade.content.Concept;
import jade.content.onto.annotations.Slot;

@SuppressWarnings("serial")
public class Profit implements Concept {
	
	private double total;

	@Slot(mandatory = true)
	public double getTotal() {
		return total;
	}
	public void setTotal(double total) {
		this.total = total;
	}

}
