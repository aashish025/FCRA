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
import models.services.DocumentDetails;

import org.owasp.esapi.ESAPI;

import utilities.ValidationException;

public class ChangeOfNameAndAddress extends AbstractRequest {
	private AssociationDetails associationDetails = new AssociationDetails();

	private String changeOfNameOrAim;
	private String newAssociationName;
	private String newAssociationAim;
	
	private String changeOfAddress;
	private String newAddress;
	private String newTown;
	private String newState;
	private String newStateDesc;
	private String newDistrict;
	private String newDistrictDesc;
	private String newPincode;

	private String changeOfReceipientBank;
	private BankDetails newReceipientBankDetails;

	private String changeOfUtilizationBank;
	private List<BankDetails> newUtilizationBankDetails = new ArrayList<BankDetails>();

	private String changeOfCommitteeMembers;
	private List<CommitteeMember> newCommitteeMemberDetails = new ArrayList<CommitteeMember>();
	
	private String changeOfNature;
	private String newNature;
	private String newNatureDesc;
	private String newReligion;
	private String newReligionDesc;
	private String newReligionOther;
	
	private List<DocumentDetails> uploadedDocuments = new ArrayList<DocumentDetails>();
	private List<DocumentDetails> deletedDocuments = new ArrayList<DocumentDetails>();
	private List<AffiddavidDocument> affiddavidDocument = new ArrayList<AffiddavidDocument>();
	private List<DocumentDetails> deletedSupportDocuments = new ArrayList<DocumentDetails>();
	private List<DocumentDetails> deletedSupportDocumentsEc = new ArrayList<DocumentDetails>();

	public ChangeOfNameAndAddress(Connection connection) {
		super(connection);
	}	
	
