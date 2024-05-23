package communicationFramework.handlers;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import communicationFramework.dto.PeerMessageDto;

/**
 * 
 * @author Vasilije Mirkovic
 *
 */

public class UDPMessageHandler implements IMessageHandler {
	
	private List<PeerMessageDto> messages = new ArrayList<PeerMessageDto>();

	public void receiveMessage(DatagramPacket packet, byte[] recvBuf) {
		Thread thread = new Thread() {
			@Override
			public void run() {
				try {
					//int byteCount = packet.getLength();
					ByteArrayInputStream byteStream = new ByteArrayInputStream(recvBuf);
					ObjectInputStream is = new ObjectInputStream(new BufferedInputStream(byteStream));
					PeerMessageDto peerMessageDto = (PeerMessageDto) is.readObject();
					System.out.println(peerMessageDto);
					is.close();
					messages.add(peerMessageDto);
				} catch (IOException | ClassNotFoundException e) {
					System.out.println("Some error happened!");
				}
			}
		};
		thread.start();
	}

	@Override
	public List<PeerMessageDto> readMessages() {
		List<PeerMessageDto> returnList = new ArrayList<PeerMessageDto>(this.messages
				.stream()
				.filter(m -> m.getCreatedAt().getTime() + m.getTimeToLive() * 1000 < System.currentTimeMillis())
				.collect(Collectors.toList()));
		this.messages.clear();
		return returnList;
	}
}
