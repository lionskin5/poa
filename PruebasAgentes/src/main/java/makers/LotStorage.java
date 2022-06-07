package makers;

import java.util.ArrayList;
import java.util.List;

import elements.lot.IsOwner;
import elements.lot.Lot;
import elements.register.Register;
import jade.core.AID;

public class LotStorage {
	
	private List<Lot> lots; // Quizá no es necesario guardas los lotes ya que ya se encuentran en owners. Pero se puede guardar para hacer queries sobre atributos de los lotes
	private List<IsOwner> owners;
	
	public LotStorage() {
		this.lots = new ArrayList<Lot>();
		this.owners = new ArrayList<IsOwner>();
	}
	
	public List<Lot> getLots() {
		return lots;
	}
	
	public List<IsOwner> getOwners() {
		return owners;
	}
	
	public void addLot(Lot l) {
		lots.add(l);
	}
	
	// En vez de con lote debería de ser de otra manera
	public void deleteLot(Lot l) {
		lots.remove(l);
	}
	
	public void addIsOwner(Lot lot, Register rt) {
		this.addIsOwner(new IsOwner(lot, rt));
	}
	
	public void addIsOwner(IsOwner io) {
		owners.add(io);
	}
	
	public void deleteIsOwner(IsOwner io) {
		owners.remove(io);
	}
	
	public void changeOwner(Lot l, Register owner) {
		for(IsOwner o: this.owners) {
			if(o.getLot() == l) // Si no me equivoco esto comprueba id, que es lo que queremos (puede haber dos lotes distintos pero iguales)
				o.setOwner(owner);
		}		
	}
	
	public List<Lot> getLotsFromAgent(AID agent) {
		
		List<Lot> agentLots = new ArrayList<Lot>();
		
		for(IsOwner o: this.owners) {
			if(o.isAgentOwner(agent))
				agentLots.add(o.getLot());
		}
		
		return agentLots;
		
	}

}
