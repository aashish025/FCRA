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

public class ElectionContribution extends AbstractRequest {
	ApplicantDetails applicantDetails = new ApplicantDetails();
	String dateOnWhichNominated;
	String natureOfContribution;
	BigDecimal value;
	String purpose;
	String donorName;
	String donorAddress;
	String donorEmail;
	String donorNationality;
	String donorNationalityDesc;
	String donorCountryOfResidence;
	String donorCountryOfResidenceDesc;
	String donorRelation;
	String utilizationDetails;
	private List<DocumentDetails> uploadedDocuments = new ArrayList<DocumentDetails>();
	private List<DocumentDetails> deletedDocuments = new ArrayList<DocumentDetails>();
	private List<AffiddavidDocument> affiddavidDocument = new ArrayList<AffiddavidDocument>();
	private List<DocumentDetails> deletedSupportDocuments = new ArrayList<DocumentDetails>();
	private List<DocumentDetails> deletedSupportDocumentsEc = new ArrayList<DocumentDetails>();
	
	public ElectionContribution(Connection connection) {
		super(connection);
	}	
	
	private void retrieveDetails() throws Exception {
		String fileNo = null;
		if(tempApplicationId != null && tempApplicationId.equals("") == false)
			fileNo = tempApplicationId;
		else
			fileNo = applicationId;
		
		StringBuffer query = new StringBuffer("SELECT APPLICANT_NAME, TO_CHAR(APPLICANT_DOB, 'dd-mm-yyyy'), APPLICANT_FATHER_NAME, "
				+ "APPLICANT_ADDRESS, APPLICANT_TOWN_CITY, APPLICANT_STATE, "
				+ "(SELECT SNAME FROM TM_STATE WHERE SCODE=APPLICANT_STATE), APPLICANT_DISTRIC, "
				+ "(SELECT DISTNAME FROM TM_DISTRICT WHERE SCODE=APPLICANT_STATE AND DISTCODE=APPLICANT_DISTRIC), "
				+ "APPLICANT_PINCODE, APPLICANT_EMAIL, APPLICANT_TELNO, APPLICANT_MOBILE_NO, "
				+ "TO_CHAR(APPLICANT_DATE_DULY_NOMINATION, 'dd-mm-yyyy'), FOREIGN_CONTRIBUTION_RECIEVED, VALUE, PURPOSE, "
				+ "DONOR_NAME, DONOR_ADDRESS, DONOR_EMAIL, DONOR_NATIONALITY, (SELECT CTR_NAME FROM TM_COUNTRY WHERE CTR_CODE=DONOR_NATIONALITY), "
				+ "RESIDENCE_COUNTRY, (SELECT CTR_NAME FROM TM_COUNTRY WHERE CTR_CODE=RESIDENCE_COUNTRY), "
				+ "DONOR_RELATIONSHIP, UTILIZATION_DETAILS, TO_CHAR(FINAL_SUBMIT_DATE, 'dd-mm-yyyy') "
				+ "FROM FC_FC1_ELECTION_ENTRY WHERE UNIQUE_FILENO=?");
		PreparedStatement statement = connection.prepareStatement(query.toString());
		statement.setString(1, applicationId);
		ResultSet rs = statement.executeQuery();
		if(rs.next()) {
			int i=1;
			applicantDetails.setName(rs.getString(i++));
			applicantDetails.setDateOfBirth(rs.getString(i++));
			applicantDetails.setNameOfFatherSpouse(rs.getString(i++));
			applicantDetails.setAddress(rs.getString(i++));
			applicantDetails.setTown(rs.getString(i++));
			applicantDetails.setState(rs.getString(i++));
			applicantDetails.setStateDesc(rs.getString(i++));
			applicantDetails.setDistrict(rs.getString(i++));
			applicantDetails.setDistrictDesc(rs.getString(i++));
			applicantDetails.setPincode(rs.getString(i++));
			applicantDetails.setEmail(rs.getString(i++));
			applicantDetails.setPhoneNumber(rs.getString(i++));
			applicantDetails.setMobile(rs.getString(i++));
			
			dateOnWhichNominated = rs.getString(i++);
			natureOfContribution = rs.getString(i++);
			String temp = rs.getString(i++);
			if(temp != null && temp.equals("") == false)
				value = new BigDecimal(temp);
			purpose = rs.getString(i++);
			donorName = rs.getString(i++);
			donorAddress = rs.getString(i++);
			donorEmail = rs.getString(i++);
			donorNationality = rs.getString(i++);
			donorNationalityDesc = rs.getString(i++);
			donorCountryOfResidence = rs.getString(i++);
			donorCountryOfResidenceDesc = rs.getString(i++);
			donorRelation = rs.getString(i++);
			utilizationDetails = rs.getString(i++);
			submissionDate = rs.getString(i++);
		}		
		rs.close();
		statement.close();		
	}
	
private void retrievedeletedDocumentDetails() throws Exception {
		String docName = null;
		String docDetails = null;
		String docId = null;

		deletedDocuments.clear();
		deletedSupportDocuments.clear();
		deletedSupportDocumentsEc.clear();
		String otherDocId="04";
		String service = "/11/";
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
		String service = "/11/";
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

	public String getDateOnWhichNominated() {
		return dateOnWhichNominated;
	}

	public void setDateOnWhichNominated(String dateOnWhichNominated) {
		this.dateOnWhichNominated = dateOnWhichNominated;
	}

	public String getNatureOfContribution() {
		return natureOfContribution;
	}

	public void setNatureOfContribution(String natureOfContribution) {
		this.natureOfContribution = natureOfContribution;
	}

	public BigDecimal getValue() {
		return value;
	}

	public void setValue(BigDecimal value) {
		this.value = value;
	}

	public String getPurpose() {
		return purpose;
	}

	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}

