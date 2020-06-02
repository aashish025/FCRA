package models.services.requests;

import java.io.File;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import models.services.AffiddavidDocument;
import models.services.ApplicantDetails;
import models.services.DocumentDetails;

import org.owasp.esapi.ESAPI;

import utilities.ValidationException;

public class ArticleContribution extends AbstractRequest {
	ApplicantDetails applicantDetails = new ApplicantDetails();
	String fcraRegistrationNumber;
	String fcraRegistrationDate;
	String receivedDate;
	String articleName;
	String articleDescription;
	String relativeName;
	String relativeAddress;
	String purpose;
	String quantity;
	BigDecimal value;
	String modeOfUtilization;
	private List<AffiddavidDocument> affiddavidDocument = new ArrayList<AffiddavidDocument>();
	private List<DocumentDetails> deletedDocuments = new ArrayList<DocumentDetails>();
	private List<DocumentDetails> deletedSupportDocuments = new ArrayList<DocumentDetails>();
	private List<DocumentDetails> deletedSupportDocumentsEc = new ArrayList<DocumentDetails>();
	
	public ArticleContribution(Connection connection) {
		super(connection);
	}	
	
	private void retrieveDetails() throws Exception {
		String fileNo = null;
		if(tempApplicationId != null && tempApplicationId.equals("") == false)
			fileNo = tempApplicationId;
		else
			fileNo = applicationId;
		
		StringBuffer query = new StringBuffer("SELECT APPLICANT_NAME, APPLICANT_ADDRESS, APPLICANT_TOWN_CITY, APPLICANT_STATE, "
				+ "(SELECT SNAME FROM TM_STATE WHERE SCODE=APPLICANT_STATE), APPLICANT_DISTRICT, "
				+ "(SELECT DISTNAME FROM TM_DISTRICT WHERE SCODE=APPLICANT_STATE AND DISTCODE=APPLICANT_DISTRICT), "
				+ "APPLICANT_PINCODE, APPLICANT_EMAIL, APPLICANT_TELNO, "
				+ "APPLICANT_FCRA_PRIORPERMSN_NO, TO_CHAR(APPLICANT_FCRA_PRIORPERMSN_DT, 'dd-mm-yyyy'), "
				+ "TO_CHAR(APPLICANT_ARTCL_DATE, 'dd-mm-yyyy'), APPLICANT_ARTCL_NAME, "
				+ "APPLICANT_ARTCL_DESCRIPTION, APPLICANT_ARTCL_RCVDPERSN_NAME, APPLICANT_ARTCL_RCVDPERSN_ADDR, APPLICANT_ARTCL_PURPOSE, "
				+ "APPLICANT_ARTCL_QUANTITY, APPLICANT_ARTCL_APPROX_VALUE, APPLICANT_ARTCL_MODE_UTILISN, "
				+ "TO_CHAR(FINAL_SUBMIT_DATE, 'dd-mm-yyyy') "
				+ "FROM FC_FC1_ARTCL_SECURITIES_ENTRY WHERE UNIQUE_FILENO=?");
		PreparedStatement statement = connection.prepareStatement(query.toString());
		statement.setString(1, applicationId);
		ResultSet rs = statement.executeQuery();
		if(rs.next()) {
			int i=1;
			applicantDetails.setName(rs.getString(i++));
			applicantDetails.setAddress(rs.getString(i++));
			applicantDetails.setTown(rs.getString(i++));
			applicantDetails.setState(rs.getString(i++));
			applicantDetails.setStateDesc(rs.getString(i++));
			applicantDetails.setDistrict(rs.getString(i++));
			applicantDetails.setDistrictDesc(rs.getString(i++));
			applicantDetails.setPincode(rs.getString(i++));
			applicantDetails.setEmail(rs.getString(i++));
			applicantDetails.setPhoneNumber(rs.getString(i++));

			fcraRegistrationNumber = rs.getString(i++);
			fcraRegistrationDate = rs.getString(i++);
			receivedDate = rs.getString(i++);
			articleName = rs.getString(i++);
			articleDescription = rs.getString(i++);
			relativeName = rs.getString(i++);
			relativeAddress = rs.getString(i++);
			purpose = rs.getString(i++);
			quantity = rs.getString(i++);
			String temp = rs.getString(i++);
			if(temp != null && temp.equals("") == false)
				value = new BigDecimal(temp);
			modeOfUtilization = rs.getString(i++);
			submissionDate = rs.getString(i++);
		}		
		rs.close();
		statement.close();		
	}
	
private void retrievedeletedDocumentDetails() throws Exception {
		
		String documentRoot = docRoot;
		
		String docName = null;
		String docDetails = null;
		String docId = null;

		deletedDocuments.clear();
		deletedSupportDocuments.clear();
		deletedSupportDocumentsEc.clear();
		String otherDocId="04";
		String service = "/09/";
		String dirPath = docUploadFolder.concat(creationDate.substring(6, 10)).concat("/").concat(creationDate.substring(3, 5)).concat("/").concat(creationDate.substring(0, 2)).concat(service).concat(documentId).concat("/");
		File deletedDir = new File(dirPath.concat(deletedDocFolder));
		String dirPathEc = docUploadFolder.concat(creationDate.substring(6, 10)).concat("/").concat(creationDate.substring(3, 5)).concat("/").concat(creationDate.substring(0, 2)).concat(service).concat(documentId).concat("/").concat(documentId).concat("_EC").concat("/"); 
		File deletedDirEc = new File(dirPathEc.concat(deletedDocFolder));
		String deletedDocumentRoot = deletedDocRoot.concat("/").concat(creationDate.substring(6, 10)).concat("/").concat(creationDate.substring(3, 5)).concat("/").concat(creationDate.substring(0, 2)).concat(service).concat(documentId).concat("/");		
				if(deletedDir.exists()) {
					File[] files = deletedDir.listFiles();
					for (File file : files) {
						String fileName = file.getName();
							String deletedDocPath = deletedDocumentRoot.concat(fileName);
							DocumentDetails deletedDoc = new DocumentDetails();
							deletedDoc.setDocId(docId);
							deletedDoc.setDocName(docName);
							deletedDoc.setDocDetails(fileName);
							deletedDoc.setDocPath(deletedDocPath);
							deletedSupportDocuments.add(deletedDoc);
				
					}
				}
				if(deletedDirEc.exists()) {
					File[] files = deletedDirEc.listFiles();
					for (File file : files) {
						String fileName = file.getName();
						String deletedDocPath = deletedDocumentRoot.concat(fileName);
						DocumentDetails deletedDoc = new DocumentDetails();
						deletedDoc.setDocId(docId);
						deletedDoc.setDocName(docName);
						deletedDoc.setDocDetails(fileName);
						deletedDoc.setDocPath(deletedDocPath);
							deletedSupportDocumentsEc.add(deletedDoc);
				
					}
				}

	}
	private void retrieveaffidavitDocumentDetails() throws Exception {
		String fileNo = null;
		if(tempApplicationId != null && tempApplicationId.equals("") == false)
			fileNo = tempApplicationId;
		else
			fileNo = applicationId;
		String documentRoot = docRoot;
		affiddavidDocument.clear();
		StringBuffer query = new StringBuffer("select FILENO,SL_NO,name,FATHER_HUSBAND_NAME,(select desig_name from TM_COMMITTEE_DESIGNATION where OFFICE_OF_ASSO=desig_code) from FC_FC8_COMMITTEE where UNIQUE_FILENO=?");
		PreparedStatement statement = connection.prepareStatement(query.toString());
		statement.setString(1, applicationId);
		ResultSet rs = statement.executeQuery();
		String afdvdfilenum =null;
		String afdvdsrnum = null;
		String afdvdname = null;
		String afdvdfname = null;
		String afdvdpost = null;
		String service = "/09/";
		String dirPath = docUploadFolder.concat(creationDate.substring(6, 10)).concat("/").concat(creationDate.substring(3, 5)).concat("/").concat(creationDate.substring(0, 2)).concat(service).concat(documentId).concat("/").concat(documentId).concat("_EC/");
		File deletedDir = new File(dirPath);
		while(rs.next()) {
			int j = 1;
			String docPath = documentRoot.concat("/").concat(creationDate.substring(6, 10)).concat("/").concat(creationDate.substring(3, 5)).concat("/").concat(creationDate.substring(0, 2)).concat(service).concat(documentId).concat("/");
			File[] files = deletedDir.listFiles();
			afdvdfilenum = rs.getString(j++);
			afdvdsrnum = rs.getString(j++);
			afdvdname = rs.getString(j++);
			afdvdfname= rs.getString(j++);
			afdvdpost= rs.getString(j++);
			docPath = docPath.concat(documentId).concat("_").concat(afdvdsrnum).concat(".pdf");
			AffiddavidDocument afdv=new AffiddavidDocument();
			afdv.setFilenum(afdvdfilenum);
			afdv.setSrnum(afdvdsrnum);
			afdv.setName(afdvdname);
			afdv.setFname(afdvdfname);
			afdv.setPost(afdvdpost);
			afdv.setDocPath(docPath);
			affiddavidDocument.add(afdv);
		}
		rs.close();
		statement.close();
		
	}
	@Override
	public void getDetails() throws Exception {
		if(ESAPI.validator().isValidInput("applicationId", applicationId, "Word", 15, false) == false){
			throw new ValidationException("Invalid Application ID");
		}
		retrieveDetails();
		retrieveaffidavitDocumentDetails();
		retrievedeletedDocumentDetails();
	}

