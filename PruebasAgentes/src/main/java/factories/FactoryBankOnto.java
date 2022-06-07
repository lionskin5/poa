package factories;

import ontologies.OntologiesNames;

public class FactoryBankOnto extends FactoryOntology {

	private static FactoryBankOnto instance;

	@Override
	protected FactoryBankOnto getInstance(){
		if(instance == null)
			instance = new FactoryBankOnto();
		return instance;
	}
	
	protected FactoryBankOnto() {
	}

	@Override
	public String ontoName() {
		return OntologiesNames.BANKONTO_CLASS;
	}

}
