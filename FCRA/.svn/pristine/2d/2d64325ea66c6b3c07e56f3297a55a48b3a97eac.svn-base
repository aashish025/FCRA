package service.download;

import java.io.OutputStream;

import javax.servlet.http.HttpServletResponse;

import org.springframework.core.io.FileSystemResource;

import dao.services.downloaders.BuildingByelawsAttachmentDownloader;
import dao.services.downloaders.ChatAttachmentDownloader;
import dao.services.downloaders.ChatProjectDocumentDownloader;
import dao.services.downloaders.DocumentType;
import dao.services.downloaders.NotificationAttachmentDownloader;
import dao.services.downloaders.PropertyDocumentsDownloader;
import utilities.Commons;

public class DownloaderService extends Commons{
	private HttpServletResponse response;
	private String propertyId;
	private String rowId;
	
	
	public void getLeaseDeed() {
		begin();
		try {
			PropertyDocumentsDownloader propertyDocumentsDownloader = new PropertyDocumentsDownloader(connection, response.getOutputStream());
			
			propertyDocumentsDownloader.setDocumentType(DocumentType.LEASE_DEED);
			propertyDocumentsDownloader.setPropertyId(propertyId);
			propertyDocumentsDownloader.getFile();
		} catch(Exception e){
			ps(e);
		}
		finish();
	}
	
	public void getNotificationAttachment(){
		begin();
		try {
			NotificationAttachmentDownloader notificationAttachmentDownloader = new NotificationAttachmentDownloader(connection, response.getOutputStream());
			notificationAttachmentDownloader.setDocumentType(DocumentType.NOTIFICATION_ATTACHMENT);
			notificationAttachmentDownloader.setRowId(rowId);
			notificationAttachmentDownloader.getFile();
		} catch(Exception e){
			ps(e);
		}
		finish();
	}
	
	public void getChatAttachment(){
		begin();
		try {
			ChatAttachmentDownloader cad = new ChatAttachmentDownloader(connection, response.getOutputStream());
			cad.setDocumentType(DocumentType.CHAT_ATTACHMENT);
			cad.setRowId(rowId);
			cad.getFile();
		} catch(Exception e){
			ps(e);
		}
		finish();
	}
	
	public String getChatAttachmentType(){
		String type=null;
		begin();
		try {
			ChatAttachmentDownloader cad = new ChatAttachmentDownloader(connection);			
			cad.setRowId(rowId);
			type=cad.getChatAttachmentType();
		} catch(Exception e){
			ps(e);
		}finally{
			finish();
		}
		return type;
	}
	
	public void getChatProjectDoc(){
		begin();
		try {
			ChatProjectDocumentDownloader cpdd = new ChatProjectDocumentDownloader(connection, response.getOutputStream());
			cpdd.setDocumentType(DocumentType.CHAT_ATTACHMENT);
			cpdd.setRowId(rowId);
			cpdd.getFile();
		} catch(Exception e){
			ps(e);
		}
		finish();
	}

	
	public void getBuildingByelawsAttachment(){
		begin();
		try {
			BuildingByelawsAttachmentDownloader buildingByelawsAttachmentDownloader = new BuildingByelawsAttachmentDownloader(connection, response.getOutputStream());
			buildingByelawsAttachmentDownloader.setDocumentType(DocumentType.NOTIFICATION_ATTACHMENT);
			buildingByelawsAttachmentDownloader.setRowId(rowId);
			//buildingByelawsAttachmentDownloader.setOfficeCode(officeCode);
			buildingByelawsAttachmentDownloader.getFile();
		} catch(Exception e){
			ps(e);
		}
		finish();
	}
	
	public void getPurchaseDeed() {
		begin();
		try {
			PropertyDocumentsDownloader propertyDocumentsDownloader = new PropertyDocumentsDownloader(connection, response.getOutputStream());
			
			propertyDocumentsDownloader.setDocumentType(DocumentType.PURCHASE_DEED);
			propertyDocumentsDownloader.setPropertyId(propertyId);
			propertyDocumentsDownloader.getFile();
		} catch(Exception e){
			ps(e);
		}
		finish();
	}

	public void getAreaMap() {
		begin();
		try {
			PropertyDocumentsDownloader propertyDocumentsDownloader = new PropertyDocumentsDownloader(connection, response.getOutputStream());
			
			propertyDocumentsDownloader.setDocumentType(DocumentType.LAND_AREA_MAP);
			propertyDocumentsDownloader.setPropertyId(propertyId);
			propertyDocumentsDownloader.getFile();
		} catch(Exception e){
			ps(e);
		}
		finish();
	}

	public void getSurveySitePlan() {
		begin();
		try {
			PropertyDocumentsDownloader propertyDocumentsDownloader = new PropertyDocumentsDownloader(connection, response.getOutputStream());
			
			propertyDocumentsDownloader.setDocumentType(DocumentType.SURVEY_SITE_PLAN);
			propertyDocumentsDownloader.setPropertyId(propertyId);
			propertyDocumentsDownloader.getFile();
		} catch(Exception e){
			ps(e);
		}
		finish();
	}

	public void getPropertyImage() {
		begin();
		try {
			PropertyDocumentsDownloader propertyDocumentsDownloader = new PropertyDocumentsDownloader(connection, response.getOutputStream());
			
			propertyDocumentsDownloader.setDocumentType(DocumentType.PROPERTY_IMAGE);
			propertyDocumentsDownloader.setPropertyId(propertyId);
			propertyDocumentsDownloader.getFile();
		} catch(Exception e){
			ps(e);
		}
		finish();
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}

	public String getPropertyId() {
		return propertyId;
	}

	public void setPropertyId(String propertyId) {
		this.propertyId = propertyId;
	}

	public String getRowId() {
		return rowId;
	}

	public void setRowId(String rowId) {
		this.rowId = rowId;
	}

}