	@Override
	public void saveDetails() throws Exception {
		if(ESAPI.validator().isValidInput("applicationId", applicationId, "Word", 15, false) == false){
			throw new ValidationException("Invalid Application ID");
		}
				
	}
	
	@Override
	public void forward() throws Exception {
	}

	public ApplicantDetails getApplicantDetails() {
		return applicantDetails;
	}

	public void setApplicantDetails(ApplicantDetails applicantDetails) {
		this.applicantDetails = applicantDetails;
	}

	public String getFcraRegistrationNumber() {
		return fcraRegistrationNumber;
	}

	public void setFcraRegistrationNumber(String fcraRegistrationNumber) {
		this.fcraRegistrationNumber = fcraRegistrationNumber;
	}

	public String getFcraRegistrationDate() {
		return fcraRegistrationDate;
	}

	public void setFcraRegistrationDate(String fcraRegistrationDate) {
		this.fcraRegistrationDate = fcraRegistrationDate;
	}

	public String getReceivedDate() {
		return receivedDate;
	}

	public void setReceivedDate(String receivedDate) {
		this.receivedDate = receivedDate;
	}

	public String getArticleName() {
		return articleName;
	}

	public void setArticleName(String articleName) {
		this.articleName = articleName;
	}

	public String getArticleDescription() {
		return articleDescription;
	}

