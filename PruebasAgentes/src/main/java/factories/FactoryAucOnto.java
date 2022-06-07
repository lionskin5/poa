package factories;

import ontologies.OntologiesNames;

public class FactoryAucOnto extends FactoryOntology {

	private static FactoryAucOnto instance;
	
	@Override
	protected FactoryAucOnto getInstance(){
		if(instance == null)
			instance = new FactoryAucOnto();
		return instance;
	}
	
	protected FactoryAucOnto() {
	}

	@Override
	public String ontoName() {
		return OntologiesNames.AUCONTO_CLASS;
	}

}
