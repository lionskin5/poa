package ontologies;

@SuppressWarnings("serial")
public class AuctionOntology extends Ontology {

	private static AuctionOntology instance;
	
	@Override
	protected AuctionOntology getInstance(){
		if(instance == null)
			instance = new AuctionOntology();
		return instance;
	}

	protected AuctionOntology() {
    	super(OntologiesNames.AUCTION_ONTOLOGY, "elements.auction");
    }

}
