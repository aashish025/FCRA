package models.services.requests;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.owasp.esapi.ESAPI;

import utilities.ValidationException;
import models.services.AssociationDetails;
import models.services.DocumentDetails;
import models.services.ShowCauseNotices;

public class Grievances extends AbstractRequest {
	private AssociationDetails associationDetails = new AssociationDetails();
	private ShowCauseNotices showCauseNotices =new ShowCauseNotices();
	private String assoRegFlag;
	private String grievanceDesc;
	private String fileCreatedDate;
	private String finalSubmissionDate;
	private String complainantName;
	private String complainantAddress;
	private String complainantEmail;
	private String complainantMobile;
	private String createdBy;

	private List<DocumentDetails> uploadedDocuments = new ArrayList<DocumentDetails>();
	private List<DocumentDetails> deletedDocuments = new ArrayList<DocumentDetails>();
	private List<ShowCauseNotices> showCauseNoticeList = new ArrayList<ShowCauseNotices>();
	
	public Grievances(Connection connection) {
		super(connection);
		// TODO Auto-generated constructor stub
	}



	@Override
	public void getDetails() throws Exception {
		
		if(ESAPI.validator().isValidInput("applicationId", applicationId, "Word", 15, false) == false){
			throw new ValidationException("Invalid Application ID");
		}
		retrieveGrievancesDetails();
		retrieveUploadedDocumentDetails();
		retrieveShowcaseNotices();
	
		
		// TODO Auto-generated method stub
		
	}

	private void retrieveShowcaseNotices()throws Exception {
		//getShowCauseNoticeList
		// TODO Auto-generated method stub
		String fileNo = null;
		if(tempApplicationId != null && tempApplicationId.equals("") == false)
			fileNo = tempApplicationId;
		else
			fileNo = applicationId;
	
		showCauseNoticeList.clear();
		StringBuffer query = new StringBuffer("SELECT T_SHOW_CAUSE_NOTICES.NOTICE_BODY,T_SHOW_CAUSE_NOTICES.NOTICE_SUBJECT,T_SHOW_CAUSE_NOTICES.GENERATED_BY||'('||TM_USER.USER_NAME||')' AS GENERATEDBY,TO_CHAR(T_SHOW_CAUSE_NOTICES.GENRATED_DATE,'DD-MM-YYYY')FROM T_SHOW_CAUSE_NOTICES,TM_USER WHERE GRIEVANCE_ID=? AND T_SHOW_CAUSE_NOTICES.GENERATED_BY=TM_USER.USER_ID");
		PreparedStatement statement = connection.prepareStatement(query.toString());
		statement.setString(1, applicationId);
		ResultSet rs = statement.executeQuery();
		while(rs.next()) {
			ShowCauseNotices showcausenoticelist=new ShowCauseNotices();
			showcausenoticelist.setNoticeBody(rs.getString(1));
			showcausenoticelist.setNoticeSubject(rs.getString(2));
			showcausenoticelist.setGeneratedBy(rs.getString(3));
			showcausenoticelist.setGeneratedDate(rs.getString(4));
			showCauseNoticeList.add(showcausenoticelist);
		}
		rs.close();
		statement.close();
	}

	private void retrieveUploadedDocumentDetails()throws Exception {
		
		String documentRoot = docRoot;
		uploadedDocuments.clear();
		StringBuffer query = new StringBuffer("SELECT CHK_CODE, CHK_NAME, CHK_DESC "
					+ "FROM T_GRIEVANCE_CHECKLIST WHERE CHK_CODE NOT IN ('05', '06') AND CHK_STATUS='Y'");
		PreparedStatement statement = connection.prepareStatement(query.toString());
		ResultSet rs = statement.executeQuery();
		String docName = null;
		String docDetails = null;
		String docId = null;
		while(rs.next()) {
			int j = 1;
			String docPath = documentRoot.concat("/").concat(creationDate.substring(6, 10)).concat("/").concat(creationDate.substring(3, 5)).concat("/").concat(creationDate.substring(0, 2)).concat("/12/").concat(documentId).concat("/");
			docId = rs.getString(j++);
			docName = rs.getString(j++);
			docDetails = rs.getString(j++);
			docPath = docPath.concat(documentId).concat("_").concat(docId).concat(".pdf");
			DocumentDetails doc = new DocumentDetails();
			doc.setDocId(docId);
			doc.setDocName(docName);
			doc.setDocDetails(docDetails);
			doc.setUploadedDate(submissionDate);
			doc.setDocPath(docPath);
			uploadedDocuments.add(doc);
		}
		rs.close();
		statement.close();
		// TODO Auto-generated method stub
		
	}

