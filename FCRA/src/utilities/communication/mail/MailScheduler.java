package utilities.communication.mail;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.mail.internet.InternetAddress;

import utilities.KVPair;

public class MailScheduler {

	public static String schedule(String fromAddress, String toAddress, String subject, String body,
			List<KVPair<String, byte[]>> attachments, List<KVPair<String, File>> files,
			java.util.Date scheduledDate,Integer priority, Connection connection) throws MailException{
		if(priority>10||priority<1){
			throw new MailException("Priority cannot be greater than 10 and less then 1");
		}
		// TODO Auto-generated method stub
				//this.connection=connection;
		
		Savepoint savepoint=null;
		String id=gen_UUID();
		try{
			boolean autoCommit=connection.getAutoCommit();
			connection.setAutoCommit(false);
			savepoint=connection.setSavepoint();
			try{
				InternetAddress.parse(fromAddress);
				InternetAddress.parse(toAddress);
			}catch(Exception e){
				throw new MailException("Invalid from/to address",e);
			}
			attachDocs(id,attachments,files,connection);
			queueMail(id,fromAddress,toAddress,subject,body,scheduledDate,new Integer(10),connection);
			connection.commit();
			connection.setAutoCommit(autoCommit);
		}catch(Exception e){
			e.printStackTrace();
			try{
				connection.rollback(savepoint);
			}catch(Exception e1){
				e1.printStackTrace();
			}
			throw new MailException("Some error occured while adding mail",e);
		}
		return id;
		
	}
	
