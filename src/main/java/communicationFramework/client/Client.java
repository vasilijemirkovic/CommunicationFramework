package communicationFramework.client;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import communicationFramework.config.MSConfig;
import communicationFramework.dto.PeerMessageDto;

/**
 * 
 * @author Vasilije Mirkovic
 *
 */

public class Client {
	
	private int port;
	private MSConfig config;
	private Socket clientSocket;
	private OutputStream out;
	private ObjectOutputStream objectOutputStream;
	
    private static final Logger LOGGER = LoggerFactory.getLogger(Client.class);


	/**
	 * makes a new client, which represents a new MS instance later
	 * @param port that represents the port of the MS
	 * @param config which contains the name of MS, minimum and maximum of it's port
	 */
	public Client(int port, MSConfig config) {
		this.port = port;
		this.config = config;
	}
	
	/**
	 * register a new instance on the given port. It is used by the CommunicationFramework class
	 * @throws UnknownHostException
	 * @throws IOException
	 */
	public void createConnection() throws UnknownHostException, IOException {
		this.clientSocket = new Socket("localhost", this.port);
		this.out = clientSocket.getOutputStream();
		this.objectOutputStream = new ObjectOutputStream(out);
		LOGGER.info("Connected to peer " + this.port);
	}
	
	/**
	 * 
	 * @return true when the if the socket is no more opened
	 */
	public boolean isClosed() {
		return this.clientSocket == null || this.clientSocket.isClosed();
	}
	
	/**
	 * method used for sending a message to a certain Peer
	 * @param message, that represents the message object of the message that should be sent
	 * @throws IOException
	 */
	public void sendMessage(PeerMessageDto message) throws IOException {
		objectOutputStream.writeObject(message);
	}

	/**
	 * In case the peer wants to unregister, it executes this method
	 */
	public void stopConnection() throws IOException {
		out.close();
		clientSocket.close();
		LOGGER.info("Socket closed for port " + this.port);
	}
	
	public MSConfig getConfig() {
		return config;
	}
	
	public void setConfig(MSConfig config) {
		this.config = config;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}
}
