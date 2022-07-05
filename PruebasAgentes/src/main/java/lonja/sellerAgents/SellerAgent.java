package lonja.sellerAgents;

import java.util.List;

import commonAgents.ExternalAgent;
import commonBehaviours.DFPhasesSubsBehaviour;
import commonBehaviours.DFServiceManager;
import commonBehaviours.DFSubsBehaviour;
import commonBehaviours.MarketAchieveInitiator;
import elements.lot.Lot;
import elements.sea.RangeAction;
import elements.sea.RangePrices;
import factories.FactoriesNames;
import factories.FactoryGlobal;
import factories.FactoryOntology;
import jade.content.Concept;
import jade.content.ContentElement;
import jade.content.lang.Codec.CodecException;
import jade.content.onto.OntologyException;
import jade.content.onto.UngroundedException;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.core.Agent;
import jade.domain.FIPANames;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import loaders.LoaderManager;
import makers.ACLMaker;
import makers.MTMaker;

@SuppressWarnings("serial")
public class SellerAgent extends ExternalAgent {
	
	private AID lotReceptor;
	private AID seaAgent;
	private List<Lot> lots; // Pensar si esto va en ExternalAgent
	private RangePrices rangePrices;
	
	private FactoryOntology factSea;

	public AID getLotReceptor() {
		return lotReceptor;
	}

	public AID getSeaAgent() {
		return seaAgent;
	}

	@Override
	public void setup() {
		super.setup();
		
		Object [] args = getArguments();
		System.out.println("Seller arguments: " + args);
		if(args != null && args.length == 1 ) {
			
			this.setBudget(LoaderManager.getSellerArguments((String) args[0]));
			System.out.println("Seller budget: " + this.getBudget());
			
			factSea = (FactoryOntology) FactoryGlobal.getInstancia(FactoriesNames.SEAFACTORY);
			this.getContentManager().registerOntology(factSea.getOnto());
			
		//	addBehaviour(new DFLReceptorSubsBehaviour(this,  DFServiceManager.createSubscriptionMessage(this, getDefaultDF(), "lot-receptor-service")));
		//	this.addBehaviour(new DFPhasesSubsBehaviour(this, DFServiceManager.createSubscriptionMessage(this, getDefaultDF(), "phases-service")));
			addBehaviour(new DFSeaSubsBehaviour(this, DFServiceManager.createSubscriptionMessage(this, getDefaultDF(), "fish-service")));
			
//			// Creo que aquí hay que poner un  BlockingRecieve y el pattern. Simplemente bloquea al agente, pero cuando se desbloquee el DFBehaviourleerá ese mensaje.
//			// Se puede bloquear un comportamiento pero hay que hacerlo así, porque puede que llegue antes que se añadan los otros comportamientos y entonces bloqueamos un comportamiento hasta que llegue otro mensaje cualquiera
//			this.blockingReceive(null); // Suspende el agente hasta que llegue un mensaje tipo. Creo que cuando se reanuda sigue por el setup
//			
//			
//			DepositLotBehaviour beha = new DepositLotBehaviour(this, null);
//			// Cambiar la cifra
//			if(this.getBudget() > 10) {
//				addBehaviour(null);
//			
//		}
			
		}
		
		
	}
	
	private class DepositLotBehaviour extends MarketAchieveInitiator {

		public DepositLotBehaviour(Agent a, ACLMessage msg) {
			super(a, msg);
		}

		@Override
		protected void initiatorInfoPerformance(ACLMessage inform) {
		}

		@Override
		protected void initiatorRefusePerformance(ACLMessage refuse) {
		}
		
	}
	
	private class GetPaidBehaviour extends MarketAchieveInitiator {

		public GetPaidBehaviour(Agent a, ACLMessage msg) {
			super(a, msg);
		}

		@Override
		protected void initiatorInfoPerformance(ACLMessage inform) {	
		}

