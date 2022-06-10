package lonja.buyerAgents;

import commonAgents.ExternalAgent;
import commonAgents.MyAgent;
import commonBehaviours.AuctionBehaviour;
import commonBehaviours.DFPhasesSubsBehaviour;
import commonBehaviours.DFServiceManager;
import commonBehaviours.DFSubsBehaviour;
import commonBehaviours.FishSubsInitiator;
import commonBehaviours.MarketAchieveInitiator;
import elements.auction.EndOfAuction;
import elements.auction.LotCFP;
import elements.auction.StartOfAuction;
import elements.bank.BankAccount;
import elements.bank.RegisterConvParam;
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
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.FIPANames;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.lang.acl.ACLMessage;
import jade.proto.states.MsgReceiver;
import loaders.LoaderManager;
import lonja.marketAgents.BankAgent;
import makers.ACLMaker;
import makers.MTMaker;


@SuppressWarnings("serial")
public class BuyerAgent extends ExternalAgent {
	
	private AID auctioneerAgent;
	private AID clientAgent;
	private AuctionBehaviour auctionFSM;
	private boolean lotAdquired;
	private int numberLotsAdquired;
	
	private FactoryOntology factAuct;
	
	public BuyerAgent() {
		factAuct = (FactoryOntology) FactoryGlobal.getInstancia(FactoriesNames.AUCTIONFACTORY);
		lotAdquired = false;
		numberLotsAdquired = 0;
	}
	
	@Override
	public void setup() {
		super.setup();
		
		Object [] args = getArguments();
		System.out.println("Buyer arguments: " + args);
		if(args != null && args.length == 1 ) {
			
			this.setBudget(LoaderManager.getBuyerArguments((String) args[0]));
			System.out.println("Buyer budget: " + this.getBudget());
			this.getContentManager().registerOntology(factAuct.getOnto());
		
			addBehaviour(new DFLAuctioneerSubsBehaviour(this,  DFServiceManager.createSubscriptionMessage(this, getDefaultDF(), "lot-auctioneer-service")));
			//addBehaviour(new DFClientSubsBehaviour(this, DFServiceManager.createSubscriptionMessage(this, getDefaultDF(), "fish-selling-service")));
			//this.addBehaviour(new DFPhasesSubsBehaviour(this, DFServiceManager.createSubscriptionMessage(this, getDefaultDF(), "phases-service")));
			
//			SecondState secondState = new SecondState(this);
//			
//			auctionFSM = new AuctionBehaviour(this);
//			auctionFSM.registerFirstState(new FirstState(this), "Waiting SOA");
//			auctionFSM.registerState(secondState, "Waiting CFP-INFORM");
//			// Este estado tiene unas transiciones dependientes de la situación. De momento es un estado inalcanzable
//			auctionFSM.registerState(new ThirdsState(this), "Waiting Proposal Response");
//			auctionFSM.registerLastState(secondState, "Waiting CFP-INFORM"); // Quizá se puede usar el mismo Behaviour, aunque a lo mejor no es conveniente		
//			// Creo que el primero y el último deben de ser el mismo objeto
//			
//			// Las transiciones
//			auctionFSM.registerDefaultTransition("Waiting SOA", "Waiting CFP-INFORM");
//			auctionFSM.registerTransition("Waiting CFP-INFORM", "Waiting CFP-INFORM", 2);
			
			auctionFSM = new AuctionBehaviour(this);
			auctionFSM.registerFirstState(new FirstState(this), "Waiting SOA");
			auctionFSM.registerLastState(new LastState(this), "Waiting EOA"); // Quizá se puede usar el mismo Behaviour, aunque a lo mejor no es conveniente
			auctionFSM.registerDefaultTransition("Waiting SOA", "Waiting EOA");
			
		}		
	}
	
	

	// Esto deben de estar como clase única
	private class DepositBehaviour extends MarketAchieveInitiator {

