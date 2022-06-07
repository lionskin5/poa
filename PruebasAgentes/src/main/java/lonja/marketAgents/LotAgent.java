package lonja.marketAgents;

import java.util.List;

import commonBehaviours.MarketAchieveResponder;
import elements.lot.Lot;
import elements.register.Register;
import factories.FactoriesNames;
import factories.FactoryGlobal;
import factories.FactoryOntology;
import jade.core.AID;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import makers.LotStorage;
import makers.RegisterStorage;
import ontologies.Ontology;

@SuppressWarnings("serial")
public abstract class LotAgent extends ServiceAgent {
	
	private LotStorage lotContainer;
	private RegisterStorage registerContainer;
	private MessageTemplate request; // Este atributo es para poder abstraer las clases hijas y poder hacer al addBaheviour en esta clase en vez de en las hijas
	private FactoryOntology factLot;

	// El contenedor se inicializa en la configuración de estos tipos de agentes
	public LotAgent(String serviceType, String serviceName) {
		super(serviceType, serviceName);
	}
	
	public LotStorage getLotContainer() {
		return lotContainer;
	}
	
	public RegisterStorage getRegisterContainer() {
		return registerContainer;
	}
	
	public Ontology getLotOnto() {
		return this.factLot.getOnto();
	}

	// Patrón Experto
	public String getLotOntoName() {
		return factLot.getOnto().getName();
	}
	
	// Abstracción de como funciona el LotStorage
	public void addLot(Lot l) {
		this.lotContainer.addLot(l);;
	}
	
	// En vez de con lote debería de ser de otra manera
	public void deleteLot(Lot l) {
		this.lotContainer.deleteLot(l);
	}
	
	public void addIsOwner(Lot l, AID sender) {
		this.lotContainer.addIsOwner(l, this.getRegist(sender));
	}
	
	public void changeOwner(Lot l, Register owner) {
		this.lotContainer.changeOwner(l, owner);
	}
	
	public Register getRegist(AID agent) {
		return this.registerContainer.getRegist(agent);
	}
	
	public List<Lot> getLotsFromAgent(AID agent) {
		return this.lotContainer.getLotsFromAgent(agent);
	}
	

	@Override
	public void setup() {
		super.setup();
		
		Object[] args = getArguments();
		if(args != null && args.length == 2) {
			this.lotContainer = (LotStorage) args[0];
			this.registerContainer = (RegisterStorage) args[1];
			this.request = (MessageTemplate) args[2];
			// Hay que añadir la factoría al ContentManager
			this.factLot = (FactoryOntology) FactoryGlobal.getInstancia(FactoriesNames.LOTFACTORY);
			this.getContentManager().registerOntology(factLot.getOnto());
		
			addBehaviour(new LotBehaviour(this, request));
		}
		
	}
	
	// Comportamiento que satisface mensajes Request sobre los lotes.
	private class LotBehaviour extends MarketAchieveResponder {

		public LotBehaviour(Agent a, MessageTemplate mt) {
			super(a, mt);
		}
			
		// Los lotes estarán almacenados en un contenedor, así que tendrá que tener acceso a ese contenedor
		@Override
		protected ACLMessage responderPerfomance(ACLMessage request) {
			return lotAgentPerfomance(request);
		} // Devuelve el mensaje ACL que será enviado internamente por el comportamiento
			
	}
			
	protected abstract ACLMessage lotAgentPerfomance(ACLMessage request);
	
	
}
