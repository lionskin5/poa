package commonBehaviours;

import jade.core.Agent;
import jade.lang.acl.ACLMessage;

@SuppressWarnings("serial")
public class MoneyTransferBehaviour extends MarketAchieveInitiator {

	public MoneyTransferBehaviour(Agent a, ACLMessage msg) {
		super(a, msg);
	}

	@Override
	protected void initiatorInfoPerformance(ACLMessage inform) {
		
	}

	@Override
	protected void initiatorRefusePerformance(ACLMessage refuse) {
		
	}
	
	
}
