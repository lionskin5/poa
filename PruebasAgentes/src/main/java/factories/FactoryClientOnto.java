package factories;

import ontologies.OntologiesNames;

public class FactoryClientOnto extends FactoryOntology {

	private static FactoryClientOnto instance;

	@Override
	protected FactoryGlobal getInstance() {
		if(instance == null)
			instance = new FactoryClientOnto();
		return instance;
	}
	
	protected FactoryClientOnto() {
	}
	
	@Override
	protected String ontoName() {
		return OntologiesNames.CLIENTONTO_CLASS;
	}

}