		@Override
		protected void initiatorRefusePerformance(ACLMessage refuse) {
		}
		
	}
	
	private class FishBehaviour extends MarketAchieveInitiator {

		public FishBehaviour(Agent a, ACLMessage msg) {
			super(a, msg);
		}

		@Override
		protected void initiatorInfoPerformance(ACLMessage inform) {
		}

		@Override
		protected void initiatorRefusePerformance(ACLMessage refuse) {
		}
		
	}	
	
	private class SeaRangeBehaviour extends MarketAchieveInitiator {

		public SeaRangeBehaviour(Agent a, ACLMessage msg) {
			super(a, msg);
		}

		@Override
		protected void initiatorInfoPerformance(ACLMessage inform) {
			
			// Hay que obtener el concepto RangePrices
			try {
				ContentElement ce = myAgent.getContentManager().extractContent(inform);
				Concept cc = null;
				if (ce instanceof Action) {
					cc = ((Action) ce).getAction();
					if(cc instanceof RangePrices) {
						rangePrices = (RangePrices) cc;
					}
				}
				System.out.println("RangePrices Seller: " + rangePrices.toString());
			} catch (UngroundedException e) {
				e.printStackTrace();
			} catch (CodecException e) {
				e.printStackTrace();
			} catch (OntologyException e) {
				e.printStackTrace();
			}
			
		}

		@Override
		protected void initiatorRefusePerformance(ACLMessage refuse) {
		}
		
	}	
	
	
	private class DFLReceptorSubsBehaviour extends DFSubsBehaviour {
		
		public DFLReceptorSubsBehaviour(Agent a, ACLMessage msg) {
			super(a, msg);
			System.out.println("Comportamiento DF Subscriptor de parte de: " + a.getClass().getName());
		}

		@Override
		public void agentPerfomance(DFAgentDescription[] dfds, ACLMessage inform) {
			lotReceptor = dfds[0].getName();
			System.out.println("Lot Receptor actualizado: " + lotReceptor);
			MessageTemplate mt = MTMaker.createMT(MessageTemplate.MatchPerformative(ACLMessage.INFORM), getCodec().getName(), ((FactoryOntology) FactoryGlobal.getInstancia(FactoriesNames.REGISTERFACTORY)).getOnto().getName());
		}	
		
	}
	
	private class DFSeaSubsBehaviour extends DFSubsBehaviour {

		public DFSeaSubsBehaviour(Agent a, ACLMessage msg) {
			super(a, msg);
			System.out.println("Sea Search Seller");
		}

		@Override
		public void agentPerfomance(DFAgentDescription[] dfds, ACLMessage inform) {
			seaAgent = dfds[0].getName();
			System.out.println("Agente Mar actualizado: " + seaAgent);
			MessageTemplate mt = MTMaker.createMT(MessageTemplate.MatchPerformative(ACLMessage.INFORM), getCodec().getName(), ((FactoryOntology) FactoryGlobal.getInstancia(FactoriesNames.SEAFACTORY)).getOnto().getName());
			//System.out.println("Suscrito al agente Mar: " + seaAgent);
			
			ACLMessage seaAgentRequest = ACLMaker.createMessage(ACLMessage.REQUEST, myAgent.getAID(), FIPANames.InteractionProtocol.FIPA_REQUEST, seaAgent, getCodec().getName(), factSea.getOnto().getName(), ""+System.currentTimeMillis());
			
			try {
				myAgent.getContentManager().fillContent(seaAgentRequest, new Action(myAgent.getAID(), new RangeAction()));
				} catch (CodecException e) {
					e.printStackTrace();
				} catch (OntologyException e) {
					e.printStackTrace();
				}
			System.out.println("Range Sea Request: " + seaAgentRequest);
			myAgent.addBehaviour(new SeaRangeBehaviour(myAgent, seaAgentRequest));
			
			
		}
		

	}
	

}