		public DepositBehaviour(Agent a, ACLMessage msg) {
			super(a, msg);
		}

		@Override
		protected void initiatorInfoPerformance(ACLMessage inform) {
		}

		@Override
		protected void initiatorRefusePerformance(ACLMessage refuse) {
		}
		
	}
	
	private class WithdrawBehaviour extends MarketAchieveInitiator {

		public WithdrawBehaviour(Agent a, ACLMessage msg) {
			super(a, msg);
		}

		@Override
		protected void initiatorInfoPerformance(ACLMessage inform) {
		}

		@Override
		protected void initiatorRefusePerformance(ACLMessage refuse) {
		}
		
	}
	
	private class DFLAuctioneerSubsBehaviour extends DFSubsBehaviour {
		
		public DFLAuctioneerSubsBehaviour(Agent a, ACLMessage msg) {
			super(a, msg);
		}

		@Override
		public void agentPerfomance(DFAgentDescription[] dfds, ACLMessage inform) {
			auctioneerAgent =  dfds[0].getName();
			System.out.println("Subastador actualizado: " + auctioneerAgent);
			
			ACLMessage request = ACLMaker.createMessage(ACLMessage.SUBSCRIBE, myAgent.getAID()
														, FIPANames.InteractionProtocol.FIPA_SUBSCRIBE
														, auctioneerAgent, ((MyAgent) myAgent).getCodec().getName()
														, ((FactoryOntology) FactoryGlobal.getInstancia(FactoriesNames.AUCTIONFACTORY)).getOnto().getName(), ""+System.currentTimeMillis());
				
			addBehaviour(new AuctionSubscribe(myAgent, request));
		}	
		
	}
	
	private class DFClientSubsBehaviour extends DFSubsBehaviour {
				public DFClientSubsBehaviour(Agent a, ACLMessage msg) {
			super(a, msg);
			System.out.println("Cliente Subs Behav");
		}

		@Override
		public void agentPerfomance(DFAgentDescription[] dfds, ACLMessage inform) {
			clientAgent = dfds[0].getName();
			
			System.out.println("Cliente actualizado: " + clientAgent);
			
			ACLMessage request = ACLMaker.createMessage(ACLMessage.SUBSCRIBE, myAgent.getAID()
					, FIPANames.InteractionProtocol.FIPA_SUBSCRIBE
					, clientAgent, ((MyAgent) myAgent).getCodec().getName()
					, ((FactoryOntology) FactoryGlobal.getInstancia(FactoriesNames.CLIENTFACTORY)).getOnto().getName(), ""+System.currentTimeMillis());
			
		}


	}
	
	private class AuctionSubscribe extends FishSubsInitiator {

		public AuctionSubscribe(Agent a, ACLMessage msg) {
			super(a, msg);
		}

		@Override
		protected void subAgentPerfomance(ACLMessage inform) {
			System.out.println("Mensaje del subastador: " + inform);
		}

		@Override
		protected void agreeAgentPerfomance(ACLMessage agree) {
			// Obtener los parámetros de la subasta
			System.out.println("Agree del Subastador");
			addBehaviour(auctionFSM);
		}
		
	}
	
	private class FirstState extends MsgReceiver {

		public FirstState(Agent a) {
			super(a, MTMaker.createMT(ACLMessage.INFORM, FIPANames.InteractionProtocol.FIPA_DUTCH_AUCTION, getCodec().getName(), factAuct.getOnto().getName())
					, MsgReceiver.INFINITE, null, null);
		}
		
