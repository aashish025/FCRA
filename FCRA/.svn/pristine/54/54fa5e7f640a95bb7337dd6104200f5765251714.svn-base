package service.masters;

import java.util.List;

import models.master.ProjectDocument;
import models.master.Service;

import org.owasp.esapi.ESAPI;

import dao.master.ProjectDocumentDao;
import utilities.Commons;
import utilities.KVPair;
import utilities.notifications.Notification;
import utilities.notifications.Status;
import utilities.notifications.Type;

public class ProjectDocumentTypeService extends Commons{
private static final Boolean recordStatus = null;
	
	List<KVPair<String, String>> documentTypeList;
	List<ProjectDocument> documentList;
    private String pageNum;
	private String recordsPerPage;
	private String sortColumn;
	private String sortOrder;
	private String totalRecords;
	private String docId;
	private String doctypeDesc;
	
	public String execute() {
		begin();
		try {
				initDocumentTypeList();
		} catch(Exception e){
			spl(e);
		}
		finally{
			finish();
		}	
		return "success";
	}
	public Boolean validateDocument() throws Exception{		
		if(ESAPI.validator().isValidInput("Document Description ",doctypeDesc, "AlphaS", 50, false) == false){
			notifyList.add(new Notification("Error!!", "Services Description - Only aplphabets allowed (50 characters max).", Status.ERROR, Type.BAR));
			return false;
		}
		return true;
	}
	 public void initDocumentTypeList() throws Exception{
			ProjectDocumentDao rdao=new ProjectDocumentDao(connection);
			documentTypeList=rdao. getKVList();
		}
	public String initializeDocumentList() {
			begin();
			try {
					populatedocumentList();
			} catch(Exception e){
				spl(e);
			}
			finally{
				finish();
			}	
			return "success";
		} 

		private void populatedocumentList() throws Exception{
			ProjectDocumentDao rdao=new ProjectDocumentDao(connection);
			rdao.setPageNum(pageNum);
			rdao.setRecordsPerPage(recordsPerPage);
			rdao.setSortColumn(sortColumn);
			rdao.setSortOrder(sortOrder);				
			documentList=rdao.gettable();
			totalRecords=rdao.getTotalRecords();
		}
		public String AddDocument() {
			begin();
			try {
					adddocumentDesc();
			} catch(Exception e){
				spl(e);
			}
			finally{
				finish();
			}	
			return "success";
		}
	
	public void adddocumentDesc() throws Exception{
		ProjectDocumentDao rdao=new ProjectDocumentDao(connection);
		ProjectDocument document=new ProjectDocument();
		if(validateDocument()==true){
          	document.setDocId(docId); 
          	document.setDoctypeDesc(doctypeDesc);
          	document.setCreatedBy(myUserId);
          	document.setCreatedIp(myIpAddress);
          	document.setRecordStatus(recordStatus);
          	document.setLastModifiedBy(myUserId);
          	document.setLastModifiedIp(myIpAddress);
	      int status=	rdao.insertRecord(document);
			if(status>0)
				notifyList.add(new Notification("Success!!", "Document  Description is Inserted successfully.", Status.SUCCESS, Type.BAR));
			}
		}
		public String EditDocument() {
			begin();
			try {
					editdocument();
			} catch(Exception e){
				ps(e);
			}finally{
				finish();
			}		
			return "success";
		}

		public void editdocument() throws Exception {
			ProjectDocumentDao rdao=new ProjectDocumentDao(connection);
			ProjectDocument document=new ProjectDocument();
			if(validateDocument()==true){
			document.setDocId(docId); 
          	document.setDoctypeDesc(doctypeDesc);
			document.setLastModifiedBy(myUserId);
		    document.setLastModifiedIp(myIpAddress);
			int status=	rdao.editRecord(document);
			if(status>0)
				notifyList.add(new Notification("Success!!", "Document Description is Edited successfully.", Status.SUCCESS, Type.BAR));
			

		}
		}
		
	public String DeleteDocument() {
		begin();
		try {
				deletedocument();
		} catch(Exception e){
			e.printStackTrace();
		}
		finally{
			finish();
		}	
		return "success";
	}

	private void deletedocument() throws Exception {
		ProjectDocumentDao rdao=new ProjectDocumentDao(connection);
		ProjectDocument document=new ProjectDocument();
		document.setDocId(docId);
		int status=rdao.removeRecord(document);
		if(status>0)
			notifyList.add(new Notification("Success!!", "Document Description is Deleted successfully.", Status.SUCCESS, Type.BAR));
	}

	public List<KVPair<String, String>> getDocumentTypeList() {
		return documentTypeList;
	}
	public void setDocumentTypeList(List<KVPair<String, String>> documentTypeList) {
		this.documentTypeList = documentTypeList;
	}
	public List<ProjectDocument> getDocumentList() {
		return documentList;
	}
	public void setDocumentList(List<ProjectDocument> documentList) {
		this.documentList = documentList;
	}
	public String getPageNum() {
		return pageNum;
	}
	public void setPageNum(String pageNum) {
		this.pageNum = pageNum;
	}
	public String getRecordsPerPage() {
		return recordsPerPage;
	}
	public void setRecordsPerPage(String recordsPerPage) {
		this.recordsPerPage = recordsPerPage;
	}
	public String getSortColumn() {
		return sortColumn;
	}
	public void setSortColumn(String sortColumn) {
		this.sortColumn = sortColumn;
	}
	public String getSortOrder() {
		return sortOrder;
	}
	public void setSortOrder(String sortOrder) {
		this.sortOrder = sortOrder;
	}
	public String getTotalRecords() {
		return totalRecords;
	}
	public void setTotalRecords(String totalRecords) {
		this.totalRecords = totalRecords;
	}
	public String getDocId() {
		return docId;
	}
	public void setDocId(String docId) {
		this.docId = docId;
	}
	public String getDoctypeDesc() {
		return doctypeDesc;
	}
	public void setDoctypeDesc(String doctypeDesc) {
		this.doctypeDesc = doctypeDesc;
	}
	public static Boolean getRecordstatus() {
		return recordStatus;
	}

}
