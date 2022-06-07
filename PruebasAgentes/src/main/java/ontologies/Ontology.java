package ontologies;

import jade.content.onto.BeanOntology;
import jade.content.onto.BeanOntologyException;

@SuppressWarnings("serial")
public abstract class Ontology extends BeanOntology {
	
	public static Ontology getInstancia(String tipo){
		
			Ontology factory = null;
			try {
				factory = (Ontology) Class.forName(tipo).newInstance();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			return factory.getInstance();
	}

	
	
	protected Ontology(String name, String pack) {
		super(name);
		try {
			//add(ClockParam.class);
			//add(TICK.class);
			add(pack);// O uno u otro
		} catch (BeanOntologyException e) {
			e.printStackTrace();
		}
	}
	
	protected abstract Ontology getInstance();

}
