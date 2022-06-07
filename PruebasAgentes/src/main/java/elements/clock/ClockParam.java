package elements.clock;

import jade.content.Concept;
import jade.content.onto.annotations.Slot;

@SuppressWarnings("serial")
public class ClockParam implements Concept {
	
	private int unitTimeMillis;
	private int numUnitDay;
	private int numSimDays;
	
	@Slot(mandatory = true)
	public int getUnitTimeMillis() {
		return unitTimeMillis;
	}
	public void setUnitTimeMillis(int unitTimeMillis) {
		this.unitTimeMillis = unitTimeMillis;
	}
	@Slot(mandatory = true)
	public int getNumUnitDay() {
		return numUnitDay;
	}
	public void setNumUnitDay(int numUnitDay) {
		this.numUnitDay = numUnitDay;
	}
	@Slot(mandatory = true)
	public int getNumSimDays() {
		return numSimDays;
	}
	public void setNumSimDays(int numSimDays) {
		this.numSimDays = numSimDays;
	}
	

}
