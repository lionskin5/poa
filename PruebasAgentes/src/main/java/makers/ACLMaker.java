package makers;

import jade.content.Concept;
import jade.content.Predicate;
import jade.content.lang.Codec.CodecException;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;

public class ACLMaker {
	
	// Pensar si hacerla privada
	public static ACLMessage createMessage(int perf, String protocol, String codec, String ontology, String reply) {
		
		ACLMessage msg = new ACLMessage(perf);
		msg.setProtocol(protocol);
		msg.setLanguage(codec);
		msg.setOntology(ontology);
		msg.setReplyWith(reply);
		
	//	System.out.println("ACLMaker: " + msg.toString());

		return msg;
		
	}
	
	
	// Pensar si hacerla privada
	public static ACLMessage createMessage(int perf, AID sender, String protocol, String codec, String ontology, String reply) {
		
		ACLMessage msg =createMessage(perf, protocol, codec, ontology, reply);
		msg.setSender(sender);
		
	//	System.out.println("ACLMaker: " + msg.toString());

		return msg;
		
	}
	
	public static ACLMessage createMessage(int perf, AID sender, String protocol, AID receiver, String codec, String ontology, String reply) {
		
		ACLMessage msg =createMessage(perf, sender, protocol, codec, ontology, reply);
		msg.addReceiver(receiver);
		
	//	System.out.println("ACLMaker: " + msg.toString());

		return msg;
		
	}
	
//	public static ACLMessage createResponse(int perf, AID sender, String protocol, AID receiver, String codec, String ontology, String reply, String inReply) {
//		
//		ACLMessage msg = createMessage(perf, sender, protocol, receiver, codec, ontology, reply);
//		msg.setInReplyTo(inReply);
//		
//		System.out.println("ACLMaker: " + msg.toString());
//		
//		return msg;
//		
//	}
	
	public static ACLMessage createMessageWithContent(int perf, AID sender, String protocol, AID receiver, String codec, String ontology, String reply, int content)  {
		
		ACLMessage msg = createMessage(perf, sender, protocol, receiver, codec, ontology, reply);
		msg.setContent(Integer.toString(content));
		
	//	System.out.println("ACLMaker with Content: " + msg.toString());
		
		return msg;
		
	}
	
	public static ACLMessage createMessageWithContentPredicate(int perf, AID sender, String protocol, AID receiver, String codec, String ontology, String reply, Agent a, Predicate ce)  {
		
		ACLMessage msg = createMessage(perf, sender, protocol, receiver, codec, ontology, reply);
		
		try {
			a.getContentManager().fillContent(msg, ce);
		} catch (CodecException e) {
			e.printStackTrace();
		} catch (OntologyException e) {
			e.printStackTrace();
		}
		
	//	System.out.println("ACLMaker with Predicate: " + msg.toString());
		
		return msg;
		
	}
	
	public static ACLMessage createMessageWithContentConcept(int perf, AID sender, String protocol, AID receiver, String codec, String ontology, String reply, Agent a, Concept ce)  {
		
		ACLMessage msg = createMessage(perf, sender, protocol, receiver, codec, ontology, reply);
		
		try {
			a.getContentManager().fillContent(msg, new Action(a.getAID(), ce));
		} catch (CodecException e) {
			e.printStackTrace();
		} catch (OntologyException e) {
			e.printStackTrace();
		}
		
	//	System.out.println("ACLMaker with Concept: " + msg.toString());
		
		return msg;
		
	}
		
	public static ACLMessage createMessageWithContentConceptNoReceiver(int perf, AID sender, String protocol, String codec, String ontology, String reply, Agent a, Concept ce)  {
			
		ACLMessage msg = createMessage(perf, sender, protocol, codec, ontology, reply);
			
		try {
			a.getContentManager().fillContent(msg, new Action(a.getAID(), ce));
		} catch (CodecException e) {
			e.printStackTrace();
		} catch (OntologyException e) {
			e.printStackTrace();
		}
			
		//System.out.println("ACLMaker with Concept No Receiver: " + msg.toString());
			
		return msg;
			
	}
	
	public static ACLMessage createMessageWithContentConceptNoSNoR(int perf, String protocol, String codec, String ontology, String reply, Agent a, Concept ce)  {
		
		ACLMessage msg = createMessage(perf, protocol, codec, ontology, reply);
			
		try {
			a.getContentManager().fillContent(msg, new Action(a.getAID(), ce));
		} catch (CodecException e) {
			e.printStackTrace();
		} catch (OntologyException e) {
			e.printStackTrace();
		}
			
	//	System.out.println("ACLMaker with Concept No S No R: " + msg.toString());
			
		return msg;
			
	}
	
	
	
	
	// createReply ya se encarga de rellenar los campos in-reply-to y conv-id
	public static ACLMessage createResponse(ACLMessage msg, int perf) {
		
		ACLMessage response = msg.createReply();
		response.setPerformative(perf);
		response.setSender((AID) msg.getAllReceiver().next()); // Las comunicaciones son una a una
		
	//	System.out.println("ACLmaker Response: " + response.toString());
				
		return response;
		
	}
	
	public static ACLMessage createReponseWithContentConcept(ACLMessage msg, int perf, Agent a, Concept ce) {
		
		ACLMessage response = createResponse(msg, perf);
		
		try {
			a.getContentManager().fillContent(response, new Action(a.getAID(), ce));
		} catch (CodecException e) {
			e.printStackTrace();
		} catch (OntologyException e) {
			e.printStackTrace();
		}
		
	//	System.out.println("ACLmaker Response with Concept: " + response.toString());
		
		return response;
		
	}
	

}
