package commonBehaviours;

import jade.core.Agent;
import jade.lang.acl.ACLMessage;

// Esta creo que no puede estar fuera
@SuppressWarnings("serial")
public class PhaseUpdaterBehaviour extends FishSubsInitiator {

	public PhaseUpdaterBehaviour(Agent a, ACLMessage msg) {
		super(a, msg);
	}

	@Override
	protected void agreeAgentPerfomance(ACLMessage agree) {
		System.out.println("AGREE del fases en Subastador");
		
	}

	@Override
	protected void subAgentPerfomance(ACLMessage inform) {
		System.out.println("INFORM de Fase" + inform);
	}

}
