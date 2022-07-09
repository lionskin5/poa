package elements.phases;

import jade.content.AgentAction;
import jade.content.onto.annotations.Slot;

@SuppressWarnings("serial")
public class PhaseNotification implements AgentAction {
	
	private String phase;
	private boolean start;
	
	@Slot(mandatory = true)
	public String getPhase() {
		return phase;
	}
	public void setPhase(String phase) {
		this.phase = phase;
	}
	@Slot(mandatory = true)
	public boolean isStart() {
		return start;
	}
	public void setStart(boolean start) {
		this.start = start;
	}

}
