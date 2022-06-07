package factories;

import ontologies.OntologiesNames;

public class FactoryRegisterOnto extends FactoryOntology {
	
	private static FactoryRegisterOnto instance;

	@Override
	protected FactoryRegisterOnto getInstance(){
		if(instance == null)
			instance = new FactoryRegisterOnto();
		return instance;
	}
	
	protected FactoryRegisterOnto() {
	}

	@Override
	public String ontoName() {
		return OntologiesNames.REGISTERONTO_CLASS;
	}

}
