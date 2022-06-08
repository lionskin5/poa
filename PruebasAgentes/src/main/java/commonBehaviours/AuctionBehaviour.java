package commonBehaviours;

import jade.core.Agent;
import jade.core.behaviours.FSMBehaviour;

@SuppressWarnings("serial")
public class AuctionBehaviour extends FSMBehaviour {
	public AuctionBehaviour(Agent a) {
		super(a);
	}
	
	@Override
	public int onEnd() {
		System.out.println("AuctionBehaviour (FSM) terminado. Agente: " + this.myAgent.getName());
		return super.onEnd();
	}
}
