package communicationFramework.server;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

import communicationFramework.config.MSConfig;
import communicationFramework.config.ServerConfigurationManager;


public class ServerConfigurationManagerTest {
	
	private static ServerConfigurationManager serverConfigurationManager;

	@BeforeAll
	public static void getPath() {
        URL resource = ServerConfigurationManagerTest.class.getClassLoader().getResource("configuration.xml");
        Path pathToConfig = null;

        if (resource == null) {
            throw new IllegalArgumentException("configuration.xml file not found!");
        }

        try {
            String decodedPath = URLDecoder.decode(resource.getPath(), "UTF-8");
            pathToConfig = Paths.get(decodedPath.substring(1));

        } catch (UnsupportedEncodingException | NullPointerException e) {
            e.printStackTrace();
        }
        serverConfigurationManager = new ServerConfigurationManager(pathToConfig.toString());
    }

	@Test
	public void loadConfigFromFileTest() throws ParserConfigurationException, SAXException, IOException {
		Map<String, MSConfig> configs = serverConfigurationManager.loadConfigFromFile();

		Assert.assertEquals(4, configs.size());
	}
	
	@Test
	public void checkConfigsFromFileTest() throws ParserConfigurationException, SAXException, IOException {
		Map<String, MSConfig> configs = serverConfigurationManager.loadConfigFromFile();

		Assert.assertTrue(configs.values().stream().anyMatch(con -> "MS-FOG-TEMP".equals(con.getName())));
		Assert.assertTrue(configs.values().stream().anyMatch(con -> "IOT-TEMP".equals(con.getName())));
		Assert.assertTrue(configs.values().stream().anyMatch(con -> "MS-FOG-CO2".equals(con.getName())));
		Assert.assertTrue(configs.values().stream().anyMatch(con -> "IOT-CO2".equals(con.getName())));
	}


}
