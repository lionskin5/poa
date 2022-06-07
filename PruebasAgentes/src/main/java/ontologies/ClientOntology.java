package ontologies;

@SuppressWarnings("serial")
public class ClientOntology extends Ontology {

private static ClientOntology instance;
	
	@Override
	protected ClientOntology getInstance(){
		if(instance == null)
			instance = new ClientOntology();
		return instance;
	}

	protected ClientOntology() {
    	super(OntologiesNames.CLIENT_ONTOLOGY, "elements.client");
    }
}
