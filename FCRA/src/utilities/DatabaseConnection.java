package utilities;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class DatabaseConnection{
	public  static String msg = "Database Connection Failure";
	public  static String rootUser ;
	private static String rootPassword ;
	private static String ipAddress;
	private static String port;
	private static String sid;
	private static String url = "jdbc:oracle:thin:@"+ipAddress+":"+port+":"+sid;
	private static Map<String,String> dbUser;
	
	
	public static Connection getConnection(boolean inJboss){
		Connection connection=null;
		try {
			if (inJboss){
				/**Using DataSource**/
				InitialContext ctx = new InitialContext();
				DataSource dataSource = (DataSource)ctx.lookup("java:/jboss/datasources/fcraPool");
				connection=dataSource.getConnection();
				DatabaseMetaData  dbMetaData = connection.getMetaData();
				rootUser = dbMetaData.getUserName();
				connection.setAutoCommit(false);
				
			}
			else{
				Class.forName("oracle.jdbc.OracleDriver");
				connection = DriverManager.getConnection(url, rootUser, rootPassword);
				connection.setAutoCommit(false);
			}
			msg=null;
		}
		catch (NamingException ne){
			msg = ne.getMessage();
		}
		catch (SQLException sqle){
			sqle.printStackTrace();
			msg = sqle.getMessage();
			msg = "Listener refused the connection";
		} 
		catch (Exception e){
			e.printStackTrace();
			msg = e.getMessage();
		}
		catch(Throwable e){ 
			msg = e.getMessage();
		}
		return connection;
	}
	@Deprecated
	public static Connection getConnection(boolean inJboss, String username, String password){
		return getConnection(inJboss,username);
	}
	public static Connection getConnection(boolean inJboss, String username){
		Connection connection=null;
		try {
			if (inJboss){
				/**Using DataSourse**/
				InitialContext ctx = new InitialContext();
				DataSource dataSource = (DataSource)ctx.lookup("java:jboss/datasources/DynamicDB");
				connection=dataSource.getConnection(username, dbUser.get(username));
				connection.setAutoCommit(false);
				msg=null;
			}
			else{
				Class.forName("oracle.jdbc.OracleDriver");
				connection = DriverManager.getConnection(url, username, dbUser.get(username));
				connection.setAutoCommit(false);
			}
		}catch (NamingException ne) {
			msg = ne.getMessage();
			ne.printStackTrace();
		}
		catch (SQLException sqle) {
			sqle.printStackTrace();
			msg = sqle.getMessage();
			msg = "Listener refused the connection";
		} 
		catch (Exception e){
			e.printStackTrace();
			msg = e.getMessage();
		}
		catch(Throwable e){ 
			msg = e.getMessage();
		}
		return connection;
	}
	public static void setRootPassword(String rootPassword) {
		DatabaseConnection.rootPassword = rootPassword;
	}
	public static void setIpAddress(String ipAddress) {
		DatabaseConnection.ipAddress = ipAddress;
	}
	public static void setPort(String port) {
		DatabaseConnection.port = port;
	}
	public static void setSid(String sid) {
		DatabaseConnection.sid = sid;
	}
	public static void setDbUser(Map<String, String> dbUser) {
		DatabaseConnection.dbUser = dbUser;
	}
	public static void setRootUser(String rootUser) {
		DatabaseConnection.rootUser = rootUser;
	}
}
