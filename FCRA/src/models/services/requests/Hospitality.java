package models.services.requests;

import java.io.File;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import models.services.AffiddavidDocument;
import models.services.AssociationDetails;
import models.services.BankDetails;
import models.services.CommitteeMember;
import models.services.DocumentDetail;
import models.services.DocumentDetails;
import models.services.HospitalityDetails;
import models.services.HostDetails;

import org.owasp.esapi.ESAPI;

import utilities.ValidationException;

public class Hospitality extends AbstractRequest {
	private String applicantName;
	private String dateOfBirth;
	private String nameOfFatherSpouse;
	
	private String address;
	private String town;
	private String state;
	private String stateDesc;
	private String district;
	private String districtDesc;
	private String pincode;
	private String phoneNumber;
	private String email;
	private String mobile;
	private String applicantOrganization;
	private String applicantDesignation;
	private String passportAvailable;
	private String passportNumber;
	private String passportPlaceOfIssue;
	private String passportDateOfIssue;
	private String passportDateOfExpiry;
	private String forwardingLetterNumber;
	private String forwardingLetterDate;
	private String forwardingOfficerName;
	private String forwardingOfficerDesignation;
	private String forwardingOfficerOfficeAddress;
	private String forwardingOfficerTown;
	private String forwardingOfficerState;
	private String forwardingOfficerStateDesc;
	private String forwardingOfficerDistrict;
	private String forwardingOfficerDistrictDesc;
	private String forwardingOfficerPincode;
	private String memberCategory;
	private String memberCategoryDesc;
	
	private List<HospitalityDetails> hospitalityDetails = new ArrayList<HospitalityDetails>();
	private List<HostDetails> hostDetails = new ArrayList<HostDetails>();
	private List<DocumentDetails> uploadedDocuments = new ArrayList<DocumentDetails>();
	private List<DocumentDetails> deletedDocuments = new ArrayList<DocumentDetails>();
	private List<DocumentDetail> deletedDocument = new ArrayList<DocumentDetail>();
	private List<DocumentDetail> deletedSupportDocuments = new ArrayList<DocumentDetail>();
	private List<DocumentDetail> deletedSupportDocumentsEc = new ArrayList<DocumentDetail>();
	private List<AffiddavidDocument> affiddavidDocument = new ArrayList<AffiddavidDocument>();
	

	public Hospitality(Connection connection) {
		super(connection);
	}	
	
