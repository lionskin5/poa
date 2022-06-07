package commonBehaviours;

import commonAgents.ExternalAgent;
import jade.core.Agent;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.lang.acl.ACLMessage;

// Este comportamiento si va a parte porque también lo necesita el registrador
@SuppressWarnings("serial")
public class DFBankSubsBehaviour extends DFSubsBehaviour {
	
	public DFBankSubsBehaviour(Agent a, ACLMessage msg) {
		super(a, msg);
	}

	@Override
	public void agentPerfomance(DFAgentDescription[] dfds, ACLMessage inform) {
		((ExternalAgent) myAgent).setBankAgent(dfds[0].getName());
	}

}
