package lonja.marketAgents;

import commonAgents.MyAgent;
import commonBehaviours.ClockUpdaterBehaviour;
import commonBehaviours.DFClockSubsBehaviour;
import commonBehaviours.DFServiceManager;
import elements.clock.ClockParam;
import elements.clock.TICK;
import factories.FactoriesNames;
import factories.FactoryAgent;
import factories.FactoryGlobal;
import factories.FactoryOntology;
import jade.content.ContentElement;
import jade.content.lang.Codec.CodecException;
import jade.content.onto.OntologyException;
import jade.content.onto.UngroundedException;
import jade.content.onto.basic.Action;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.WakerBehaviour;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.proto.AchieveREResponder;
import loaders.LoaderManager;
import makers.LotStorage;
import makers.MTMaker;
import makers.RegisterStorage;

@SuppressWarnings("serial")
public class Director extends MyAgent {
	
	// Los agentes. Se le deben de pasar los agentes a crear en un fichero
	private int numberOfLanes;	
	private FactoryAgent fact;
	private FactoryOntology factClock;
	private int numUnitDay;
	
	public Director() {
		super();
		factClock = (FactoryOntology) FactoryGlobal.getInstancia(FactoriesNames.CLOCKFACTORY);
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
		
		this.getContentManager().registerOntology(factClock.getOnto());
		
		Object [] args = getArguments();
		if (args != null && args.length == 1) {
			this.numberOfLanes = LoaderManager.getDirectorArgument((String) args[0]);
			System.out.println("Número de líneas: " + this.numberOfLanes);
			
			// Nos suscribimos al DF para recibir el AID del reloj	
			addBehaviour(new DirectorClockSubs(this, DFServiceManager.createSubscriptionMessage(this, getDefaultDF(), "simulated-time")));
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
			fact.createAgent("Registrador", lonja.marketAgents.RegisterAgent.class.getName(), args);
			fact.createAgent("Banco", lonja.marketAgents.BankAgent.class.getName(), null);
			fact.createAgent("Subastador", lonja.marketAgents.AuctioneerAgent.class.getName(), args2); // Este puede que requiera argumentos más adelante
			
			fact.createAgent("Receptor de Lotes", lonja.marketAgents.LotReceptorAgent.class.getName(), args3);
			fact.createAgent("Repartidor de Lotes", lonja.marketAgents.LotDeliverymanAgent.class.getName(), args4);
			
			fact.createAgent("Encargado de Lotes", lonja.marketAgents.LotManagerAgent.class.getName(), args5);
			fact.createAgent("AgenteFases", lonja.marketAgents.PhaseAgent.class.getName(), null);
			
		}

	}
	
	private class WakeUpAgentsBehaviour extends WakerBehaviour {

		public WakeUpAgentsBehaviour(Agent a, long timeout) {
			super(a, timeout);
		}	
	}
	
	private class SleepAgentsBehaviour extends WakerBehaviour {

		public SleepAgentsBehaviour(Agent a, long timeout) {
			super(a, timeout);
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
			addBehaviour(new CreateAgentsBehaviour());
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
	
//	private class DFClockSubsBehaviour extends DFSubsBehaviour {
//		
//		public DFClockSubsBehaviour(Agent a, ACLMessage msg) {
//			super(a, msg);
//		}
//
//		@Override
//		public void agentPerfomance(DFAgentDescription[] dfds, ACLMessage inform) {
//				AID clockAgent = dfds[0].getName();
//				ACLMessage request = ACLMaker.createMessage(ACLMessage.SUBSCRIBE, myAgent.getAID()
//															, FIPANames.InteractionProtocol.FIPA_SUBSCRIBE
//															, clockAgent, getCodec().getName()
//															, factClock.getOnto().getName(), ""+System.currentTimeMillis());
//				//ACLMessage request = new ACLMessage(ACLMessage.SUBSCRIBE);
//				//request.setProtocol(FIPANames.InteractionProtocol.FIPA_SUBSCRIBE);
//				//request.setReplyWith(""+System.currentTimeMillis()); // En vez de "" añadir una función abstracta que implementará cada hijo
//				//request.addReceiver(clockAgent);
//				
//				System.out.println("El SUBSCRIBE: " + request.toString());
//				System.out.println("Me suscribo al reloj");
//				myAgent.addBehaviour(new ClockUpdaterBehaviour(getAgent(), request));
//		}	
//		
//	}
//	
//	private class ClockUpdaterBehaviour extends FishSubsInitiator {
//
//		public ClockUpdaterBehaviour(Agent a, ACLMessage msg) {
//			super(a, msg);
//		}
//		
//		@Override
//		protected void agreeAgentPerfomance(ACLMessage agree) {
//			
//			ClockParam params = null;
//			
//			try {
//				ContentElement ce = myAgent.getContentManager().extractContent(agree);
//				Concept cc = null;
//				if (ce instanceof Action) {
//					cc = ((Action) ce).getAction();
//					if(cc instanceof ClockParam) {
//						params = (ClockParam) cc;
//					}
//				}
//			} catch (UngroundedException e) {
//				e.printStackTrace();
//			} catch (CodecException e) {
//				e.printStackTrace();
//			} catch (OntologyException e) {
//				e.printStackTrace();
//			}
//			
//			numUnitDay = params.getNumUnitDay();
//			System.out.println("Actualizo numUnitDay " + numUnitDay);
//			addBehaviour(new CreateAgentsBehaviour());
//		}
//
//		@Override
//		protected void subAgentPerfomance(ACLMessage inform) {
//			System.out.println("Llega un inform");
//			TICK tick = null;
//			
//			try { // Aquí es necesario recuperar el objeto Action, y a partir de éste obtener la accion y hacerle un casting a TICK
//				ContentElement ce = myAgent.getContentManager().extractContent(inform);
//				if (ce instanceof Action) {
//					tick = (TICK) ((Action) ce).getAction();
//				}
//				System.out.println("Ha llegado un TICK: " + tick.toString());
//			} catch (UngroundedException e) {
//				e.printStackTrace();
//			} catch (CodecException e) {
//				e.printStackTrace();
//			} catch (OntologyException e) {
//				e.printStackTrace();
//			}
//			
//			//String content = inform.getContent();
//			//System.out.println("El contenido del reloj es: " + content);
//			//Aquí habría que comprobar si se ha terminado, y en el caso del director necesita controlar si se ha pasado de fase
//			
//		}		
//	}

}
