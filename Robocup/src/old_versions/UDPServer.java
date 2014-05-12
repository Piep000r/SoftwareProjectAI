package udp;
import java.net.*;

/**
 *	Erweitert Class:Thread um Empfangsroutinen zu parallelisieren.
 */
public class UDPServer extends Thread {
	DatagramSocket socket;			// Socketverbindung zum Robocup-Server
	String destIp;					// IP des Zielhosts
	int destPort;					// Port des Zielhosts

	/**
	 * Lädt IP und Port und erzeugt einen Socket für die Datenübermittlung und startet den Empfangsthread
	 * @param ip
	 * @param port
	 * @throws Exception
	 */
	public UDPServer(String ip, int port) throws Exception {
		socket = new DatagramSocket(getFreePort());
		destIp = ip;
		destPort = port;
		this.start();
	}

	/**
	 * Sendet msg an die beim Konstruktor angegebene IP und den Port
	 * @param msg
	 * @throws Exception
	 */
	public void send(String msg) throws Exception {
		byte[] sendBuffer = new byte[1024];
		msg = msg + "\0";
		sendBuffer = msg.getBytes();
		DatagramPacket outData = new DatagramPacket(sendBuffer,
				sendBuffer.length, InetAddress.getByName(destIp), destPort);
		socket.send(outData);
	}

	/**
	 * Bereitet Datenpakete vor, liest die Daten vom Socket und extrahiert den String
	 * @return String
	 * @throws Exception
	 */
	public String receive() throws Exception {
		DatagramPacket temp = new DatagramPacket(new byte[1024], 1024);
		socket.receive(temp);
		int len = temp.getLength();
		destPort = temp.getPort();
		byte[] data = temp.getData();
		return new String(data, 0, len - 1);
	}

	/**
	 * Schließt den Socket und beendet damit die Run-Methode und somit auch den Thread!
	 */
	public void close() {
		socket.close();
	}

	/**
	 * Öffnet temporär einen Socket, liest den gebundenen Port, schließt den Socket und gibt den freien Port zurück
	 * @return port
	 * @throws Exception
	 */
	private static int getFreePort() throws Exception {
		DatagramSocket temp = new DatagramSocket();
		int port = temp.getLocalPort();
		temp.close();
		Thread.sleep(500); //
		return port;
	}

	/**
	 * Eigenständiger Thread, der die Daten des Sockets empfängt.
	 */
	public void run() {
		String rec;
		while (!socket.isClosed()) {
			try {
				// Auf Antwort warten
				rec = receive();
				// Daten lesen
				parse(rec);
			} catch (Exception e) {
				boolean s = e.getMessage().equals(new String("socket closed"));
				if (s == true)
					System.out.println(e.getMessage());
				if (s == false)
					e.printStackTrace();
			}
		}
	}
	
	/* parst den eingehenden String */
	public void parse(String msg) throws Exception{
		System.out.println(msg);
	}
}