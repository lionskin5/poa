package commonAgents;

import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import utils.AgentLoggerWrapper;

@SuppressWarnings("serial")
public class AgenteReset extends Agent {
	
	
	private Primero primero;
	private Segundo segundo;
	
	
	public AgenteReset() {
		primero = new Primero();
		segundo = new Segundo();
	}
	
	@Override
	public void setup() {
		this.addBehaviour(primero);
	}
	
	private class Primero extends OneShotBehaviour {

		@Override
		public void action() {
			System.out.println("Lanzando primero");
			myAgent.addBehaviour(segundo);
			
		}
		
	}
	
	private class Segundo extends OneShotBehaviour {

		private int numero = 0;
		
		@Override
		public void action() {
		//	System.out.println("Número: " + numero);
			//this.reset();
		//	numero++;
		//	this.conReset();
			myAgent.addBehaviour(primero);
			System.out.println("Activo");
			
			
		}
		
		public void conReset() {
			this.reset();
			myAgent.addBehaviour(this);
		}
		
		public void sinReset() {
			myAgent.addBehaviour(this);
		}
		
		@Override
		public int onEnd() {
			myAgent.doSuspend();
			return super.onEnd();
		}
		
		
	}
	
	
}
