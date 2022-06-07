package loaders;

public class ClockLoader {
	
	private int unitTimeMillis;
	private int numUnitDay;
	private int numSimDays;
	
	public int getUnitTimeMillis() {
		return unitTimeMillis;
	}
	public void setUnitTimeMillis(int unitTimeMillis) {
		this.unitTimeMillis = unitTimeMillis;
	}
	public int getNumUnitDay() {
		return numUnitDay;
	}
	public void setNumUnitDay(int numUnitDay) {
		this.numUnitDay = numUnitDay;
	}
	public int getNumSimDays() {
		return numSimDays;
	}
	public void setNumSimDays(int numSimDays) {
		this.numSimDays = numSimDays;
	}
	
	@Override
	public String toString() {
		return super.toString() +  ",\n" + "unitTimeMillis=" + unitTimeMillis + ",\n" +
				"numUnitDay=" + numUnitDay + ",\n" +
				"numSimDays=" + numSimDays + "]";
	}

}
