package ontologies;

@SuppressWarnings("serial")
public class BankOntology extends Ontology {

	private static BankOntology instance;
	
	@Override
	protected BankOntology getInstance(){
		if(instance == null)
			instance = new BankOntology();
		return instance;
	}

	protected BankOntology() {
    	super(OntologiesNames.BANK_ONTOLOGY, "elements.bank");
    }

}
