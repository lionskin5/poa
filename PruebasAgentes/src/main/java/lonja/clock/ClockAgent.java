package lonja.clock;

import commonAgents.MyAgent;
import commonBehaviours.FishSubsManager;
import commonBehaviours.FishSubsResponder;
import elements.clock.ClockParam;
import elements.clock.TICK;
import factories.FactoriesNames;
import factories.FactoryGlobal;
import factories.FactoryOntology;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.SubscriptionResponder;
import jade.proto.SubscriptionResponder.Subscription;
import loaders.LoaderManager;
import lonja.marketAgents.ServiceAgent;
import makers.ACLMaker;
import makers.MTMaker;
import utils.FMNames;

@SuppressWarnings("serial")
public class ClockAgent extends ServiceAgent {

	private ClockParam params;
	
	private FishSubsResponder responder;
	
	private FactoryOntology factClock;
	
	public ClockAgent() {
		super("simulated-time", "Lonja-Simulated-Time");
		factClock = (FactoryOntology) FactoryGlobal.getInstancia(FactoriesNames.CLOCKFACTORY);
	}
	
	@Override
	public String toString() {
		return "ClockAgentConfig [unitTimeMillis=" + params.getUnitTimeMillis() + ", numUnitDay=" + params.getNumUnitDay() + ", numSimDays="
				+ params.getNumSimDays() + "]";
	}
	
	public int getUnitTimeMillis() {
		return params.getUnitTimeMillis();
	}
	public int getNumUnitDay() {
		return params.getNumUnitDay();
	}
	public int getNumSimDays() {
		return this.params.getNumSimDays();
	}
	public FishSubsResponder getResponder() {
		return responder;
	}
	public void setResponder(FishSubsResponder responder) {
		this.responder = responder;
	}

	@Override
	public void setup() {
		
		System.out.println("Inicializando Clock");
		super.setup();
		
		Object[] args = getArguments();
		if (args != null && args.length == 1) {
			
			int cArgs [] = LoaderManager.getClockArguments((String) args[0]);
			
			int unitTimeMillis =cArgs[0];
			int numUnitDay = cArgs[1];
			int numSimDays = cArgs[2];
			this.params = new ClockParam();
			this.params.setUnitTimeMillis(unitTimeMillis);
			this.params.setNumUnitDay(numUnitDay);
			this.params.setNumSimDays(numSimDays);
			
			this.getContentManager().registerLanguage(this.getCodec()); // Creo que esto no hace falta
			this.getContentManager().registerOntology(factClock.getOnto());
			
			ClockTickerBehaviour clockBehaviour = new ClockTickerBehaviour(this, unitTimeMillis, numUnitDay, numSimDays);
			addBehaviour(clockBehaviour);
			
			responder = new ClockSubsResponder(this, MTMaker.createMT(SubscriptionResponder.createMessageTemplate(ACLMessage.SUBSCRIBE),
												this.getCodec().getName(), this.factClock.getOnto().getName()), new FishSubsManager());
			addBehaviour(responder);
		
	} else {
		System.out.println("Son necesarios 3 argumentos (<unitTimeMillis>,<numUnitDay>,<numSimDays>)");
		doDelete();
	}
		
		
		
	}
	
	@Override
	public void takeDown() {
		System.out.println("Terminando Clock");
		super.takeDown();
		try {
			DFService.deregister(this);
		} catch(FIPAException fe) {
			fe.printStackTrace();
		}
		
	}
	
	private class ClockSubsResponder extends FishSubsResponder {

		public ClockSubsResponder(Agent a, MessageTemplate mt, FishSubsManager csm) {
			super(a, mt, csm);
		}

		@Override
		public ACLMessage createAgree(ACLMessage subscription) {
			ACLMessage response = ACLMaker.createReponseWithContentConcept(subscription, ACLMessage.AGREE, myAgent, params); 
			return response;
		}
		
	}
	
	
	private class ClockTickerBehaviour extends TickerBehaviour {
		
		private int unitTimeMillis; // Número de milisegundos que forman una unidad de tiempo
		private int numUnitDay; // Número de unidades que forman un dÃ­a
		private int numSimDays; // Número de dÃ­as que durarÃ¡ la simulaciÃ³n
		
		private int time = 0; // contador de unidades de tiempo
		private int day = 0; // contador de días

		public ClockTickerBehaviour(Agent a, int unitTimeMillis, int numUnitDay, int numSimDays) {
			super(a, unitTimeMillis); // El periodo es unitTimeMillis
			this.unitTimeMillis = unitTimeMillis;
			this.numUnitDay = numUnitDay;
			this.numSimDays = numSimDays;
		}

		@Override
		protected void onTick() {
			System.out.println("TICK");
			System.out.println(ClockAgent.class.getName());
			System.out.println(ClockAgent.class.getCanonicalName());
			System.out.println(ClockAgent.class.getSimpleName());
			
			((MyAgent)this.getAgent()).info(FMNames.TICKER_B, FMNames.TICK);
			time += 1;
			if (numUnitDay <= time) {
				System.out.println("Un día más");
				time = 0;
				day += 1;
			}
			
			// Implementar si fin de la simulación
			
			notifySubsciptors(false);
			
			if (isSimEnd()) {
				System.out.println("FIN");
				getAgent().doDelete();
				
			}
			
		}
		
		public boolean isSimEnd() {
			return day >= numSimDays;
		}
		
		// Notificar del tick a los suscriptos
		private void notifySubsciptors (boolean end) {
			
			ACLMessage msg = ACLMaker.createMessageWithContentConceptNoSNoR(ACLMessage.INFORM, FIPANames.InteractionProtocol.FIPA_SUBSCRIBE, getCodec().getName()
																			, factClock.getOnto().getName(), ""+System.currentTimeMillis(), myAgent, new TICK());
			
			System.out.println(((FishSubsManager) ((ClockAgent) this.myAgent).getResponder().getMySubscriptionManager()).getSubs().size());
			for(Subscription subscription: ((FishSubsManager) responder.getMySubscriptionManager()).getSubs()) {
				subscription.notify(msg);
			}
		}
		
		
		
	}
	

}
