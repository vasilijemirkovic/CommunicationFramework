package communicationFramework.server;

import java.util.List;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import communicationFramework.client.ClientManager;
import communicationFramework.config.MSConfig;
import communicationFramework.config.ServerConfigurationManager;
import communicationFramework.dto.PeerMessageDto;

/**
 * 
 * @author Vasilije Mirkovic
 *
 */
public class CommunicationFramework {

	private TCPServer tcpServer;

	private ServerConfigurationManager serverConfigurationManager;
	private ClientManager clientManager;
	private String msType;
	
    private static final Logger LOGGER = LoggerFactory.getLogger(CommunicationFramework.class);


	public CommunicationFramework() {}

	/**
	 * Initializes Communication Framework with a certain ms type and a path to the XML configuration.
	 * @param msType
	 * @param configurationPath
	 */
	public CommunicationFramework(String msType, String configurationPath) {
		this.msType = msType;
		this.serverConfigurationManager = new ServerConfigurationManager(configurationPath);
		MSConfig config = this.serverConfigurationManager.getConfigByName(msType);
		int diff = config.getPortEndRange() - config.getPortStartRange();
		int port = config.getPortStartRange() + new Random().nextInt(diff);
		
		this.clientManager = new ClientManager(port, this.serverConfigurationManager);
		this.tcpServer = new TCPServer(config, port, this.clientManager);
		Thread tcpThread = new Thread(tcpServer);

		tcpThread.start();
		
		Thread clientManagerThread = new Thread(this.clientManager);
		
		clientManagerThread.start();

		LOGGER.info("Communication Framework started by: " + msType);
	}


	public List<PeerMessageDto> getMessages(){
		return this.tcpServer.getMessages();
	}
	
	/**
	 * This function is called when the Microservice wants to send the message
	 * @param peerMessageDto
	 */
	public void sendMessage(PeerMessageDto peerMessageDto) {
		this.clientManager.sendMessage(peerMessageDto);
	}

	public String getMsType() {
		return msType;
	}
}