	public void setArticleDescription(String articleDescription) {
		this.articleDescription = articleDescription;
	}

	public String getRelativeName() {
		return relativeName;
	}

	public void setRelativeName(String relativeName) {
		this.relativeName = relativeName;
	}

	public String getRelativeAddress() {
		return relativeAddress;
	}

	public void setRelativeAddress(String relativeAddress) {
		this.relativeAddress = relativeAddress;
	}

	public String getPurpose() {
		return purpose;
	}

	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}

	public String getQuantity() {
		return quantity;
	}

	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}

	public BigDecimal getValue() {
		return value;
	}

	public void setValue(BigDecimal value) {
		this.value = value;
	}

	public String getModeOfUtilization() {
		return modeOfUtilization;
	}

	public void setModeOfUtilization(String modeOfUtilization) {
		this.modeOfUtilization = modeOfUtilization;
	}
	public List<AffiddavidDocument> getAffiddavidDocument() {
		return affiddavidDocument;
	}

	public void setAffiddavidDocument(List<AffiddavidDocument> affiddavidDocument) {
		this.affiddavidDocument = affiddavidDocument;
	}

	public List<DocumentDetails> getDeletedDocuments() {
		return deletedDocuments;
	}

	public void setDeletedDocuments(List<DocumentDetails> deletedDocuments) {
		this.deletedDocuments = deletedDocuments;
	}

	public List<DocumentDetails> getDeletedSupportDocuments() {
		return deletedSupportDocuments;
	}

	public void setDeletedSupportDocuments(List<DocumentDetails> deletedSupportDocuments) {
		this.deletedSupportDocuments = deletedSupportDocuments;
	}

	public List<DocumentDetails> getDeletedSupportDocumentsEc() {
		return deletedSupportDocumentsEc;
	}

	public void setDeletedSupportDocumentsEc(List<DocumentDetails> deletedSupportDocumentsEc) {
		this.deletedSupportDocumentsEc = deletedSupportDocumentsEc;
	}
	
}