	private void retrieveDetails() throws Exception {
		String fileNo = null;
		if(tempApplicationId != null && tempApplicationId.equals("") == false)
			fileNo = tempApplicationId;
		else
			fileNo = applicationId;
		
		StringBuffer query = new StringBuffer("SELECT APPT_NAME, TO_CHAR(APPT_BIRTH_DATE, 'dd-mm-yyyy'), APPT_FATHER_HUSBAND_NAME, PRESENT_ADD, "
				+ "PRESENT_TOWN_CITY, PRESENT_STATE, (SELECT SNAME FROM TM_STATE WHERE SCODE=PRESENT_STATE), "
				+ "PRESENT_DISTRICT, (SELECT DISTNAME FROM TM_DISTRICT WHERE SCODE=PRESENT_STATE AND DISTCODE=PRESENT_DISTRICT), "
				+ "PRESENT_PINCODE, APPT_TELPHONE, APPT_EMAIL, APPT_MOBILE, EMP_ORG, EMP_DESIG, "
				+ "PASSPORT_NO, PASSPORT_PLACE_ISSUE, TO_CHAR(PASSPORT_DATE_ISSUE, 'dd-mm-yyyy'), TO_CHAR(PASSPORT_VALIDITY, 'dd-mm-yyyy'), "
				+ "FWD_LETTER_NO, TO_CHAR(FWD_LETTER_DATE, 'dd-mm-yyyy'), FWD_OFF_NAME, FWD_OFF_DES, FWD_OFF_ADD, FWD_OFF_TOWN_CITY, "
				+ "FWD_OFF_STATE, (SELECT SNAME FROM TM_STATE WHERE SCODE=FWD_OFF_STATE), "
				+ "FWD_OFF_DISTRICT, (SELECT DISTNAME FROM TM_DISTRICT WHERE SCODE=FWD_OFF_STATE AND DISTCODE=FWD_OFF_DISTRICT), "
				+ "FWD_OFF_PINCODE, STATUTORY_PROVISSION_CODE, (SELECT CATEGORY_NAME FROM TM_MEMBER_CATEGORY WHERE CATEGORY_CODE=STATUTORY_PROVISSION_CODE), "
				+ "TO_CHAR(FORM_SUBMISSION_DATE, 'dd-mm-yyyy') "
				+ "FROM FC_H_ENTRY WHERE UNIQUE_FILENO=?");
		PreparedStatement statement = connection.prepareStatement(query.toString());
		statement.setString(1, applicationId);
		ResultSet rs = statement.executeQuery();
		if(rs.next()) {
			int i=1;
			applicantName = rs.getString(i++);
			dateOfBirth = rs.getString(i++);
			nameOfFatherSpouse = rs.getString(i++);
			address = rs.getString(i++);
			town = rs.getString(i++);
			state = rs.getString(i++);
			stateDesc = rs.getString(i++);
			district = rs.getString(i++);
			districtDesc = rs.getString(i++);
			pincode = rs.getString(i++);
			phoneNumber = rs.getString(i++);
			email = rs.getString(i++);
			mobile = rs.getString(i++);
			applicantOrganization = rs.getString(i++);
			applicantDesignation = rs.getString(i++);
			passportNumber = rs.getString(i++);
			if(passportNumber != null && passportNumber.equals("") == false)
				passportAvailable = "Y";
			else
				passportAvailable = "N";
			passportPlaceOfIssue = rs.getString(i++);
			passportDateOfIssue = rs.getString(i++);
			passportDateOfExpiry = rs.getString(i++);
			forwardingLetterNumber = rs.getString(i++);
			forwardingLetterDate = rs.getString(i++);
			forwardingOfficerName = rs.getString(i++);
			forwardingOfficerDesignation = rs.getString(i++);
			forwardingOfficerOfficeAddress = rs.getString(i++);
			forwardingOfficerTown = rs.getString(i++);
			forwardingOfficerState = rs.getString(i++);
			forwardingOfficerStateDesc = rs.getString(i++);
			forwardingOfficerDistrict = rs.getString(i++);
			forwardingOfficerDistrictDesc = rs.getString(i++);
			forwardingOfficerPincode = rs.getString(i++);
			memberCategory = rs.getString(i++);
			memberCategoryDesc = rs.getString(i++);

			submissionDate = rs.getString(i++);
		}		
		rs.close();
		statement.close();		
	}
	
	private void retrieveHospitalityDetails() throws Exception {
		String fileNo = null;
		if(tempApplicationId != null && tempApplicationId.equals("") == false)
			fileNo = tempApplicationId;
		else
			fileNo = applicationId;
		
		hospitalityDetails.clear();
		StringBuffer query = new StringBuffer("SELECT VISITING_COUNTRY, (SELECT CTR_NAME FROM TM_COUNTRY WHERE CTR_CODE=VISITING_COUNTRY), "
				+ "VISITING_PLACE, TO_CHAR(DURATION_STATY_FROM, 'dd-mm-yyyy'), TO_CHAR(DURATION_STAY_TO, 'dd-mm-yyyy'), PURPOSE, "
				+ "NATURE_HOS_CASH, NATURE_HOS_CASH_CURRENCY, (SELECT CURR_NAME FROM TM_CURRENCY WHERE CURR_CODE=NATURE_HOS_CASH_CURRENCY), "
				+ "NATURE_HOS_KIND, TO_CHAR(DURATION_HOSPI_FROM, 'dd-mm-yyyy'), TO_CHAR(DURATION_HOSPI_TO, 'dd-mm-yyyy'), EXPENDIUTE, "
				+ "EXPENDITURE_CURRENCY, (SELECT CURR_NAME FROM TM_CURRENCY WHERE CURR_CODE=EXPENDITURE_CURRENCY), REMARKS "
				+ "FROM FC_H_HOSPITALITY_DETAILS WHERE UNIQUE_FILENO=?");
		PreparedStatement statement = connection.prepareStatement(query.toString());
		statement.setString(1, applicationId);
		ResultSet rs = statement.executeQuery();
		while(rs.next()) {
			int i=1;
			HospitalityDetails details = new HospitalityDetails();
			
			details.setHospitalityCountry(rs.getString(i++));
			details.setHospitalityCountryDesc(rs.getString(i++));
			details.setHospitalityCity(rs.getString(i++));
			details.setStayFromDate(rs.getString(i++));
			details.setStayToDate(rs.getString(i++));
			details.setPurposeOfVisit(rs.getString(i++));
			String temp = rs.getString(i++);
			if(temp != null && temp.equals("") == false)
				details.setHospitalityCash(new BigDecimal(temp));
			details.setHospitalityCurrency(rs.getString(i++));
			details.setHospitalityCurrencyDesc(rs.getString(i++));
			details.setHospitalityKind(rs.getString(i++));
			details.setHospitalityDurationFromDate(rs.getString(i++));
			details.setHospitalityDurationToDate(rs.getString(i++));
			temp = rs.getString(i++);
			if(temp != null && temp.equals("") == false)
				details.setAmountOfExpenditure(new BigDecimal(temp));
			details.setExpenditureCurrency(rs.getString(i++));
			details.setExpenditureCurrencyDesc(rs.getString(i++));
			details.setRemarks(rs.getString(i++));
			
			hospitalityDetails.add(details);
		}		
		rs.close();
		statement.close();		
	}
			
