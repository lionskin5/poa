package lonja.marketAgents;

import java.util.List;

import commonAgents.MyAgent;
import commonBehaviours.ClockUpdaterBehaviour;
import commonBehaviours.DFClockSubsBehaviour;
import commonBehaviours.DFServiceManager;
import commonBehaviours.FishSubsManager;
import commonBehaviours.FishSubsResponder;
import commonBehaviours.PhasesSubsManager;
import elements.auction.LotCFP;
import elements.clock.ClockParam;
import elements.clock.TICK;
import elements.phases.Phase;
import elements.phases.PhaseNotification;
import factories.FactoriesNames;
import factories.FactoryGlobal;
import factories.FactoryOntology;
import jade.content.ContentElement;
import jade.content.lang.Codec.CodecException;
import jade.content.onto.OntologyException;
import jade.content.onto.UngroundedException;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.SubscriptionResponder;
import jade.proto.SubscriptionResponder.Subscription;
import makers.ACLMaker;
import makers.MTMaker;
import utils.FMNames;
import utils.PhasesNames;

@SuppressWarnings("serial")
public class PhaseAgent extends ServiceAgent {
	
	private int numberTicks;
	private int numUnitDay;
	private Phase fishPhase;
	private Phase lotMoneyPhase;
	private Phase subsAuctionPhase;
	private Phase auctionPhase;
	private Phase takeOutPhase;
	
	private FactoryOntology factPhases;
	private FactoryOntology factClock;
	
	private FishSubsResponder responder;

	public PhaseAgent() {
		super("phases-service", "Fish Market Phases");
		this.fishPhase = new Phase();
		this.lotMoneyPhase = new Phase();
		this.subsAuctionPhase = new Phase();
		this.auctionPhase = new Phase();
		this.takeOutPhase = new Phase();
		
		this.fishPhase.setName(PhasesNames.FISHPHASE);
		this.lotMoneyPhase.setName(PhasesNames.LOTMONEYPHASE);
		this.subsAuctionPhase.setName(PhasesNames.SUBSAUCTIONPHASE);
		this.auctionPhase.setName(PhasesNames.AUCTIONPHASE);
		this.takeOutPhase.setName(PhasesNames.TAKEOUTPHASE);
		
		factPhases = (FactoryOntology) FactoryGlobal.getInstancia(FactoriesNames.PHASESFACTORY);
		factClock = (FactoryOntology) FactoryGlobal.getInstancia(FactoriesNames.CLOCKFACTORY);
		
	}
	
	@Override
	public void setup() {
		
		super.setup();
		
		System.out.println("Creo el agente fases");
		this.getContentManager().registerOntology(factPhases.getOnto());
		this.getContentManager().registerOntology(factClock.getOnto());
			
		addBehaviour(new PhaseClockSubs(this, DFServiceManager.createSubscriptionMessage(this, getDefaultDF(), "simulated-time")));
		responder = new PhasesSubsResponder(this, MTMaker.createMT(SubscriptionResponder.createMessageTemplate(ACLMessage.SUBSCRIBE), getCodec().getName(), factPhases.getOnto().getName()), new PhasesSubsManager());
		addBehaviour(responder);
			
	}
	
	private void setUpPhases() {
		
		System.out.println("Set Up Phases");
		int half = (int) (1d/2*this.numUnitDay);
		this.fishPhase.setStart(0);
		System.out.println("setupAgentes " + half);
		this.fishPhase.setEnd((int) (half + (1d/3*half)));
		
		this.lotMoneyPhase.setStart((int) (1d/6*this.numUnitDay));
		this.lotMoneyPhase.setEnd(half-1);
		
		this.subsAuctionPhase.setStart((int) (1d/6*this.numUnitDay));
		this.subsAuctionPhase.setEnd(half-1);
		
		this.auctionPhase.setStart(half);
		this.auctionPhase.setEnd((int) ((5d/6*this.numUnitDay)-1));
		
		this.takeOutPhase.setStart((int) (5d/6*this.numUnitDay));
		this.takeOutPhase.setEnd((int) (9d/10*this.numUnitDay));
		
		System.out.println("Fases inicializadas: " + this.toString());
		
	}
	
