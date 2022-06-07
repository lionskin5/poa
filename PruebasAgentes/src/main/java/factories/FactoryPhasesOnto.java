package factories;

import ontologies.OntologiesNames;

public class FactoryPhasesOnto extends FactoryOntology {

	private static FactoryPhasesOnto instance;
	
	@Override
	protected FactoryGlobal getInstance(){
		if(instance == null)
			instance = new FactoryPhasesOnto();
		return instance;
	}
	
	protected FactoryPhasesOnto() {
	}

	@Override
	public String ontoName() {
		return OntologiesNames.PHASESONTO_CLASS;
	}

}
