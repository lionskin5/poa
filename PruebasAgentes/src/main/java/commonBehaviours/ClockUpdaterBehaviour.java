package commonBehaviours;

import elements.clock.ClockParam;
import jade.content.Concept;
import jade.content.ContentElement;
import jade.content.lang.Codec.CodecException;
import jade.content.onto.OntologyException;
import jade.content.onto.UngroundedException;
import jade.content.onto.basic.Action;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;

@SuppressWarnings("serial")
public abstract class ClockUpdaterBehaviour extends FishSubsInitiator {

	public ClockUpdaterBehaviour(Agent a, ACLMessage msg) {
		super(a, msg);
	}
	
	@Override
	protected void agreeAgentPerfomance(ACLMessage agree) {
		
		ClockParam params = null;
		System.out.println("AGREE en ClockUpdater");
		
		try {
			ContentElement ce = myAgent.getContentManager().extractContent(agree);
			Concept cc = null;
			if (ce instanceof Action) {
				cc = ((Action) ce).getAction();
				if(cc instanceof ClockParam) {
					params = (ClockParam) cc;
				}
			}
		} catch (UngroundedException e) {
			e.printStackTrace();
		} catch (CodecException e) {
			e.printStackTrace();
		} catch (OntologyException e) {
			e.printStackTrace();
		}
		
		agreeBehaviourPerfomance(params);
	}
//
//	@Override
//	protected void subAgentPerfomance(ACLMessage inform) {
//		System.out.println("Llega un inform");
//		TICK tick = null;
//		
//		try { // Aquí es necesario recuperar el objeto Action, y a partir de éste obtener la accion y hacerle un casting a TICK
//			ContentElement ce = myAgent.getContentManager().extractContent(inform);
//			if (ce instanceof Action) {
//				tick = (TICK) ((Action) ce).getAction();
//			}
//			System.out.println("Ha llegado un TICK: " + tick.toString());
//		} catch (UngroundedException e) {
//			e.printStackTrace();
//		} catch (CodecException e) {
//			e.printStackTrace();
//		} catch (OntologyException e) {
//			e.printStackTrace();
//		}
//		
//		//String content = inform.getContent();
//		//System.out.println("El contenido del reloj es: " + content);
//		//Aquí habría que comprobar si se ha terminado, y en el caso del director necesita controlar si se ha pasado de fase
//		
//	}
	
	public abstract void agreeBehaviourPerfomance(ClockParam params);

}
