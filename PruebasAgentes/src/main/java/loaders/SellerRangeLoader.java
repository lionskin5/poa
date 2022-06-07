package loaders;

import elements.lot.Range;

public class SellerRangeLoader extends SellerLoader {
	
	private int range;

	public int getRange() {
		return range;
	}
	public void setRange(int range) {
		this.range = range;
	}
	
	@Override
	public String toString() {
		return super.toString() +  ",\n" + "range=" + Range.values()[range] + "]";
	}

}
