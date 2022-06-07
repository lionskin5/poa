package factories;

import ontologies.Ontology;

public abstract class FactoryOntology extends FactoryGlobal {
	
	// Aqu� se puede poner otro patr�n experto. Que la factor�a devuelve el nombre de la ontolog�a en vez de la ontolog�a. Aunque para los ACLMessage
	// es necesario meter la ontolog�a y no su nombre. Se podr�a crear un getOntoName para el caso de los MessageTemplate, pero no s�.
	public Ontology getOnto() {
		return Ontology.getInstancia(ontoName());
	}
	
	protected abstract String ontoName();

}
