package utilities.communication.sms;



import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.sql.Types;
import java.util.UUID;

public class SMSScheduler  {
	
	public static String schedule(String toAddress,String message,Connection connection) throws SMSException {
		// TODO Auto-generated method stub
		//this.connection=connection;
		String id=null;
		try{
			Double.parseDouble(toAddress);
		}catch(Exception e){
			throw new SMSException("Invalid number, should be a numeric value of 10 digits");
		}
		if(toAddress.length()>10 ||toAddress.length()<10){
			throw new SMSException("Invalid number, 10 digits expected");
		}
		try{
			id=addToDatabase(toAddress,message,connection,null,new Integer(10));
		}catch(Exception e){
			throw new SMSException("Error inserting into database",e);
		}
		
		return id;
	}
	
	public static String schedule(String toAddress,String message,Connection connection,Date scheduledDate) throws SMSException{
		return schedule(toAddress,message,connection,scheduledDate,new Integer(10));
	}
	public static String schedule(String toAddress,String message,Connection connection,Date scheduledDate,Integer priority) throws SMSException{
		String id=null;
		try{
			Double.parseDouble(toAddress);
		}catch(Exception e){
			throw new SMSException("Invalid number, should be a numeric value of 10 digits");
		}
		if(toAddress.length()>10 ||toAddress.length()<10){
			throw new SMSException("Invalid number, 10 digits expected");
		}
		if(priority >10 ||priority<1){
			throw new SMSException("Priority can be in 1 to 10, 10 being the most prior");
		}
		Savepoint savepoint=null;
		try{
			boolean autoCommit=connection.getAutoCommit();
			connection.setAutoCommit(false);
			savepoint=connection.setSavepoint();
			id=addToDatabase(toAddress,message,connection,scheduledDate,priority);
			connection.commit();
			connection.setAutoCommit(autoCommit);
		}catch(Exception e){
			e.printStackTrace();
			try{
				connection.rollback(savepoint);
			}catch(Exception e1){
				e1.printStackTrace();
			}
			throw new SMSException("Error inserting into database",e);
		}
		return id;
	}
	private static String addToDatabase(String to,String message,Connection connection,java.util.Date scheduledDate,Integer priority) throws Exception{
		
		String id=gen_UUID();
		StringBuffer query=new StringBuffer("INSERT INTO T_SMS_DETAILS(sms_id,dated,scheduled_date,to_address,message," +
				"status_id,priority,status_Date,record_status) values(?,sysdate,?,?,?,0,?,sysdate,0)");
		PreparedStatement statement=connection.prepareStatement(query.toString());
		statement.setString(1, id);
		if(scheduledDate!=null)
			statement.setDate(2,new java.sql.Date(scheduledDate.getTime()));
		else
			statement.setNull(2,Types.NULL);
		statement.setString(3, to);
		statement.setString(4, message);
		if(priority!=null)
			statement.setInt(5,priority);
		else
			statement.setInt(5,10);
		int updateCount=statement.executeUpdate();
		statement.close();
		return id;
		
	}

	private static String gen_UUID() throws SQLException{
		String rV="";
		UUID idOne = UUID.randomUUID();
		rV=String.valueOf(idOne).replaceAll("-", "");
		return rV;
	}

}