	private void retrieveHostDetails() throws Exception {
		String fileNo = null;
		if(tempApplicationId != null && tempApplicationId.equals("") == false)
			fileNo = tempApplicationId;
		else
			fileNo = applicationId;
		
		hostDetails.clear();
		StringBuffer query = new StringBuffer("SELECT IND_ORG, (SELECT HOST_TYPE_DESC FROM TM_NATURE_OF_HOST WHERE HOST_TYPE=IND_ORG), "
				+ "IND_ORG_NAME, IND_PRESENT_ADD, IND_ORG_EMAIL, IND_ORG_MOBILE, IND_ORG_TELEPHONE, "
				+ "NATURE_OF_DEALING, IND_NATIONALITY, (SELECT CTR_NAME FROM TM_COUNTRY WHERE CTR_CODE=IND_NATIONALITY), "
				+ "IND_PROFESSION, (SELECT OCC_NAME FROM TM_OCCUPATION WHERE OCC_CODE=IND_PROFESSION), IND_PASSPORT_NUMBER "
				+ "FROM FC_H_PARTICULARS_HOST WHERE UNIQUE_FILENO=?");
		PreparedStatement statement = connection.prepareStatement(query.toString());
		statement.setString(1, applicationId);
		ResultSet rs = statement.executeQuery();
		while(rs.next()) {
			int i=1;
			HostDetails details = new HostDetails();
			
			details.setNatureOfHost(rs.getString(i++));
			details.setNatureOfHostDesc(rs.getString(i++));
			details.setNameOfHost(rs.getString(i++));
			details.setAddressOfHost(rs.getString(i++));
			details.setEmail(rs.getString(i++));
			details.setMobile(rs.getString(i++));
			details.setPhoneNumber(rs.getString(i++));
			details.setRelationshipWithHost(rs.getString(i++));
			details.setNationalityOfHost(rs.getString(i++));
			details.setNationalityOfHostDesc(rs.getString(i++));
			details.setProfessionOfHost(rs.getString(i++));
			details.setProfessionOfHostDesc(rs.getString(i++));
			details.setPassportNumberOfHost(rs.getString(i++));

			hostDetails.add(details);
		}		
		rs.close();
		statement.close();		
	}
	
