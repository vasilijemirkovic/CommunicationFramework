package communicationFramework.handlers;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import communicationFramework.client.ClientManager;
import communicationFramework.dto.PeerMessageDto;

/**
 * 
 * @author Vasilije Mirkovic
 *
 */

public class TCPMessageHandler implements IMessageHandler, Runnable {
	
	private List<PeerMessageDto> messages = new ArrayList<PeerMessageDto>();
    private ClientManager clientManager;
	private Socket socket;
	private InputStream in;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(TCPMessageHandler.class);
	
	public TCPMessageHandler(ClientManager clientManager, Socket socket) {
		this.clientManager = clientManager;
		this.socket = socket;
		try {
			this.in = socket.getInputStream();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * storing the messages into the list of messages
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public void receiveMessage() throws IOException, ClassNotFoundException {
		ObjectInputStream objectInputStream = null;
		objectInputStream = new ObjectInputStream(in);
    	while(!socket.isClosed()) {
			PeerMessageDto message = (PeerMessageDto) objectInputStream.readObject();
			if(message.getCreatedAt().getTime() + message.getTimeToLive() * 1000 > System.currentTimeMillis()) {
				LOGGER.info(System.currentTimeMillis() + " " + message.toString());
				messages.add(message);
				clientManager.sendMessage(message);
			}
    	}
    	objectInputStream.close();
	
	}

	
	@Override
	public List<PeerMessageDto> readMessages() {
		List<PeerMessageDto> returnList = new ArrayList<PeerMessageDto>(this.messages
				.stream()
				.filter(m -> m.getCreatedAt().getTime() + m.getTimeToLive() * 1000 > System.currentTimeMillis())
				.collect(Collectors.toList()));
		this.messages.clear();
		return returnList;
	}


	@Override
	public void run() {
		try {
			this.receiveMessage();
		} catch (ClassNotFoundException | IOException e) {
			LOGGER.error("Error in socket receiving");
		}
	}
}
