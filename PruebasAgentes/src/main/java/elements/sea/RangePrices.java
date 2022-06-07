package elements.sea;

import jade.content.Concept;
import jade.content.onto.annotations.Slot;

@SuppressWarnings("serial")
public class RangePrices implements Concept {
	
	private int lowRangeUnder;
	private double lowRangeTop;
	private int mediumRangeUnder;
	private double mediumRangeTop;
	private int highRangeUnder;
	private double highRangeTop;
	
	@Slot(mandatory = true)
	public int getLowRangeUnder() {
		return lowRangeUnder;
	}
	public void setLowRangeUnder(int lowRangeUnder) {
		this.lowRangeUnder = lowRangeUnder;
	}
	@Slot(mandatory = true)
	public double getLowRangeTop() {
		return lowRangeTop;
	}
	public void setLowRangeTop(double lowRangeTop) {
		this.lowRangeTop = lowRangeTop;
	}
	@Slot(mandatory = true)
	public int getMediumRangeUnder() {
		return mediumRangeUnder;
	}
	public void setMediumRangeUnder(int mediumRangeUnder) {
		this.mediumRangeUnder = mediumRangeUnder;
	}
	@Slot(mandatory = true)
	public double getMediumRangeTop() {
		return mediumRangeTop;
	}
	public void setMediumRangeTop(double mediumRangeTop) {
		this.mediumRangeTop = mediumRangeTop;
	}
	@Slot(mandatory = true)
	public int getHighRangeUnder() {
		return highRangeUnder;
	}
	public void setHighRangeUnder(int highRangeUnder) {
		this.highRangeUnder = highRangeUnder;
	}
	@Slot(mandatory = true)
	public double getHighRangeTop() {
		return highRangeTop;
	}
	public void setHighRangeTop(double highRangeTop) {
		this.highRangeTop = highRangeTop;
	}

}
