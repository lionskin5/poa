package commonBehaviours;

import jade.core.Agent;
import jade.lang.acl.ACLMessage;

@SuppressWarnings("serial")
public class RegisterBehaviour extends MarketAchieveInitiator {

	public RegisterBehaviour(Agent a, ACLMessage msg) {
		super(a, msg);
		System.out.println("Creando RegisterBehaviour: " + a.getClass().getName() +  "\n" + msg);
	}

	@Override
	protected void initiatorInfoPerformance(ACLMessage inform) {
		System.out.println("Registro hecho");
	}

	@Override
	protected void initiatorRefusePerformance(ACLMessage refuse) {
		System.out.println("Registro fallido");
	}

}
