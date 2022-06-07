package ontologies;

@SuppressWarnings("serial")
public class RegisterOntology extends Ontology {
	
	private static RegisterOntology instance;
	
	@Override
	protected RegisterOntology getInstance(){
		if(instance == null)
			instance = new RegisterOntology();
		return instance;
	}

	protected RegisterOntology() {
    	super(OntologiesNames.REGISTER_ONTOLOGY, "elements.register");
    }

}
