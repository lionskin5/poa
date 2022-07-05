package elements.auction;

import elements.lot.Fish;
import elements.lot.Range;
import jade.content.Concept;
import jade.content.onto.annotations.Slot;

@SuppressWarnings("serial")
public class LoteVacio implements Concept {
	
	private Fish type;
	private Range range;
	private int kg;
	private int quality;
	//private Register owner;
	
	// Probar más tarde con el constructor que comprueba que la calidad es un valor entre 0 y 10 y, si se puedem comprobar en el set también
	
	@Slot(mandatory = true)
	public Fish getType() {
		return type;
	}
	public void setType(Fish type) {
		this.type = type;
	}
	@Slot(mandatory = true)
	public Range getRange() {
		return range;
	}
	public void setRange(Range range) {
		this.range = range;
	}
	@Slot(mandatory = true)
	public int getKg() {
		return kg;
	}
	public void setKg(int kg) {
		this.kg = kg;
	}
	@Slot(mandatory = true)
	public int getQuality() {
		return quality;
	}
	public void setQuality(int quality) {
		this.quality = quality;
	}
//	@Slot(mandatory = true)
//	public Register getOwner() {
//		return owner;
//	}
//	public void setOwner(Register owner) {
//		this.owner = owner;
//	}
	
	

}