		@Override
		protected void handleMessage(ACLMessage msg) {
			
			
			StartOfAuction soa = null;
			System.out.println("Inform recibido del Auctioneer: " + msg.getSender());
			
			try {
				ContentElement ce = myAgent.getContentManager().extractContent(msg);
				Concept cc = null;
				if (ce instanceof Action) {
					cc = ((Action) ce).getAction();
					if(cc instanceof StartOfAuction) {
						soa = (StartOfAuction) cc;
					}
				}
			} catch (UngroundedException e) {
				e.printStackTrace();
			} catch (CodecException e) {
				e.printStackTrace();
			} catch (OntologyException e) {
				e.printStackTrace();
			}
			
			if(soa != null) {
				System.out.println("Start Of Auction recibido correctamente");
				// Me preparo para el comienzo de la subasta
				// Ingreso dinero por ejemplo según los lotes que haya
			}
			
			else {
				// Algo como desuscribirse
			}
			
		}
		
	}
	
	private class ProposeCFP extends MsgReceiver {
		
		private int buyLote = 0;

		public ProposeCFP(Agent a) {			
			super(a, MTMaker.createMT(ACLMessage.CFP, FIPANames.InteractionProtocol.FIPA_DUTCH_AUCTION, getCodec().getName(), factAuct.getOnto().getName())
					, MsgReceiver.INFINITE, null, null); // En un principio siempre recibiremos el propose.		
		}
		
		@Override
		protected void handleMessage(ACLMessage msg) {
			
			ACLMessage response = null;
			LotCFP lot = null;
			
			
			try {
				ContentElement ce = myAgent.getContentManager().extractContent(msg);
				Concept cc = null;
				if (ce instanceof Action) {
					cc = ((Action) ce).getAction();
					if(cc instanceof LotCFP) {
						lot = (LotCFP) cc;
						}
					}
			} catch (UngroundedException e) {
				e.printStackTrace();
			} catch (CodecException e) {
				e.printStackTrace();
			} catch (OntologyException e) {
				e.printStackTrace();
			}	
			
			System.out.println("Lote: " + lot.getLot() + " " + lot.getPrice());
		}		
		
	}
	
	private class SendPropose extends
	
	private class LastState extends MsgReceiver {
		
		public LastState(Agent a) {
			super(a, MTMaker.createMT(ACLMessage.INFORM, FIPANames.InteractionProtocol.FIPA_DUTCH_AUCTION, getCodec().getName(), factAuct.getOnto().getName())
					, MsgReceiver.INFINITE, null, null);
		}
		
		@Override
		protected void handleMessage(ACLMessage msg) {
			
			EndOfAuction eoa = null;
			System.out.println("Inform recibido del Auctioneer: " + msg.getSender());
			
			try {
				ContentElement ce = myAgent.getContentManager().extractContent(msg);
				Concept cc = null;
				if (ce instanceof Action) {
					cc = ((Action) ce).getAction();
					if(cc instanceof EndOfAuction) {
						eoa = (EndOfAuction) cc;
					}
				}
			} catch (UngroundedException e) {
				e.printStackTrace();
			} catch (CodecException e) {
				e.printStackTrace();
			} catch (OntologyException e) {
				e.printStackTrace();
			}
			
			if(eoa != null) {
				System.out.println("End Of Auction recibido correctamente");				
				// Fin de la subasta
			}
			
			else {
				// Algo como desuscribirse
			}
			
		}
		
		@Override
		public int onEnd() {
//			AuctionBehaviour auctionFSM = new AuctionBehaviour(myAgent);
//			auctionFSM.registerFirstState(new FirstState(myAgent), "Waiting SOA");
//			auctionFSM.registerLastState(new LastState(myAgent), "Waiting EOA"); // Quizá se puede usar el mismo Behaviour, aunque a lo mejor no es conveniente
//			auctionFSM.registerDefaultTransition("Waiting SOA", "Waiting EOA");
//			myAgent.addBehaviour(auctionFSM);
			return super.onEnd();
		}
			
	}
	
	private class SecondState extends MsgReceiver {
		
		private boolean endAuction;
		
