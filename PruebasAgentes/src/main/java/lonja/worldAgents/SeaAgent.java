package lonja.worldAgents;

import java.util.Random;

import commonBehaviours.MarketAchieveResponder;
import elements.lot.Fish;
import elements.lot.Lot;
import elements.lot.Range;
import elements.sea.FishAction;
import elements.sea.Fishing;
import elements.sea.RangeAction;
import elements.sea.RangePrices;
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
public class SeaAgent extends ServiceAgent {

	// Límites inferior y superior para cada rango
//	private int lowRangeUnder;
//	private double lowRangeTop;
//	private int mediumRangeUnder;
//	private double mediumRangeTop;
//	private int highRangeUnder;
//	private double highRangeTop;
	
	private RangePrices rangesPrices;
	
	private double lowScale;
	private double mediumScale;
	private double highScale;
	
	private FactoryOntology factSea;
	
	public SeaAgent() {
		super("fish-service", "Fish Market Fishing");
		this.rangesPrices = new RangePrices();
		// Inicializar factorías y cosas
	}
	
	public double getLRU() {
		return this.rangesPrices.getLowRangeUnder();
	}
	public double getLRT() {
		return this.rangesPrices.getLowRangeTop();
	}
	public double getMRU() {
		return this.rangesPrices.getMediumRangeUnder();
	}
	public double getMRT() {
		return this.rangesPrices.getMediumRangeTop();
	}
	public double getHRU() {
		return this.rangesPrices.getHighRangeUnder();
	}
	public double getHRT() {
		return this.rangesPrices.getHighRangeTop();
	}
	
	@Override
	public void setup() {
	
		super.setup();	

		Object [] args = getArguments();
		if (args != null && args.length == 1) {
			int seaArgs [] = LoaderManager.getSeaArguments((String) args[0]);
			
			this.rangesPrices.setLowRangeUnder(seaArgs[0]);
			this.rangesPrices.setMediumRangeUnder(seaArgs[1]);
			this.rangesPrices.setHighRangeUnder(seaArgs[2]);
			
			this.rangesPrices.setLowRangeTop(rangesPrices.getMediumRangeUnder() - 0.01);
			this.rangesPrices.setMediumRangeTop(rangesPrices.getHighRangeUnder() - 0.01);
			this.rangesPrices.setHighRangeTop(seaArgs[3] - 0.01); // El tope de la gama alta será el tope máximo - 0.01
			
//			this.lowRangeUnder = seaArgs[0];
//			this.mediumRangeUnder = seaArgs[1];
//			this.highRangeUnder = seaArgs[2];
//			this.lowRangeTop = mediumRangeUnder - 0.01;
//			this.mediumRangeTop = highRangeUnder - 0.01;
//			this.highRangeTop = seaArgs[3] - 0.01;
			
			this.lowScale = (this.rangesPrices.getLowRangeTop() - this.rangesPrices.getLowRangeUnder())/5;
			this.mediumScale = (this.rangesPrices.getMediumRangeTop() - this.rangesPrices.getMediumRangeUnder())/5;
			this.highScale = (this.rangesPrices.getHighRangeTop() - this.rangesPrices.getHighRangeUnder())/5;
			
			// El resultado de la resta debe de ser un double
			// Se supone que la división da un double porque la resta convierte el resultado en double
			System.out.println("Rangos: " + rangesPrices.getLowRangeUnder() + " " + rangesPrices.getLowRangeTop() + " "
											+ rangesPrices.getMediumRangeUnder() + " " + rangesPrices.getMediumRangeTop() + " "
											+ rangesPrices.getHighRangeUnder() + " " + rangesPrices.getHighRangeTop());
			
			System.out.println("Escalas: " + lowScale + " " + mediumScale + " " + highScale);
			
			// Añadir el comportamiento y crear el mensaje que requerirá
			factSea = (FactoryOntology) FactoryGlobal.getInstancia(FactoriesNames.SEAFACTORY);
			this.getContentManager().registerOntology(factSea.getOnto());
			
			MessageTemplate mtFB = MTMaker.createMTWithMatchExpr(MarketAchieveResponder.createMessageTemplate(FIPANames.InteractionProtocol.FIPA_REQUEST), this.getCodec().getName(), factSea.getOnto().getName(), "FishAction");
			//MessageTemplate mt = MTMaker.createMT(MarketAchieveResponder.createMessageTemplate(FIPANames.InteractionProtocol.FIPA_REQUEST), this.getCodec().getName(), factSea.getOnto().getName());
			addBehaviour(new FishingBehaviour(this, mtFB));
			MessageTemplate mtRRB = MTMaker.createMTWithMatchExpr(MarketAchieveResponder.createMessageTemplate(FIPANames.InteractionProtocol.FIPA_REQUEST), this.getCodec().getName(), factSea.getOnto().getName(), "RangeAction");
			addBehaviour(new RangeRequestBehaviour(this, mtRRB));
			
		}
		
	}
	
	
	@Override
	public void takeDown() {
		System.out.println("Terminando SeaAgent");
		super.takeDown();
		try {
			DFService.deregister(this);
		} catch(FIPAException fe) {
			fe.printStackTrace();
		}
		
	}
	
	
	
	
	// Comportamiento que ante una petición de pesca, devuelve los lotes en cuestión
	private class FishingBehaviour extends MarketAchieveResponder {

