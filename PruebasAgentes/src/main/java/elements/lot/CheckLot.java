package elements.lot;

import elements.register.Register;
import jade.content.AgentAction;
import jade.content.onto.annotations.AggregateResult;

@SuppressWarnings("serial")
@AggregateResult(type = Lot.class) // Espero que esto este bien
public class CheckLot implements AgentAction {
// Seguramente en esta clase haya que hacer un IRE más adelante. No lo sé.
	
	private Fish type;
	private int quality;
	private Register owner;
	
	public Fish getType() {
		return type;
	}
	public void setType(Fish type) {
		this.type = type;
	}
	public int getQuality() {
		return quality;
	}
	public void setQuality(int quality) {
		this.quality = quality;
	}
	public Register getOwner() {
		return owner;
	}
	public void setOwner(Register owner) {
		this.owner = owner;
	}

}
