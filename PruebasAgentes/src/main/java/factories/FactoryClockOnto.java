package factories;

import ontologies.OntologiesNames;

public class FactoryClockOnto extends FactoryOntology {
	
	private static FactoryClockOnto instance;
	
	@Override
	protected FactoryClockOnto getInstance(){
		if(instance == null)
			instance = new FactoryClockOnto();
		return instance;
	}
	
	protected FactoryClockOnto() {
	}

	@Override
	public String ontoName() {
		return OntologiesNames.CLOCKONTO_CLASS;
	}

}