	private void retrieveRegistrationDetails() throws Exception {
		String fileNo = null;
		if(tempApplicationId != null && tempApplicationId.equals("") == false)
			fileNo = tempApplicationId;
		else
			fileNo = applicationId;
		
		StringBuffer query = new StringBuffer("SELECT A.ASSO_NAME, A.ASSO_ADDRESS, A.ASSO_TOWN_CITY, SUBSTR(A.STDIST, 1, 2), "
				+ "(SELECT SNAME FROM TM_STATE WHERE SCODE=SUBSTR(A.STDIST, 1, 2)), SUBSTR(A.STDIST, 3), (SELECT DISTNAME FROM TM_DISTRICT WHERE SCODE=SUBSTR(A.STDIST, 1, 2) AND DISTCODE=SUBSTR(A.STDIST, 3)), "
				+ "A.ASSO_PIN, A.ASSO_NATURE, "
				+ "(SELECT NATURE_DESC FROM TM_NATURE WHERE NATURE_CODE=A.ASSO_NATURE), ASSO_RELIGION, "
				+ "(SELECT RELIGION_DESC FROM TM_RELIGION WHERE RELIGION_CODE=ASSO_RELIGION), A.RCN, TO_CHAR(A.REG_DATE, 'dd-mm-yyyy'),"
				+ "TO_CHAR(FORM_SUBMISSION_DATE, 'dd-mm-yyyy') "
				+ "FROM FC_INDIA A, FC_FC6_NAMEADDRESS_CHANGE C WHERE A.RCN=C.RCN AND C.UNIQUE_FILENO=?");
		PreparedStatement statement = connection.prepareStatement(query.toString());
		statement.setString(1, applicationId);
		ResultSet rs = statement.executeQuery();
		if(rs.next()) {
			int i=1;
			associationDetails.setAssociationName(rs.getString(i++));
			associationDetails.setAddress(rs.getString(i++));
			associationDetails.setTown(rs.getString(i++));
			associationDetails.setState(rs.getString(i++));
			associationDetails.setStateDesc(rs.getString(i++));
			associationDetails.setDistrict(rs.getString(i++));
			associationDetails.setDistrictDesc(rs.getString(i++));
			associationDetails.setPincode(rs.getString(i++));
			associationDetails.setNature(rs.getString(i++));
			associationDetails.setNatureDesc(rs.getString(i++));
			associationDetails.setReligion(rs.getString(i++));
			associationDetails.setReligionDesc(rs.getString(i++));
			associationDetails.setFcraRegistrationNumber(rs.getString(i++));
			associationDetails.setFcraRegistrationDate(rs.getString(i++));
			
			submissionDate = rs.getString(i++);
			
			String nature = associationDetails.getNature(); 
	 		if(nature != null) {
	 			String assoNatureDesc="";
	 			StringBuffer query1 = new StringBuffer("SELECT NATURE_DESC FROM TM_NATURE  WHERE NATURE_CODE=?");
	 			PreparedStatement statement1 = connection.prepareStatement(query1.toString());
		 		for(int j=0;j<nature.length();j++){
		 			String natureCode = nature.substring(j, j+1);
		 			statement1.setString(1, natureCode);
		 			String delim=(j==0 ? "" : ",");
		 			ResultSet rs5 = statement1.executeQuery();
		 			if(rs5.next()){
		 				assoNatureDesc=assoNatureDesc + delim + rs5.getString(1);
		 			}
		 			rs5.close();
		 		}
		 		statement1.close();
		 		associationDetails.setNatureDesc(assoNatureDesc);
	 		}
			
		}
		rs.close();
		statement.close();		
	}
	
/*	private void retrieveRegisteredRecipientBankDetails() throws Exception {
		String fileNo = null;
		if(tempApplicationId != null && tempApplicationId.equals("") == false)
			fileNo = tempApplicationId;
		else
			fileNo = applicationId;
		
		StringBuffer query = new StringBuffer("SELECT A.ACCOUNT_NO, A.BANK_NAME, (SELECT B.BANK_NAME FROM TM_BANKS B WHERE to_char(BANK_CODE)=A.BANK_NAME), "
				+ "A.BANK_ADDRESS, A.BANK_TOWN_CITY, SUBSTR(BANK_STDIST, 1, 2), (SELECT SNAME FROM TM_STATE WHERE SCODE=SUBSTR(BANK_STDIST, 1, 2)), SUBSTR(BANK_STDIST, 3), "
				+ "(SELECT DISTNAME FROM TM_DISTRICT WHERE SCODE=SUBSTR(BANK_STDIST, 1, 2) AND DISTCODE=SUBSTR(BANK_STDIST, 3)), "
				+ "A.BANK_PIN "
				+ "FROM FC_BANK A, FC_FC6_FORM C WHERE A.RCN=C.RCN AND C.UNIQUE_FILENO=?");
		PreparedStatement statement = connection.prepareStatement(query.toString());
		statement.setString(1,applicationId);
		ResultSet rs = statement.executeQuery();
		if(rs.next()) {
			int i=1;
			// Bank details
			BankDetails bankDetails = new BankDetails();
			bankDetails.setAccountNumber(rs.getString(i++));
			bankDetails.setBankId(rs.getString(i++));
			bankDetails.setBankName(rs.getString(i++));
			bankDetails.setAddress(rs.getString(i++));
			bankDetails.setTown(rs.getString(i++));
			bankDetails.setState(rs.getString(i++));
			bankDetails.setStateDesc(rs.getString(i++));
			bankDetails.setDistrict(rs.getString(i++));
			bankDetails.setDistrictDesc(rs.getString(i++));
			bankDetails.setPincode(rs.getString(i++));
			//bankDetails.setIfscCode(rs.getString(i++));
			associationDetails.setReceipientBankDetails(bankDetails);
		}
		rs.close();
		statement.close();
	}*/
	
