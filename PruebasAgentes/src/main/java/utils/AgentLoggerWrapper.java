package utils;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;

import jade.core.Agent;
import jade.util.Logger;
import lonja.buyerAgents.BuyerAgent;
import lonja.clock.ClockAgent;
import lonja.sellerAgents.SellerAgent;

public class AgentLoggerWrapper {	
	private Logger logger;
	protected Agent agent;
	private String color;
	
	public AgentLoggerWrapper(Agent agent) {
		this.agent = agent;
		this.color = ColorManager.getColor(agent.getLocalName());
		this.logger = Logger.getMyLogger(agent.getClass().getCanonicalName());
		System.out.println("Logger de: " + agent.getLocalName() + " de la clase: " + agent.getClass().getCanonicalName());
	}
	
	public void info(String behaviour, String msg) {
		Object[] params = {this.agent.getLocalName(), behaviour, color};
		System.out.println(params[0]+","+params[1]+","+params[2]);
		this.logger.log(Level.INFO, msg, params);
	}
	
	public void close() {
		for(Handler handler: this.logger.getHandlers()) {
			if(handler instanceof FileHandler) {
				((FileHandler)handler).close();
			}
		}
	}
}

class ColorManager {
	private static Map<String,String> colorMapping = new HashMap<String, String>();
	private static String[] colors = {"#8BA900", "#0080A9", "#7600A9", "#0017A9",  "#DABF00", "#DA7700", "#DA1600", "#A9008B"};
	private static String[] buyersCs = {"#8BA900", "#0080A9", "#7600A9", "#0017A9",  "#DABF00", "#DA7700", "#DA1600", "#A9008B"};
	private static String[] sellerCs = {"#8BA900", "#0080A9", "#7600A9", "#0017A9",  "#DABF00", "#DA7700", "#DA1600", "#A9008B"};
	private static String[] othersCs = {"#8BA900", "#0080A9", "#7600A9", "#0017A9",  "#DABF00", "#DA7700", "#DA1600", "#A9008B"};
	private static int index, indexB, indexS, indexO = -1;
	
	public static String getColor(String name) {
		String color = colorMapping.get(name);
		if(color == null) {
			index = (index + 1) % colors.length;
			color =  colors[index];
			colorMapping.put(name, color);
		}
		return color;
	}
	
	// Creo que se puede hacer pasando la clase. Usar agent.getLocalName() tal y como hace en el contructor del Wrapper
	public static String getColor2(String agent, String name) {
		
		String color = colorMapping.get(name);
		
		if(color == null) {
			
			if(agent.equals(BuyerAgent.class.getCanonicalName())) {
				indexB = (indexB + 1) % buyersCs.length;
				color =  buyersCs[indexB];
				colorMapping.put(name, color);
			}
			
			else if(agent.equals(SellerAgent.class.getCanonicalName())) {
				indexS = (indexS + 1) % sellerCs.length;
				color =  sellerCs[indexS];
				colorMapping.put(name, color);
			}
			
			else if(agent.equals(ClockAgent.class.getCanonicalName())) {
				indexO = (indexO + 1) % othersCs.length;
				color =  othersCs[indexO];
				colorMapping.put(name, color);
			}
			
		}

		return color;
	}

}
