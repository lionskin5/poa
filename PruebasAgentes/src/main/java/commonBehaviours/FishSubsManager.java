package commonBehaviours;

import java.util.ArrayList;
import java.util.List;

import jade.domain.FIPAAgentManagement.FailureException;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.proto.SubscriptionResponder.Subscription;
import jade.proto.SubscriptionResponder.SubscriptionManager;

public class FishSubsManager implements SubscriptionManager {
	
	private List<Subscription> subs = new ArrayList<Subscription>();

	public List<Subscription> getSubs() {
		return subs;
	} // Cambiar por el aliasing. O no.


	@Override
	public boolean deregister(Subscription subscription) throws FailureException {
		
		// Si el agente no es de cierto tipo enviar REFUSE
		
		synchronized (subs) {
			subs.remove(subscription);
		}	
		
		return true; // Siempre hay que enviar un INFORM. Esto puede que se haga en SubscriptionResponder
	}

	@Override
	public boolean register(Subscription subscription) throws RefuseException, NotUnderstoodException {
		synchronized (subs) {
			subs.add(subscription);
		}
		return true; // Siempre hay que enviar un AGREE
	}	
	
	
}