	private void retrieveDetailsOfChange() throws Exception {
		String fileNo = null;
		if(tempApplicationId != null && tempApplicationId.equals("") == false)
			fileNo = tempApplicationId;
		else
			fileNo = applicationId;
		
/*		StringBuffer query = new StringBuffer("SELECT ASSO_NAME_CHANGE_ST, ASSO_CHANGE_STATE_ST, ASSO_CHANGE_DESIGBANK_STATUS, "
				+ "ASSO_CHANGE_UTILISATION_STATUS, ASSO_CHANGE_MEMBER, ASSO_CHANGE_NATURE, ASSO_NAME, MAIN_AIM, ASSO_ADDRESS, ASSO_TOWN_CITY, "
				+ "ASSO_STATE, (SELECT SNAME FROM TM_STATE WHERE SCODE=ASSO_STATE), "
				+ "ASO_DISTRICT, (SELECT DISTNAME FROM TM_DISTRICT WHERE SCODE=ASSO_STATE AND DISTCODE=ASO_DISTRICT), "
				+ "ASSO_PIN, ACCOUNT_NO, BANK_NAME, (SELECT B.BANK_NAME FROM TM_BANKS B WHERE to_char(BANK_CODE)=C.BANK_NAME), "
				+ "BANK_ADDRESS, BANK_TOWNCITY, "
				+ "BANK_STATE, (SELECT SNAME FROM TM_STATE WHERE SCODE=BANK_STATE), "
				+ "BANK_DISTRICT, (SELECT DISTNAME FROM TM_DISTRICT WHERE SCODE=BANK_STATE AND DISTCODE=BANK_DISTRICT), "
				+ "BANK_PIN, BANK_BRANCH_IFSC_CODE, ASSO_NATURE, ASSO_RELIGIOUS, "
				+ "(SELECT RELIGION_DESC FROM TM_RELIGION WHERE RELIGION_CODE=ASSO_RELIGIOUS), RELIGION_OTHER "
				+ "FROM FC_FC6_FORM C WHERE C.UNIQUE_FILENO=?");  */
		
		StringBuffer query = new StringBuffer("SELECT is_name_change, is_address_change, CHANGED_TO_ASSONAME, CHANGED_TO_ASSOADDRESS,CHANGED_TO_ASSOTOWNCITY,CHANGED_TO_ASSOSTATE,"
				+ "(SELECT SNAME FROM TM_STATE WHERE SCODE=CHANGED_TO_ASSOSTATE) as newStateDesc, CHANGED_TO_ASSODISTRICT, (SELECT DISTNAME FROM TM_DISTRICT WHERE SCODE=CHANGED_TO_ASSOSTATE AND DISTCODE=CHANGED_TO_ASSODISTRICT) as newDistrictDesc,"
				+ "CHANGED_TO_ASSOPIN FROM FC_FC6_NAMEADDRESS_CHANGE C WHERE C.UNIQUE_FILENO=?");
	
		
		PreparedStatement statement = connection.prepareStatement(query.toString());
		statement.setString(1, applicationId);
		ResultSet rs = statement.executeQuery();
		if(rs.next()) {
			int i=1;
			changeOfNameOrAim = rs.getString("is_name_change");
			changeOfAddress = rs.getString("is_address_change");
			String changOfNameFlag = changeOfNameOrAim;
			String ChangeOfAddressFlag = changeOfAddress;
			//changeOfReceipientBank = rs.getString(i++);
			//changeOfUtilizationBank = rs.getString(i++);
			//changeOfCommitteeMembers = rs.getString(i++);
			//changeOfNature = rs.getString(i++);
			if(changOfNameFlag.equals("Y")){
				newAssociationName = rs.getString("CHANGED_TO_ASSONAME");	
			}
			if(ChangeOfAddressFlag.equals("Y")){
				newAddress = rs.getString("CHANGED_TO_ASSOADDRESS");
				newTown = rs.getString("CHANGED_TO_ASSOTOWNCITY");
				newState = rs.getString("CHANGED_TO_ASSOSTATE");
				newStateDesc = rs.getString("newStateDesc");
				newDistrict = rs.getString("CHANGED_TO_ASSODISTRICT");
				newDistrictDesc = rs.getString("newDistrictDesc");
				newPincode = rs.getString("CHANGED_TO_ASSOPIN");
			}
			//newAssociationName = rs.getString(i++);
			//newAssociationAim = rs.getString(i++);

			//newAddress = rs.getString(i++);
			//newTown = rs.getString(i++);
			//newState = rs.getString(i++);
			//newStateDesc = rs.getString(i++);
			//newDistrict = rs.getString(i++);
			//newDistrictDesc = rs.getString(i++);
			//newPincode = rs.getString(i++);

	/*		// Bank details
			BankDetails bankDetails = new BankDetails();
			bankDetails.setAccountNumber(rs.getString(i++));
			bankDetails.setBankId(rs.getString(i++));
			bankDetails.setBankName(rs.getString(i++));
			bankDetails.setAddress(rs.getString(i++));
			bankDetails.setTown(rs.getString(i++));
			bankDetails.setState(rs.getString(i++));
			bankDetails.setStateDesc(rs.getString(i++));
			bankDetails.setDistrict(rs.getString(i++));
			bankDetails.setDistrictDesc(rs.getString(i++));
			bankDetails.setPincode(rs.getString(i++));
			bankDetails.setIfscCode(rs.getString(i++));
			newReceipientBankDetails = bankDetails;
			
			newNature = rs.getString(i++);
			newReligion = rs.getString(i++);
			newReligionDesc = rs.getString(i++);
			newReligionOther = rs.getString(i++);
			String nature = newNature;
	 		if(nature != null) {
	 			String assoNatureDesc="";
	 			StringBuffer query1 = new StringBuffer("SELECT NATURE_DESC FROM TM_NATURE  WHERE NATURE_CODE=?");
	 			PreparedStatement statement1 = connection.prepareStatement(query1.toString());
		 		for(int j=0;j<nature.length();j++){
		 			String natureCode = nature.substring(j, j+1);
		 			statement1.setString(1, natureCode);
		 			String delim=(j==0 ? "" : ",");
		 			ResultSet rs5 = statement1.executeQuery();
		 			if(rs5.next()){
		 				assoNatureDesc=assoNatureDesc + delim + rs5.getString(1);
		 			}
		 			rs5.close();
		 		}
		 		statement1.close();
		 		newNatureDesc = assoNatureDesc;
	 		} */
		}
		rs.close();
		statement.close();
	}
	
/*	private void retrieveChangeInUtilizationBanks() throws Exception {
		String fileNo = null;
		if(tempApplicationId != null && tempApplicationId.equals("") == false)
			fileNo = tempApplicationId;
		else
			fileNo = applicationId;
		
		newUtilizationBankDetails.clear();
		StringBuffer query = new StringBuffer("SELECT ACCOUNT_NO, BANK_NAME, (SELECT B.BANK_NAME FROM TM_BANKS B WHERE to_char(BANK_CODE)=C.BANK_NAME), "
				+ "BANK_ADDRESS, BANK_TOWNCITY, BANK_STATE, (SELECT SNAME FROM TM_STATE WHERE SCODE=BANK_STATE), "
				+ "BANK_DISTRICT, (SELECT DISTNAME FROM TM_DISTRICT WHERE SCODE=BANK_STATE AND DISTCODE=BANK_DISTRICT), "
				+ "BANK_PIN, BANK_IFSC_CODE "
				+ "FROM FC_FC6_UTILIZATION_BANK C WHERE C.UNIQUE_FILENO=?");
		PreparedStatement statement = connection.prepareStatement(query.toString());
		statement.setString(1, applicationId);
		ResultSet rs = statement.executeQuery();
		while(rs.next()) {
			int i=1;
			
			// Bank details
			BankDetails bankDetails = new BankDetails();
			bankDetails.setAccountNumber(rs.getString(i++));
			bankDetails.setBankId(rs.getString(i++));
			bankDetails.setBankName(rs.getString(i++));
			bankDetails.setAddress(rs.getString(i++));
			bankDetails.setTown(rs.getString(i++));
			bankDetails.setState(rs.getString(i++));
			bankDetails.setStateDesc(rs.getString(i++));
			bankDetails.setDistrict(rs.getString(i++));
			bankDetails.setDistrictDesc(rs.getString(i++));
			bankDetails.setPincode(rs.getString(i++));
			bankDetails.setIfscCode(rs.getString(i++));
			newUtilizationBankDetails.add(bankDetails);
		}
		rs.close();
		statement.close();
	}
	*/
/*	private void retrieveChangeInCommitteeMemberDetails() throws Exception {
		String fileNo = null;
		if(tempApplicationId != null && tempApplicationId.equals("") == false)
			fileNo = tempApplicationId;
		else
			fileNo = applicationId;
		
		newCommitteeMemberDetails.clear();
		StringBuffer query = new StringBuffer("SELECT NAME, FATHER_HUSBAND_NAME, NATIONALITY, "
				+ "(SELECT CTR_NAME FROM TM_COUNTRY WHERE CTR_CODE=NATIONALITY), "
				+ "AADHAAR, OCCUPATION, (SELECT OCC_NAME FROM TM_OCCUPATION WHERE OCC_CODE=OCCUPATION), OCCUPATION_OTHER, "
				+ "OFFICE_OF_ASSO, (SELECT DESIG_NAME FROM TM_COMMITTEE_DESIGNATION WHERE TO_CHAR(DESIG_CODE)=OFFICE_OF_ASSO), "
				+ "OFFICE_OF_ASSO_OTHER, BEARERS_RELATIONSHIP, (SELECT RELATION_NAME FROM TM_COMMITTEE_RELATION WHERE TO_CHAR(RELATION_CODE)=BEARERS_RELATIONSHIP), "
				+ "BEARERS_RELATIONSHIP_OTHER, ADDRESS_OF_ASSO, "
				+ "ADDRESS_OF_RESID, EMAIL_ID, LANDLINE, MOBILE, TO_CHAR(DOB, 'dd-mm-yyyy'), PLACE_BIRTH, PASSPORT_NO, PER_ADD_FC, "
				+ "WHETHER_IN_YN, PIO_NO, RES_INDIA_STATUS, TO_CHAR(RES_INDIA_DATE, 'dd-mm-yyyy') "
				+ "FROM FC_FC6_COMMITTEE a LEFT JOIN FC_EC5_FOREIGNERS b  ON a.UNIQUE_FILENO=b.UNIQUE_FILENO AND a.SL_NO=b.SL_NO WHERE a.UNIQUE_FILENO=?");
		PreparedStatement statement = connection.prepareStatement(query.toString());
		statement.setString(1, applicationId);
		ResultSet rs = statement.executeQuery();
		while(rs.next()) {
			int i=1;
			CommitteeMember member = new CommitteeMember();
			
			member.setName(rs.getString(i++));
			member.setNameOfFatherSpouse(rs.getString(i++));
			member.setNationality(rs.getString(i++));
			member.setNationalityDesc(rs.getString(i++));
			//nationalityOther;
			member.setAadhaarNumber(rs.getString(i++));
			member.setOccupation(rs.getString(i++));
			member.setOccupationDesc(rs.getString(i++));
			member.setOccupationOther(rs.getString(i++));
			member.setDesignationInAssociation(rs.getString(i++));
			member.setDesignationInAssociationDesc(rs.getString(i++));
			member.setDesignationInAssociationOther(rs.getString(i++));
			member.setRelationWithOfficeBearers(rs.getString(i++));
			member.setRelationWithOfficeBearersDesc(rs.getString(i++));
			member.setRelationWithOfficeBearersOther(rs.getString(i++));
			member.setOfficeAddress(rs.getString(i++));
			member.setResidenceAddress(rs.getString(i++));
			member.setEmail(rs.getString(i++));
			member.setPhoneNumber(rs.getString(i++));
			String mobile = rs.getString(i++);
			//String mobileCode = rs.getString(i++);	
			member.setMobile(mobile);
			
			member.setDateOfBirth(rs.getString(i++));
			member.setPlaceOfBirth(rs.getString(i++));
			member.setPassportNumber(rs.getString(i++));
			member.setAddressInForeignCountry(rs.getString(i++));
			member.setPersonOfIndianOrigin(rs.getString(i++));
			member.setPioOciCardNumber(rs.getString(i++));
			member.setResidentInIndia(rs.getString(i++));
			member.setDateFromWhichResidingInIndia(rs.getString(i++));
			newCommitteeMemberDetails.add(member);
		}		
		rs.close();
		statement.close();		
	}
	*/
	private void retrieveUploadedDocumentDetails() throws Exception {
		/* *
		String fileNo = null;
		if(tempApplicationId != null && tempApplicationId.equals("") == false)
			fileNo = tempApplicationId;
		else
			fileNo = applicationId;
		
		uploadedDocuments.clear();
		StringBuffer query = new StringBuffer("SELECT UPLOADED_DOC, TO_CHAR(SUBMIT_DATE, 'dd-mm-yyyy') "
				+ "FROM FC_FC6_UPLOAD_DOC WHERE UNIQUE_FILENO=?");
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
			query = new StringBuffer("SELECT CHK_NAME, CHK_NAME "
					+ "FROM FC_FC6_CHECKLIST WHERE CHK_CODE=? AND CHK_CODE NOT IN ('04', '05')");
			statement = connection.prepareStatement(query.toString());
			
			for(int i=0; i<uploadedList.length(); i+=2) {
				String docId = uploadedList.substring(i, i+2);
				statement.setString(1, docId);
				rs = statement.executeQuery();
				String docName = null;
				String docDetails = null;
				String docPath = documentRoot.concat("/").concat(creationDate.substring(6, 10)).concat("/").concat(creationDate.substring(3, 5)).concat("/").concat(creationDate.substring(0, 2)).concat("/06/").concat(documentId).concat("/");
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
		StringBuffer query = new StringBuffer("SELECT CHK_CODE, CHK_NAME, CHK_NAME FROM FC_FC6_CHECKLIST_NAMEADDRESS "
				+ "WHERE CHK_CODE NOT IN ('01', '02') AND STATUS='Y'");
		
		PreparedStatement statement = connection.prepareStatement(query.toString());
		ResultSet rs = statement.executeQuery();
		String docName = null;
		String docDetails = null;
		String docId = null;

		deletedDocuments.clear();
		String otherDocId="13";
		String service = "/13/";
		String dirPath = docUploadFolder.concat(creationDate.substring(6, 10)).concat("/").concat(creationDate.substring(3, 5)).concat("/").concat(creationDate.substring(0, 2)).concat(service).concat(documentId).concat("/");
		File deletedDir = new File(dirPath.concat(deletedDocFolder));
		String deletedDocumentRoot = deletedDocRoot.concat("/").concat(creationDate.substring(6, 10)).concat("/").concat(creationDate.substring(3, 5)).concat("/").concat(creationDate.substring(0, 2)).concat(service).concat(documentId).concat("/");		
		while(rs.next()) {
			int j = 1;
			String docPath = documentRoot.concat("/").concat(creationDate.substring(6, 10)).concat("/").concat(creationDate.substring(3, 5)).concat("/").concat(creationDate.substring(0, 2)).concat("/13/").concat(documentId).concat("/");
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
		
		String documentRoot = docRoot;
		String docName = null;
		String docDetails = null;
		String docId = null;

		deletedDocuments.clear();
		deletedSupportDocuments.clear();
		deletedSupportDocumentsEc.clear();
		String otherDocId="04";
		String service = "/13/";
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
		String service = "/13/";
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
		retrieveRegistrationDetails();
		//retrieveRegisteredRecipientBankDetails();
		retrieveDetailsOfChange();
		//retrieveChangeInUtilizationBanks();
		//retrieveChangeInCommitteeMemberDetails();
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

	
	public AssociationDetails getAssociationDetails() {
		return associationDetails;
	}

	public void setAssociationDetails(AssociationDetails associationDetails) {
		this.associationDetails = associationDetails;
	}

	public String getChangeOfNameOrAim() {
		return changeOfNameOrAim;
	}

	public void setChangeOfNameOrAim(String changeOfNameOrAim) {
		this.changeOfNameOrAim = changeOfNameOrAim;
	}

	public String getNewAssociationName() {
		return newAssociationName;
	}

	public void setNewAssociationName(String newAssociationName) {
		this.newAssociationName = newAssociationName;
	}

	public String getNewAssociationAim() {
		return newAssociationAim;
	}

	public void setNewAssociationAim(String newAssociationAim) {
		this.newAssociationAim = newAssociationAim;
	}

	public String getChangeOfAddress() {
		return changeOfAddress;
	}

	public void setChangeOfAddress(String changeOfAddress) {
		this.changeOfAddress = changeOfAddress;
	}

	public String getNewAddress() {
		return newAddress;
	}

	public void setNewAddress(String newAddress) {
		this.newAddress = newAddress;
	}

	public String getNewTown() {
		return newTown;
	}

	public void setNewTown(String newTown) {
		this.newTown = newTown;
	}

	public String getNewState() {
		return newState;
	}

	public void setNewState(String newState) {
		this.newState = newState;
	}

	public String getNewStateDesc() {
		return newStateDesc;
	}

	public void setNewStateDesc(String newStateDesc) {
		this.newStateDesc = newStateDesc;
	}

	public String getNewDistrict() {
		return newDistrict;
	}

	public void setNewDistrict(String newDistrict) {
		this.newDistrict = newDistrict;
	}

	public String getNewDistrictDesc() {
		return newDistrictDesc;
	}

	public void setNewDistrictDesc(String newDistrictDesc) {
		this.newDistrictDesc = newDistrictDesc;
	}

	public String getNewPincode() {
		return newPincode;
	}

	public void setNewPincode(String newPincode) {
		this.newPincode = newPincode;
	}

	public String getChangeOfReceipientBank() {
		return changeOfReceipientBank;
	}

	public void setChangeOfReceipientBank(String changeOfReceipientBank) {
		this.changeOfReceipientBank = changeOfReceipientBank;
	}

	public BankDetails getNewReceipientBankDetails() {
		return newReceipientBankDetails;
	}

	public void setNewReceipientBankDetails(BankDetails newReceipientBankDetails) {
		this.newReceipientBankDetails = newReceipientBankDetails;
	}

	public String getChangeOfUtilizationBank() {
		return changeOfUtilizationBank;
	}

	public void setChangeOfUtilizationBank(String changeOfUtilizationBank) {
		this.changeOfUtilizationBank = changeOfUtilizationBank;
	}

	public List<BankDetails> getNewUtilizationBankDetails() {
		return newUtilizationBankDetails;
	}

	public void setNewUtilizationBankDetails(
			List<BankDetails> newUtilizationBankDetails) {
		this.newUtilizationBankDetails = newUtilizationBankDetails;
	}

	public String getChangeOfCommitteeMembers() {
		return changeOfCommitteeMembers;
	}

	public void setChangeOfCommitteeMembers(String changeOfCommitteeMembers) {
		this.changeOfCommitteeMembers = changeOfCommitteeMembers;
	}

	public List<CommitteeMember> getNewCommitteeMemberDetails() {
		return newCommitteeMemberDetails;
	}

	public void setNewCommitteeMemberDetails(
			List<CommitteeMember> newCommitteeMemberDetails) {
		this.newCommitteeMemberDetails = newCommitteeMemberDetails;
	}

	public String getChangeOfNature() {
		return changeOfNature;
	}

	public void setChangeOfNature(String changeOfNature) {
		this.changeOfNature = changeOfNature;
	}

	public String getNewNature() {
		return newNature;
	}

	public void setNewNature(String newNature) {
		this.newNature = newNature;
	}

	public String getNewNatureDesc() {
		return newNatureDesc;
	}

	public void setNewNatureDesc(String newNatureDesc) {
		this.newNatureDesc = newNatureDesc;
	}

	public String getNewReligion() {
		return newReligion;
	}

	public void setNewReligion(String newReligion) {
		this.newReligion = newReligion;
	}

	public String getNewReligionDesc() {
		return newReligionDesc;
	}

	public void setNewReligionDesc(String newReligionDesc) {
		this.newReligionDesc = newReligionDesc;
	}

	public String getNewReligionOther() {
		return newReligionOther;
	}

	public void setNewReligionOther(String newReligionOther) {
		this.newReligionOther = newReligionOther;
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
