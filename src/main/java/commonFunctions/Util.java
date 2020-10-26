package commonFunctions;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Util {

	static Properties prop = null;
	
	public static void loadProperties(String filePath){
		prop = new Properties();
		InputStream input = null;
		
		try {
			input = new FileInputStream(filePath);
			prop.load(input);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}finally{
			if (input!=null) {
				try {
					input.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					//Log.error("Failed to load property file");
					//ErrorCollector.Verifyfail("Failed to load the property file");
				}
			}
		}
	}
	
	public static String getConfigData(String key){
		loadProperties(System.getProperty("user.dir")+ "\\src\\main\\resources\\Config.properties");
		String value = prop.getProperty(key);
		return value;
	}

}



