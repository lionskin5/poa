package lonja.marketAgents;

import java.util.ArrayList;
import java.util.List;

import commonBehaviours.AuctionBehaviour;
import commonBehaviours.DFPhasesSubsBehaviour;
import commonBehaviours.DFServiceManager;
import commonBehaviours.FishSubsManager;
import commonBehaviours.FishSubsResponder;
import commonBehaviours.MarketAchieveInitiator;
import elements.auction.StartOfAuction;
import elements.lot.Lot;
import factories.FactoriesNames;
import factories.FactoryGlobal;
import factories.FactoryOntology;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.FIPANames;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.ProposeResponder;
import jade.proto.SubscriptionResponder;
import jade.proto.SubscriptionResponder.Subscription;
import makers.ACLMaker;
import makers.LotStorage;
import makers.MTMaker;

@SuppressWarnings("serial")
public class AuctioneerAgent extends ServiceAgent {
	
	
	private LotStorage lots;
	private List<Lot> auctionLots = new ArrayList<Lot>(); // Cambiar luego
	private Lot actualLot;
	private int actualPrice;
	private boolean endOfAuction;
	
	private FactoryOntology factAuct;
	private FactoryOntology factPhases;
	private FishSubsResponder responder;
	private AuctionBehaviour auctionFSM;
	
	public AuctioneerAgent() {
		super("lot-auctioneer-service", "Fish Market Lot Auctioneer");
		endOfAuction = false;
		factAuct = (FactoryOntology) FactoryGlobal.getInstancia(FactoriesNames.AUCTIONFACTORY);
		factPhases = (FactoryOntology) FactoryGlobal.getInstancia(FactoriesNames.PHASESFACTORY);
	}

	@Override
	public void setup() {
		super.setup();
		
		Object[] args = getArguments();
		
		this.getContentManager().registerOntology(factAuct.getOnto());
		
		if(args != null && args.length == 1) {
			this.lots = (LotStorage) args[0];
			responder = new AuctionnerSubsResponder(this, MTMaker.createMT(SubscriptionResponder.createMessageTemplate(ACLMessage.SUBSCRIBE)
												, this.getCodec().getName(), this.factAuct.getOnto().getName()), new FishSubsManager());
			
			this.getContentManager().registerOntology(factAuct.getOnto());
			this.getContentManager().registerOntology(factPhases.getOnto());
			
			
			
			this.addBehaviour(new DFPhasesSubsBehaviour(this, DFServiceManager.createSubscriptionMessage(this, getDefaultDF(), "phases-service")));
			
			// Creando el comportamiento de la subasta
			auctionFSM = new AuctionBehaviour(this);
			
			SecondState second = new SecondState(); 
			auctionFSM.registerFirstState( new FirstState(), "Start of Auction");
			auctionFSM.registerState(second, "CFP-INFORM");
			auctionFSM.registerState(new ThirdState(this), "Proposal Decisor"); // Meter el MT de la proposición
			auctionFSM.registerLastState(second, "CFP-INFORM");
			
			// En estos dos se pasa directamente
			auctionFSM.registerDefaultTransition("Start of Auction", "CFP-INFORM");
			auctionFSM.registerTransition("CFP-INFORM", "CFP-INFORM", 2);
			
			// Si el último estado devuelve cero se vuelve al estado dos. En el último parámetro
			// hay que poner los estados OneShot, seguramente. COMPROBAR
			// Creo que habría que reiniciar todos menos el tercero, incluido el último.
			// El tercero como es un Responder se reinicia solo, aunque no pueda ejecutarse en la máquina FSM.
			// El último también hay que reiniciarlo, es un OneShotBehaviour	
			
			this.addBehaviour(responder);
		//	this.addBehaviour(auctionFSM); // No hay que meterla sin más, si no cuando el de fase avise.
			
			
		}
			
		// Inscribe sus dos servicios al DF
		//DFServiceManager.register(this, "lot-auctioneer-service", "Fish Market Lot Auctioneer");	 Inscribir el otro aquí
		// Falta otro
			
			
		// Se inscribe al reloj o al director para que le avise
		// 	
			
		}
		
	private class AuctionnerSubsResponder extends FishSubsResponder {

		public AuctionnerSubsResponder(Agent a, MessageTemplate mt, FishSubsManager csm) {
			super(a, mt, csm);
		}

