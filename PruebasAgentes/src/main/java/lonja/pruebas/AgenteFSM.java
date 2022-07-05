package lonja.pruebas;

import java.util.ArrayList;

import commonAgents.MyAgent;
import jade.core.Agent;
import jade.core.behaviours.FSMBehaviour;
import jade.core.behaviours.OneShotBehaviour;

@SuppressWarnings("serial")
public class AgenteFSM extends MyAgent {
	
	private static final String FIRST_STATE = "First State";
	private static final String INTERMEDIATE_STATE_1 = "Intermediate State 1";
	private static final String LAST_STATE = "Last State";
	
	private FSMPrueba fsmPrueba;
	
	private int estado;
	
	public FSMPrueba getFsmPrueba() {
		return fsmPrueba;
	}
	
	@Override
	public void setup() {
		super.setup();
		
		System.out.println("SetUp AgentePrueba");
		estado = 1;
		
		fsmPrueba = new FSMPrueba(this);
		fsmPrueba.registerFirstState(new FirstState(), FIRST_STATE);
		fsmPrueba.registerState(new IntermediateState(), INTERMEDIATE_STATE_1);
		fsmPrueba.registerLastState(new LastState(), LAST_STATE);
		fsmPrueba.registerTransition(FIRST_STATE, INTERMEDIATE_STATE_1, 2);
		fsmPrueba.registerTransition(INTERMEDIATE_STATE_1, FIRST_STATE, 3);
		fsmPrueba.registerTransition(FIRST_STATE, INTERMEDIATE_STATE_1, 4);
		fsmPrueba.registerTransition(FIRST_STATE, LAST_STATE, 1);
		fsmPrueba.registerDefaultTransition(INTERMEDIATE_STATE_1, LAST_STATE);
		
		this.addBehaviour(fsmPrueba);
		
	}
	
	
	private class FSMPrueba extends FSMBehaviour {
		public FSMPrueba(Agent a) {
			super(a);
		}
		
		@Override
		public void onStart() {
			System.out.println("FSMPrueba empieza");
			super.onStart();
		}
		
		@Override
		public int onEnd() {
			System.out.println("FSMPrueba terminado.");
			return super.onEnd();
		}
	}
	
	private class FirstState extends OneShotBehaviour {
	
		@Override
		public void onStart() {
			System.out.println("FirstState onStart");
			super.onStart();
		}
		
		@Override
		public int onEnd() {
			System.out.println("FirstState onEnd");
			System.out.println("Valor onEnd devuelto: " + super.onEnd());
			System.out.println("Valor del estado a devolver: " + estado );
			return estado;
		}
		
		@Override
		public void action() {
			System.out.println("FirstState action");
			System.out.println("Acción del primer estado");
			System.out.println("Último estado devuelto por el FSM: " + ((AgenteFSM) (this.myAgent)).getFsmPrueba().getLastExitValue());
			estado ++;
			}
	}
	
	private class IntermediateState extends OneShotBehaviour {
		
		@Override
		public void onStart() {
			System.out.println("IntermediateState onStart");
			super.onStart();
		}
		
		@Override
		public int onEnd() {
			System.out.println("IntermediateState onEnd");
			System.out.println("Valor onEnd devuelto: " + super.onEnd());
			System.out.println("Valor del estado a devolver: " + estado );
			return estado;
		}
		
		@Override
		public void action() {
			System.out.println("IntermediateState action");
			System.out.println("Último estado devuelto por el FSM: " + ((AgenteFSM) (this.myAgent)).getFsmPrueba().getLastExitValue());
			estado ++;
			System.out.println("Acción del estado intermedio");
			}
	}
	
	private class LastState extends OneShotBehaviour {
		
		@Override
		public void onStart() {
			System.out.println("LastState onStart");
			super.onStart();
		}
		
		@Override
		public int onEnd() {
			System.out.println("LastState onEnd");
			System.out.println("Valor onEnd devuelto: " + super.onEnd());
			return super.onEnd();
		}
		
		@Override
		public void action() {
			System.out.println("LastState action");
			System.out.println("Último estado devuelto por el FSM: " + getFsmPrueba().getLastExitValue());
			System.out.println("Acción del último estado");	
			}
	}

}
