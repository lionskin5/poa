package factories;

import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;

public class FactoryAgent extends FactoryGlobal {

	private static FactoryAgent instance;
	private AgentContainer mc; // Necesita el AgentContainer para crear los agentes en la plataforma adecuada.
	// Necesitamos el set para que luego el director pueda crear los agentes en la misma plataforma. Si luego creas dos plataformas, simplemente intercambiar el mc entre los dos agentcontainer
	
	protected FactoryAgent() {
	}
	
	@Override
	protected FactoryAgent getInstance() {
		if(instance == null)
			instance = new FactoryAgent();
		return instance;
	}
	
	public void setMc(AgentContainer mc) {
		this.mc = mc;
	}

	public void createAgent(String name, String agentClass, Object [] args) {
		
		AgentController agent = null;
		
		try {
			agent = this.mc.createNewAgent(name, agentClass, args);
			agent.start();
		} catch (StaleProxyException e) {
			e.printStackTrace();
		}
	}
	
}
