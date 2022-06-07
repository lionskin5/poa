package factories;

import ontologies.Ontology;

public abstract class FactoryOntology extends FactoryGlobal {
	
	// Aquí se puede poner otro patrón experto. Que la factoría devuelve el nombre de la ontología en vez de la ontología. Aunque para los ACLMessage
	// es necesario meter la ontología y no su nombre. Se podría crear un getOntoName para el caso de los MessageTemplate, pero no sé.
	public Ontology getOnto() {
		return Ontology.getInstancia(ontoName());
	}
	
	protected abstract String ontoName();

}
