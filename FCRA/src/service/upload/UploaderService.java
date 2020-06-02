package service.upload;

import org.springframework.web.multipart.MultipartFile;

import dao.services.NotificationDao;
import dao.services.uploader.UploaderDao;
import utilities.Commons;

	public class UploaderService extends Commons{
	private MultipartFile attachment;
	private String uploadId;
	private String fileName;	
	
	public String initUploadAttachment() {
		begin();
		try {
				uploadAttachment();
		} catch(Exception e){
			ps(e);
		}
		finally{
			finish();
		}	
		return "success";
	}  
	
	public void uploadAttachment() throws Exception{
		UploaderDao udao=new UploaderDao(connection);
		udao.setAttachment(attachment);
		int index=sessionId.indexOf(".");
		String sessId=sessionId.substring(0, index);
		udao.setSessionId(sessId);
		udao.setUploadId(uploadId);
		udao.setFileName(fileName);
		udao.uploadAttachment();
	}
	
	public String initDeleteAttachment() {
		begin();
		try {
				deleteAttachment();
		} catch(Exception e){
			ps(e);
		}
		finally{
			finish();
		}	
		return "success";
	}  
	
	public void deleteAttachment() throws Exception{
		UploaderDao udao=new UploaderDao(connection);		
		int index=sessionId.indexOf(".");
		String sessId=sessionId.substring(0, index);
		udao.setSessionId(sessId);
		udao.setUploadId(uploadId);
		udao.deleteAttachment();
	}
	
	public MultipartFile getAttachment() {
		return attachment;
	}
	public void setAttachment(MultipartFile attachment) {
		this.attachment = attachment;
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
