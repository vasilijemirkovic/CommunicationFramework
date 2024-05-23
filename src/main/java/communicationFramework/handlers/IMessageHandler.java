package communicationFramework.handlers;

import java.util.List;

import communicationFramework.dto.PeerMessageDto;

public interface IMessageHandler {

	public List<PeerMessageDto> readMessages();
}
