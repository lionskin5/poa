package makers;

import jade.lang.acl.MessageTemplate;

public class MTMaker {
	
	public static MessageTemplate createMT(int perf, String protocol, String codec, String ontology) {
		
		MessageTemplate mt = MessageTemplate.and(
				MessageTemplate.and(
				MessageTemplate.and(
				MessageTemplate.MatchPerformative(perf)
				, MessageTemplate.MatchProtocol(protocol))
				, MessageTemplate.MatchLanguage(codec))
				, MessageTemplate.MatchOntology(ontology));
		System.out.println("El MT: " + mt.toString());
		
		return mt;
		
	}

	public static MessageTemplate createMT(MessageTemplate mtPerf, String codec, String ontology) {
		
		MessageTemplate mt = MessageTemplate.and(
				MessageTemplate.and(
					mtPerf,
				MessageTemplate.MatchLanguage(codec))
				,MessageTemplate.MatchOntology(ontology));
		
		System.out.println("El MT: " + mt.toString());
		
		return mt;
		
	}
	
	public static MessageTemplate createMTWithMatchExpr(MessageTemplate mtPerf, String codec, String ontology, String content) {
		
		AgentActionMatchExpression me = new AgentActionMatchExpression(content);
		
		MessageTemplate mt = MessageTemplate.and(createMT(mtPerf, codec, ontology)
													, new MessageTemplate(me));
		
		System.out.println("El MT: " + mt.toString());
		
		return mt;
		
	}
	
	public static MessageTemplate createMTOR(int perf1, int perf2, String protocol, String codec, String ontology) {

		MessageTemplate mt = MessageTemplate.or(createMT(perf1, protocol, codec, ontology)
												, createMT(perf2, protocol, codec, ontology));	
			
		System.out.println("El MT: " + mt.toString());
		
		return mt;	
	}
	
	public static MessageTemplate createMTORWithMT(MessageTemplate mt1, MessageTemplate mt2) {

		MessageTemplate mt = MessageTemplate.or(mt1, mt2);	
			
		System.out.println("El MT: " + mt.toString());
		
		return mt;	
	}
	
}
