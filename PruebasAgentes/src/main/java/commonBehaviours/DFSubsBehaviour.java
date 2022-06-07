package commonBehaviours;

import commonAgents.MyAgent;
import jade.core.Agent;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.lang.acl.ACLMessage;
import jade.proto.SubscriptionInitiator;
import utils.FMNames;

@SuppressWarnings("serial")
public abstract class DFSubsBehaviour extends SubscriptionInitiator {

	public DFSubsBehaviour(Agent a, ACLMessage msg) {
		super(a, msg);
		((MyAgent)this.getAgent()).info(FMNames.DF_SUBS_B, FMNames.SERVSUBS + " " + msg.getContent()); // Crear un utils para obtener una parte del AClMessage y también para formar este strings
	}
	
	@Override
	protected void handleAgree(ACLMessage agree) {
		System.out.println("Mensaje AGREE de" + myAgent.getClass().getName() + " Sender: " + agree.getSender().toString());	// Para hacer el protocolo completo
		((MyAgent)this.getAgent()).info(FMNames.DF_SUBS_B, FMNames.AGREE);
	}
	
	@Override
	protected void handleRefuse(ACLMessage refuse) {
		this.myAgent.doDelete(); // Si el DF no deja al agente preguntarle, este debe morir
		((MyAgent)this.getAgent()).info(FMNames.DF_SUBS_B, FMNames.REFUSE);
	}
	
	@Override
	protected void handleInform(ACLMessage inform) {
		System.out.println("INFORM del DF" + inform.toString());
		DFAgentDescription[] dfds = DFServiceManager.decodeNotification(inform);
		if(dfds.length > 0) {
			this.agentPerfomance(dfds, inform);
			cancel(inform.getSender(), true); // El sender siempre será el DF con el que nos hemos comunicado. Envía un cancel para dejar de estar subscrito. Este debe de matar el behaviour
		}
		((MyAgent)this.getAgent()).info(FMNames.DF_SUBS_B, FMNames.INFO + " " + inform.getContent());
	}
	
	// Patrón no se qué, aquí cada hijo implementará lo que se hace con el inform
	public abstract void agentPerfomance(DFAgentDescription[] dfds, ACLMessage inform);

}
