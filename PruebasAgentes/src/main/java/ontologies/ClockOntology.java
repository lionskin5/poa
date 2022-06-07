package ontologies;


// Patrón Singleton
@SuppressWarnings("serial")
public class ClockOntology extends Ontology {
	
	private static ClockOntology instance;
	
	@Override
	protected ClockOntology getInstance(){
		if(instance == null)
			instance = new ClockOntology();
		return instance;
	}

	protected ClockOntology() {
    	super(OntologiesNames.CLOCK_ONTOLOGY, "elements.clock");
    }
    
}
