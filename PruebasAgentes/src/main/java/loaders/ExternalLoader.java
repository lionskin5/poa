package loaders;

public class ExternalLoader {

	private int budget;

	public int getBudget() {
		return budget;
	}
	public void setBudget(int budget) {
		this.budget = budget;
	}
	
	@Override
	public String toString() {
		return super.toString() + ",\n" +  "budget = " + budget + "]";
	}
	
}
