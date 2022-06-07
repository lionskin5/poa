package commonAgents;

import commonBehaviours.DFServiceManager;
import commonBehaviours.DFSubsBehaviour;
import commonBehaviours.RegisterBehaviour;
import factories.FactoriesNames;
import factories.FactoryGlobal;
import factories.FactoryOntology;
import jade.core.AID;
import jade.core.Agent;
import jade.domain.FIPANames;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.lang.acl.ACLMessage;
import makers.ACLMaker;

@SuppressWarnings("serial")
public abstract class ExternalAgent extends MyAgent {
	
	private int budget;
	private AID registerAgent;
	private AID bankAgent;
	
	private FactoryOntology factRegist;
	private FactoryOntology factBank;

	public int getBudget() {
		return budget;
	}
	public void setBudget(int budget) {
		this.budget = budget;
	}
	public AID getRegisterAgent() {
		return registerAgent;
	}
	public void setRegisterAgent(AID registerAgent) {
		this.registerAgent = registerAgent;
	}
	public AID getBankAgent() {
		return bankAgent;
	}
	public void setBankAgent(AID bankAgent) {
		this.bankAgent = bankAgent;
	}
	public FactoryOntology getFactRegist() {
		return factRegist;
	}
	public FactoryOntology getFactBank() {
		return factBank;
	}
	
	@Override
	public void setup() {
		super.setup();

		// Habría que registrar estas factorías en el contentmanager, ¿no?
		
		factRegist = (FactoryOntology) FactoryGlobal.getInstancia(FactoriesNames.REGISTERFACTORY);
		factBank = (FactoryOntology) FactoryGlobal.getInstancia(FactoriesNames.BANKFACTORY);
			
		System.out.println("External DF Register entrando");
		addBehaviour(new DFRegisterSubsBehaviour(this, DFServiceManager.createSubscriptionMessage(this, getDefaultDF(), "register-service")));
		//addBehaviour(new DFBankSubsBehaviour(this, DFServiceManager.createSubscriptionMessage(this, getDefaultDF(), "bank-service")));
		
//		MessageTemplate mt = MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.INFORM), MessageTemplate.MatchProtocol("fipa-subscribe"));
//		
//		MessageTemplate mt2 = MessageTemplate.MatchPerformative(ACLMessage.INFORM);
//		
//		MessageTemplate pattern = MTMaker.createMT(mt, this.getCodec().getName(), "FIPA-Agent-Management"); // No he encontrado esto en ninguna clase	
//		// Este comportamiento no funciona pues no tiene receiver!!!
//		System.out.println("Voy a bloquear mi agente");
//		ACLMessage msg = this.blockingReceive();
//		System.out.println("ACL bloqueado: " + msg);
//		//this.putBack(msg);
//		
//		System.out.println("External desbloqueado");
//		
//		// Este Behaviour no se puede inscribir hasta que sabemos el agente registrador
//		addBehaviour(new RegisterBehaviour(this
//					,ACLMaker.createMessageWithContent(ACLMessage.REQUEST, FIPANames.InteractionProtocol.FIPA_REQUEST, this.registerAgent
//					, this.getCodec().getName(), this.factRegist.getOnto().getName(), ""+System.currentTimeMillis(), budget) // Cambiar el budget por otro. Budget es el dinero propio del agente
//					));
		
		
	}

	private class DFRegisterSubsBehaviour extends DFSubsBehaviour {

		public DFRegisterSubsBehaviour(Agent a, ACLMessage msg) {
			super(a, msg);
			System.out.println("DFRegister ExternalAgent");
		}

		@Override
		public void agentPerfomance(DFAgentDescription[] dfds, ACLMessage inform) {
			((ExternalAgent) myAgent).setRegisterAgent(dfds[0].getName());		
			
			addBehaviour(new RegisterBehaviour(myAgent
					,ACLMaker.createMessageWithContent(ACLMessage.REQUEST, myAgent.getAID(), FIPANames.InteractionProtocol.FIPA_REQUEST, registerAgent
					, getCodec().getName(), factRegist.getOnto().getName(), ""+System.currentTimeMillis(), budget) // Cambiar el budget por otro. Budget es el dinero propio del agente
					));
			
		}
	}

}
