package lonja.marketAgents;

import java.util.ArrayList;
import java.util.List;

import commonBehaviours.DFServiceManager;
import commonBehaviours.DFSubsBehaviour;
import commonBehaviours.MarketAchieveInitiator;
import commonBehaviours.MarketAchieveResponder;
import elements.bank.BankAccCreation;
import elements.bank.RegisterConvParam;
import elements.register.Register;
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
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.states.MsgReceiver;
import makers.ACLMaker;
import makers.MTMaker;
import makers.RegisterStorage;

@SuppressWarnings("serial")
public class RegisterAgent extends ServiceAgent {
	
	private RegisterStorage registerContainer;
	private AID bankAgent;
//	private List<Register> clientRegisters;
	
	FactoryOntology factRegis = (FactoryOntology) FactoryGlobal.getInstancia(FactoriesNames.REGISTERFACTORY);
	FactoryOntology factBank = (FactoryOntology) FactoryGlobal.getInstancia(FactoriesNames.BANKFACTORY);
	
	public RegisterAgent() {
		super("register-service", "Fish Market Register");
	}
	
	public AID getBankAgent() {
		return bankAgent;
	}
	public void setBankAgent(AID bankAgent) {
		this.bankAgent = bankAgent;
	}
	
	public void addClient(Register client) {
		this.registerContainer.addRegister(client);
	}

	@Override
	public void setup() {
		super.setup();
		
		Object[] args = getArguments();
		if(args != null && args.length == 1) {
			this.registerContainer = (RegisterStorage) args[0];
			
			this.getContentManager().registerOntology(factRegis.getOnto());
			this.getContentManager().registerOntology(factBank.getOnto());
			
			// Nos suscribimos al DF para recibir el AID del banquero
			addBehaviour(new DFBankSubsBehaviour(this, DFServiceManager.createSubscriptionMessage(this, getDefaultDF(), "bank-service")));
			
		}
		
	}
	
	private class DFBankSubsBehaviour extends DFSubsBehaviour {
		
		public DFBankSubsBehaviour(Agent a, ACLMessage msg) {
			super(a, msg);
			System.out.println("Creando suscripción al banco");
		}

		@Override
		public void agentPerfomance(DFAgentDescription[] dfds, ACLMessage inform) {
			System.out.println("Cambiando el agente bancario");
			System.out.println("Datos del DF: " + dfds[0].getName());
			((RegisterAgent) this.getAgent()).setBankAgent(dfds[0].getName());
			MessageTemplate mt = MTMaker.createMT(MarketAchieveResponder.createMessageTemplate(FIPANames.InteractionProtocol.FIPA_REQUEST), getCodec().getName(), factRegis.getOnto().getName());
			addBehaviour(new RegisterResponder2(myAgent, mt)); // Más de un minuto de espera. Única forma de hacerlo sin crear un comportamiento propio que hereda de OneShotBehaviour
		}	
		
	}
	
	private class RegisterResponder2 extends MsgReceiver {
		
		public RegisterResponder2(Agent a, MessageTemplate mt) {
			super(a, mt, MsgReceiver.INFINITE, null, null);
		}
		
//		@Override
//		protected void handleMessage(ACLMessage msg) {
//			
//			// Comprobar si ya se ha dado de alta
//			
//			Register newRegist = new Register();
//			newRegist.setMemberAID(msg.getSender());
//			addClient(newRegist); // El problema está en el registro
//						
//			System.out.println("Nuevo registro: " + newRegist.getMemberAID());
//			System.out.println("Register: " + newRegist);
//			System.out.println(msg);
//						
//			ACLMessage bankRequest = ACLMaker.createMessage(ACLMessage.REQUEST, myAgent.getAID(), FIPANames.InteractionProtocol.FIPA_REQUEST, bankAgent, getCodec().getName(), factBank.getOnto().getName(), ""+System.currentTimeMillis());
//			System.out.println("En RegisterResponder banco es: " + bankAgent);
//			BankAccCreation bankA = new BankAccCreation();
//			bankA.setDeposit(Integer.parseInt(msg.getContent()));
//			bankA.setOwner(newRegist);
//						
//			try {
//				myAgent.getContentManager().fillContent(bankRequest, new Action(myAgent.getAID(), bankA)); // El contenido será un entero que habrá que convertir a String
//				} catch (CodecException e) {
//					e.printStackTrace();
//				} catch (OntologyException e) {
//					e.printStackTrace();
//				}
//			System.out.println("La bankRequest es: " + bankRequest);
//			addBehaviour(new BankAccCreatRequest(myAgent, bankRequest));
//		}	
		
