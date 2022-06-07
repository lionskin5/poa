package ontologies;

@SuppressWarnings("serial")
public class SeaOntology extends Ontology {

	private static SeaOntology instance;
	
	@Override
	protected SeaOntology getInstance(){
		if(instance == null)
			instance = new SeaOntology();
		return instance;
	}

	protected SeaOntology() {
    	super(OntologiesNames.SEA_ONTOLOGY, "elements.sea");
    }

}
