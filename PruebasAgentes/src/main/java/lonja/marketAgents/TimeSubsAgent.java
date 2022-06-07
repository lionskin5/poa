package lonja.marketAgents;

import commonAgents.MyAgent;
import commonBehaviours.DFServiceManager;
import commonBehaviours.DFSubsBehaviour;
import commonBehaviours.FishSubsInitiator;
import elements.clock.ClockParam;
import elements.clock.TICK;
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
import makers.ACLMaker;

// Pensar si borrarla. Ha sido necesaria meterla a Director debido al agente de las fases

@SuppressWarnings("serial")
public class TimeSubsAgent extends MyAgent {
	
	private FactoryOntology factClock;
	private int numUnitDay;
	
	public TimeSubsAgent() {
		factClock = (FactoryOntology) FactoryGlobal.getInstancia(FactoriesNames.CLOCKFACTORY);
	}
	
	public int getNumUnitDay() {
		return numUnitDay;
	}

	public void setup() {
		System.out.println("Pidiendo al DF el reloj");
		super.setup();
		
		this.getContentManager().registerOntology(factClock.getOnto());
		
		// Nos suscribimos al DF para recibir el AID del reloj	
		addBehaviour(new DFClockSubsBehaviour(this, DFServiceManager.createSubscriptionMessage(this, getDefaultDF(), "simulated-time")));
		
	}
	
	
	private class DFClockSubsBehaviour extends DFSubsBehaviour {
		
		public DFClockSubsBehaviour(Agent a, ACLMessage msg) {
			super(a, msg);
		}

		@Override
		public void agentPerfomance(DFAgentDescription[] dfds, ACLMessage inform) {
				AID clockAgent = dfds[0].getName();
				ACLMessage request = ACLMaker.createMessage(ACLMessage.SUBSCRIBE, myAgent.getAID()
															, FIPANames.InteractionProtocol.FIPA_SUBSCRIBE
															, clockAgent, getCodec().getName()
															, factClock.getOnto().getName(), ""+System.currentTimeMillis());
				//ACLMessage request = new ACLMessage(ACLMessage.SUBSCRIBE);
				//request.setProtocol(FIPANames.InteractionProtocol.FIPA_SUBSCRIBE);
				//request.setReplyWith(""+System.currentTimeMillis()); // En vez de "" añadir una función abstracta que implementará cada hijo
				//request.addReceiver(clockAgent);
				
				System.out.println("El SUBSCRIBE: " + request.toString());
				System.out.println("Me suscribo al reloj");
				myAgent.addBehaviour(new ClockUpdaterBehaviour(getAgent(), request));
		}	
		
	}
	
	private class ClockUpdaterBehaviour extends FishSubsInitiator {

		public ClockUpdaterBehaviour(Agent a, ACLMessage msg) {
			super(a, msg);
		}
		
		@Override
		protected void agreeAgentPerfomance(ACLMessage agree) {
			
			ClockParam params = null;
			
			try {
				ContentElement ce = myAgent.getContentManager().extractContent(agree);
				Concept cc = null;
				if (ce instanceof Action) {
					cc = ((Action) ce).getAction();
					if(cc instanceof ClockParam) {
						params = (ClockParam) cc;
					}
				}
			} catch (UngroundedException e) {
				e.printStackTrace();
			} catch (CodecException e) {
				e.printStackTrace();
			} catch (OntologyException e) {
				e.printStackTrace();
			}
			
			numUnitDay = params.getNumUnitDay();
			System.out.println("Actualizo numUnitDay " + numUnitDay);
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
			
		}		
	}

		
	

}
