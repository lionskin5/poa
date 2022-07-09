package lonja.marketAgents;

import java.util.LinkedList;
import java.util.List;

import commonBehaviours.ClockUpdaterBehaviour;
import commonBehaviours.DFClockSubsBehaviour;
import commonBehaviours.DFPhasesSubsBehaviour;
import commonBehaviours.DFServiceManager;
import commonBehaviours.FishSubsInitiator;
import commonBehaviours.FishSubsManager;
import commonBehaviours.FishSubsResponder;
import elements.activities.Sleep;
import elements.activities.WakeUp;
import elements.auction.StartOfAuction;
import elements.clock.ClockParam;
import elements.clock.TICK;
import elements.phases.Phase;
import elements.phases.PhaseNotification;
import factories.FactoriesNames;
import factories.FactoryAgent;
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
import jade.core.behaviours.WakerBehaviour;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.AchieveREResponder;
import jade.proto.SubscriptionResponder;
import jade.proto.SubscriptionResponder.Subscription;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;
import loaders.LoaderManager;
import makers.ACLMaker;
import makers.LotStorage;
import makers.MTMaker;
import makers.RegisterStorage;
import utils.PhasesNames;

@SuppressWarnings("serial")
public class Director extends ServiceAgent {
	
	// Los agentes. Se le deben de pasar los agentes a crear en un fichero
	private int numberOfLanes;	
	private FactoryAgent fact;
	private FactoryOntology factPhases;
	private FactoryOntology factClock;
	private AID phaseAgent; 
	private int numUnitDay;
	
	private List<AgentController> marketAgents;
	private boolean agentStart;
	
	private FishSubsResponder responder;
	
	public Director() {
		super("agent-activity-service", "Agent Activity Agent");
		factClock = (FactoryOntology) FactoryGlobal.getInstancia(FactoriesNames.CLOCKFACTORY);
		factPhases = (FactoryOntology) FactoryGlobal.getInstancia(FactoriesNames.PHASESFACTORY);
		this.fact = (FactoryAgent) FactoryGlobal.getInstancia(FactoriesNames.AGENTFACTORY);
	}
	
	public int getNumberOfLanes() {
		return numberOfLanes;
	}

	@Override
	public void setup() {
		super.setup();
		System.out.println("Setup Director");
		System.out.println("Pidiendo al DF el reloj");
		
		this.getContentManager().registerOntology(factPhases.getOnto());
		this.getContentManager().registerOntology(factClock.getOnto());
		
		marketAgents = new LinkedList<AgentController>();
		agentStart = false;
		
		Object [] args = getArguments();
		if (args != null && args.length == 1) {
			this.numberOfLanes = LoaderManager.getDirectorArgument((String) args[0]);
			System.out.println("Número de líneas: " + this.numberOfLanes);
			
			// Nos suscribimos al DF para recibir el AID del reloj	
			addBehaviour(new CreateAgentsBehaviour());
			addBehaviour(new DirectorClockSubs(this, DFServiceManager.createSubscriptionMessage(this, getDefaultDF(), "simulated-time")));
			this.addBehaviour(new PhaseSubscription(this, DFServiceManager.createSubscriptionMessage(this, getDefaultDF(), "phases-service")));
		//	this.addBehaviour(responder);
		}
		
	}
	
	@Override
	public void takeDown() {
		
		System.out.println("Terminando Director");
		// Aquí habría que destruir a todos los agentes
		
		
	}
	
	private class CreateAgentsBehaviour extends OneShotBehaviour {
		@Override
		public void action() {
			System.out.println("Creando agentes de la lonja");
			// Aquí la factoría ya tiene el AgentContainer con la plataforma deseada
			
			FactoryOntology ontoFact = (FactoryOntology) FactoryGlobal.getInstancia(FactoriesNames.LOTFACTORY);
			
			LotStorage ls = new LotStorage();
			RegisterStorage rs = new RegisterStorage();
			Object[] args = {rs};
			Object [] args2 = {ls};
			Object[] args3 = {ls, rs, MTMaker.createMT(AchieveREResponder.createMessageTemplate(FIPANames.InteractionProtocol.FIPA_REQUEST), getCodec().getName(), ontoFact.getOnto().getName())};
			Object [] args4 = {ls, rs, MTMaker.createMTWithMatchExpr(AchieveREResponder.createMessageTemplate(FIPANames.InteractionProtocol.FIPA_REQUEST), getCodec().getName(), ontoFact.getOnto().getName(), "ChangeOwner")};
			Object[] args5 = {ls, rs, MTMaker.createMT(AchieveREResponder.createMessageTemplate(FIPANames.InteractionProtocol.FIPA_QUERY), getCodec().getName(), ontoFact.getOnto().getName())};
			// Ninguno de estos tres requiere argumentos
			
			
			marketAgents.add(fact.createAgent2("Registrador", lonja.marketAgents.RegisterAgent.class.getName(), args));
			
			marketAgents.add(fact.createAgent2("Banco", lonja.marketAgents.BankAgent.class.getName(), null));
			marketAgents.add(fact.createAgent2("Subastador", lonja.marketAgents.AuctioneerAgent.class.getName(), args2)); // Este puede que requiera argumentos más adelante
			
			marketAgents.add(fact.createAgent2("Receptor de Lotes", lonja.marketAgents.LotReceptorAgent.class.getName(), args3));
			marketAgents.add(fact.createAgent2("Repartidor de Lotes", lonja.marketAgents.LotDeliverymanAgent.class.getName(), args4));
			
			marketAgents.add(fact.createAgent2("Encargado de Lotes", lonja.marketAgents.LotManagerAgent.class.getName(), args5));		
			
			fact.createAgent("AgenteFases", lonja.marketAgents.PhaseAgent.class.getName(), null);
			
		}

	}
	
