package commonBehaviours;

import commonAgents.MyAgent;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import jade.proto.SubscriptionInitiator;
import utils.FMNames;

@SuppressWarnings("serial")
public abstract class FishSubsInitiator extends SubscriptionInitiator {

	public FishSubsInitiator(Agent a, ACLMessage msg) {
		super(a, msg);
		((MyAgent)this.getAgent()).info(FMNames.SUBS_B, FMNames.SERVSUBS + " " + msg.getAllReceiver().next());
	}
	
	@Override
	protected void handleAgree(ACLMessage agree) {
		System.out.println("Mensaje AGREE recibido. Sender: " + agree.getSender().toString());	// Para hacer el protocolo completo
		agreeAgentPerfomance(agree);
	}
	
	@Override
	protected void handleRefuse(ACLMessage refuse) {
		this.myAgent.doDelete(); // Si no puedo subscribirme a algún agente no podré realizar mi cometido
	}
	
	@Override
	protected void handleInform(ACLMessage inform) {
		System.out.println("Llega un inform " + inform);
		subAgentPerfomance(inform);
	}
	
	protected abstract void agreeAgentPerfomance(ACLMessage agree);
	protected abstract void subAgentPerfomance(ACLMessage inform);

}