	private void retrieveGrievancesDetails()throws Exception{
		String fileNo = null; String regFlag="";
		if(tempApplicationId != null && tempApplicationId.equals("") == false)
			fileNo = tempApplicationId;
		else
			fileNo = applicationId;
		
		StringBuffer query5 = new StringBuffer("SELECT ASSO_REGISTERED_FLAG FROM T_GRIEVANCES WHERE APPLICATION_ID=?");
		PreparedStatement statement = connection.prepareStatement(query5.toString()); 
	 		statement.setString(1,applicationId);
	 		ResultSet rs4=statement.executeQuery();
	 		if(rs4.next()){
	 			if(rs4.getString(1)==null){			
	 			}
	 			else if(rs4.getString(1).equals("Y")){
	 				regFlag="Y";	 	 				
	 			} 
	 		}
	 	if(regFlag=="Y"){
	 	StringBuffer query1= new StringBuffer("SELECT T_GRIEVANCES.ASSO_REGISTERED_FLAG,"	 	         
							+" T_GRIEVANCES.ASSO_RCN,FC_INDIA.ASSO_NAME,T_GRIEVANCES.ASSO_STATE,(SELECT SNAME FROM TM_STATE WHERE SCODE=substr(FC_INDIA.STDIST,1,2)),"
							+" T_GRIEVANCES.ASSO_DISTRICT,(SELECT DISTNAME FROM TM_DISTRICT WHERE DISTCODE=SUBSTR(FC_INDIA.STDIST,-3,3)),"
							+" FC_INDIA.ASSO_ADDRESS,FC_INDIA.ASSO_TOWN_CITY,FC_INDIA.ASSO_PIN,T_GRIEVANCES.GRIEVANCE_DESCRIPTION,T_GRIEVANCES.COMPLAINANT_NAME,"
							+" T_GRIEVANCES.COMPLAINANT_ADDRESS,T_GRIEVANCES.COMPLAINANT_EMAIL,T_GRIEVANCES.COMPLAINANT_MOBILE,"
							+" TO_CHAR(T_GRIEVANCES.FILE_CREATED_DATE,'DD-MM-YYYY') AS FILE_CREATED_DATE,"
							+" TO_CHAR(T_GRIEVANCES.FINAL_SUBMISSION_DATE,'DD-MM-YYYY') AS FINAL_SUBMISSION_DATE,"
							+" T_GRIEVANCES.CREATED_BY FROM T_GRIEVANCES,FC_INDIA WHERE APPLICATION_ID=? AND T_GRIEVANCES.ASSO_RCN=FC_INDIA.RCN ");
		 	PreparedStatement statement3 = connection.prepareStatement(query1.toString());
			statement3.setString(1, applicationId);
			ResultSet rs3 = statement3.executeQuery();
						if(rs3.next()) {
						int i=1;						
						assoRegFlag=rs3.getString(i++);			
						associationDetails.setFcraRegistrationNumber(rs3.getString(i++));
						associationDetails.setAssociationName(rs3.getString(i++));
						associationDetails.setState(rs3.getString(i++));
						associationDetails.setStateDesc(rs3.getString(i++));
						associationDetails.setDistrict(rs3.getString(i++));
						associationDetails.setDistrictDesc(rs3.getString(i++));
						associationDetails.setAddress(rs3.getString(i++));
						associationDetails.setTown(rs3.getString(i++));
						associationDetails.setPincode(rs3.getString(i++));
						grievanceDesc=rs3.getString(i++);
						complainantName=rs3.getString(i++);
						complainantAddress=rs3.getString(i++);
						complainantEmail=rs3.getString(i++);
						complainantMobile=rs3.getString(i++);
						fileCreatedDate=rs3.getString(i++);
						submissionDate=rs3.getString(i++);
						createdBy=rs3.getString(i++);								
					}	 		
	 	}
	 	else {
	 		StringBuffer query2 = new StringBuffer("SELECT ASSO_REGISTERED_FLAG,"	 	
							 + " ASSO_RCN,ASSO_NAME,ASSO_STATE,(SELECT SNAME FROM TM_STATE WHERE SCODE=ASSO_STATE),"
							 + " ASSO_DISTRICT,(SELECT DISTNAME FROM TM_DISTRICT WHERE SCODE=ASSO_STATE AND DISTCODE=ASSO_DISTRICT),"
							 + " ASSO_ADDRESS,ASSO_TOWN_CITY,ASSO_PIN,GRIEVANCE_DESCRIPTION,COMPLAINANT_NAME,"
							 + " COMPLAINANT_ADDRESS,COMPLAINANT_EMAIL,COMPLAINANT_MOBILE,TO_CHAR(FILE_CREATED_DATE,'DD-MM-YYYY') AS FILE_CREATED_DATE,"
							 + " TO_CHAR(FINAL_SUBMISSION_DATE,'DD-MM-YYYY') AS FINAL_SUBMISSION_DATE,"
							 + " CREATED_BY FROM T_GRIEVANCES WHERE APPLICATION_ID=? ");
		
		PreparedStatement statement3 = connection.prepareStatement(query2.toString());
		statement3.setString(1, applicationId);
		ResultSet rs3 = statement3.executeQuery();
		if(rs3.next()) {
			int i=1;
			
			assoRegFlag=rs3.getString(i++);			
			associationDetails.setFcraRegistrationNumber(rs3.getString(i++));
			associationDetails.setAssociationName(rs3.getString(i++));
			associationDetails.setState(rs3.getString(i++));
			associationDetails.setStateDesc(rs3.getString(i++));
			associationDetails.setDistrict(rs3.getString(i++));
			associationDetails.setDistrictDesc(rs3.getString(i++));
			associationDetails.setAddress(rs3.getString(i++));
			associationDetails.setTown(rs3.getString(i++));
			associationDetails.setPincode(rs3.getString(i++));
			grievanceDesc=rs3.getString(i++);
			complainantName=rs3.getString(i++);
			complainantAddress=rs3.getString(i++);
			complainantEmail=rs3.getString(i++);
			complainantMobile=rs3.getString(i++);
			fileCreatedDate=rs3.getString(i++);
			submissionDate=rs3.getString(i++);
			createdBy=rs3.getString(i++);								
		}
			rs3.close();
			statement3.close();
	 	}
		}
		
