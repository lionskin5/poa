package lonja.marketAgents;

import elements.lot.BringLot;
import jade.content.Concept;
import jade.content.ContentElement;
import jade.content.lang.Codec.CodecException;
import jade.content.onto.OntologyException;
import jade.content.onto.UngroundedException;
import jade.content.onto.basic.Action;
import jade.lang.acl.ACLMessage;
import makers.ACLMaker;

@SuppressWarnings("serial")
public class LotReceptorAgent extends LotAgent {
	
	public LotReceptorAgent() {
		super("lot-receptor-service", "Lot Receptor");
	}

	@Override
	protected ACLMessage lotAgentPerfomance(ACLMessage request) {
		
		ACLMessage msg = null;
		System.out.println("Nuevo lote del vendedor: " + request.getSender().toString());
		
		try {
			ContentElement ce = this.getContentManager().extractContent(request);
			Concept cc = null;
			if (ce instanceof Action) {
				cc = ((Action) ce).getAction();
				if(cc instanceof BringLot) {
					BringLot bl = (BringLot) cc;
					System.out.println("Ha llegado una acción BringLot: " + bl.toString());
					this.addLot(bl.getLot());
					this.addIsOwner(bl.getLot(), request.getSender());
					msg = ACLMaker.createResponse(request, ACLMessage.INFORM);
				}
				
			}
		} catch (UngroundedException e) {
			e.printStackTrace();
		} catch (CodecException e) {
			e.printStackTrace();
		} catch (OntologyException e) {
			e.printStackTrace();
		}
		return msg;
	}
	
	
}
