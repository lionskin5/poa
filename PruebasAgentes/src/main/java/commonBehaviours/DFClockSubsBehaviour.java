package commonBehaviours;

import commonAgents.MyAgent;
import factories.FactoriesNames;
import factories.FactoryGlobal;
import factories.FactoryOntology;
import jade.core.AID;
import jade.core.Agent;
import jade.domain.FIPANames;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.lang.acl.ACLMessage;
import makers.ACLMaker;

@SuppressWarnings("serial")
public abstract class DFClockSubsBehaviour extends DFSubsBehaviour {

	public DFClockSubsBehaviour(Agent a, ACLMessage msg) {
		super(a, msg);
	}

	@Override
	public void agentPerfomance(DFAgentDescription[] dfds, ACLMessage inform) {
			AID clockAgent = dfds[0].getName();
			ACLMessage request = ACLMaker.createMessage(ACLMessage.SUBSCRIBE, myAgent.getAID()
														, FIPANames.InteractionProtocol.FIPA_SUBSCRIBE
														, clockAgent, ((MyAgent) myAgent).getCodec().getName()
														, ((FactoryOntology) FactoryGlobal.getInstancia(FactoriesNames.CLOCKFACTORY)).getOnto().getName()
														, ""+System.currentTimeMillis());
			
			System.out.println("El SUBSCRIBE: " + request.toString());
			System.out.println("Me suscribo al reloj");
			behaviourPerfomance(request);
	}
	
	public abstract void behaviourPerfomance(ACLMessage request);
	
}
