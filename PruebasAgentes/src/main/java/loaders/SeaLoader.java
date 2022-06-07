package loaders;

public class SeaLoader {
	
	private int lowRange;
	private int mediumRange;
	private int highRange;
	private int topRange;
	
	public int getLowRange() {
		return lowRange;
	}
	public void setLowRange(int lowRange) {
		this.lowRange = lowRange;
	}
	public int getMediumRange() {
		return mediumRange;
	}
	public void setMediumRange(int mediumRange) {
		this.mediumRange = mediumRange;
	}
	public int getHighRange() {
		return highRange;
	}
	public void setHighRange(int highRange) {
		this.highRange = highRange;
	}
	public int getTopRange() {
		return topRange;
	}
	public void setTopRange(int topRange) {
		this.topRange = topRange;
	}
	@Override
	public String toString() {
		return super.toString()  + ",\n"
						+ "[lowRange=" + lowRange + ",\n" +
						", mediumRange=" + mediumRange + ",\n" +
						", highRange=" + highRange + ",\n" +
						", topRange=" + topRange + "]";
	}

}
