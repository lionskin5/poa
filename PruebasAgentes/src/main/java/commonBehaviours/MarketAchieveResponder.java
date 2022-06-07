package commonBehaviours;

import jade.core.Agent;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.AchieveREResponder;
import makers.ACLMaker;

// Revisar en RegisterAgent, pues se enviarían dos failure y el inform lo envía otro comportamiento. Revisar entonces si dejarlo como estaba entonces
@SuppressWarnings("serial")
public abstract class MarketAchieveResponder extends AchieveREResponder {

	public MarketAchieveResponder(Agent a, MessageTemplate mt) {
		super(a, mt);
	}
	
	@Override
	protected ACLMessage handleRequest(ACLMessage request) throws NotUnderstoodException, RefuseException {
		return commonHandle(request);
	}
	
	protected ACLMessage commonHandle(ACLMessage request) {
		
		ACLMessage msg = null;
		
		
		msg = responderPerfomance(request);
		
		// Añade un if cada vez que se llama a esta función pero conseguimos una refactoring extra al poder hacer los failures en el comportamiento padre
		// Devuelve un failure
		if(msg == null) {
			msg = ACLMaker.createResponse(request, ACLMessage.FAILURE);
		}
		return msg;
		
	}
	
//	@Override
//	public int onEnd() {
//		super.onEnd();
//		System.out.println("Reseteando MarketAchieveResponder");
//		this.reset();
//		myAgent.addBehaviour(this);
//		return 0;
//	}
//	
	protected abstract ACLMessage responderPerfomance(ACLMessage request); // Creo que podemos quitar msg de aquí

}