		@Override
		public ACLMessage createAgree(ACLMessage subscription) {
			ACLMessage response = ACLMaker.createResponse(subscription, ACLMessage.AGREE); // Aquí habrá que enviar los parámetros de la subasta
			return response;
		}
		
	}
	
//	private class DFPhasesSubsBehaviour extends DFSubsBehaviour {
//
//		public DFPhasesSubsBehaviour(Agent a, ACLMessage msg) {
//			super(a, msg);
//		}
//
//		@Override
//		public void agentPerfomance(DFAgentDescription[] dfds, ACLMessage inform) {
//			AID phaseAgent = dfds[0].getName();
//			
//			ACLMessage request = ACLMaker.createMessage(ACLMessage.SUBSCRIBE, myAgent.getAID()
//					, FIPANames.InteractionProtocol.FIPA_SUBSCRIBE
//					, phaseAgent, getCodec().getName()
//					, factPhases.getOnto().getName(), ""+System.currentTimeMillis());
//			
//			System.out.println("Me suscribo al Fases " + phaseAgent);
//			addBehaviour(new PhaseUpdaterBehaviour(myAgent, request));		
//		}
//	}
//	
//	private class PhaseUpdaterBehaviour extends FishSubsInitiator {
//
//		public PhaseUpdaterBehaviour(Agent a, ACLMessage msg) {
//			super(a, msg);
//		}
//
//		@Override
//		protected void agreeAgentPerfomance(ACLMessage agree) {
//			System.out.println("AGREE del fases en Subastador");
//			
//		}
//
//		@Override
//		protected void subAgentPerfomance(ACLMessage inform) {
//			
//			System.out.println("INFORM de Fase" + inform);
//			
//			
//		}
//		
//	}
//	
	private class LotRequest extends MarketAchieveInitiator {

		public LotRequest(Agent a, ACLMessage msg) {
			super(a, msg);
		}

		@Override
		protected void initiatorInfoPerformance(ACLMessage inform) {
			
		}

		@Override
		protected void initiatorRefusePerformance(ACLMessage refuse) {
			
		}		
		
	}
	// Los dos primeros estados no necesitan redefinir onEnd() ya que tienen transiciones directas
	private class FirstState extends OneShotBehaviour {

		@Override
		public void action() {
			StartOfAuction sto = new StartOfAuction();
			ACLMessage msg = ACLMaker.createMessageWithContentConceptNoReceiver(ACLMessage.INFORM, myAgent.getAID(), FIPANames.InteractionProtocol.FIPA_DUTCH_AUCTION, getCodec().getName()
																				, factAuct.getOnto().getName(), ""+System.currentTimeMillis(), myAgent, sto);
			
			for(Subscription subscription: ((FishSubsManager) responder.getMySubscriptionManager()).getSubs()) {
				System.out.println("Notificando start of auction");
				subscription.notify(msg);
				}
			
			if(!auctionLots.isEmpty())
				actualLot = auctionLots.remove(0); // Puede ser que salte excepción si la lista esta vacía
			
			}
	}
	
	private class SecondState extends OneShotBehaviour {

		List<ACLMessage> answers = new ArrayList<ACLMessage>();
		ACLMessage propose = null;
		private boolean end = false;
		
		
		MessageTemplate templateP = MTMaker.createMT(ACLMessage.PROPOSE, FIPANames.InteractionProtocol.FIPA_DUTCH_AUCTION, getCodec().getName(), factAuct.getOnto().getName());
		
		@Override
		public void action() {
			
			ACLMessage msg = null;
			
			if(actualLot == null) {
				end = true;		
//				msg = ACLMaker.createMessageWithContentConceptNoReceiver(ACLMessage.INFORM, myAgent.getAID(), FIPANames.InteractionProtocol.FIPA_DUTCH_AUCTION
//																		, getCodec().getName(), factAuct.getOnto().getName(), ""+System.currentTimeMillis(), myAgent, null);
				
				msg = ACLMaker.createMessage(ACLMessage.INFORM, myAgent.getAID(), FIPANames.InteractionProtocol.FIPA_DUTCH_AUCTION
						, getCodec().getName(), factAuct.getOnto().getName(), ""+System.currentTimeMillis());
			}
			
			// Hay que comprobar el precio antes de volver a pedir un lote
			// Aquí hay que comprobar el valor actual del lote y si se ha llegado al mínimo
			else if(true) {
				msg = ACLMaker.createMessageWithContentConceptNoReceiver(ACLMessage.CFP, myAgent.getAID(), FIPANames.InteractionProtocol.FIPA_DUTCH_AUCTION
						, getCodec().getName(), factAuct.getOnto().getName(), ""+System.currentTimeMillis(), myAgent, null);
			}
			
			// Hay que meterle el lote como parámetro. Hacerle un atributo y ponerle un set o pasarlo directamente en el constructor y el setUp
			for(Subscription subscription: ((FishSubsManager) responder.getMySubscriptionManager()).getSubs()) {
				System.out.println("CFP-1 Lote");
				subscription.notify(msg);
				}
			
			
			while((propose = myAgent.blockingReceive(templateP, 500)) != null) {
				System.out.println("Nuevo Propose");
				answers.add(propose);
			}
			
			for(ACLMessage prop : answers)
				myAgent.postMessage(prop);
			
			if(!answers.isEmpty()) {
				auctionFSM.registerDefaultTransition("CFP-INFORM", "Proposal Decisor"); // Transición directa entre el segundo estado y el tercero
				auctionFSM.registerDefaultTransition("Proposal Decisor", "CFP-INFORM");
			}
			
		}
		
