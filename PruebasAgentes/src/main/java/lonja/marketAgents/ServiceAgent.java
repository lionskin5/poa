package lonja.marketAgents;

import commonAgents.MyAgent;
import commonBehaviours.DFServiceManager;

@SuppressWarnings("serial")
public abstract class ServiceAgent extends MyAgent {
	
	private String serviceType;
	private String serviceName;
	
	public ServiceAgent(String serviceType, String serviceName) {
		super();
		this.serviceType = serviceType;
		this.serviceName = serviceName;
	}

	public String getServiceType() {
		return serviceType;
	}
	public String getServiceName() {
		return serviceName;
	}
	
	public void setup() {
		super.setup();
		DFServiceManager.register(this, serviceType, serviceName);
	}
	

}
