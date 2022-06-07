package lonja.marketAgents;

import commonBehaviours.MarketAchieveResponder;
import elements.lot.ChangeOwner;
import elements.lot.LotList;
import elements.lot.TakeAwayLot;
import jade.content.Concept;
import jade.content.ContentElement;
import jade.content.lang.Codec.CodecException;
import jade.content.onto.OntologyException;
import jade.content.onto.UngroundedException;
import jade.content.onto.basic.Action;
import jade.core.Agent;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import makers.ACLMaker;
import makers.MTMaker;

@SuppressWarnings("serial")
public class LotDeliverymanAgent extends LotAgent {

	public LotDeliverymanAgent() {
		super("lot-delivery", "Lonja-lot-delivery");
	}
	
	@Override
	public void setup() {
		super.setup();
		addBehaviour(new LotRequestBehaviour(this, MTMaker.createMTWithMatchExpr(MarketAchieveResponder.createMessageTemplate(FIPANames.InteractionProtocol.FIPA_REQUEST)
													, this.getCodec().getName(), this.getLotOntoName(), "TakeAwayLot")));
	}
	
	private class LotRequestBehaviour extends MarketAchieveResponder {

		public LotRequestBehaviour(Agent a, MessageTemplate mt) {
			super(a, mt);
		}

		@Override
		protected ACLMessage responderPerfomance(ACLMessage request) {
			
			ACLMessage msg = null;
			
			try {
				ContentElement ce = myAgent.getContentManager().extractContent(request);
				Concept cc = null;
				if (ce instanceof Action) {
					cc = ((Action) ce).getAction();
					if(cc instanceof TakeAwayLot) {
						TakeAwayLot tal = (TakeAwayLot) cc;
						System.out.println("Ha llegado una acción TakeAwayLot: " + tal.toString());
						LotList ll = new LotList(((LotAgent) myAgent).getLotsFromAgent(request.getSender()));			
						msg = ACLMaker.createReponseWithContentConcept(request, ACLMessage.INFORM, myAgent, ll);
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

	@Override
	protected ACLMessage lotAgentPerfomance(ACLMessage request) {
		
		System.out.println("Cambio de propietario de un lote. Lo envía: " + request.getSender().toString());
		ACLMessage msg = null;
		// Igual todo esto se puede factorizar. O igual no, para cuando se envíen conceptos. Si se llegan a envíar. El otro debe de envíar lotes aunque igual hacer un parser/maker con dos funciones
		try {
			ContentElement ce = this.getContentManager().extractContent(request);
			Concept cc = null;
			if (ce instanceof Action) {
				cc = ((Action) ce).getAction();
				if(cc instanceof ChangeOwner) {
					ChangeOwner co = (ChangeOwner) cc;
					System.out.println("Ha llegado una acción ChangeOwner: " + co.toString());
					
					// Introducir un lote y enviar un Inform
					this.changeOwner(co.getLot(), co.getNewOwner());
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
