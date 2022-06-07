package ontologies;

@SuppressWarnings("serial")
public class LotOntology extends Ontology {

	private static LotOntology instance;

	@Override
	protected LotOntology getInstance() {
		if(instance == null)
			instance = new LotOntology();
		return instance;
	}
	
	protected LotOntology() {
		super(OntologiesNames.LOT_ONTOLOGY, "elements.bank");
	}


}
