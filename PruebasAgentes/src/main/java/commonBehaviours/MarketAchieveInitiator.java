package commonBehaviours;

import commonAgents.MyAgent;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import jade.proto.AchieveREInitiator;
import utils.FMNames;

@SuppressWarnings("serial")
public abstract class MarketAchieveInitiator extends AchieveREInitiator {

	// Los Initiator no se deben de resetear, si no la CPU solo se encargará en enviar mensajes
	// Los Initiator hay que añadirlos explícitamente. Si siempre son iguales, se puede guardar una copia
	public MarketAchieveInitiator(Agent a, ACLMessage msg) {
		super(a, msg);
		((MyAgent)this.getAgent()).info(FMNames.REQ_QUERY_B, FMNames.REQUEST + " " + msg.getContent());
	}

	@Override
	protected void handleInform(ACLMessage inform) {
		((MyAgent)this.getAgent()).info(FMNames.REQ_QUERY_B, FMNames.INFO + " " + inform.getContent());
		initiatorInfoPerformance(inform);
	}
	
	@Override
	protected void handleRefuse(ACLMessage refuse) {
		initiatorRefusePerformance(refuse);
	}
	
	protected abstract void initiatorInfoPerformance(ACLMessage inform);
	protected abstract void initiatorRefusePerformance(ACLMessage refuse);
	
}
