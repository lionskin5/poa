package lonja.marketAgents;

import java.security.acl.Owner;

import commonAgents.MyAgent;
import elements.lot.BringLot;
import elements.lot.IsOwner;
import jade.content.Concept;
import jade.content.ContentElement;
import jade.content.abs.AbsIRE;
import jade.content.abs.AbsPredicate;
import jade.content.lang.Codec.CodecException;
import jade.content.lang.sl.SL2Vocabulary;
import jade.content.onto.OntologyException;
import jade.content.onto.UngroundedException;
import jade.content.onto.basic.Action;
import jade.lang.acl.ACLMessage;
import makers.ACLMaker;

@SuppressWarnings("serial")
public class LotManagerAgent extends LotAgent {
		
	public LotManagerAgent() {
		super("lot-querys", "Lonja-lot-querys");
	}

	
	// Está mal. Este Agente se encarga de informar sobre los lotes, no de recibirlos. Recibe mensajes de los compradores y devuelve listas de los lotes que se piden.
	@Override
	protected ACLMessage lotAgentPerfomance(ACLMessage request) {
		
		System.out.println("Requerimiento de información sobre lotes");
		ACLMessage msg = null;
		
		String quantifier = null;
		
		
		try {
			ContentElement ce = this.getContentManager().extractContent(request);
			AbsIRE ire = null;
			if (ce instanceof AbsIRE) {
				ire = (AbsIRE) ce;
				quantifier = ire.getTypeName();
				if(quantifier.equals(SL2Vocabulary.ALL)) {
					
					AbsPredicate predicate = (AbsPredicate) ire.getProposition();
					
					
					IsOwner isOwner = (IsOwner) getLotOnto().toObject(predicate);
					
					System.out.println("Preguntar por los lotes del agente: " + isOwner.getOwner().getMemberAID());
					
					// A partir del isOwner, obtener todos los lotes y añadirlos al mensaje.
					
					
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
