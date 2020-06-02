package service.masters;

import java.util.List;
import models.master.SubstageDocument;
import utilities.Commons;
import utilities.KVPair;
import utilities.notifications.Notification;
import utilities.notifications.Status;
import utilities.notifications.Type;
import dao.master.ApplicationSubStageDao;
import dao.master.ProjectDocumentDao;

import dao.master.SubstageDocumentDao;


public class SubstageDocumentServices  extends Commons {
	List<KVPair<String, String>> substagedocumentTypeList;
	List<KVPair<String, String>> documentTypeList;
	List<KVPair<String, String>> applicationsubTypeList;
	List<KVPair<String, String>> proposalTypeList;
	List<SubstageDocument> substagedocumentList;
     private String pageNum;
	private String recordsPerPage;
	private String sortColumn;
	private String sortOrder;
	private String totalRecords;
	private String proposalDesc;
   	private String substageDesc;
   	private String documentDesc;
   	private String rowId;
  
   
	
	 public String execute() {
		begin();
		try {
		 initproposalList();
		 initappsublist();
		 initdocumentlist();
		 initsubstagedocumentList();
		} catch(Exception e){
			spl(e);
		}
		finally{
			finish();
		}	
		return "success";
	}
	 public void initproposalList() throws Exception{
		
    	
	 }
	
	 
	 public void initappsublist() throws Exception{
		 ApplicationSubStageDao assd= new ApplicationSubStageDao(connection);
		 applicationsubTypeList=assd.getKVList();
	 }
	 public void initdocumentlist() throws Exception{
		 ProjectDocumentDao assd= new ProjectDocumentDao(connection);
		 documentTypeList=assd.getKVList();
	 }
	 
	
     public void  initsubstagedocumentList() throws Exception{
    	SubstageDocumentDao bdao=new SubstageDocumentDao(connection);
    	 substagedocumentTypeList=bdao.getKVList();
	}
     public String initializeList() {
  		begin();
  		try {
  				populatesubstagedocList();
  		} catch(Exception e){
  			spl(e);
  		}
  		finally{
  			finish();
  		}	
  		return "success";
  	} 

  	private void populatesubstagedocList() throws Exception{
  		SubstageDocumentDao bdao=new SubstageDocumentDao(connection);
  		bdao.setPageNum(pageNum);
  		bdao.setRecordsPerPage(recordsPerPage);
  	     bdao.setSortColumn(sortColumn);
  		bdao.setSortOrder(sortOrder);				
  		substagedocumentList=bdao.getsubtable();
  		totalRecords=bdao.getTotalRecords();
  	}
  	
 public String AddSubstageDocument() {
 		begin();
 		try {
 				addsubstagedoc();
 		} catch(Exception e){
 			spl(e);
 		}
 		finally{
 			finish();
 		}	
 		return "success";
 	}
 	

 	
 	public void addsubstagedoc() throws Exception{
 		SubstageDocumentDao bdao=new SubstageDocumentDao(connection);
 		SubstageDocument ssd=new SubstageDocument();
 		   ssd.setSubstageDesc(substageDesc);
	 		ssd.setProposalDesc(proposalDesc);
	 		ssd.setDocumentDesc(documentDesc);
	 		 ssd.setCreatedIp(myIpAddress);
	 	     ssd.setCreatedBy(myUserId);;
	 	     ssd.setLastModifiedBy(myUserId);
	 		ssd.setLastModifiedIp(myIpAddress);
	 	   int status=	bdao.insertRecord(ssd);
 		if(status>0)
 			notifyList.add(new Notification("Success!!", "Application Sub Stage Document is Inserted successfully.", Status.SUCCESS, Type.BAR));
 		}
 	
 		
 	
 	public String EditSubstageDocument() {
 		
 		begin();
 		try {
 				editSubstageDocument();
 		} catch(Exception e){
 			ps(e);
 		}finally{
 			finish();
 		}		
 		return "success";
 	}

 	public void editSubstageDocument() throws Exception {
 		SubstageDocumentDao bdao=new SubstageDocumentDao(connection);
 		SubstageDocument ssd=new SubstageDocument();
 		
 		 ssd.setSubstageDesc(substageDesc);
	 		ssd.setProposalDesc(proposalDesc);
	 		ssd.setDocumentDesc(documentDesc);
 	 		ssd.setRowIdentifier(rowId);
 	 		 ssd.setLastModifiedBy(myUserId);
 	 		ssd.setLastModifiedIp(myIpAddress);
 		    int status=	bdao.editRecord(ssd);
 		if(status>0)
 			notifyList.add(new Notification("Success!!", " Application Sub Stage Document is Edited successfully.", Status.SUCCESS, Type.BAR));
 		}

 	
 	 public String DeleteSubstageDocument() {
 	 	begin();
 	 	try {
 	 			deletesubdoc();
 	 	} catch(Exception e){
 	 		e.printStackTrace();
 	 	}
 	 	finally{
 	 		finish();
 	 	}	
 	 	return "success";
 	 }

 	 private void deletesubdoc() throws Exception {
 		SubstageDocumentDao bdao=new SubstageDocumentDao(connection);
 		SubstageDocument ssd=new SubstageDocument();
 			ssd.setRowIdentifier(rowId);
 	 	int status=bdao.deleteRecord(ssd);
 	 	if(status>0)
 	 		notifyList.add(new Notification("Success!!", "Project Sub Stage Description is Deleted successfully.", Status.SUCCESS, Type.BAR));
 	 }
 	 




	public List<KVPair<String, String>> getSubstagedocumentTypeList() {
		return substagedocumentTypeList;
	}
	public void setSubstagedocumentTypeList(
			List<KVPair<String, String>> substagedocumentTypeList) {
		this.substagedocumentTypeList = substagedocumentTypeList;
	}
	public List<KVPair<String, String>> getDocumentTypeList() {
		return documentTypeList;
	}
	public void setDocumentTypeList(List<KVPair<String, String>> documentTypeList) {
		this.documentTypeList = documentTypeList;
	}
	public List<KVPair<String, String>> getApplicationsubTypeList() {
		return applicationsubTypeList;
	}
	public void setApplicationsubTypeList(
			List<KVPair<String, String>> applicationsubTypeList) {
		this.applicationsubTypeList = applicationsubTypeList;
	}
	public List<KVPair<String, String>> getProposalTypeList() {
		return proposalTypeList;
	}
	public void setProposalTypeList(List<KVPair<String, String>> proposalTypeList) {
		this.proposalTypeList = proposalTypeList;
	}
	public List<SubstageDocument> getSubstagedocumentList() {
		return substagedocumentList;
	}
	public void setSubstagedocumentList(List<SubstageDocument> substagedocumentList) {
		this.substagedocumentList = substagedocumentList;
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
	public String getProposalDesc() {
		return proposalDesc;
	}
	public void setProposalDesc(String proposalDesc) {
		this.proposalDesc = proposalDesc;
	}
	public String getSubstageDesc() {
		return substageDesc;
	}
	public void setSubstageDesc(String substageDesc) {
		this.substageDesc = substageDesc;
	}
	public String getDocumentDesc() {
		return documentDesc;
	}
	public void setDocumentDesc(String documentDesc) {
		this.documentDesc = documentDesc;
	}
	public String getRowId() {
		return rowId;
	}
	public void setRowId(String rowId) {
		this.rowId = rowId;
	}     
 

}
