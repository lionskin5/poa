package launcher;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import jade.util.Logger;

import org.yaml.snakeyaml.Yaml;

import elements.activities.Sleep;
import factories.FactoriesNames;
import factories.FactoryAgent;
import factories.FactoryGlobal;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.ControllerException;
import loaders.ConfigLoader;
import loaders.ScenarioLoader;
import makers.LotStorage;
import utils.AgentLoggingHTMLFormatter;

public class FishMarketLauncher {
	
	private static FactoryAgent fact = (FactoryAgent) FactoryGlobal.getInstancia(FactoriesNames.AGENTFACTORY);

	public static void main(String[] args) throws SecurityException, IOException {
		
		if (args.length == 1) {
			
			String config_file = args[0];
			Yaml yaml = new Yaml();
			InputStream inputStream = new FileInputStream(config_file);
			ScenarioLoader scenario = yaml.load(inputStream);
			
			initLogging(scenario.getName());
			
			System.out.println(scenario);
			
			// Obtenemos una instancia del entorno runtime de Jade
			Runtime rt = Runtime.instance();
			// Terminamos la m√°quinq virtual si no hubiera ning√∫n contenedor de agentes activo
			rt.setCloseVM(true);
			// Lanzamos una plataforma en el puerto 8888
			// Y creamos un profile de la plataforma a partir de la cual podemos
			// crear contenedores
			Profile pMain = new ProfileImpl(null, 8888, null);
			System.out.println("Lanzamos una plataforma desde clase principal..."+pMain);
			// Creamos el contenedor
			AgentContainer mc = rt.createMainContainer(pMain);
			fact.setMc(mc);
			// Creamos un RMA (la GUI de JADE)
			System.out.println("Lanzando el agente RMA en el contenedor main ...");
			fact.createAgent("rma", "jade.tools.rma.rma", new Object[0]);

			// INICIALIZACI”N DE LOS AGENTES
			// Hay que leer los nuevos ficheros en los agentes si o si. O en un manager
		//	fact.createAgent("Agente Reset", commonAgents.AgenteReset.class.getName(), null);	
				
			
			// ClockAgent
		//	ClockAgentConfig cc = scenario.getClock();
			ConfigLoader cl = scenario.getClock();
			Object[] clockArgs = {cl.getConfig()};
			System.out.println(cl);
			fact.createAgent(cl.getName(), lonja.clock.ClockAgent.class.getName(), clockArgs);
			
//			// Director
			ConfigLoader dt = scenario.getDirector();
			Object[] directorArgs = {dt.getConfig()};
			fact.createAgent(dt.getName(), lonja.marketAgents.Director.class.getName(), directorArgs);
			
			// SeaAgent
//			ConfigLoader sea = scenario.getSea();
//			Object[] seaArgs = {sea.getConfig()};
//			fact.createAgent(sea.getName(), lonja.worldAgents.SeaAgent.class.getName(), seaArgs);
			
			// SeaAgent
//			ConfigLoader client = scenario.getClient();
//			Object[] clientArgs = {client.getConfig()};
//			fact.createAgent(client.getName(), lonja.worldAgents.ClientAgent.class.getName(), clientArgs);
			
			// SellerAgent
//			List<ConfigLoader> sl = scenario.getSellers();
//			for(ConfigLoader seller: sl) {
//				Object[] sellerArgs = {seller.getConfig()};
//				fact.createAgent(seller.getName(), lonja.sellerAgents.SellerAgent.class.getName(), sellerArgs);
//			}
			
//			// AuctioneerAgent
//			LotStorage ls = new LotStorage();
//			Object [] args2 = {ls};
//			fact.createAgent("Subastador", lonja.marketAgents.AuctioneerAgent.class.getName(), args2); // Este puede que requiera argumentos m·s adelante
//			
//			// BuyerAgent
//			List<ConfigLoader> bl = scenario.getBuyers();
//			for(ConfigLoader buyer: bl) {
//				Object[] buyerArgs = {buyer.getConfig()};
//				fact.createAgent(buyer.getName(), lonja.buyerAgents.BuyerAgent.class.getName(), buyerArgs);
//			}
			
			
			// AgenteFSM
//			fact.createAgent("AgenteFSM", lonja.pruebas.AgenteFSM.class.getName(), null);
		}		

	}
	
	
	// Hacerla privada si eso y quitarle static
	public static void initLogging(String scenarioName) throws SecurityException, IOException {
	      LogManager lm = LogManager.getLogManager();
	      
	      Logger logger = Logger.getMyLogger("lonja");
	      logger.setLevel(Level.INFO);
	      
	      FileHandler html_handler = new FileHandler("logs/"+scenarioName+".html");
	      html_handler.setFormatter(new AgentLoggingHTMLFormatter());
	      logger.addHandler(html_handler);

	      lm.addLogger(logger);
	}

}
