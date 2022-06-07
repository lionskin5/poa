package loaders;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

import org.yaml.snakeyaml.Yaml;

public class LoaderManager {
	
	public static int [] getClockArguments(String file) {
		
		int args[] = new int[3];
		try {
			Yaml yaml = new Yaml();
			InputStream inputStream;
			inputStream = new FileInputStream(file);
			ClockLoader cl = yaml.load(inputStream);
			args[0] = cl.getUnitTimeMillis();
			args[1] = cl.getNumUnitDay();
			args[2] = cl.getNumSimDays();
			//getLogger().info("initAgentFromConfigFile", config.toString());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}	
		return args;	
	}
	
	public static int getDirectorArgument(String file) {
		
		int numberOflanes = 0;
		
		try {
			Yaml yaml = new Yaml();
			InputStream inputStream;
			inputStream = new FileInputStream(file);
			DirectorLoader dl = yaml.load(inputStream);
			numberOflanes = dl.getNumberOfLanes();
			//getLogger().info("initAgentFromConfigFile", config.toString());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return numberOflanes;
	}
	
	public static int [] getSeaArguments(String file) {
		
		int args[] = new int[4];
		
		try {
			Yaml yaml = new Yaml();
			InputStream inputStream;
			inputStream = new FileInputStream(file);
			SeaLoader sea = yaml.load(inputStream);
			args[0] = sea.getLowRange();
			args[1] = sea.getMediumRange();
			args[2] = sea.getHighRange();
			args[3] = sea.getTopRange();
			//getLogger().info("initAgentFromConfigFile", config.toString());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return args;
		
	}
	
	public static double [][] getClientArguments(String file) {
		
		double [][] args = new double [2][];
		double [] fishArray = new double [9];
		double [] qualityArray = new double [5];
		
		try {
			Yaml yaml = new Yaml();
			InputStream inputStream;
			inputStream = new FileInputStream(file);
			ClientLoader client = yaml.load(inputStream);
			List<Double> fishList = client.getFishPrices();
			List<Integer> qualities = client.getQualityBonus();
			int i = 0; int j = 0;
			for(Double fish : fishList) {
				fishArray[i] = fish;
				i++;
			}
			for(Integer qua : qualities) {
				qualityArray[j] = ((double) qua)/100;
				j++;
			}
			args[0] = fishArray;
			args[1] = qualityArray;
			//getLogger().info("initAgentFromConfigFile", config.toString());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return args;
		
		
	}
	
	public static double [] getClientPrices(String file) {

		double [] args = new double [9];
		
		try {
			Yaml yaml = new Yaml();
			InputStream inputStream;
			inputStream = new FileInputStream(file);
			ClientLoader client = yaml.load(inputStream);
			List<Double> fishList = client.getFishPrices();
			int i = 0;
			for(Double fish : fishList) {
				args[i] = fish;
				i++;
			}
			//getLogger().info("initAgentFromConfigFile", config.toString());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return args;
		
	}
	
	public static double [] getClientQuality(String file) {
		
		double [] args = new double [5];
		
		try {
			Yaml yaml = new Yaml();
			InputStream inputStream;
			inputStream = new FileInputStream(file);
			ClientLoader client = yaml.load(inputStream);
			List<Integer> qualities = client.getQualityBonus();
			int i = 0;
			for(Integer qua : qualities) {
				args[i] = ((double) qua)/100;
				i++;
			}
			//getLogger().info("initAgentFromConfigFile", config.toString());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return args;
		
	}
	
	public static int getSellerArguments(String file) {
		
		int budget = 0;
		
		try {
			Yaml yaml = new Yaml();
			InputStream inputStream;
			inputStream = new FileInputStream(file);
			SellerLoader sl = yaml.load(inputStream);
			budget = sl.getBudget();
			//getLogger().info("initAgentFromConfigFile", config.toString());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		return budget;
		
	}
	
	public static int [] getSellerRangeArguments(String file) {
		
		int args[] = new int[1];
		
		try {
			Yaml yaml = new Yaml();
			InputStream inputStream;
			inputStream = new FileInputStream(file);
			SellerRangeLoader sl = yaml.load(inputStream);
			args[0] = sl.getBudget();
			args[1] = sl.getRange();
			//getLogger().info("initAgentFromConfigFile", config.toString());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		return args;
		
	}
	
	public static int getBuyerArguments(String file) {
		
		int budget = 0;
		
		try {
			Yaml yaml = new Yaml();
			InputStream inputStream;
			inputStream = new FileInputStream(file);
			BuyerLoader bl = yaml.load(inputStream);
			budget = bl.getBudget();
			//getLogger().info("initAgentFromConfigFile", config.toString());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		return budget;
		
	}

}