	public String getDonorName() {
		return donorName;
	}

	public void setDonorName(String donorName) {
		this.donorName = donorName;
	}

	public String getDonorAddress() {
		return donorAddress;
	}

	public void setDonorAddress(String donorAddress) {
		this.donorAddress = donorAddress;
	}

	public String getDonorEmail() {
		return donorEmail;
	}

	public void setDonorEmail(String donorEmail) {
		this.donorEmail = donorEmail;
	}

	public String getDonorNationality() {
		return donorNationality;
	}

	public void setDonorNationality(String donorNationality) {
		this.donorNationality = donorNationality;
	}

	public String getDonorNationalityDesc() {
		return donorNationalityDesc;
	}

	public void setDonorNationalityDesc(String donorNationalityDesc) {
		this.donorNationalityDesc = donorNationalityDesc;
	}

	public String getDonorCountryOfResidence() {
		return donorCountryOfResidence;
	}

	public void setDonorCountryOfResidence(String donorCountryOfResidence) {
		this.donorCountryOfResidence = donorCountryOfResidence;
	}

	public String getDonorCountryOfResidenceDesc() {
		return donorCountryOfResidenceDesc;
	}

	public void setDonorCountryOfResidenceDesc(String donorCountryOfResidenceDesc) {
		this.donorCountryOfResidenceDesc = donorCountryOfResidenceDesc;
	}

	public String getDonorRelation() {
		return donorRelation;
	}

	public void setDonorRelation(String donorRelation) {
		this.donorRelation = donorRelation;
	}

	public String getUtilizationDetails() {
		return utilizationDetails;
	}

	public void setUtilizationDetails(String utilizationDetails) {
		this.utilizationDetails = utilizationDetails;
	}
	public List<AffiddavidDocument> getAffiddavidDocument() {
		return affiddavidDocument;
	}

	public void setAffiddavidDocument(List<AffiddavidDocument> affiddavidDocument) {
		this.affiddavidDocument = affiddavidDocument;
	}

	public List<DocumentDetails> getUploadedDocuments() {
		return uploadedDocuments;
	}

	public void setUploadedDocuments(List<DocumentDetails> uploadedDocuments) {
		this.uploadedDocuments = uploadedDocuments;
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
