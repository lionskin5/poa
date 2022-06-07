package lonja.sellerAgents;

import elements.lot.Range;
import loaders.LoaderManager;

@SuppressWarnings("serial")
public class SellerRangeAgent extends SellerAgent {
	
	private Range range;
	
	@Override
	public void setup() {
		
		// Este setup podr�a meterse dentro del if, pero suponemos que siempre habr� argumentos, si no, no tendr�a sentido haber lanzado el agente.
		super.setup();
		
		
		Object [] args = getArguments();
		System.out.println("Seller arguments: " + args);
		if(args != null && args.length == 1 ) {
			
			int sellerArgs[] = LoaderManager.getSellerRangeArguments((String) args[0]);
			this.setBudget(sellerArgs[0]);
			this.range = Range.values()[sellerArgs[1]];
			
		}
		
	}
	
	
	
	

}