	private void retrieveUploadedDocumentDetails() throws Exception {
		/* *
		String fileNo = null;
		if(tempApplicationId != null && tempApplicationId.equals("") == false)
			fileNo = tempApplicationId;
		else
			fileNo = applicationId;
		
		uploadedDocuments.clear();
		StringBuffer query = new StringBuffer("SELECT UPLOADED_DOC, TO_CHAR(SUBMIT_DATE, 'dd-mm-yyyy') "
				+ "FROM FC_H_UPLOADED_DOC WHERE UNIQUE_FILENO=?");
		PreparedStatement statement = connection.prepareStatement(query.toString());
		statement.setString(1, applicationId);
		ResultSet rs = statement.executeQuery();
		String uploadedList = null;
		String submissionDate = null;
		String documentRoot = docRoot;
		if(rs.next()) {
			int i=1;
			uploadedList = rs.getString(i++);
			submissionDate = rs.getString(i++);
		}
		rs.close();
		statement.close();
		
		
		if(uploadedList != null && uploadedList.equals("") == false) {
			query = new StringBuffer("SELECT CHK_NAME, CHK_DESC "
					+ "FROM FC_H_CHECKLIST WHERE CHK_CODE=? AND CHK_CODE NOT IN ('01')");
			statement = connection.prepareStatement(query.toString());
			
			for(int i=0; i<uploadedList.length(); i+=2) {
				String docId = uploadedList.substring(i, i+2);
				statement.setString(1, docId);
				rs = statement.executeQuery();
				String docName = null;
				String docDetails = null;
				String docPath = documentRoot.concat("/").concat(creationDate.substring(6, 10)).concat("/").concat(creationDate.substring(3, 5)).concat("/").concat(creationDate.substring(0, 2)).concat("/07/").concat(documentId).concat("/");
				if(rs.next()) {
					int j = 1;
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
			}
			statement.close();
		}
		* */
		String documentRoot = docRoot;
		uploadedDocuments.clear();
		StringBuffer query = new StringBuffer("SELECT CHK_CODE, CHK_NAME, CHK_DESC "
					+ "FROM FC_H_CHECKLIST WHERE CHK_CODE NOT IN ('01') AND CHK_STATUS='Y'");
		PreparedStatement statement = connection.prepareStatement(query.toString());
		ResultSet rs = statement.executeQuery();
		String docName = null;
		String docDetails = null;
		String docId = null;

		deletedDocuments.clear();
		String otherDocId="04";
		String service = "/07/";
		String dirPath = docUploadFolder.concat(creationDate.substring(6, 10)).concat("/").concat(creationDate.substring(3, 5)).concat("/").concat(creationDate.substring(0, 2)).concat(service).concat(documentId).concat("/");
		File deletedDir = new File(dirPath.concat(deletedDocFolder));
		String deletedDocumentRoot = deletedDocRoot.concat("/").concat(creationDate.substring(6, 10)).concat("/").concat(creationDate.substring(3, 5)).concat("/").concat(creationDate.substring(0, 2)).concat(service).concat(documentId).concat("/");		
		while(rs.next()) {
			int j = 1;
			String docPath = documentRoot.concat("/").concat(creationDate.substring(6, 10)).concat("/").concat(creationDate.substring(3, 5)).concat("/").concat(creationDate.substring(0, 2)).concat("/07/").concat(documentId).concat("/");
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

			if(docId.equals(otherDocId)) {
				if(deletedDir.exists()) {
					File[] files = deletedDir.listFiles();
					for (File file : files) {
						String fileName = file.getName();
						if(fileName.startsWith(documentId.concat("_").concat(docId))) {
							String deletedDocPath = deletedDocumentRoot.concat(fileName);
							DocumentDetails deletedDoc = new DocumentDetails();
							deletedDoc.setDocId(docId);
							deletedDoc.setDocName(docName);
							deletedDoc.setDocDetails(fileName);
							deletedDoc.setDocPath(deletedDocPath);
							deletedDocuments.add(deletedDoc);
						}
					}
				}
			}
		}
		rs.close();
		statement.close();

	}
	private void retrievedeletedDocumentDetails() throws Exception {
		String fileNo = null;
		if(tempApplicationId != null && tempApplicationId.equals("") == false)
			fileNo = tempApplicationId;
		else
			fileNo = applicationId;
		String documentRoot = docRoot;
		//uploadedDocuments.clear();
		
		String docName = null;
		String docDetails = null;
		String docId = null;
		System.out.println("fileno----1111");
		//deletedDocument.clear();
		deletedSupportDocuments.clear();
		deletedSupportDocumentsEc.clear();
		String otherDocId="04";
		String service = "/07/";
		String dirPath = docUploadFolder.concat(creationDate.substring(6, 10)).concat("/").concat(creationDate.substring(3, 5)).concat("/").concat(creationDate.substring(0, 2)).concat(service).concat(documentId).concat("/");
		File deletedDir = new File(dirPath.concat(deletedDocFolder));
		String dirPathEc = docUploadFolder.concat(creationDate.substring(6, 10)).concat("/").concat(creationDate.substring(3, 5)).concat("/").concat(creationDate.substring(0, 2)).concat(service).concat(documentId).concat("/").concat(documentId).concat("_EC").concat("/"); 
		File deletedDirEc = new File(dirPathEc.concat(deletedDocFolder));
		String deletedDocumentRoot = docRoot.concat("/").concat(creationDate.substring(6, 10)).concat("/").concat(creationDate.substring(3, 5)).concat("/").concat(creationDate.substring(0, 2)).concat(service).concat(documentId).concat("/").concat("Deleted/");		
				if(deletedDir.exists()) {
					File[] files = deletedDir.listFiles();
					for (File file : files) {
						
						String fileName = file.getName();
							String deletedDocPath = deletedDocumentRoot.concat(fileName);
							DocumentDetail deletedDoc = new DocumentDetail();
							deletedDoc.setDocId(docId);
							deletedDoc.setDocName(docName);
							deletedDoc.setDocDetails(fileName);
							deletedDoc.setDocPath(deletedDocPath);
							deletedSupportDocuments.add(deletedDoc);
							System.out.println("fileno----1"+deletedDocPath);
					}
				}
				/*if(deletedDirEc.exists()) {
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
				}*/

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
		String service = "/07/";
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
		retrieveHospitalityDetails();
		retrieveHostDetails();
		retrieveUploadedDocumentDetails();
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

	public String getApplicantName() {
		return applicantName;
	}

	public void setApplicantName(String applicantName) {
		this.applicantName = applicantName;
	}

	public String getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(String dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public String getNameOfFatherSpouse() {
		return nameOfFatherSpouse;
	}

	public void setNameOfFatherSpouse(String nameOfFatherSpouse) {
		this.nameOfFatherSpouse = nameOfFatherSpouse;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getTown() {
		return town;
	}

	public void setTown(String town) {
		this.town = town;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getStateDesc() {
		return stateDesc;
	}

	public void setStateDesc(String stateDesc) {
		this.stateDesc = stateDesc;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public String getDistrictDesc() {
		return districtDesc;
	}

	public void setDistrictDesc(String districtDesc) {
		this.districtDesc = districtDesc;
	}

	public String getPincode() {
		return pincode;
	}

	public void setPincode(String pincode) {
		this.pincode = pincode;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getApplicantOrganization() {
		return applicantOrganization;
	}

	public void setApplicantOrganization(String applicantOrganization) {
		this.applicantOrganization = applicantOrganization;
	}

	public String getApplicantDesignation() {
		return applicantDesignation;
	}

	public void setApplicantDesignation(String applicantDesignation) {
		this.applicantDesignation = applicantDesignation;
	}

	public String getPassportAvailable() {
		return passportAvailable;
	}

	public void setPassportAvailable(String passportAvailable) {
		this.passportAvailable = passportAvailable;
	}

	public String getPassportNumber() {
		return passportNumber;
	}

	public void setPassportNumber(String passportNumber) {
		this.passportNumber = passportNumber;
	}

	public String getPassportPlaceOfIssue() {
		return passportPlaceOfIssue;
	}

	public void setPassportPlaceOfIssue(String passportPlaceOfIssue) {
		this.passportPlaceOfIssue = passportPlaceOfIssue;
	}

	public String getPassportDateOfIssue() {
		return passportDateOfIssue;
	}

	public void setPassportDateOfIssue(String passportDateOfIssue) {
		this.passportDateOfIssue = passportDateOfIssue;
	}

	public String getPassportDateOfExpiry() {
		return passportDateOfExpiry;
	}

	public void setPassportDateOfExpiry(String passportDateOfExpiry) {
		this.passportDateOfExpiry = passportDateOfExpiry;
	}

	public String getForwardingLetterNumber() {
		return forwardingLetterNumber;
	}

	public void setForwardingLetterNumber(String forwardingLetterNumber) {
		this.forwardingLetterNumber = forwardingLetterNumber;
	}

	public String getForwardingLetterDate() {
		return forwardingLetterDate;
	}

	public void setForwardingLetterDate(String forwardingLetterDate) {
		this.forwardingLetterDate = forwardingLetterDate;
	}

	public String getForwardingOfficerName() {
		return forwardingOfficerName;
	}

	public void setForwardingOfficerName(String forwardingOfficerName) {
		this.forwardingOfficerName = forwardingOfficerName;
	}

	public String getForwardingOfficerDesignation() {
		return forwardingOfficerDesignation;
	}

	public void setForwardingOfficerDesignation(String forwardingOfficerDesignation) {
		this.forwardingOfficerDesignation = forwardingOfficerDesignation;
	}

	public String getForwardingOfficerOfficeAddress() {
		return forwardingOfficerOfficeAddress;
	}

	public void setForwardingOfficerOfficeAddress(
			String forwardingOfficerOfficeAddress) {
		this.forwardingOfficerOfficeAddress = forwardingOfficerOfficeAddress;
	}

	public String getForwardingOfficerTown() {
		return forwardingOfficerTown;
	}

	public void setForwardingOfficerTown(String forwardingOfficerTown) {
		this.forwardingOfficerTown = forwardingOfficerTown;
	}

	public String getForwardingOfficerState() {
		return forwardingOfficerState;
	}

	public void setForwardingOfficerState(String forwardingOfficerState) {
		this.forwardingOfficerState = forwardingOfficerState;
	}

	public String getForwardingOfficerStateDesc() {
		return forwardingOfficerStateDesc;
	}

	public void setForwardingOfficerStateDesc(String forwardingOfficerStateDesc) {
		this.forwardingOfficerStateDesc = forwardingOfficerStateDesc;
	}

	public String getForwardingOfficerDistrict() {
		return forwardingOfficerDistrict;
	}

	public void setForwardingOfficerDistrict(String forwardingOfficerDistrict) {
		this.forwardingOfficerDistrict = forwardingOfficerDistrict;
	}

	public String getForwardingOfficerDistrictDesc() {
		return forwardingOfficerDistrictDesc;
	}

	public void setForwardingOfficerDistrictDesc(
			String forwardingOfficerDistrictDesc) {
		this.forwardingOfficerDistrictDesc = forwardingOfficerDistrictDesc;
	}

	public String getForwardingOfficerPincode() {
		return forwardingOfficerPincode;
	}

	public void setForwardingOfficerPincode(String forwardingOfficerPincode) {
		this.forwardingOfficerPincode = forwardingOfficerPincode;
	}

	public String getMemberCategory() {
		return memberCategory;
	}

	public void setMemberCategory(String memberCategory) {
		this.memberCategory = memberCategory;
	}

	public String getMemberCategoryDesc() {
		return memberCategoryDesc;
	}

	public void setMemberCategoryDesc(String memberCategoryDesc) {
		this.memberCategoryDesc = memberCategoryDesc;
	}

	public List<HospitalityDetails> getHospitalityDetails() {
		return hospitalityDetails;
	}

	public void setHospitalityDetails(List<HospitalityDetails> hospitalityDetails) {
		this.hospitalityDetails = hospitalityDetails;
	}

	public List<HostDetails> getHostDetails() {
		return hostDetails;
	}

	public void setHostDetails(List<HostDetails> hostDetails) {
		this.hostDetails = hostDetails;
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
	public List<AffiddavidDocument> getAffiddavidDocument() {
		return affiddavidDocument;
	}

	public void setAffiddavidDocument(List<AffiddavidDocument> affiddavidDocument) {
		this.affiddavidDocument = affiddavidDocument;
	}

	public List<DocumentDetail> getDeletedDocument() {
		return deletedDocument;
	}

	public void setDeletedDocument(List<DocumentDetail> deletedDocument) {
		this.deletedDocument = deletedDocument;
	}

	public List<DocumentDetail> getDeletedSupportDocuments() {
		return deletedSupportDocuments;
	}

	public void setDeletedSupportDocuments(List<DocumentDetail> deletedSupportDocuments) {
		this.deletedSupportDocuments = deletedSupportDocuments;
	}

	public List<DocumentDetail> getDeletedSupportDocumentsEc() {
		return deletedSupportDocumentsEc;
	}

	public void setDeletedSupportDocumentsEc(List<DocumentDetail> deletedSupportDocumentsEc) {
		this.deletedSupportDocumentsEc = deletedSupportDocumentsEc;
	}

	
	
}