		@Override
		protected void handleMessage(ACLMessage msg) {
			
			// Comprobar si ya se ha dado de alta
			
			Register newRegist = new Register();
			newRegist.setMemberAID(msg.getSender());
			addClient(newRegist); // El problema está en el registro
						
			System.out.println("Nuevo registro: " + newRegist.getMemberAID());
			System.out.println(msg);
						
			ACLMessage bankRequest = ACLMaker.createMessage(ACLMessage.REQUEST, myAgent.getAID(), FIPANames.InteractionProtocol.FIPA_REQUEST, bankAgent, getCodec().getName(), factBank.getOnto().getName(), ""+System.currentTimeMillis());
			BankAccCreation bankA = new BankAccCreation();
			bankA.setDeposit(Integer.parseInt(msg.getContent()));
			bankA.setOwner(newRegist);
			RegisterConvParam params = new RegisterConvParam();
			params.setAccount(bankA);
			params.setReplyWith(msg.getReplyWith());
			params.setConvid(msg.getConversationId());
						
			try {
				myAgent.getContentManager().fillContent(bankRequest, new Action(myAgent.getAID(), params)); // El contenido será un entero que habrá que convertir a String
				} catch (CodecException e) {
					e.printStackTrace();
				} catch (OntologyException e) {
					e.printStackTrace();
				}
			System.out.println("La bankRequest es: " + bankRequest);
			addBehaviour(new BankAccCreatRequest(myAgent, bankRequest));
		}
		
		@Override
		public int onEnd() {
			System.out.println("Reseteando Behaviour");
			super.onEnd();
			reset();
			myAgent.addBehaviour(this);
			return 0;
		}
		
	}
	
	private class RegisterResponder extends MarketAchieveResponder {

		public RegisterResponder(Agent a, MessageTemplate mt) {
				super(a, mt);
			}
		
//		@Override
//		public void onStart() {
//			
//			System.out.println("Hola RegisterAgent");
//		}
//		
		// En este caso se enviarían dos failure. Revisar
		@Override
		protected ACLMessage responderPerfomance(ACLMessage request) {
			
			
			// Comprobar si ya se ha dado de alta
			
			ACLMessage msg = null;
			
			Register newRegist = new Register();
			newRegist.setMemberAID(request.getSender());
			addClient(newRegist); // El problema está en el registro
			
			System.out.println("Nuevo registro: " + newRegist.getMemberAID());
			System.out.println("Register: " + newRegist);

//			ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
//			msg.addReceiver(bankAgent);
//			msg.setLanguage(getCodec().getName());
//			msg.setOntology(BankOntology.getInstance().getName());
//			msg.setReplyWith(request.getSender().toString());
			
			ACLMessage bankRequest = ACLMaker.createMessage(ACLMessage.REQUEST, myAgent.getAID(), FIPANames.InteractionProtocol.FIPA_REQUEST, bankAgent, getCodec().getName(), factBank.getOnto().getName(), request.getSender().toString());
			System.out.println("En RegisterResponder banco es: " + bankAgent);
			BankAccCreation bankA = new BankAccCreation();
			bankA.setDeposit(Integer.parseInt(request.getContent()));
			bankA.setOwner(newRegist);
			try {
				myAgent.getContentManager().fillContent(bankRequest, new Action(myAgent.getAID(), bankA)); // El contenido será un entero que habrá que convertir a String
			} catch (CodecException e) {
				e.printStackTrace();
			} catch (OntologyException e) {
				e.printStackTrace();
			}
			
			this.block(3000);
			System.out.println("La bankRequest es: " + bankRequest);
			addBehaviour(new BankAccCreatRequest(myAgent, bankRequest)); // Añadir el mensaje y a quien se lo envía
			// Espero el inform y devuelvo un inform, eso se puede hacer un handleinform
			// O guardamos el aid, o lo enviamos en contenido o no enviamos inform. Supongo que habrá que meterlo en el contenido o en reply-with. Esta última parece buena O enviar el inform directamente, pues siempre se crearán las cuentas bancarias
			return null;
			}
		}
	
