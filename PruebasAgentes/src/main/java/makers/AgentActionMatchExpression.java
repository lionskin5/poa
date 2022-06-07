package makers;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import commonAgents.MyAgent;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate.MatchExpression;

@SuppressWarnings("serial")
public class AgentActionMatchExpression implements MatchExpression {

	private String agentAction;
	private Pattern pattern;
	private Matcher matcher;
	
	public AgentActionMatchExpression(String agentAction) {
		this.agentAction = agentAction;
		this.pattern = Pattern.compile(this.agentAction);
		this.matcher = null;
	}
	
	@Override
	public boolean match(ACLMessage arg0) {
		
		System.out.println("ME: " + this.agentAction + " Content del mensaje a comparar: " + arg0.getContent());
		this.matcher = pattern.matcher(arg0.getContent());
		return matcher.find();
	
	}
	
}
