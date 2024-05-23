package communicationFramework.client;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

import communicationFramework.config.MSConfig;
import communicationFramework.config.ServerConfigurationManager;
import communicationFramework.dto.PeerMessageDto;

/**
 * @author Vasilije Mirkovic
 */
public class ClientManager implements Runnable{
	
	
	private Map<Integer, Client> clients = new ConcurrentHashMap<>();
	private ServerConfigurationManager serverConfigurationManager;
	private int myPort;
	
    private static final Logger LOGGER = LoggerFactory.getLogger(ClientManager.class);
	
	/**
	 * makes a new client manager, which is used later for sending a message
	 * @param myPort
	 * @param serverConfigurationManager
	 */
	public ClientManager(int myPort, ServerConfigurationManager serverConfigurationManager) {
		this.myPort = myPort;
		this.serverConfigurationManager = serverConfigurationManager;
	}
	
	/**
	 * starts the thread which searches for the port, where the next MS instance is
	 */
	@Override
	public void run() {
		LOGGER.info("Searching for clients");
		while(true) {
			for(MSConfig msConfig : this.serverConfigurationManager.getConfigs()) {
				for(int i = msConfig.getPortStartRange(); i < msConfig.getPortEndRange(); i++) {
					if(i == this.myPort) {
						continue;
					}
					Client client = this.clients.get(i);
					if(client != null) {
						if(client.isClosed()) {
							this.clients.remove(i);
						}
					} else {
						client = new Client(i, msConfig);
						try {
							client.createConnection();
							clients.put(i, client);
						} catch (IOException e) {
						}
					}
				}
			}
		}
	}
	
	
	/**
	 * send a message to all registered instances
	 * @param message
	 */
	public void sendMessage(PeerMessageDto message) {
		for(Client client : this.clients.values()) {
			try {
				client.sendMessage(message);
			} catch (IOException e) {
				this.clients.remove(client.getPort());
				LOGGER.error("Error sending to client " + client.getPort());
				e.printStackTrace();
			}
		}
	}
}