	// Comportamiento que crea una cuenta bancaria
	private class BankAccCreatRequest extends MarketAchieveInitiator {

		// En msg se debe de encontrar a quien se le envía
		public BankAccCreatRequest(Agent a, ACLMessage msg) {
			super(a, msg);
			System.out.println("BankAccCreatRequest creado con: " + msg.toString());
			}

//		@Override
//		protected void initiatorInfoPerformance(ACLMessage inform) {
//			 // Puede ser que esto no funcione. El protocolo es FIPA_REQUEST porque es el que se envío en el REQUEST inicial del Inititator
//			// Podemos o debemos enviar el agente bancario por aquí, creo yo.
//			System.out.println("INFORM del Bank");
//			
//			// Tengo que extraer el receiver del AgentAction
//			BankAccCreation acccreat = null;
//			AID receiverAID = null;
//			
//			try { // Aquí es necesario recuperar el objeto Action, y a partir de éste obtener la accion y hacerle un casting a TICK
//				ContentElement ce = myAgent.getContentManager().extractContent(inform);
//				Concept cc = null;
//				if (ce instanceof Action) {
//					cc = ((Action) ce).getAction();
//					if(cc instanceof BankAccCreation) {
//						acccreat = (BankAccCreation) cc;
//						System.out.println("He recibido el BANKACCCREATION: " + acccreat.toString());
//						receiverAID = acccreat.getOwner().getMemberAID();
//						}
//				}
//			} catch (UngroundedException e) {
//				e.printStackTrace();
//			} catch (CodecException e) {
//				e.printStackTrace();
//			} catch (OntologyException e) {
//				e.printStackTrace();
//			}
//			
//			ACLMessage msg = ACLMaker.createMessage(ACLMessage.INFORM, myAgent.getAID(), FIPANames.InteractionProtocol.FIPA_REQUEST, receiverAID, getCodec().getName(), factRegis.getOnto().getName());
//			myAgent.send(msg);
//			System.out.println("Mensaje enviado a: " + msg.getAllReceiver().next());
//			System.out.println("Register: " + acccreat.getOwner());
//			// Envía un inform al usuario indicando que se le ha creado el registro y su cuenta bancaria	
//			
////			MessageTemplate mt = MTMaker.createMT(MarketAchieveResponder.createMessageTemplate(FIPANames.InteractionProtocol.FIPA_REQUEST), getCodec().getName(), factBank.getOnto().getName());
////			addBehaviour(new JustSendMensagge(myAgent, mt));
////			
////			ACLMessage send = ACLMaker.createMessageWithContentConcept(ACLMessage.REQUEST, myAgent.getAID(), FIPANames.InteractionProtocol.FIPA_REQUEST, myAgent.getAID()
////																		, getCodec().getName(), factBank.getOnto().getName(), ""+System.currentTimeMillis(), myAgent, acccreat);
////			System.out.println("Me autoenvío: " + send);
////			myAgent.send(send);
//			
//		}
		
