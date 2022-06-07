package factories;

import ontologies.OntologiesNames;

public class FactorySeaOnto extends FactoryOntology {

	private static FactorySeaOnto instance;

	@Override
	protected FactoryGlobal getInstance() {
		if(instance == null)
			instance = new FactorySeaOnto();
		return instance;
	}
	
	protected FactorySeaOnto() {
	}
	
	@Override
	protected String ontoName() {
		return OntologiesNames.SEAONTO_CLASS;
	}

}
