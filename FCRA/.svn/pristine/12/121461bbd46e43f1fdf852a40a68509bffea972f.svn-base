package dao.services.uploader;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;

import org.springframework.web.multipart.MultipartFile;

public class UploaderDao {
	private MultipartFile attachment;
	private String sessionId;
	private String uploadId;
	private String fileName;
	private Connection connection=null;
	
	public UploaderDao(Connection connection){
		this.connection=connection;
	}
	
	public String uploadAttachment() throws Exception{
		PreparedStatement statement=null;
		InputStream is = null;
		if(attachment != null)
			is = attachment.getInputStream();
		String name= fileName.substring(0,fileName.lastIndexOf('.'));
		String ext = fileName.substring(fileName.lastIndexOf('.')+1);
		StringBuffer query1 = new StringBuffer("INSERT INTO T_UPLOAD_CACHE(SESSION_ID,UPLOAD_ID,DOCUMENT,NAME,TYPE) VALUES(?,?,?,?,?)");
			statement = connection.prepareStatement(query1.toString());
			statement.setString(1, sessionId);
			statement.setString(2, uploadId);			
			if(is == null)
				statement.setNull(3, java.sql.Types.BLOB);
			else
				statement.setBinaryStream (3, is, (int) attachment.getSize() );		
			statement.setString(4, name);
			statement.setString(5, ext);
			statement.executeUpdate();
			statement.close();
			return null;
	}
	
	public String deleteAttachment() throws Exception{
		PreparedStatement statement=null;		
		StringBuffer query = new StringBuffer("DELETE FROM T_UPLOAD_CACHE WHERE SESSION_ID=? AND UPLOAD_ID=?");
			statement = connection.prepareStatement(query.toString());
			statement.setString(1, sessionId);
			statement.setString(2, uploadId);											
			statement.executeUpdate();
			statement.close();
			return null;
	}
	
	public MultipartFile getAttachment() {
		return attachment;
	}
	public void setAttachment(MultipartFile attachment) {
		this.attachment = attachment;
	}
	public String getSessionId() {
		return sessionId;
	}
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	public String getUploadId() {
		return uploadId;
	}
	public void setUploadId(String uploadId) {
		this.uploadId = uploadId;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
}
