package loaders;

// Esta clase carga el nombre y el fichero de scenario.yaml
public class ConfigLoader {
	
	String name;
	String config;

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getConfig() {
		return config;
	}
	public void setConfig(String config) {
		this.config = config;
	}

	@Override
	public String toString() {
		return  "ConfigLoader " + "[name = " + name + ",\n" + "config " + config + "]"; // Creo que esto devuelve la clase dinámica
	}

}
