package communicationFramework.services;

public interface IService {
	public void sendMessage(String messageName, String messageContent, int timeToLive);
}