	private void checkPhases() {
		
		// Añadir comportamientos o enviar mensajes
		if(numberTicks == fishPhase.getStart()) {
			System.out.println("Enviar FishStart");
			notifyPhase(((PhasesSubsManager) responder.getMySubscriptionManager()).getDirectorSubs(), fishPhase, true);
		}
			
		if(numberTicks == fishPhase.getEnd()) {
			System.out.println("Enviar FishEnd");
			notifyPhase(((PhasesSubsManager) responder.getMySubscriptionManager()).getDirectorSubs(), fishPhase, false);
		}
			
		
		if(numberTicks == lotMoneyPhase.getStart())
			System.out.println("Enviar LotMoneyStart");
		if(numberTicks == lotMoneyPhase.getEnd())
			System.out.println("Enviar LotMoneyEnd");
		
		if(numberTicks == subsAuctionPhase.getStart())
			System.out.println("Enviar SubAucStart");
		if(numberTicks == subsAuctionPhase.getEnd())
			System.out.println("Enviar SubAucEnd");
		
		if(numberTicks == auctionPhase.getStart())
			System.out.println("Enviar AuctionStart");
		if(numberTicks == auctionPhase.getEnd())
			System.out.println("Enviar AuctionEnd");
		
		if(numberTicks == takeOutPhase.getStart()) {
			System.out.println("Enviar TakeOutStart");
		}

		if(numberTicks == takeOutPhase.getEnd()) {
			System.out.println("Enviar TakeOutEnd");
			notifyPhase(((PhasesSubsManager) responder.getMySubscriptionManager()).getDirectorSubs(), takeOutPhase, false);
		}

	}
	
	private void notifyPhase(List<Subscription> subs, Phase phase, boolean start) {
		for(Subscription subscription: subs) {
			ACLMessage msg = createMessagePhase(phase, start);
			System.out.println("Notificando fase a:" + subscription.getMessage().getSender());
			subscription.notify(msg);
		}
	}
	
	private ACLMessage createMessagePhase(Phase phase, boolean start) {
		
		PhaseNotification noti = new PhaseNotification();
		noti.setPhase(phase.getName());
		noti.setStart(start);
		
		return ACLMaker.createMessageWithContentConceptNoSNoR(ACLMessage.INFORM, FIPANames.InteractionProtocol.FIPA_SUBSCRIBE, getCodec().getName()
				, factPhases.getOnto().getName(), ""+System.currentTimeMillis(), this, noti);
	}
	
	private class PhasesSubsResponder extends FishSubsResponder {

		public PhasesSubsResponder(Agent a, MessageTemplate mt, SubscriptionManager csm) {
			super(a, mt, csm);
			}

		@Override
		public ACLMessage createAgree(ACLMessage subscription) {
			return ACLMaker.createResponse(subscription, ACLMessage.AGREE);
		}
		
	}
	
	private class PhaseClockSubs extends DFClockSubsBehaviour {

		public PhaseClockSubs(Agent a, ACLMessage msg) {
			super(a, msg);
		}

		@Override
		public void behaviourPerfomance(ACLMessage request) {
			myAgent.addBehaviour(new PhaseClockUpdater(getAgent(), request));
		}
		
	}
	
	private class PhaseClockUpdater extends ClockUpdaterBehaviour {

		public PhaseClockUpdater(Agent a, ACLMessage msg) {
			super(a, msg);
		}

		@Override
		public void agreeBehaviourPerfomance(ClockParam params) {
			numUnitDay = params.getNumUnitDay();
			System.out.println("Actualizo numUnitDay " + numUnitDay);
			setUpPhases();
			checkPhases();
		}

		@Override
		protected void subAgentPerfomance(ACLMessage inform) {
			System.out.println("Fases TICK");
			TICK tick = null;
			
			try { // Aquí es necesario recuperar el objeto Action, y a partir de éste obtener la accion y hacerle un casting a TICK
				ContentElement ce = myAgent.getContentManager().extractContent(inform);
				if (ce instanceof Action) {
					tick = (TICK) ((Action) ce).getAction();
				}
				System.out.println("Ha llegado un TICK: " + tick.toString());
			} catch (UngroundedException e) {
				e.printStackTrace();
			} catch (CodecException e) {
				e.printStackTrace();
			} catch (OntologyException e) {
				e.printStackTrace();
			}
			
			numberTicks++;
			System.out.println("Número de ticks: " + numberTicks);
			checkPhases();
			
		}		
		
	}
	
	
	@Override
	public String toString() {
		return "[numUnitDay=" + numUnitDay + ",\n" + 
				"fishPhase=" + fishPhase.toString() + ",\n" + 
				"lotMoneyPhase=" + lotMoneyPhase.toString() + ",\n" + 
				"subsAuctionPhase=" + subsAuctionPhase.toString() + ",\n" + 
				"auctionPhase=" + auctionPhase.toString() + ",\n" + 
				"takeOutPhase=" + takeOutPhase.toString() + "]"; 
	}

}
