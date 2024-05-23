package communicationFramework.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import communicationFramework.client.ClientManager;
import communicationFramework.config.MSConfig;
import communicationFramework.dto.PeerMessageDto;
import communicationFramework.handlers.TCPMessageHandler;


/**
 * 
 * @author Vasilije Mirkovic
 *
 */
public class TCPServer implements Runnable {

	private MSConfig config;

	private ServerSocket tcpServerSocket;
	
	private ClientManager clientManager;
	
	private List<TCPMessageHandler> handlers = new ArrayList<>();
	
	private int port;
	
    private static final Logger LOGGER = LoggerFactory.getLogger(TCPServer.class);

	/**
	 * Makes the TCPServer to register the MS's instance on port
	 * @param msConfig
	 * @param port
	 * @param clientManager
	 */
	public TCPServer(MSConfig msConfig, int port, ClientManager clientManager) {
		this.config = msConfig;
		this.port = port;
		this.clientManager = clientManager;
	}

	/**
	 * Starts the thread
	 */
	@Override
	public void run() {
		try {
			this.tcpServerSocket = new ServerSocket(port);

			LOGGER.info("TCP Server started on port " + port);

			while (true) {
				Socket socket = this.tcpServerSocket.accept();
				TCPMessageHandler tcpMessageHandler = new TCPMessageHandler(clientManager, socket);
				this.handlers.add(tcpMessageHandler);
				Thread thread = new Thread(tcpMessageHandler);
				thread.start();
			}
		} catch (IOException e) {
			LOGGER.error("TCP Server exception: " + e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * Stops the thread
	 */
	public void stop() {
		if (tcpServerSocket != null) {
			try {
				tcpServerSocket.close();
				LOGGER.info("TCP Server stopped!");
			} catch (IOException e) {
				LOGGER.error("TCP Server exception: " + e.getMessage());
				e.printStackTrace();
			}
		}
		try {
			tcpServerSocket.close();
			LOGGER.info("TCP Server stopped!");
		} catch (IOException e) {
			LOGGER.error("TCP Server exception: " + e.getMessage());
			e.printStackTrace();
		}
	}
	

	public int getPort() {
		return this.port;
	}
	
	public List<PeerMessageDto> getMessages(){
		List<PeerMessageDto> messages = new ArrayList<>();
		for(TCPMessageHandler h : this.handlers) {
			messages.addAll(h.readMessages());
		}
		return messages;
	}


	public MSConfig getConfig() {
		return config;
	}
}