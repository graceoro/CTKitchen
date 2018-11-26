
import java.sql.Connection; //make sure these packets are java sql ones
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class JDBC {
	static int checkDB(String username, String password, String whichButton) {
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		Boolean usernameExists = false;
		Boolean passwordCorrect = false;
		
		System.out.println("PASSWORD ----" + username + "----");

		try {
			System.out.println("Trying to connect to database... ");
			Class.forName("com.mysql.jdbc.Driver"); //function allows u to create a class at runtime instead of compile
			System.out.println("---------");
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/CutthroatKitchen?user=root&password=cs201&useSSL=false"); //connect from jdbc -> mysql
			System.out.println("Connected");

			//check if username exists.
			ps = conn.prepareStatement("SELECT * FROM Users WHERE username=?");
			ps.setString(1, username);
			rs = ps.executeQuery();
			
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
			
			if(username.isEmpty() || password.isEmpty()) {
				System.out.println("----- return 5 ------");
				return 5;
			}
			else {
				if(rs.isBeforeFirst() == false) {		//nothing in result set
					if(whichButton.equals("register")) {
						//insert into database
						ps = conn.prepareStatement("INSERT INTO Users(username, password_, characterID) VALUES(?,?,?)");
						ps.setString(1, username);
						ps.setString(2, password);
						ps.setInt(3, 0);
						ps.executeUpdate();
						System.out.println("----- return 1 ------");
						return 1;		//created account
					}
					else if(whichButton.equals("login")) {
						System.out.println("----- return 2 (!!!) ------");
						return 2;
					}
				}
				else {		//is something in result set
					rs.next();		//STUPID
					if(rs.getString("username").equals(username) && !rs.getString("password_").equals(password) && whichButton.equals("login")) {
						System.out.println("----- return 2 ------");
						return 2;
					}
					else if(rs.getString("username").equals(username) && whichButton.equals("register")) {
						System.out.println();
						System.out.println("DB USERNAME: " + rs.getString("username") + " DB PASSWORD: " + rs.getString("password_") );
						System.out.println("Input username: " + username + " Input Password: " + password);
						System.out.println();
						
						System.out.println("----- return 3 ------");
						return 3;		//matching username and passwords
					}
					else if(rs.getString("username").equals(username) && rs.getString("password_").equals(password) && whichButton.equals("login")) {
						System.out.println();
						System.out.println("DB USERNAME: " + rs.getString("username") + " DB PASSWORD: " + rs.getString("password_") );
						System.out.println("Input username: " + username + " Input Password: " + password);
						System.out.println();
						
						System.out.println("----- return 4 ------");
						return 4;		//username and password do match
					}
					
					
				}
			}
			
			
		} catch(SQLException sqle) {
			System.out.println("sqle: " + sqle.getMessage());
			sqle.printStackTrace();
		} catch(ClassNotFoundException cnfe) {
			System.out.println("cnfe: " + cnfe.getMessage());
			cnfe.printStackTrace();
		} finally {
			try {
				if(rs != null) {
					rs.close();
				}       
				if(st != null) {
					st.close();
				}
				if(conn != null) {
					conn.close();
				}
			}catch(SQLException sqle) {
				System.out.println("sqle in jdbc: " + sqle.getMessage());
			}
		}
		return -1;
	}
}
