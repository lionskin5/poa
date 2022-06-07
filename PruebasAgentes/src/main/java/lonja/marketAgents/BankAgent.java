package lonja.marketAgents;

import java.util.ArrayList;
import java.util.List;

import commonBehaviours.MarketAchieveResponder;
import elements.bank.BankAccount;
import elements.bank.Deposit;
import elements.bank.RegisterConvParam;
import elements.bank.TransferAmount;
import elements.bank.Withdraw;
import factories.FactoriesNames;
import factories.FactoryGlobal;
import factories.FactoryOntology;
import jade.content.Concept;
import jade.content.ContentElement;
import jade.content.lang.Codec.CodecException;
import jade.content.onto.OntologyException;
import jade.content.onto.UngroundedException;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.core.Agent;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import makers.ACLMaker;
import makers.MTMaker;

@SuppressWarnings("serial")
public class BankAgent extends ServiceAgent {
	
	
	// Esto será concepto de una ontología
	private List<BankAccount> bankAccounts;
	private FactoryOntology factBank;
	
	public BankAgent() {
		super("bank-service", "Fish Market Bank");
		this.bankAccounts = new ArrayList<>();
	}
	
	public void addAccount(BankAccount ba) {
		this.bankAccounts.add(ba);
	}
	
	public BankAccount getAccount(AID agent) {
		for(BankAccount ba : this.bankAccounts) {
			if(ba.getOwner().getMemberAID() == agent) // Cuidado con esta comparación
				return ba;
		}
		return null;
	}
	
	@Override
	public void setup() {
		
		// Aquí se da de alta
		super.setup();
		
		factBank = (FactoryOntology) FactoryGlobal.getInstancia(FactoriesNames.BANKFACTORY);
		this.getContentManager().registerOntology(factBank.getOnto());
		this.getContentManager().registerOntology(((FactoryOntology) FactoryGlobal.getInstancia(FactoriesNames.REGISTERFACTORY)).getOnto());

		//MessageTemplate mt = MTMaker.createMT(MarketAchieveResponder.createMessageTemplate(FIPANames.InteractionProtocol.FIPA_REQUEST), this.getCodec().getName(), factBank.getOnto().getName());
		MessageTemplate mtACR = MTMaker.createMTWithMatchExpr(MarketAchieveResponder.createMessageTemplate(FIPANames.InteractionProtocol.FIPA_REQUEST), this.getCodec().getName(), factBank.getOnto().getName(), "RegisterConvParam");
		System.out.println("Bank agent MT: " + mtACR.toString());
		MessageTemplate mtMAR = MTMaker.createMTORWithMT(
										MTMaker.createMTORWithMT(MTMaker.createMTWithMatchExpr(MarketAchieveResponder.createMessageTemplate(FIPANames.InteractionProtocol.FIPA_REQUEST), this.getCodec().getName(), factBank.getOnto().getName(), "Deposit")
										, MTMaker.createMTWithMatchExpr(MarketAchieveResponder.createMessageTemplate(FIPANames.InteractionProtocol.FIPA_REQUEST), this.getCodec().getName(), factBank.getOnto().getName(), "Withdraw"))
										, MTMaker.createMTWithMatchExpr(MarketAchieveResponder.createMessageTemplate(FIPANames.InteractionProtocol.FIPA_REQUEST), this.getCodec().getName(), factBank.getOnto().getName(), "TransferAmount"));
		// El mismo mt para ambos, el comportamiento tendrá que comporbar la acción
		addBehaviour(new AccCreationResponder(this, mtACR));
		addBehaviour(new MoneyActionsResponder(this, mtMAR)); // Crea FAILURE si le metes el mismo mt y además lee mensajes de otros behaviours!
		
	}
	
	private class AccCreationResponder extends MarketAchieveResponder {

