package dao.services.downloaders;

import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;



public class ChatAttachmentDownloader extends Downloader {
	private String rowId;
	
	public ChatAttachmentDownloader(Connection connection, OutputStream outputStream) {
		super(connection, outputStream);		
	}
	
	public ChatAttachmentDownloader(Connection connection) {
		super(connection);		
	}
	
	public void getFile() throws Exception {
		if(connection == null) {
			throw new Exception("Invalid connection");
		}
		if(documentType == null) {
			throw new Exception("Invalid document type");
		}
		if(rowId == null) {
			throw new Exception("Invalid property id");
		}
		
		getChatAttachment();
	}
	private void getChatAttachment() throws Exception {
		InputStream inputStream = null;
		try {
			String query = "SELECT DATA FROM T_PC_CHAT_ATTACHMENT WHERE ROWID=?";
			PreparedStatement statement = connection.prepareStatement(query.toString());
			statement.setString(1, rowId);
			ResultSet rs = statement.executeQuery();
			if(rs.next()) {
				inputStream = rs.getBinaryStream(1);
				copyData(inputStream);
			}
		}catch (Exception e) {
			throw e;
		}finally {
			if(inputStream != null)
				inputStream.close();
			if(outputStream != null) {
				outputStream.flush();
				outputStream.close();
			}
		}
	}
	
	public String getChatAttachmentType() throws Exception {
		String type = null;
		if(rowId == null) {
			throw new Exception("Invalid property id");
		}
		try {
			String query = "SELECT TYPE FROM T_PC_CHAT_ATTACHMENT WHERE ROWID=?";
			PreparedStatement statement = connection.prepareStatement(query.toString());
			statement.setString(1, rowId);
			ResultSet rs = statement.executeQuery();
			if(rs.next()) {
				type=rs.getString(1);
			}
		}catch (Exception e) {
			throw e;
		}
		return type;
	}
	

	public String getRowId() {
		return rowId;
	}

	public void setRowId(String rowId) {
		this.rowId = rowId;
	}
	
	

}