	public static String schedule(String fromAddress, String toAddress, String subject, String body,
			List<KVPair<String, byte[]>> attachments, List<KVPair<String, File>> files,
			java.util.Date scheduledDate, Connection connection) throws MailException {
		return schedule(fromAddress,toAddress,subject,body,attachments,files,scheduledDate, new Integer(10), connection);
	}
	private static String queueMail(String mailId,String from, String to,String subject,String body,java.util.Date scheduledDate,Integer priority,Connection connection) throws MailException{
		try{
			StringBuffer query=new StringBuffer("insert into t_mail_details (mail_id,dated,from_address,to_address," +
					"status_id,priority,record_status,mail_subject,mail_body,scheduled_date,status_date) " +
					"values(?,sysdate,?,?,?,?,?,?,?,?,sysdate)");
			PreparedStatement statement=connection.prepareStatement(query.toString());
			int i=0;
			statement.setString(++i,mailId);
			statement.setString(++i, from);
			statement.setString(++i, to);
			statement.setInt(++i,0);
			statement.setInt(++i,priority);
			statement.setInt(++i,0);
			statement.setString(++i, subject);
			statement.setString(++i, body);
			if(scheduledDate==null){
				statement.setNull(++i, Types.NULL);
			}else{
				statement.setDate(++i, new java.sql.Date(scheduledDate.getTime()));
			}
			
			statement.execute();
			statement.close();
			//statement.setString(++i, x)
		}catch(SQLException sqle){
			//ps(sqle);
			throw new MailException("SQL Error occured due to :-",sqle);
		}finally{
		}
		return "success";
	}
	private static String attachDocs(String mailId,List<KVPair<String, byte[]>> attachments, 
			List<KVPair<String, File>> files,Connection connection) throws MailException{
		try{
			StringBuffer query=null;
			List<String> ids=new ArrayList<String>();
			if(!noe(attachments)){
				for(KVPair<String,byte[]> kv: attachments){
					ids.add(saveByteArray(kv,connection));
				}
			}
			if(!noe(files)){
				for(KVPair<String,File> kv: files){
					ids.add(saveFile(kv,connection));
				}
			}
			if(ids.size()>0){
				query= new StringBuffer("insert into t_mail_attachment_Details(mail_id,document_id,record_status) values (?,?,?)");
				PreparedStatement statement=connection.prepareStatement(query.toString());
				for(String s:ids){
					statement.setString(1, mailId);
					statement.setString(2, s);
					statement.setInt(3, 0);
					statement.addBatch();
				}
				statement.executeBatch();
				statement.close();
			}
		}catch(SQLException sqle){
			//ps(sqle);
			throw new MailException("SQL Error Occured",sqle);
		}finally{
			
		}
		return "success";
	}
	private static String saveByteArray(KVPair<String,byte[]> kv, Connection connection) throws MailException{
		String id="";
		StringBuffer query=null;
		InputStream is=null;
		try{
			String fileName=kv.getK();
			int docType=pullFileTypeByName(fileName,connection);
			byte[] doc=kv.getV();
			int size=pullByteArrayToStreamSize(doc);
			is = new ByteArrayInputStream(doc);
			id=gen_UUID();
			query=new StringBuffer("insert into t_document_details(document_id,document_name,document_type," +
					"dated,status_id,record_status,document) values(?,?,?,sysdate,?,?,?)");
			PreparedStatement statement=connection.prepareStatement(query.toString());
			statement.setString(1, id);
			statement.setString(2, fileName);
			statement.setInt(3, docType);
			statement.setInt(4, 0);
			statement.setInt(5, 0);
			statement.setBinaryStream(6, is,size);
			statement.execute();
			statement.close();
		}catch(SQLException sqle){
			throw new MailException("SQL Error Occured",sqle);
		}finally{
			try{
				is.close();
			}catch(IOException ioe){
				ioe.printStackTrace();
			}
		}
		return id;
	}
	private static int pullByteArrayToStreamSize(byte[] bytes) throws MailException{
		int size=0;
		InputStream in=null;
		ByteArrayOutputStream  bos=null;
		try{
			in= new ByteArrayInputStream(bytes);
			byte []buffer = new byte[4096];
			bos =new ByteArrayOutputStream();
			int read = 0;
	        while ( (read = in.read(buffer)) != -1 ) {
	        	bos.write(buffer, 0, read);
	        }
	        size=bos.size();
		}catch(Exception e){
			throw new MailException("Error occured while calculation bytestream size", e);
		}finally{
			try{
				in.close();
				bos.close();
			}catch(Exception e){
				e.printStackTrace();
			}	
		}
		return size;
	}
	private static String saveFile(KVPair<String,File> kv,Connection connection) throws MailException{
		String id="";
		InputStream in=null;
		ByteArrayOutputStream  bos=null;
		try{
			in= new FileInputStream(kv.getV());
			byte []buffer = new byte[4096];
			bos =new ByteArrayOutputStream();
			int read = 0;
	        while ( (read = in.read(buffer)) != -1 ) {
	        	bos.write(buffer, 0, read);
	        }
	        id=saveByteArray(new KVPair<String,byte[]>(kv.getK(),bos.toByteArray()),connection);			
		}catch(IOException ioe){
			throw new MailException("IO error Occured",ioe);
		}finally{
			try{
				in.close();
				bos.close();
			}catch(IOException ioe){
				ioe.printStackTrace();
			}
		}
		return id;
	}
	private static int pullFileTypeByName(String fN,Connection connection) throws SQLException,MailException{
		int type=0;
		StringBuffer query=null;
		String extn=fN.substring(fN.lastIndexOf(".")+1);
		/*query=new StringBuffer("select type_id from t_documenttype_master where extension=?");
		PreparedStatement statement=connection.prepareStatement(query.toString());
		statement.setString(1, extn.toUpperCase());
		ResultSet resultSet=statement.executeQuery();
		if(resultSet.next()){
			type=resultSet.getInt(1);
		}else{
			statement.close();
			throw new MailException("File Type of file ("+fN+") cannot be determined.");
		}
		statement.close();*/
		return 1;
	}

	private static String gen_UUID() {
		String rV="";
		UUID idOne = UUID.randomUUID();
		rV=String.valueOf(idOne).replaceAll("-", "");
		return rV;
	}
	private static boolean noe(List attachments){
		if(attachments==null||attachments.isEmpty()){
			return true;
		}else{
			return false;
		}
	}


}