		public AccCreationResponder(Agent a, MessageTemplate mt) {
			super(a, mt);
		}

//		@Override
//		protected ACLMessage responderPerfomance(ACLMessage request, ACLMessage msg) { // Aquí se crea la cuenta bancaria
//			
//			System.out.println("Solicitud de creación de cuenta: " + request);
//			BankAccCreation acccreat = null;
//			System.out.println(request.getReplyWith());
//			
//			try {
//				ContentElement ce = myAgent.getContentManager().extractContent(request);
//				Concept cc = null;
//				if (ce instanceof Action) {
//					cc = ((Action) ce).getAction();
//					if(cc instanceof BankAccCreation) {
//						acccreat = (BankAccCreation) cc;
//						System.out.println("Ha llegado un BANKACCCREATION: " + acccreat.toString());
//						BankAccount newBA = new BankAccount();
//						newBA.setDeposit(acccreat.getDeposit());
//						newBA.setOwner(acccreat.getOwner());
//						((BankAgent) myAgent).addAccount(newBA);
//						// Devolver un inform
//						msg = ACLMaker.createReponseWithContentConcept(request, ACLMessage.INFORM, myAgent, acccreat);
//						}
//				}
//			} catch (UngroundedException e) {
//				e.printStackTrace();
//			} catch (CodecException e) {
//				e.printStackTrace();
//			} catch (OntologyException e) {
//				e.printStackTrace();
//			}	
//			return msg;
//		}
		
		@Override
		protected ACLMessage responderPerfomance(ACLMessage request) { // Aquí se crea la cuenta bancaria
			
			ACLMessage msg = null;
			System.out.println("Solicitud de creación de cuenta: " + request);
			RegisterConvParam params = null;
			System.out.println(request.getReplyWith());
			
			try {
				ContentElement ce = myAgent.getContentManager().extractContent(request);
				Concept cc = null;
				if (ce instanceof Action) {
					cc = ((Action) ce).getAction();
					if(cc instanceof RegisterConvParam) {
						params = (RegisterConvParam) cc;
						System.out.println("Ha llegado un RegisterConvParam: " + params.toString());
					//	BankAccount newBA = new BankAccount();
						BankAccount newBA = new BankAccount(params.getAccount().getDeposit(), params.getAccount().getOwner());
						//newBA.setDeposit(params.getAccount().getDeposit());
						//newBA.setOwner(params.getAccount().getOwner());
						((BankAgent) myAgent).addAccount(newBA);
						// Devolver un inform
						msg = ACLMaker.createReponseWithContentConcept(request, ACLMessage.INFORM, myAgent, params);
						}
				}
			} catch (UngroundedException e) {
				e.printStackTrace();
			} catch (CodecException e) {
				e.printStackTrace();
			} catch (OntologyException e) {
				e.printStackTrace();
			}	
			return msg;
		}
		
		
	}
	
	private class MoneyActionsResponder extends MarketAchieveResponder {

		public MoneyActionsResponder(Agent a, MessageTemplate mt) {
			super(a, mt);
		}	
		
		@Override	
		protected ACLMessage responderPerfomance(ACLMessage request) {
			
			ACLMessage msg = null;

			try {
				ContentElement ce = myAgent.getContentManager().extractContent(request);
				Concept cc = null;
				if (ce instanceof Action) {
					cc = ((Action) ce).getAction();
					if(cc instanceof Deposit) {
						Deposit dp = (Deposit) cc;
						System.out.println("Ha llegado un Deposit: " + dp.toString());
						// Deposito el dinero en la cuenta correspondiente
						AID receiver = dp.getTo();
						BankAccount ba = getAccount(receiver);
						ba.deposit(dp.getAmount());
						// Devolver un inform y actualizar
						msg = ACLMaker.createResponse(request, ACLMessage.INFORM);
						}
					else if (cc instanceof Withdraw) {
						Withdraw wd = (Withdraw) cc;
						System.out.println("Ha llegado un Withdraw: " + wd.toString());
						AID receiver = wd.getFrom();
						BankAccount ba = getAccount(receiver);
						ba.withdraw(wd.getAmount());
						// Devolver un inform y actualizar
						msg = ACLMaker.createResponse(request, ACLMessage.INFORM);
						}
					else if (cc instanceof TransferAmount) {
						TransferAmount ta = (TransferAmount) cc;
						System.out.println("Ha llegado un TransferAmount: " + ta.toString());
						AID from = ta.getFrom();
						AID to = ta.getTo();
						double amount = ta.getDeposit();
						BankAccount baFrom = getAccount(from);
						BankAccount baTo = getAccount(to);
						baFrom.withdraw(amount);
						baTo.deposit(amount);
						// Devolver un inform y actualizar
						msg = ACLMaker.createResponse(request, ACLMessage.INFORM);
					}
				}
			} catch (UngroundedException e) {
				e.printStackTrace();
			} catch (CodecException e) {
				e.printStackTrace();
			} catch (OntologyException e) {
				e.printStackTrace();
			}
			
			return msg;
			}
		}		
		
		
}
