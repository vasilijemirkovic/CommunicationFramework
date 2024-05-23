package communicationFramework.server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Random;

import communicationFramework.config.MSConfig;
import communicationFramework.handlers.UDPMessageHandler;

/**
 * 
 * @author Vasilije Mirkovic
 *
 */
public class UDPServer implements Runnable {

	private MSConfig config;

	private DatagramSocket udpServerSocket;

	private UDPMessageHandler udpMessageHandler;

	public UDPServer(MSConfig msConfig) {
		this.config = msConfig;
		this.udpMessageHandler = new UDPMessageHandler();
	}

	@Override
	public void run() {
		byte[] buf = new byte[256];
		@SuppressWarnings("unused")
		InetAddress inetAddress = null;
		try {
			inetAddress = InetAddress.getByName("localhost");
		} catch (UnknownHostException e1) {
			e1.printStackTrace();
		}
		int diff = this.config.getPortEndRange() - this.config.getPortStartRange();
		int port = this.config.getPortStartRange() + new Random().nextInt(diff);

		try {
			this.udpServerSocket = new DatagramSocket(port);
		} catch (SocketException e2) {
			e2.printStackTrace();
		}
		System.out.println("UDP Server started on port " + port);
		DatagramPacket packet = new DatagramPacket(buf, buf.length);
		while (true) {
			try {
				this.udpServerSocket.receive(packet);
				System.out.println("New client connected on UDP Server!");
				this.udpMessageHandler.receiveMessage(packet, buf);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void stop() {
		if (udpServerSocket != null) {
			udpServerSocket.close();
			System.out.println("UDP Server stopped!");
		}
	}
}
