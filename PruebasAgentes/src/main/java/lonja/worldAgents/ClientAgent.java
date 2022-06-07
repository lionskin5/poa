package lonja.worldAgents;

import java.util.Random;

import commonBehaviours.MarketAchieveResponder;
import elements.client.Profit;
import elements.client.Sell;
import elements.lot.Lot;
import factories.FactoriesNames;
import factories.FactoryGlobal;
import factories.FactoryOntology;
import jade.content.Concept;
import jade.content.ContentElement;
import jade.content.lang.Codec.CodecException;
import jade.content.onto.OntologyException;
import jade.content.onto.UngroundedException;
import jade.content.onto.basic.Action;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import loaders.LoaderManager;
import lonja.marketAgents.ServiceAgent;
import makers.ACLMaker;
import makers.MTMaker;

@SuppressWarnings("serial")
public class ClientAgent extends ServiceAgent {

	private double fishPrices [];
	private double qualityBonus [];
	private double randomBonus;
	private FactoryOntology factClient;

	public ClientAgent() {
		super("fish-selling-service", "Fish Market World Client");
		// Inicializar factorías y cosas aquí
	}
	
	@Override
	public void setup() {
		
		super.setup();	

		Object [] args = getArguments();
		if (args != null && args.length == 1) {
			double clientArgs [][] = LoaderManager.getClientArguments((String) args[0]);
			//this.fishPrices = LoaderManager.getClientPrices((String) args[0]);
			//this.qualityBonus = LoaderManager.getClientQuality((String) args[0]);
			
			this.fishPrices = clientArgs[0];
			this.qualityBonus = clientArgs[1];

			// El resultado de la resta debe de ser un double
			System.out.println("Precios: " + fishPrices.length);
			System.out.println("Bonus de Calidad: " + qualityBonus.length);
			factClient = (FactoryOntology) FactoryGlobal.getInstancia(FactoriesNames.CLIENTFACTORY);
			this.getContentManager().registerOntology(factClient.getOnto());
			
			MessageTemplate mt = MTMaker.createMT(MarketAchieveResponder.createMessageTemplate(FIPANames.InteractionProtocol.FIPA_REQUEST), this.getCodec().getName(), factClient.getOnto().getName());
			addBehaviour(new SellingBehaviour(this, mt));
			
		}	
		
	}
	
	@Override
	public void takeDown() {
		System.out.println("Terminando Cliente");
		super.takeDown();
		try {
			DFService.deregister(this);
		} catch(FIPAException fe) {
			fe.printStackTrace();
		}
		
	}
	
	
	// Comportamiento que ante una petición de pesca, devuelve los lotes en cuestión
		private class SellingBehaviour extends MarketAchieveResponder {

			public SellingBehaviour(Agent a, MessageTemplate mt) {
				super(a, mt);
			}

			// Calcula 
			@Override
			protected ACLMessage responderPerfomance(ACLMessage request) {
				
				Lot lot = null;
				Profit profit = null;
				Random ran = new Random();
				int ranResult = ran.nextInt(2);
				
				try {
					ContentElement ce = myAgent.getContentManager().extractContent(request);
					Concept cc = null;
					if (ce instanceof Action) {
						cc = ((Action) ce).getAction();
						if(cc instanceof Sell) {
							lot = ((Sell) cc).getLot();
							profit = new Profit();
							}
					}
				} catch (UngroundedException e) {
					e.printStackTrace();
				} catch (CodecException e) {
					e.printStackTrace();
				} catch (OntologyException e) {
					e.printStackTrace();
				}
				
				
				double price = (fishPrices[lot.getType().getIndex()] + fishPrices[lot.getType().getIndex()]*qualityBonus[lot.getType().getIndex()]) * lot.getKg();
				
				if(ranResult == 0)
					price+=price*randomBonus;
				else
					price-=price*randomBonus;
				
				profit.setTotal(price);		
				
				return ACLMaker.createReponseWithContentConcept(request, ACLMessage.INFORM, myAgent, profit);
				
		}
			
	}
	

}