		@Override
		protected void initiatorInfoPerformance(ACLMessage inform) {
			 // Puede ser que esto no funcione. El protocolo es FIPA_REQUEST porque es el que se envío en el REQUEST inicial del Inititator
			// Podemos o debemos enviar el agente bancario por aquí, creo yo.
			System.out.println("INFORM del Bank");
			
			// Tengo que extraer el receiver del AgentAction
			RegisterConvParam params = null;
			AID receiverAID = null;
			String inReplyTo = null;
			String convid = null;
			
			try {
				ContentElement ce = myAgent.getContentManager().extractContent(inform);
				Concept cc = null;
				if (ce instanceof Action) {
					cc = ((Action) ce).getAction();
					if(cc instanceof RegisterConvParam) {
						params = (RegisterConvParam) cc;
						System.out.println("He recibido los parámetors en registrador: " + params.toString());
						receiverAID = params.getAccount().getOwner().getMemberAID();
						inReplyTo = params.getReplyWith();
						convid = params.getConvid();
						}
				}
			} catch (UngroundedException e) {
				e.printStackTrace();
			} catch (CodecException e) {
				e.printStackTrace();
			} catch (OntologyException e) {
				e.printStackTrace();
			}
			
			ACLMessage msg = ACLMaker.createMessage(ACLMessage.INFORM, myAgent.getAID(), FIPANames.InteractionProtocol.FIPA_REQUEST, receiverAID
													, getCodec().getName(), factRegis.getOnto().getName(), ""+System.currentTimeMillis());
			msg.setInReplyTo(inReplyTo);
			msg.setConversationId(convid);
			myAgent.send(msg);
			System.out.println("Mensaje enviado a: " + msg.getAllReceiver().next());
			System.out.println("Parámetros: " + params.getReplyWith() + " " + params.getConvid());
			System.out.println(msg);
			// Envía un inform al usuario indicando que se le ha creado el registro y su cuenta bancaria	
			
//			MessageTemplate mt = MTMaker.createMT(MarketAchieveResponder.createMessageTemplate(FIPANames.InteractionProtocol.FIPA_REQUEST), getCodec().getName(), factBank.getOnto().getName());
//			addBehaviour(new JustSendMensagge(myAgent, mt));
//			
//			ACLMessage send = ACLMaker.createMessageWithContentConcept(ACLMessage.REQUEST, myAgent.getAID(), FIPANames.InteractionProtocol.FIPA_REQUEST, myAgent.getAID()
//																		, getCodec().getName(), factBank.getOnto().getName(), ""+System.currentTimeMillis(), myAgent, acccreat);
//			System.out.println("Me autoenvío: " + send);
//			myAgent.send(send);
			
		}

		@Override
		protected void initiatorRefusePerformance(ACLMessage refuse) {
			System.out.println("FAILURE del Bank");
			 // Puede ser que esto no funcione. El protocolo es FIPA_REQUEST porque es el que se envío en el REQUEST inicial del Inititator
			ACLMessage msg = ACLMaker.createMessage(ACLMessage.FAILURE, myAgent.getAID(), FIPANames.InteractionProtocol.FIPA_REQUEST, new AID(refuse.getInReplyTo(), AID.ISLOCALNAME)
													, getCodec().getName(), factBank.getOnto().getName(), ""+System.currentTimeMillis());
			myAgent.send(msg);
			// Envía un inform al usuario indicando que se le ha creado el registro y su cuenta bancaria
			
			}
		
		}
	
	private class JustSendMensagge extends MarketAchieveResponder {

		public JustSendMensagge(Agent a, MessageTemplate mt) {
			super(a, mt);
			System.out.println("Just Send: " + mt);
		}

		@Override
		protected ACLMessage responderPerfomance(ACLMessage request) {
			System.out.println("Just Send mensaje recibido: " + request);

			BankAccCreation acccreat = null;
			
			try {
				ContentElement ce = myAgent.getContentManager().extractContent(request);
				Concept cc = null;
				if (ce instanceof Action) {
					cc = ((Action) ce).getAction();
					if(cc instanceof BankAccCreation) {
						acccreat = (BankAccCreation) cc;
						System.out.println("Just Send BANKACCCREATION: " + acccreat.toString());
						// Devolver un inform
						}
				}
			} catch (UngroundedException e) {
				e.printStackTrace();
			} catch (CodecException e) {
				e.printStackTrace();
			} catch (OntologyException e) {
				e.printStackTrace();
			}	
			
			return ACLMaker.createMessage(ACLMessage.INFORM, myAgent.getAID(), FIPANames.InteractionProtocol.FIPA_REQUEST, acccreat.getOwner().getMemberAID()
											, getCodec().getName(), factRegis.getOnto().getName(), ""+System.currentTimeMillis());
		}
		
		
	}

}
