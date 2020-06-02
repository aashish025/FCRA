package dao.services.downloaders;

import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;



public class NotificationAttachmentDownloader extends Downloader {
	private String rowId;
	
	public NotificationAttachmentDownloader(Connection connection, OutputStream outputStream) {
		super(connection, outputStream);
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
		
		getNotificationAttachment();
	}
	private void getNotificationAttachment() throws Exception {
		InputStream inputStream = null;
		try {
			String query = "SELECT NOTIFICATION_DOCUMENT FROM T_NOTIFICATION_DOCUMENT WHERE ROWID=?";
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
	
	

	public String getRowId() {
		return rowId;
	}

	public void setRowId(String rowId) {
		this.rowId = rowId;
	}
	
	

}
