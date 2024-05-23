package communicationFramework;

import java.net.URL;
import java.nio.file.Paths;

import communicationFramework.server.CommunicationFramework;

import java.nio.file.Path;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * 
 * @author Vasilije Mirkovic
 *
 */

public class Application {

	public static void main(String[] args) {

		URL resource = Application.class.getClassLoader().getResource("configuration.xml");

		if (resource == null) {
			throw new IllegalArgumentException("configuration.xml file not found!");
		}

		try {
			String decodedPath = URLDecoder.decode(resource.getPath(), "UTF-8");
			Path pathToConfig = Paths.get(decodedPath.substring(1));

			@SuppressWarnings("unused")
			CommunicationFramework communicationFrameworkObject = new CommunicationFramework("IOT-TEMP",
					pathToConfig.toString());

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
}

/*
 * This is a test class, which I made to test Communication Framework. This
 * class presents, how it should looks like, when one Microservice wants to
 * start a Communication Framework.
 * 
 * There are two parameters in constructor. First one is msType, which is the
 * name of an Instance of MS, and second one is the path of the XML file, where
 * that instance is made.
 */