		@Override
		public int onEnd() {	
			if(end) {
				return 5;
			}
			return 2;
		}
	}
	
	private class ThirdState extends ProposeResponder {
		
		private boolean accept = false; // Con esto debería de poder responder a todos

		public ThirdState(Agent a) {
			super(a, MTMaker.createMT(ACLMessage.PROPOSE, FIPANames.InteractionProtocol.FIPA_DUTCH_AUCTION, getCodec().getName(), factAuct.getOnto().getName()));
			System.out.println("ProposeResponder Inicializado");
		}
		
		// tiene un método para crear el mt en el agente
		@Override
		protected ACLMessage prepareResponse(ACLMessage propose) throws NotUnderstoodException, RefuseException {
			
			// Aquí hay que obtenerlos todos y responder al que antes llego con ciertas reglas. No vale con responder a cualquiera, no sabemos como se obtiene de la cola de mensajes cuando hay varios esperando
			// Para ello: Obtener la cola de mensajes y usar MessageQueue receive(MessageTemplate pattern)
			
			int bet = Integer.parseInt(propose.getContent()); // Esto será un concepto o AgentAction.
			ACLMessage msg = null; // Aquí se acepta el propose siempre y se devuelve accept-proposal. Por lo tanto, no hay reject-proposal. Bueno en el caso de un mensaje extraño.  
			
			
			System.out.println("Se ha aceptado ya? " + accept);
			
			if(bet == actualPrice && !accept) {
				
				msg = ACLMaker.createResponse(propose, ACLMessage.ACCEPT_PROPOSAL);
				auctionLots.remove(0);
				accept = true;
				
				if(auctionLots.size() == 0)
					endOfAuction = true;
				
				// Y envía mensaje al bancario para hacer transferencias y también cambia el owner del lote y también avisa al encargado de lotes que es el que cambia el owner
				
			}
			
			else {
				msg = ACLMaker.createResponse(propose, ACLMessage.REJECT_PROPOSAL);
			}
			
			auctionFSM.deregisterDefaultTransition("CFP-INFORM");
			auctionFSM.deregisterDefaultTransition("Proposal Decisor"); // Quita las transiciones pero, ¡no borra el estado!
			
			return msg;	
		}
	}
	
	private class LastState extends OneShotBehaviour {

		@Override
		public void action() {
			
			ACLMessage msg;
			
			if(endOfAuction) {
				msg = ACLMaker.createMessage(ACLMessage.INFORM, myAgent.getAID(), FIPANames.InteractionProtocol.FIPA_DUTCH_AUCTION
											, getCodec().getName(), factAuct.getOnto().getName(), ""+System.currentTimeMillis());	
			}
			
			else {
				msg = ACLMaker.createMessage(ACLMessage.CFP, myAgent.getAID(), FIPANames.InteractionProtocol.FIPA_DUTCH_AUCTION
						, getCodec().getName(), factAuct.getOnto().getName(), ""+System.currentTimeMillis());
				// Hay que meterle el lote como parámetro. Hacerle un atributo y ponerle un set o pasarlo directamente en el constructor y el setUp
			}

			for(Subscription subscription: ((FishSubsManager) responder.getMySubscriptionManager()).getSubs()) {
				System.out.println("CFP-2 OR [no-bids] INFORM");
				subscription.notify(msg);
				}	
			
			
			
		}

		@Override
		public int onEnd() {
			if (endOfAuction) {
				return 5;
			}
			return 0;
		}			
	}
	

}