	@Override
	public void saveDetails() throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void forward() throws Exception {
		// TODO Auto-generated method stub
		
	}

	public AssociationDetails getAssociationDetails() {
		return associationDetails;
	}

	public void setAssociationDetails(AssociationDetails associationDetails) {
		this.associationDetails = associationDetails;
	}

	public String getAssoRegFlag() {
		return assoRegFlag;
	}

	public void setAssoRegFlag(String assoRegFlag) {
		this.assoRegFlag = assoRegFlag;
	}

	public String getGrievanceDesc() {
		return grievanceDesc;
	}

	public void setGrievanceDesc(String grievanceDesc) {
		this.grievanceDesc = grievanceDesc;
	}

	public String getFileCreatedDate() {
		return fileCreatedDate;
	}

	public void setFileCreatedDate(String fileCreatedDate) {
		this.fileCreatedDate = fileCreatedDate;
	}

	
	public String getFinalSubmissionDate() {
		return finalSubmissionDate;
	}

	public void setFinalSubmissionDate(String finalSubmissionDate) {
		this.finalSubmissionDate = finalSubmissionDate;
	}

	public String getComplainantName() {
		return complainantName;
	}

	public void setComplainantName(String complainantName) {
		this.complainantName = complainantName;
	}

	public String getComplainantAddress() {
		return complainantAddress;
	}

	public void setComplainantAddress(String complainantAddress) {
		this.complainantAddress = complainantAddress;
	}

	public String getComplainantEmail() {
		return complainantEmail;
	}

	public void setComplainantEmail(String complainantEmail) {
		this.complainantEmail = complainantEmail;
	}

	public String getComplainantMobile() {
		return complainantMobile;
	}

	public void setComplainantMobile(String complainantMobile) {
		this.complainantMobile = complainantMobile;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public List<DocumentDetails> getUploadedDocuments() {
		return uploadedDocuments;
	}

	public void setUploadedDocuments(List<DocumentDetails> uploadedDocuments) {
		this.uploadedDocuments = uploadedDocuments;
	}

	public ShowCauseNotices getShowCauseNotices() {
		return showCauseNotices;
	}

	public void setShowCauseNotices(ShowCauseNotices showCauseNotices) {
		this.showCauseNotices = showCauseNotices;
	}

	public List<ShowCauseNotices> getShowCauseNoticeList() {
		return showCauseNoticeList;
	}
	public void setShowCauseNoticeList(List<ShowCauseNotices> showCauseNoticeList) {
		this.showCauseNoticeList = showCauseNoticeList;
	}



	public List<DocumentDetails> getDeletedDocuments() {
		return deletedDocuments;
	}



	public void setDeletedDocuments(List<DocumentDetails> deletedDocuments) {
		this.deletedDocuments = deletedDocuments;
	}
	 
	
}
