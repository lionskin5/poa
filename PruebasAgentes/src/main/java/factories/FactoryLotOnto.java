package factories;

import ontologies.OntologiesNames;

public class FactoryLotOnto extends FactoryOntology {

	private static FactoryLotOnto instance;
	
	@Override
	protected FactoryGlobal getInstance(){
		if(instance == null)
			instance = new FactoryLotOnto();
		return instance;
	}
	
	protected FactoryLotOnto() {
	}

	@Override
	public String ontoName() {
		return OntologiesNames.LOTONTO_CLASS;
	}

}
