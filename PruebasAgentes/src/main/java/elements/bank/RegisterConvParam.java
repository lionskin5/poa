package elements.bank;

import jade.content.AgentAction;
import jade.content.onto.annotations.Slot;

@SuppressWarnings("serial")
public class RegisterConvParam implements AgentAction {
	
	private BankAccCreation account;
	private String replyWith;
	private String convid;
	
	@Slot(mandatory = true)
	public BankAccCreation getAccount() {
		return account;
	}
	public void setAccount(BankAccCreation account) {
		this.account = account;
	}
	@Slot(mandatory = true)
	public String getReplyWith() {
		return replyWith;
	}
	public void setReplyWith(String replyWith) {
		this.replyWith = replyWith;
	}
	@Slot(mandatory = true)
	public String getConvid() {
		return convid;
	}
	public void setConvid(String convid) {
		this.convid = convid;
	}
	

}
