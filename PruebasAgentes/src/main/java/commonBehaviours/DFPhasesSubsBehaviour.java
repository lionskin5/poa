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
public class DFPhasesSubsBehaviour extends DFSubsBehaviour {

	public DFPhasesSubsBehaviour(Agent a, ACLMessage msg) {
		super(a, msg);
	}

	@Override
	public void agentPerfomance(DFAgentDescription[] dfds, ACLMessage inform) {
		AID phaseAgent = dfds[0].getName();
		
		ACLMessage request = ACLMaker.createMessage(ACLMessage.SUBSCRIBE, myAgent.getAID()
				, FIPANames.InteractionProtocol.FIPA_SUBSCRIBE
				, phaseAgent, ((MyAgent) getAgent()).getCodec().getName()
				, ((FactoryOntology) FactoryGlobal.getInstancia(FactoriesNames.PHASESFACTORY)).getOnto().getName()
				, ""+System.currentTimeMillis());
		
		System.out.println("Me suscribo al Fases " + phaseAgent);
		getAgent().addBehaviour(new PhaseUpdaterBehaviour(myAgent, request));		
	}

}
