package elements.sea;

import jade.content.AgentAction;
import jade.content.onto.annotations.Slot;

@SuppressWarnings("serial")
public class FishAction implements AgentAction {
	
	private int budget;

	@Slot(mandatory = true)
	public int getBudget() {
		return budget;
	}
	public void setBudget(int budget) {
		this.budget = budget;
	}

}