		public SecondState(Agent a) {
			super(a, MTMaker.createMTOR(ACLMessage.CFP, ACLMessage.INFORM, FIPANames.InteractionProtocol.FIPA_DUTCH_AUCTION, getCodec().getName(), factAuct.getOnto().getName())
					, MsgReceiver.INFINITE, null, null);
			endAuction = false;
		}
		
		@Override
		protected void handleMessage(ACLMessage msg) {
			
			ACLMessage response;
			// Obtengo el contenido
			
			// Termina la subasta, meto otro behaviour si tengo que recoger lotes o si tengo que sacar dinero
			if(msg.getPerformative() == ACLMessage.INFORM) {
				endAuction = true;
				
			}
			
			else {
			
				System.out.println("CFP recibido del Auctioneer: " + msg);
				
				// Si me gusta el contenido envío un proposal
				// Si no entiendo el contenido envío un not-understood
				if(true) {
					response = ACLMaker.createReponseWithContentConcept(msg, ACLMessage.PROPOSE, myAgent, null); // Añadir un concepto de la subasta
					myAgent.send(response);
					auctionFSM.registerDefaultTransition("Waiting CFP-INFORM", "Waiting Proposal Response"); // Transición directa entre el segundo estado y el tercero
					auctionFSM.registerDefaultTransition("Waiting Proposal Response", "Waiting CFP-INFORM"); // Transición directa entre el tercer estado y el segundo. Tras recibir la respuesta volvemos a esperar un CFP-INFORM
					// Estas transiciones hay que borrarlas en el tercer estado
					}
				
				// Si no me gusta paso reseteo este estado
				else {
					System.out.println("Reseteando CFP-INFORM");
					// No hay que hacer nada. Pues ya hay una transición a sí mismo
	//				auctionFSM.registerState(new ThirdsState(myAgent), "Waiting Proposal Response");
	//				auctionFSM.registerDefaultTransition(null, null); // Transición directa entre este estado y el siguiente
				}
				
				// Si envío una propuesta añado un behaviour para recibir el accept or reject
				// Si envío propuesat añado ThirsState/LastState esperando una respuesta
				// Si no lo añado esperando un nuevo CFP o INFORM
				
				// Si envío un propose tengo que añadir un estado intermediario antes del final y quitar la transición de este al final y añadir dos transiciones
				// De este al intermediario y del instermediario al final
			
			}
			
		}
		
		
		public int onEnd() {
			if(endAuction)
				return 5;
			
			return 2;
			
		}
		
	}
	
	// LastState. Creo que el segundo es a la vez LastState pero como dos objetos distintos
	private class ThirdsState extends MsgReceiver {
		
		public ThirdsState(Agent a) {
			super(a, MTMaker.createMTOR(ACLMessage.ACCEPT_PROPOSAL, ACLMessage.REJECT_PROPOSAL, FIPANames.InteractionProtocol.FIPA_DUTCH_AUCTION, getCodec().getName(), factAuct.getOnto().getName())
					, MsgReceiver.INFINITE, null, null);
		}
		
		@Override
		protected void handleMessage(ACLMessage msg) {
			if(msg.getPerformative() == ACLMessage.ACCEPT_PROPOSAL && lotAdquired)
				lotAdquired = true;
			if(msg.getPerformative() == ACLMessage.ACCEPT_PROPOSAL)
				numberLotsAdquired++;
			
			auctionFSM.deregisterDefaultTransition("Waiting CFP-INFORM");
			auctionFSM.deregisterDefaultTransition("Waiting Proposal Response"); // Quita las transiciones pero, ¡no borra el estado!
			
		}
		
	}
	
	private class SellBehaviour extends MarketAchieveInitiator {

		public SellBehaviour(Agent a, ACLMessage msg) {
			super(a, msg);
		}

		@Override
		protected void initiatorInfoPerformance(ACLMessage inform) {
			
		}

		@Override
		protected void initiatorRefusePerformance(ACLMessage refuse) {
			
		}
		
	}
	
	


}
