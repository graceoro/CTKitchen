import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class ServerThread extends Thread {
	
	private BufferedReader br;
	private PrintWriter pw;
	
	private JavaSocket js;
	Socket sock;
	InputStream is = null;
	OutputStream os = null;
	
	public ServerThread(Socket s, JavaSocket js) {
		this.sock = s;
		this.js = js;
		
		try {
			
			br = new BufferedReader(new InputStreamReader(s.getInputStream()));
			pw = new PrintWriter(s.getOutputStream());
			this.start();
			
			
			
		} catch (IOException ioe) {
			System.out.println("ioe in ServerThread: " + ioe.getMessage());
		}
	
	}
	
	
	public void send(OutputStream os, String username, String sendBack) {
		try {
			String toSend = username + "," + sendBack;
			byte[] toSendBytes = toSend.getBytes();
			int toSendLen = toSendBytes.length;
			byte[] toSendLenBytes = new byte[4];
			toSendLenBytes[0] = (byte)(toSendLen & 0xff);
			toSendLenBytes[1] = (byte)((toSendLen >> 8) & 0xff);
			toSendLenBytes[2] = (byte)((toSendLen >> 16) & 0xff);
			toSendLenBytes[3] = (byte)((toSendLen >> 24) & 0xff);
			os.write(toSendLenBytes);
			os.write(toSendBytes);
			os.flush();
		} catch (IOException ioe) {
			System.out.println("ioe in send: " + ioe.getMessage());
		}
	}
	
	public String receive(InputStream is, OutputStream os) {
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
			String whichButton = arr[2];
			//userMap.put(username, password);

			System.out.println("Server received: " + received);
			//System.out.println("Inserted " + username + " with password " + password + " into userMap");
		} catch (IOException ioe) {
			System.out.println("ioe in receive: " + ioe.getMessage());
		}
		return received;
	}
	
	public void run() {
		try {
			String result;
			boolean keepGettingInput = true;
			
			while(keepGettingInput == true) {
				is = sock.getInputStream();
				os = sock.getOutputStream();
				result = checkDB(is, os);   //resturns username,dbNum
				
				/*
				 * no username & register --- return 1    ---- insert into database
				 * 
				 * no username & login  || username/password no match & login ----- return 2    ----- send string back saying username or password incorrect 
				 * 
				 * username and password match & register ---- return 3   (only check username) ---- send string back saying username already exists; login
				 * 
				 * username and password match & login ---- return 4  ----- send string back saying successful login, also return username string
				 * 
				 *
				 */
				String[] resultArr = result.split(",");
				String username = resultArr[0];
				int dbResult = Integer.parseInt(resultArr[1]);
				
				if(dbResult == 1) {
					keepGettingInput = false;
					send(os, username, "Account Created!");
				}
				else if(dbResult == 2) {
					send(os, "fail", "Username or Password incorrect. Please try again.");
				}
				else if(dbResult == 3) {
					send(os, "fail", "Username already exists, please Login");
				}
				else if(dbResult == 4) {
					send(os, username, "Login Successful");
				}
			}
			
			//while(true) {
//				String line = br.readLine();
//				if(line != null && !line.equals("null")) {
//					js.s
//				}
				
			//int isAvailable = is.available();
				
				
//				while(isAvailable == 0) {
//					try {
//						System.out.println("Before sleep");
//						
//						isAvailable = is.available();
//						Thread.sleep(100);
//					} catch (InterruptedException ie) {
//						System.out.println("ie: " + ie.getMessage());
//						ie.printStackTrace();
//					}
//				}
			//}
		} catch (IOException ioe) {
			System.out.println("ioe in ServerThread.run(): " + ioe.getMessage());
		} 
	}
	
	public String checkDB(InputStream is, OutputStream os) {
		
		//String sendBack = null;
		int sendNum = 0;

		String received = receive(is, os);
		String[] arr = received.split(",");
		String username = arr[0];
		String password = arr[1];
		String whichButton = arr[2];

		//jdbc
		int dbNum = JDBC.checkDB(username, password, whichButton);
//		if(dbNum == 1) {
//			sendBack = "Account Created";
//			sendNum = 1;
//		}
//		else if(dbNum == 2) {
//			sendBack = "Logged In";
//			sendNum = 2;
//		}
//		else if(dbNum == 3) {
//			sendBack = "Username and Password do not match";
//			sendNum = 3;
//		}
		System.out.println("JDBC Test");
//		send(os, username, password, sendBack);
//		send(os, username, password, sendBack);
		sendNum = dbNum;
		return username + "," + dbNum;
		//os.flush();
	}

}
