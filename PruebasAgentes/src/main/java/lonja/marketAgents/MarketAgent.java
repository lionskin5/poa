package lonja.marketAgents;

import commonAgents.MyAgent;
import commonBehaviours.DFServiceManager;
import commonBehaviours.DFSubsBehaviour;
import commonBehaviours.FishSubsInitiator;
import elements.activities.Sleep;
import elements.activities.WakeUp;
import factories.FactoriesNames;
import factories.FactoryGlobal;
import factories.FactoryOntology;
import jade.content.Concept;
import jade.content.ContentElement;
import jade.content.lang.Codec.CodecException;
import jade.content.onto.OntologyException;
import jade.content.onto.UngroundedException;
import jade.content.onto.basic.Action;
import jade.core.Agent;
import jade.domain.FIPANames;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.lang.acl.ACLMessage;
import makers.ACLMaker;

@SuppressWarnings("serial")
public class MarketAgent extends ServiceAgent {
	
	private FactoryOntology factAgentAct;
	
	public MarketAgent(String serviceType, String serviceName) {
		super(serviceType, serviceName);
		factAgentAct = (FactoryOntology) FactoryGlobal.getInstancia(FactoriesNames.AGENTACTIVITYFACTORY);
	}
	
	@Override
	public void setup() {
		super.setup();
		this.getContentManager().registerOntology(factAgentAct.getOnto());
		addBehaviour(new DirectorSubscription(this,  DFServiceManager.createSubscriptionMessage(this, getDefaultDF(), "agent-activity-service")));
		
	}
	
	private class DirectorSubscription extends DFSubsBehaviour {

		public DirectorSubscription(Agent a, ACLMessage msg) {
			super(a, msg);
		}

		@Override
		public void agentPerfomance(DFAgentDescription[] dfds, ACLMessage inform) {	
			System.out.println("AID del director: " + dfds[0].getName());
			ACLMessage request = ACLMaker.createMessage(ACLMessage.SUBSCRIBE, myAgent.getAID()
														, FIPANames.InteractionProtocol.FIPA_SUBSCRIBE
														, dfds[0].getName(), ((MyAgent) myAgent).getCodec().getName()
														, ((FactoryOntology) FactoryGlobal.getInstancia(FactoriesNames.AGENTACTIVITYFACTORY)).getOnto().getName(), ""+System.currentTimeMillis());
				
			addBehaviour(new AgentActivityBehaviour(myAgent, request));
		}
	
	}
	
	public class AgentActivityBehaviour extends FishSubsInitiator {

		public AgentActivityBehaviour(Agent a, ACLMessage msg) {
			super(a, msg);
		}

		@Override
		protected void agreeAgentPerfomance(ACLMessage agree) {
			System.out.println("AGREE de actividad del Director");
		}

		@Override
		protected void subAgentPerfomance(ACLMessage inform) {
			System.out.println("Recibo una orden del director " + inform.getContent());
			
			Sleep sleep = null;
			WakeUp wakeUp = null;
			
			try {
				ContentElement ce = myAgent.getContentManager().extractContent(inform);
				Concept cc = null;
				if (ce instanceof Action) {
					cc = ((Action) ce).getAction();
					if(cc instanceof Sleep) {
						sleep = (Sleep) cc;
					}
					else if(cc instanceof WakeUp) {
						wakeUp = (WakeUp) cc;
					}
					}
				} catch (UngroundedException e) {
					e.printStackTrace();
				} catch (CodecException e) {
					e.printStackTrace();
				} catch (OntologyException e) {
					e.printStackTrace();
				}
			
			
		}

	}
	
}
