package communicationFramework.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @author Vasilije Mirkovic
 *
 */

public class PeerMessageDto implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String messageContent;
	private EMessageType messageType;
	private int timeToLive;
	private Date createdAt;
	
	public PeerMessageDto() {}
	
	public PeerMessageDto(String messageContent, EMessageType messageType, int timeToLive) {
		this.messageContent = messageContent;
		this.messageType = messageType;
		this.timeToLive = timeToLive;
		this.createdAt = new Date();
	}

	
	public String getMessageContent() {
		return messageContent;
	}
	
	public void setMessageContent(String messageContent) {
		this.messageContent = messageContent;
	}

	public EMessageType getMessageType() {
		return messageType;
	}

	public void setMessageType(EMessageType messageType) {
		this.messageType = messageType;
	}

	public int getTimeToLive() {
		return timeToLive;
	}

	public void setTimeToLive(int timeToLive) {
		this.timeToLive = timeToLive;
	}

	@Override
	public String toString() {
		return "PeerMessageDto [messageContent=" + messageContent + ", messageType=" + messageType
				+ ", timeToLive=" + timeToLive + "]";
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}
}
