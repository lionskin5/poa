package commonBehaviours;

import jade.core.AID;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;

public class DFServiceManager {
	

	public static void register(Agent a, String type, String name) {
		
		System.out.println("DF Register: " + type);
		DFAgentDescription dfd = new DFAgentDescription(); // Esta clase es para crear la descripci�n del agente que habr� en el DF
		dfd.setName(a.getAID());
		ServiceDescription sd = new ServiceDescription(); // Esta clase es para crear la descripci�n de un servicio que proporcionar� el agente
		sd.setType(type);
		sd.setName(name);
		dfd.addServices(sd);
		try {
			DFService.register(a, dfd); // Se registra la descripci�n del agente que tendr� todos los servicios de �ste
		} catch(FIPAException fe) {
			fe.printStackTrace();
		}
	}
	
	public static void deregister(Agent a, String type, String name) {
		
		System.out.println("DF Deregister");
		DFAgentDescription dfd = new DFAgentDescription(); // Esta clase es para crear la descripci�n del agente que habr� en el DF
		dfd.setName(a.getAID());
		ServiceDescription sd = new ServiceDescription(); // Esta clase es para crear la descripci�n de un servicio que proporcionar� el agente
		sd.setType(type);
		sd.setName(name);
		dfd.addServices(sd);
		try {
			DFService.deregister(a, dfd); // Se registra la descripci�n del agente que tendr� todos los servicios de �ste
		} catch(FIPAException fe) {
			fe.printStackTrace();
		}	
	}
	
	public static DFAgentDescription[] decodeNotification(ACLMessage msg) {
		
		DFAgentDescription[] dfds = null;
		
		try {
			dfds = DFService.decodeNotification(msg.getContent());
		} catch (FIPAException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return dfds;
	}
	
	// A�adir el decodeNotification y quiz�s un decode que busque el reloj o un tipo de agente concreto
	public static ACLMessage createSubscriptionMessage(Agent a, AID dfName, String service) {
		
		DFAgentDescription template = new DFAgentDescription();
		ServiceDescription sd = new ServiceDescription();
		sd.setType(service);
		template.addServices(sd);
		
	//	System.out.println("createSUbS " + a.getName() + DFService.createSubscriptionMessage(a, dfName, template, null));
		
		return DFService.createSubscriptionMessage(a, dfName, template, null);
	}
	
	
	
	
}
