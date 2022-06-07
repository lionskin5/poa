package factories;

public abstract class FactoryGlobal {
	
	public static FactoryGlobal getInstancia(String tipo) {
		
		FactoryGlobal factory = null;
		try {
			factory = (FactoryGlobal) Class.forName(tipo).newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return factory.getInstance();
	}

	protected abstract FactoryGlobal getInstance();

}
