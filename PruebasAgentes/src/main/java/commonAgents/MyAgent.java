package commonAgents;

import utils.AgentLoggerWrapper;
import jade.content.lang.Codec;
import jade.content.lang.sl.SLCodec;
import jade.core.Agent;

@SuppressWarnings("serial")
public abstract class MyAgent extends Agent {
	
	private AgentLoggerWrapper logger;
	private Codec codec = new SLCodec();
	

	// Aquí lo mismo que con los de las ontologías. Se puede hacer un getCodecName para meter más patrón Experto
	public Codec getCodec() {
		return codec;
	}
	
	public AgentLoggerWrapper getLogger() {
		return this.logger;
	}
	
	public void info(String behaviour, String msg) {
		this.logger.info(behaviour, msg);
	}
	
	@Override
	public void setup() {
		super.setup();
		logger = new AgentLoggerWrapper(this);
		System.out.println("Starting Agent: " + this.getClass().getName());
		this.getContentManager().registerLanguage(codec);
	}
	
	@Override
	public void takeDown() {
		super.takeDown();
		this.logger.close();
	}

}
