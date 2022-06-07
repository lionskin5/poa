package commonBehaviours;

import commonAgents.MyAgent;
import jade.core.Agent;
import jade.domain.FIPAAgentManagement.FailureException;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.SubscriptionResponder;
import utils.FMNames;

@SuppressWarnings("serial")
public abstract class FishSubsResponder extends SubscriptionResponder {

	public FishSubsResponder(Agent a, MessageTemplate mt, SubscriptionManager csm) {
		super(a, mt, csm);
	}
	
	public SubscriptionManager getMySubscriptionManager() {
		return this.mySubscriptionManager;
	}
	
	// Redefinir los dos handle para poder enviar AGREE e INFORM, o REFUSE
	protected ACLMessage handleSubscription(ACLMessage subscription) throws NotUnderstoodException, RefuseException {
		
		System.out.println("Ha llegado una subscripción: " + subscription);
		ACLMessage response = createAgree(subscription);
		this.getMySubscriptionManager().register(this.createSubscription(subscription));
		//((MyAgent) this.getAgent()).info(behaviour, msg);
		((MyAgent)this.getAgent()).info(FMNames.SUBS_B, "FISHResponder " +  FMNames.SUBSCRIPTION + " " + subscription.getSender());
		// Falta la parte de refuse
		return response;
		
	}
	// Hacer el deregister y enviar inform si eso
	protected ACLMessage handleCancel(ACLMessage cancel) throws FailureException {
		return cancel;
	}
	
	public abstract ACLMessage createAgree(ACLMessage subscription);


}
