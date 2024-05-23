package communicationFramework.config;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import communicationFramework.exceptions.MsLoadConfigException;
import communicationFramework.exceptions.MsTypeInvalidException;

/**
 * 
 * @author Vasilije Mirkovic
 *
 */

public class ServerConfigurationManager {

	private Map<String, MSConfig> configs;
	private String path;
	private static final Logger LOGGER = LoggerFactory.getLogger(ServerConfigurationManager.class);


	public ServerConfigurationManager(String path) {
		this.path = path;
		try {
			this.configs = this.loadConfigFromFile();
		} catch (ParserConfigurationException | SAXException e) {
			throw new MsLoadConfigException("Config file XML invalid!");
		} catch (IOException e) {
			throw new MsLoadConfigException("Config file could not be found!");
		}
	}

	/**
	 * 
	 * @return map which contains the registered Peers. It consists of name of the
	 *         Peer as the key and the MSConfig as the value MSConfig consists of
	 *         name of the Peer, minumum and maximum port range of Peer
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	public Map<String, MSConfig> loadConfigFromFile() throws ParserConfigurationException, SAXException, IOException {
		Map<String, MSConfig> configs = new HashMap<String, MSConfig>();
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
		DocumentBuilder db = dbf.newDocumentBuilder();

		Document doc = db.parse(new File(this.path));
		try {
			NodeList list = doc.getElementsByTagName("configurations").item(0).getChildNodes();

			for (int temp = 0; temp < list.getLength(); temp++) {
				Node node = list.item(temp);
				if (node.getNodeType() == Node.ELEMENT_NODE && node.getNodeName().equals("configuration")) {

					Element element = (Element) node;
					String name = element.getAttribute("name");
					Integer minPortRange = Integer
							.valueOf(element.getElementsByTagName("minPortRange").item(0).getTextContent());
					Integer maxPortRange = Integer
							.valueOf(element.getElementsByTagName("maxPortRange").item(0).getTextContent());
					if (minPortRange >= maxPortRange) {
						throw new Exception();
					}
					MSConfig config = new MSConfig(name, minPortRange, maxPortRange);
					configs.put(name, config);
				}
			}
		} catch (Exception e) {
			throw new MsLoadConfigException("Configuration syntax wrong!");
		}
		if (configs.isEmpty()) {
			throw new MsLoadConfigException("No configurations found!");
		}
		LOGGER.info("Config loaded from file!");
		return configs;
	}

	/**
	 * 
	 * @param name
	 * @return a specific MSConfig from the list of all registered MSConfigs
	 */
	public MSConfig getConfigByName(String name) {
		if (!this.configs.containsKey(name)) {
			throw new MsTypeInvalidException();
		}
		return this.configs.get(name);
	}

	/**
	 * 
	 * @return list of registered instances
	 */
	public List<MSConfig> getConfigs() {
		return new ArrayList<MSConfig>(this.configs.values());
	}

}
