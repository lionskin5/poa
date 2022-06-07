package loaders;

public class DirectorLoader {
	
	private int numberOfLanes;
	
	public int getNumberOfLanes() {
		return numberOfLanes;
	}
	public void setNumberOfLanes(int numberOfLanes) {
		this.numberOfLanes = numberOfLanes;
	}
	
	@Override
	public String toString() {
		return super.toString() + "numberOfLanes=" + numberOfLanes + "]";
	}

}
