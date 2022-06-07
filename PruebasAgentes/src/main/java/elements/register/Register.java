package elements.register;

import jade.content.Concept;
import jade.content.onto.annotations.Slot;
import jade.core.AID;

@SuppressWarnings("serial")
public class Register implements Concept {
	
	private AID memberAID;
	
	@Slot(mandatory = true)
	public AID getMemberAID() {
		return memberAID;
	}
	public void setMemberAID(AID memberAID) {
		this.memberAID = memberAID;
	} // El nombre del agente. Quizá se puede cambiar por un id o por el AID, pero de momento lo dejo así

	public boolean isAgentMember(AID agent) {
		return this.memberAID == agent;
	}

}