	private void startAgents() {
		for(AgentController agent: marketAgents) {
			try {
				System.out.println("Empezando agente " + agent.getName());
				agent.start();
			} catch (StaleProxyException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void wakeUp() {
		for(AgentController agent: marketAgents) {
			try {
				System.out.println("Activando agente " + agent.getName());
				agent.activate();
			} catch (StaleProxyException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void sleep() {
		for(AgentController agent: marketAgents) {
			try {
				System.out.println("Durmiendo agente " + agent.getName());
				agent.suspend();
			} catch (StaleProxyException e) {
				e.printStackTrace();
			}
		}
	}
	
	private class DirectorClockSubs extends DFClockSubsBehaviour {

		public DirectorClockSubs(Agent a, ACLMessage msg) {
			super(a, msg);
		}

		@Override
		public void behaviourPerfomance(ACLMessage request) {
			myAgent.addBehaviour(new DirectorClockUpdater(getAgent(), request));
		}
		
	}
	
	private class DirectorClockUpdater extends ClockUpdaterBehaviour {

		public DirectorClockUpdater(Agent a, ACLMessage msg) {
			super(a, msg);
		}

		@Override
		public void agreeBehaviourPerfomance(ClockParam params) {
			numUnitDay = params.getNumUnitDay();
			System.out.println("Actualizo numUnitDay " + numUnitDay);
		//	addBehaviour(new CreateAgentsBehaviour());
		}
		
		@Override
		protected void subAgentPerfomance(ACLMessage inform) {
			System.out.println("Llega un inform");
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
			
			//String content = inform.getContent();
			//System.out.println("El contenido del reloj es: " + content);
			//Aquí habría que comprobar si se ha terminado, y en el caso del director necesita controlar si se ha pasado de fase
			
			if(tick != null) {
				
			}
			
		}
		
	}
	
	private class PhaseSubscription extends DFPhasesSubsBehaviour {

		public PhaseSubscription(Agent a, ACLMessage msg) {
			super(a, msg);
		}

		@Override
		public void subsPhase(ACLMessage request) {
			System.out.println("Me suscribo al Fases " + phaseAgent);
			getAgent().addBehaviour(new PhaseUpdaterBehaviour(myAgent, request));
		}	
	}
	
	public class PhaseUpdaterBehaviour extends FishSubsInitiator {

		public PhaseUpdaterBehaviour(Agent a, ACLMessage msg) {
			super(a, msg);
		}

		@Override
		protected void agreeAgentPerfomance(ACLMessage agree) {
			System.out.println("AGREE del fases en Director");
		}

		@Override
		protected void subAgentPerfomance(ACLMessage inform) {
			
			System.out.println("Director recibe una fase " + inform.getContent());
			
			PhaseNotification fase = null;
			
			try {
				ContentElement ce = myAgent.getContentManager().extractContent(inform);
				Concept cc = null;
				if (ce instanceof Action) {
					cc = ((Action) ce).getAction();
					if(cc instanceof PhaseNotification) {
						fase = (PhaseNotification) cc;
						}
					}
				} catch (UngroundedException e) {
					e.printStackTrace();
				} catch (CodecException e) {
					e.printStackTrace();
				} catch (OntologyException e) {
					e.printStackTrace();
				}
			
			checkPhase(fase);
			
		}
		
		private void checkPhase(PhaseNotification notif) {
			
			String fase = notif.getPhase();
			boolean start = notif.isStart();
			
			if(PhasesNames.FISHPHASE.equals(fase)) {
				if(start) {
					System.out.println("Director envía Wake Up");
					
					if(agentStart) {
						wakeUp();
					}
					else {
						startAgents();
						agentStart = true;
					}
				}
				return;
			}
			if(PhasesNames.TAKEOUTPHASE.equals(fase)) {
				if(!start) {
					System.out.println("Director envía Sleep");
					sleep();
				}
				return;
			}
			
		}

	}

}
