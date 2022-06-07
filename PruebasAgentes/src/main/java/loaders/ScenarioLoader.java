package loaders;

import java.util.List;

/**>
 * Define los parámetros que conforman un escenario para su simulación desde un fichero YAML.
 * Ref: https://www.baeldung.com/java-snake-yaml
 * 
 * @author pablo
 *
 */
public class ScenarioLoader {

	private String name;
	private String description;
	private ConfigLoader clock;
	private ConfigLoader director;
	private ConfigLoader sea;
	private ConfigLoader client;
	private List<ConfigLoader> sellers;
	private List<ConfigLoader> buyers;

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public ConfigLoader getClock() {
		return clock;
	}
	public void setClock(ConfigLoader clock) {
		this.clock = clock;
	}
	public ConfigLoader getDirector() {
		return director;
	}
	public ConfigLoader getSea() {
		return sea;
	}
	public void setSea(ConfigLoader sea) {
		this.sea = sea;
	}
	public ConfigLoader getClient() {
		return client;
	}
	public void setClient(ConfigLoader client) {
		this.client = client;
	}
	public void setDirector(ConfigLoader director) {
		this.director = director;
	}
	public List<ConfigLoader> getSellers() {
		return sellers;
	}
	public void setSellers(List<ConfigLoader> sellers) {
		this.sellers = sellers;
	}
	public List<ConfigLoader> getBuyers() {
		return buyers;
	}
	public void setBuyers(List<ConfigLoader> buyers) {
		this.buyers = buyers;
	}
	@Override
	public String toString() {
		return super.toString() +  ",\n" + "description=" + description + ",\n" +
				"clock=" + clock + ",\n" +
				"director=" + director + ",\n" +
				"sea=" + sea + ",\n" +
				"client=" + client + ",\n" +
				"sellers=" + sellers + ",\n" +
				"buyers=" + buyers + "]";
	}

}