		public FishingBehaviour(Agent a, MessageTemplate mt) {
			super(a, mt);
		}

		// Calcula el lote
		@Override
		protected ACLMessage responderPerfomance(ACLMessage request) {		
			
			int budget = 0;
			Fishing fishResult = null;
			SeaAgent agent = (SeaAgent) this.myAgent;
			Range range = null;
			int quality = -1;
			Random ran = new Random();
			int fishType = ran.nextInt(3);	
			
			try {
				ContentElement ce = myAgent.getContentManager().extractContent(request);
				Concept cc = null;
				if (ce instanceof Action) {
					cc = ((Action) ce).getAction();
					if(cc instanceof FishAction) {
						budget = ((FishAction) cc).getBudget();
						fishResult = new Fishing();
						}
				}
			} catch (UngroundedException e) {
				e.printStackTrace();
			} catch (CodecException e) {
				e.printStackTrace();
			} catch (OntologyException e) {
				e.printStackTrace();
			}
			
			// Obtenemos el rango de calidad. Rangos de 0 a 4 (5 rangos). La calidad irá de 1 a 5.
			if(budget >= agent.getLRU() && budget <= agent.getLRT()) {
				quality = (int) ((budget-agent.getLRU())/agent.lowScale);
				range = Range.LOW;
			}
			else if (budget >= agent.getMRU() && budget <= agent.getMRT()) {
				quality = (int) ((budget-agent.getMRU())/agent.mediumScale);
				range = Range.MEDIUM;
				fishType +=3;
			}
			else {
				quality = (int) ((budget-agent.getHRU())/agent.highScale);
				range = Range.HIGH;
				fishType +=6;
			}
			
			
			// Obtenemos el número de kg del lote. Se trata de una obtención aleatoria. De 1 a 10.
			int kg = ran.nextInt(10) + 1;
			
			
			System.out.println("Se creará el siguiente lote: " + fishType + " " + range + " " + kg + " " + quality+1);
			
			// Falta por obtener el pescado
			// Hay que crear los tipos
			fishResult.setLot(new Lot(Fish.values()[fishType], range, kg, quality+1));			
			
			return ACLMaker.createReponseWithContentConcept(request, ACLMessage.INFORM, myAgent, fishResult);
		}
		
	}
	
	// Comportamiento que envía los precios de las distintas gamas de pescados
	private class RangeRequestBehaviour extends MarketAchieveResponder {

		public RangeRequestBehaviour(Agent a, MessageTemplate mt) {
			super(a, mt);
		}

		@Override
		protected ACLMessage responderPerfomance(ACLMessage request) {
			
			RangeAction ra = null;
			ACLMessage response = null;
			
			try {
				ContentElement ce = myAgent.getContentManager().extractContent(request);
				Concept cc = null;
				if (ce instanceof Action) {
					cc = ((Action) ce).getAction();
					if(cc instanceof RangeAction) {
						ra = (RangeAction) cc;
						response = ACLMaker.createReponseWithContentConcept(request, ACLMessage.INFORM, myAgent, rangesPrices);
					}
				}
				System.out.println("RangeAction Sea: " + ra.toString());
			} catch (UngroundedException e) {
				e.printStackTrace();
			} catch (CodecException e) {
				e.printStackTrace();
			} catch (OntologyException e) {
				e.printStackTrace();
			}
			
			return response;
		}
		
	}

}
