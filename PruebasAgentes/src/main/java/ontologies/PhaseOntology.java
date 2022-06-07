package ontologies;

@SuppressWarnings("serial")
public class PhaseOntology extends Ontology {
	
	private static PhaseOntology instance;
	
	@Override
	protected PhaseOntology getInstance(){
		if(instance == null)
			instance = new PhaseOntology();
		return instance;
	}

	protected PhaseOntology() {
    	super(OntologiesNames.PHASES_ONTOLOGY, "elements.phases");
    }

}
