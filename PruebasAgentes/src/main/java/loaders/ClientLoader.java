package loaders;

import java.util.List;

public class ClientLoader extends ConfigLoader {
	
	private List<Double> fishPrices;
	private List<Integer> qualityBonus;
	
	public List<Double> getFishPrices() {
		return fishPrices;
	}
	public void setFishPrices(List<Double> fishPrices) {
		this.fishPrices = fishPrices;
	}
	public List<Integer> getQualityBonus() {
		return qualityBonus;
	}
	public void setQualityBonus(List<Integer> qualityBonus) {
		this.qualityBonus = qualityBonus;
	}
	
	@Override
	public String toString() {
		return super.toString() + "[fishPrices=" + fishPrices + ",\n" +
									"qualityBonus=" + qualityBonus + "]";
	}
	

}
