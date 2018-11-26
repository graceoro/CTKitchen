
import java.io.IOException;
//import com.google.gson.Gson;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class JavaSocket{

	private Vector<ServerThread> playerThreads;
	//private Map<String, String> userMap;

	public JavaSocket(int port)
	{
		ServerSocket ss = null; //Passive class waiting to connect -- after connect u get a socket allowing for communication
		Socket s = null;
//
//		InputStream is = null;
//		OutputStream os = null;

		try {
			System.out.println("Trying to bind to port " + port);
			ss = new ServerSocket(port);
			System.out.println("Bound to port " + port);
			playerThreads = new Vector<ServerThread>();
			//userMap = new HashMap<>();

			Boolean match = false;

			while(true) {
				System.out.println("--------test-----");
				s = ss.accept(); 		//blocking line code -- always wait for user to connect
				System.out.println("Connection from " + s.getInetAddress());
				ServerThread st = new ServerThread(s, this);
				playerThreads.add(st);

			}
			

		} catch(IOException ioe) {
			System.out.println("ioe " + ioe.getMessage() );
		} finally {
			try {
				if (ss != null) {
					ss.close(); //maybe server is already made
				}
				if(s != null) {
					s.close();
				}
			} catch(IOException e) {
				System.out.println("ioe in constructor: " + e.getMessage());
			}

		}
	}
	
	//broadcast win to all computers 
	public void broadcast(String message, ServerThread st) {
		//someone wins 
		for(ServerThread thread : playerThreads) {
			if(thread != st) {
				thread.sendMessage(message);
			}
			
		}
	}
	
	public void deleteThread(ServerThread st) {
		for(int i=0; i<playerThreads.size(); i++) {
			if(playerThreads.get(i) == st) {
				playerThreads.remove(i);
			}
		}
		
	}
/*
	private String receive(InputStream is, OutputStream os) {
		String received = null;
		try {
			byte[] lenBytes = new byte[4];
			is.read(lenBytes, 0, 4);
			int len = (((lenBytes[3] & 0xff) << 24) | ((lenBytes[2] & 0xff) << 16) |
					((lenBytes[1] & 0xff) << 8) | (lenBytes[0] & 0xff));
			byte[] receivedBytes = new byte[len];
			is.read(receivedBytes, 0, len);
			received = new String(receivedBytes, 0, len);

			String[] arr = received.split(",");
			String username = arr[0];
			String password = arr[1];
			userMap.put(username, password);

			System.out.println("Server received: " + received);
			System.out.println("Inserted " + username + " with password " + password + " into userMap");
		} catch (IOException ioe) {
			System.out.println("ioe in receive: " + ioe.getMessage());
		}
		return received;
	}
	*/

/*
	public void send(OutputStream os, String username, String password, String sendBack) {
		try {
			String toSend = "Echo " + username + " " + password + " ACTION: " + sendBack;
			byte[] toSendBytes = toSend.getBytes();
			int toSendLen = toSendBytes.length;
			byte[] toSendLenBytes = new byte[4];
			toSendLenBytes[0] = (byte)(toSendLen & 0xff);
			toSendLenBytes[1] = (byte)((toSendLen >> 8) & 0xff);
			toSendLenBytes[2] = (byte)((toSendLen >> 16) & 0xff);
			toSendLenBytes[3] = (byte)((toSendLen >> 24) & 0xff);
			os.write(toSendLenBytes);
			os.write(toSendBytes);
		} catch (IOException ioe) {
			System.out.println("ioe in send: " + ioe.getMessage());
		}
	}
	*/

	/*private BufferedReader recv;

	public void run()
	{

        try
        {
            clientSocket = serverSocket.accept();
            System.out.println("Client Connected from " + clientSocket.getInetAddress().getHostAddress() + ":" + clientSocket.getPort());

            recv = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            System.out.println("Data Recieved: " + recv.readLine());

            clientSocket.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

	}*/

	public static void main(String [] args)
	{
		new JavaSocket(6789);
	}
}

/*

C# 
- have a loop w a parameter of game state (done or not)
- If a a game is done, send necessary data back to server

Java
- instead of is.avaialble, use is finished
- while true
- nested while loop w parameter not finished game
- outside this while loop, before big while loop, broadcast info. Then delete the thread from vector.

*/