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
import models.services.DonorDetails;

import org.owasp.esapi.ESAPI;

import utilities.ValidationException;

public class Renewal extends AbstractRequest {
	private AssociationDetails associationDetails = new AssociationDetails();
	private AssociationDetails fcraDetails = new AssociationDetails();
	private String memberConvictedByCourt;
	private String memberUnderProsecution;
	private String memberFoundGuiltyOfMisutilization;
	private String memberProhibitedFromAcceptingForeignContribution;
	private String memberKeyFunctionaryOfOtherAssn;
	private String memberKeyFunctionaryOfOtherAssnSec1314;
	private String detailsOfDiscrepancies;

	//Other Details
	private String branchOfForeignOrganisation;
	private String nameOfForeignOrganisation;
	private String addressOfForeignOrganisation;
	private String regnOrPriorPermnNumberOfForeignOrganisation;
	private String dateOfRegnOrPriorPermnForeignOrganisation;
	
	private String attractsSection10;
	private String orderNumberUnderSection11_3;
	private String dateOfOrderUnderSection11_3;
	private String directedToSeekPriorPermission;
	private String orderNumberSeekingPriorPermission;
	private String dateOfOrderSeekingPriorPermission;
	private String proceededAgainstFCRA;
	private String proceededAgainstFCRADetails;
	
	private List<DocumentDetails> uploadedDocuments = new ArrayList<DocumentDetails>();
	private List<DocumentDetails> deletedDocuments = new ArrayList<DocumentDetails>();
	private List<AnnualReturns> annualReturns = new ArrayList<AnnualReturns>();
	private List<DocumentDetail> deletedDocument = new ArrayList<DocumentDetail>();
	private List<AffiddavidDocument> affiddavidDocument = new ArrayList<AffiddavidDocument>();
	private List<DocumentDetail> deletedSupportDocuments = new ArrayList<DocumentDetail>();
	private List<DocumentDetail> deletedSupportDocumentsEc = new ArrayList<DocumentDetail>();

	public Renewal(Connection connection) {
		super(connection);
	}	
	
	private void retrieveRegistrationDetails() throws Exception {
		String fileNo = null;
		if(tempApplicationId != null && tempApplicationId.equals("") == false)
			fileNo = tempApplicationId;
		else
			fileNo = applicationId;
		
		StringBuffer query = new StringBuffer("SELECT CHEIF_FUNCTIONARY_NAME, ASSO_NAME, ASSO_ADDRESS, ASSO_TOWN_CITY, ASSO_STATE, "
				+ "(SELECT SNAME FROM TM_STATE WHERE SCODE=ASSO_STATE), ASSO_DISTRICT, (SELECT DISTNAME FROM TM_DISTRICT WHERE SCODE=ASSO_STATE AND DISTCODE=ASSO_DISTRICT), "
				+ "ASSO_PIN_CODE, ASSO_OFFICIAL_EMAIL,"
				+ "ASSO_OFFICIAL_WEBSITE, ASSO_OFFICIAL_TELEPHONE, ASSO_CHEIF_MOBILE, ASSO_CHEIF_TELEPHONE, ACT_REGISTARTION, "
				+ "(SELECT ACT_NAME FROM TM_SOCIETY_ACTS WHERE ACT_CODE=ACT_REGISTARTION), ACT_REGISTARTION_OTHER, "
				+ "ASSO_REGISTRATION_NO, PLACE_OF_REGISTRATION, to_char(DATE_OF_REGISTRATION, 'dd-mm-yyyy'), PAN_NO, ASSO_NATURE, "
				+ "(SELECT NATURE_DESC FROM TM_NATURE WHERE NATURE_CODE=ASSO_NATURE), ASSO_RELIGION, "
				+ "(SELECT RELIGION_DESC FROM TM_RELIGION WHERE RELIGION_CODE=ASSO_RELIGION), "
				+ "ASSO_RELIGION_OTHER, MAIN_AIMS, ASSO_FCRA_RCN, to_char(ASSO_FCRA_DATE_OF_REGISTRATION, 'dd-mm-yyyy'), "
				+ "CONVITECTED_BY_COURT, PROSECUTION_OFFENCE_PENDING, GUILTY_DIVERSION_MISUTIL_FUNDS, "
				+ "PROHIBITED_FOREIGN_CONTIBUTION, CHEIF_OFFICE_BEAR_YES_NO, MEMBER_OF_ANOTHER_ASSO, CHEIF_PARTRON_DETAIL, "
				+ "BRANCH_ASSO_ALREDY_REG_YES_NO, BRANCH_ASSO_ALREDY_REG_NAME, BRANCH_ASSO_ALREDY_REG_ADDRESS, ASSO_REG_NO, TO_CHAR(ASSO_REG_DATE, 'dd-mm-yyyy'), "
				+ "ATTRACT_SECTION_10_YES_NO, ATTRACT_SECTION_10_FILENO, TO_CHAR(ATTRACT_SECTION_10_DATE, 'dd-mm-yyyy'), DIRECTED_IN_SECTION_9A_YES_NO, "
				+ "DIRECTED_IN_SECTION_9A_FILENO, TO_CHAR(DIRECTED_IN_SECTION_9A_DATE, 'dd-mm-yyyy'), EARLIER_PROCEEDED_YES_NO, EARLIER_PROCEEDED_DETAILS, "
				+ "TO_CHAR(FORM_SUBMISSION_DATE, 'dd-mm-yyyy') "
				+ "FROM FC_FC5_ENTRY_NEW1 WHERE UNIQUE_FILENO=?");
		PreparedStatement statement = connection.prepareStatement(query.toString());
		statement.setString(1, applicationId);
		ResultSet rs = statement.executeQuery();
		if(rs.next()) {
			int i=1;
			associationDetails.setChiefFunctionaryName(rs.getString(i++));
			associationDetails.setAssociationName(rs.getString(i++));
			associationDetails.setAddress(rs.getString(i++));
			associationDetails.setTown(rs.getString(i++));
			associationDetails.setState(rs.getString(i++));
			associationDetails.setStateDesc(rs.getString(i++));
			associationDetails.setDistrict(rs.getString(i++));
			associationDetails.setDistrictDesc(rs.getString(i++));
			associationDetails.setPincode(rs.getString(i++));
			associationDetails.setEmail(rs.getString(i++));
			associationDetails.setWebsite(rs.getString(i++));
			associationDetails.setAssociationPhoneNumber(rs.getString(i++));
			associationDetails.setChiefFunctionaryMobile(rs.getString(i++));
			associationDetails.setChiefFunctionaryPhoneNumber(rs.getString(i++));
			associationDetails.setRegisteredUnderAct(rs.getString(i++));
			associationDetails.setRegisteredUnderActDesc(rs.getString(i++));
			associationDetails.setRegisteredUnderActOther(rs.getString(i++));
			associationDetails.setRegistrationNumber(rs.getString(i++));
			associationDetails.setPlaceOfRegistration(rs.getString(i++));
			associationDetails.setDateOfRegistration(rs.getString(i++));
			associationDetails.setPanNumber(rs.getString(i++));
			associationDetails.setNature(rs.getString(i++));
			associationDetails.setNatureDesc(rs.getString(i++));
			associationDetails.setReligion(rs.getString(i++));
			associationDetails.setReligionDesc(rs.getString(i++));
			associationDetails.setReligionOther(rs.getString(i++));
			associationDetails.setAssociationAim(rs.getString(i++));
			associationDetails.setFcraRegistrationNumber(rs.getString(i++));
			associationDetails.setFcraRegistrationDate(rs.getString(i++));
			
			// Otherdetails
			memberConvictedByCourt = rs.getString(i++);
			memberUnderProsecution = rs.getString(i++);
			memberFoundGuiltyOfMisutilization = rs.getString(i++);
			memberProhibitedFromAcceptingForeignContribution = rs.getString(i++);
			memberKeyFunctionaryOfOtherAssn = rs.getString(i++);
			memberKeyFunctionaryOfOtherAssnSec1314 = rs.getString(i++);
			detailsOfDiscrepancies = rs.getString(i++);
			branchOfForeignOrganisation = rs.getString(i++);
			nameOfForeignOrganisation = rs.getString(i++);
			addressOfForeignOrganisation = rs.getString(i++);
			regnOrPriorPermnNumberOfForeignOrganisation = rs.getString(i++);
			dateOfRegnOrPriorPermnForeignOrganisation = rs.getString(i++);
			attractsSection10 = rs.getString(i++);
			orderNumberUnderSection11_3 = rs.getString(i++);
			dateOfOrderUnderSection11_3 = rs.getString(i++);
			directedToSeekPriorPermission = rs.getString(i++);
			orderNumberSeekingPriorPermission = rs.getString(i++);
			dateOfOrderSeekingPriorPermission = rs.getString(i++);
			proceededAgainstFCRA = rs.getString(i++);
			proceededAgainstFCRADetails = rs.getString(i++);
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
	
	private void retrieveFCRARegistrationDetails() throws Exception {
		StringBuffer query = new StringBuffer("SELECT A.ASSO_NAME, A.ASSO_ADDRESS, A.ASSO_TOWN_CITY, SUBSTR(A.STDIST, 1, 2), "
				+ "(SELECT SNAME FROM TM_STATE WHERE SCODE=SUBSTR(A.STDIST, 1, 2)), SUBSTR(A.STDIST, 3), "
				+ "(SELECT DISTNAME FROM TM_DISTRICT WHERE SCODE=SUBSTR(A.STDIST, 1, 2) AND DISTCODE=SUBSTR(A.STDIST, 3)), "
				+ "A.ASSO_PIN, A.ASSO_NATURE, (SELECT NATURE_DESC FROM TM_NATURE WHERE NATURE_CODE=A.ASSO_NATURE), A.ASSO_RELIGION, "
				+ "(SELECT RELIGION_DESC FROM TM_RELIGION WHERE RELIGION_CODE=A.ASSO_RELIGION), A.RCN, TO_CHAR(A.REG_DATE, 'dd-mm-yyyy'), "
				+ "to_char(case when A.valid_to is null then CASE WHEN reg_date IS NULL THEN null ELSE add_months(reg_date,60)-1 END else valid_to end, 'dd-mm-yyyy'), "
				+ "to_char(LAST_RENEWED_ON, 'dd-mm-yyyy') "
				+ "FROM FC_INDIA A, FC_FC5_ENTRY_NEW1 C WHERE A.RCN=C.ASSO_FCRA_RCN AND C.UNIQUE_FILENO=?");
		PreparedStatement statement = connection.prepareStatement(query.toString());
		statement.setString(1, applicationId);
		ResultSet rs = statement.executeQuery();
		if(rs.next()) {
			int i=1;
			fcraDetails.setAssociationName(rs.getString(i++));
			fcraDetails.setAddress(rs.getString(i++));
			fcraDetails.setTown(rs.getString(i++));
			fcraDetails.setState(rs.getString(i++));
			fcraDetails.setStateDesc(rs.getString(i++));
			fcraDetails.setDistrict(rs.getString(i++));
			fcraDetails.setDistrictDesc(rs.getString(i++));
			fcraDetails.setPincode(rs.getString(i++));
			fcraDetails.setNature(rs.getString(i++));
			fcraDetails.setNatureDesc(rs.getString(i++));
			fcraDetails.setReligion(rs.getString(i++));
			fcraDetails.setReligionDesc(rs.getString(i++));
			fcraDetails.setFcraRegistrationNumber(rs.getString(i++));
			fcraDetails.setFcraRegistrationDate(rs.getString(i++));
			fcraDetails.setRegistrationValidUpto(rs.getString(i++));
			fcraDetails.setRegistrationRenewedOn(rs.getString(i++));
			
			String nature = fcraDetails.getNature(); 
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
		 		fcraDetails.setNatureDesc(assoNatureDesc);
	 		}
		}
		rs.close();
		statement.close();		
	}
	
	private void retrieveFCRARecipientBankDetails() throws Exception {
		
		StringBuffer query = new StringBuffer("SELECT A.ACCOUNT_NO, A.BANK_NAME, "
				+ "(SELECT B.BANK_NAME FROM TM_BANKS B WHERE to_char(BANK_CODE)=A.BANK_NAME), A.BANK_ADDRESS, A.BANK_TOWN_CITY, "
				+ "SUBSTR(BANK_STDIST, 1, 2), (SELECT SNAME FROM TM_STATE WHERE SCODE=SUBSTR(BANK_STDIST, 1, 2)), SUBSTR(BANK_STDIST, 3), "
				+ "(SELECT DISTNAME FROM TM_DISTRICT WHERE SCODE=SUBSTR(BANK_STDIST, 1, 2) AND DISTCODE=SUBSTR(BANK_STDIST, 3)), A.BANK_PIN "
				+ "FROM FC_BANK A, FC_FC5_ENTRY_NEW1 C WHERE A.RCN=C.ASSO_FCRA_RCN AND C.UNIQUE_FILENO=?");
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
			fcraDetails.setReceipientBankDetails(bankDetails);
		}
		rs.close();
		statement.close();
	}
	
	private void retrieveAnnualReturns() throws Exception {
		annualReturns.clear();
		
		StringBuffer query = new StringBuffer("SELECT C.UNIQUE_FILENO, C.BLKYEAR, B.CURRENT_STAGE,  "
				+ "(SELECT STAGE_DESC FROM TM_APPLICATION_STAGE WHERE STAGE_ID=B.CURRENT_STAGE), B.CURRENT_STATUS, "
				+ "(SELECT FILESTATUS_DESC FROM TM_FILESTATUS WHERE FILESTATUS_ID=B.CURRENT_STATUS), C.RCN, TO_CHAR(C.FINAL_SUBMISSION_DATE, 'dd-mm-yyyy') "
				+ "FROM FC_FC5_ENTRY_NEW1 A, FC_FC3_TALLY C, T_APPLICATION_STAGE_DETAILS B "
				+ "WHERE A.ASSO_FCRA_RCN=C.RCN AND B.APPLICATION_ID=C.UNIQUE_FILENO AND A.UNIQUE_FILENO=? ORDER BY C.BLKYEAR");
		PreparedStatement statement = connection.prepareStatement(query.toString());
		statement.setString(1, applicationId);
		ResultSet rs = statement.executeQuery();
		while(rs.next()) {
			int i=1;
			AnnualReturns annualReturn = new AnnualReturns(connection);
			annualReturn.setApplicationId(rs.getString(i++));
			annualReturn.setBlockYear(rs.getString(i++));
			annualReturn.setCurrentStage(rs.getString(i++));
			annualReturn.setCurrentStageDesc(rs.getString(i++));
			annualReturn.setCurrentStatus(rs.getString(i++));
			annualReturn.setCurrentStatusDesc(rs.getString(i++));
			annualReturn.getAssociationDetails().setFcraRegistrationNumber(rs.getString(i++));
			annualReturn.setSubmissionDate(rs.getString(i++));
			annualReturns.add(annualReturn);
		}
		rs.close();
		statement.close();				
	}
	
	private void retrieveRecipientBankDetails() throws Exception {
		String fileNo = null;
		if(tempApplicationId != null && tempApplicationId.equals("") == false)
			fileNo = tempApplicationId;
		else
			fileNo = applicationId;
		
		List<DonorDetails> donorList = associationDetails.getDonorDetails();
		donorList.clear();
		StringBuffer query = new StringBuffer("SELECT ACCOUNT_NO, BANK_NAME, (SELECT B.BANK_NAME FROM TM_BANKS B WHERE to_char(BANK_CODE)=A.BANK_NAME), "
				+ "BANK_ADDRESS, BANK_TOWNCITY, BANK_STATE, (SELECT SNAME FROM TM_STATE WHERE SCODE=BANK_STATE), BANK_DISTRICT, "
				+ "(SELECT DISTNAME FROM TM_DISTRICT WHERE SCODE=BANK_STATE AND DISTCODE=BANK_DISTRICT), "
				+ "BANK_PIN, BANK_IFSC_CODE "
				+ "FROM FC_FC5_RECEIPT_BANK A WHERE UNIQUE_FILENO=?");
		PreparedStatement statement = connection.prepareStatement(query.toString());
		statement.setString(1, applicationId);
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
			bankDetails.setIfscCode(rs.getString(i++));
			associationDetails.setReceipientBankDetails(bankDetails);
		}
		rs.close();
		statement.close();
		
	}
	
	private void retrieveCommitteeMemberDetails() throws Exception {
		String fileNo = null;
		if(tempApplicationId != null && tempApplicationId.equals("") == false)
			fileNo = tempApplicationId;
		else
			fileNo = applicationId;
		
		List<CommitteeMember> committeeList = associationDetails.getCommitteeMembers();
		committeeList.clear();
		StringBuffer query = new StringBuffer("SELECT NAME, FATHER_HUSBAND_NAME, NATIONALITY, "
				+ "(SELECT CTR_NAME FROM TM_COUNTRY WHERE CTR_CODE=NATIONALITY), "
				+ "AADHAAR, OCCUPATION, (SELECT OCC_NAME FROM TM_OCCUPATION WHERE OCC_CODE=OCCUPATION), OCCUPATION_OTHER, "
				+ "OFFICE_OF_ASSO, (SELECT DESIG_NAME FROM TM_COMMITTEE_DESIGNATION WHERE to_char(DESIG_CODE)=OFFICE_OF_ASSO), "
				+ "OFFICE_OF_ASSO_OTHER, BEARERS_RELATIONSHIP, (SELECT RELATION_NAME FROM TM_COMMITTEE_RELATION WHERE to_char(RELATION_CODE)=BEARERS_RELATIONSHIP), "
				+ "BEARERS_RELATIONSHIP_OTHER, ADDRESS_OF_ASSO, "
				+ "ADDRESS_OF_RESID, EMAIL_ID, LANDLINE, MOBILE, MOBILE_CTR_CODE, TO_CHAR(DOB, 'dd-mm-yyyy'), PLACE_BIRTH, PASSPORT_NO, PER_ADD_FC, "
				+ "WHETHER_IN_YN, PIO_NO, RES_INDIA_STATUS, TO_CHAR(RES_INDIA_DATE, 'dd-mm-yyyy') "
				+ "FROM FC_FC5_COMMITTEE a LEFT JOIN FC_EC5_FOREIGNERS b  ON a.FILENO=b.FILENO AND a.SL_NO=b.SL_NO WHERE a.UNIQUE_FILENO=?");
		PreparedStatement statement = connection.prepareStatement(query.toString());
		statement.setString(1, applicationId);
		ResultSet rs = statement.executeQuery();
		while(rs.next()) {
			int i=1;
			CommitteeMember member = new CommitteeMember();
			
			member.setName(rs.getString(1));
			member.setNameOfFatherSpouse(rs.getString(2));
			member.setNationality(rs.getString(3));
			member.setNationalityDesc(rs.getString(4));
			//nationalityOther;
			member.setAadhaarNumber(rs.getString(5));
			member.setOccupation(rs.getString(6));
			if(rs.getString(6).equals("99")){ 
				member.setOccupationDesc(rs.getString(7)+" ("+rs.getString(8)+")");	
			}
			else{
			    member.setOccupationDesc(rs.getString(7)); 
			}
			member.setOccupationOther(rs.getString(8));
			member.setDesignationInAssociation(rs.getString(9));
    
			if(rs.getString(9).equals("99")){
				member.setDesignationInAssociationDesc(rs.getString(10)+" ("+rs.getString(11)+")");
			}
			else
				 member.setDesignationInAssociationDesc(rs.getString(10)); 
			
			member.setDesignationInAssociationOther(rs.getString(11));
			member.setRelationWithOfficeBearers(rs.getString(12));
			if(rs.getString(12).equals("99")){
				member.setRelationWithOfficeBearersDesc(rs.getString(13)+" ("+rs.getString(14)+")");
			}
			else 
		        member.setRelationWithOfficeBearersDesc(rs.getString(13));
			
			member.setOfficeAddress(rs.getString(15));
			member.setResidenceAddress(rs.getString(16));
			member.setEmail(rs.getString(17));
			member.setPhoneNumber(rs.getString(18));
			String mobile = rs.getString(19);
			String mobileCode = rs.getString(20);
			//member.setMobile(mobileCode+"-"+mobile);
			if(mobileCode==null && mobile==null){
				member.setMobile("-");	
			}
			else if(mobileCode==null){
					member.setMobile(mobile);
				}
			else
				member.setMobile(mobileCode+"-"+mobile);
		
			member.setDateOfBirth(rs.getString(21));
			member.setPlaceOfBirth(rs.getString(22));
			member.setPassportNumber(rs.getString(23));
			member.setAddressInForeignCountry(rs.getString(24));
			member.setPersonOfIndianOrigin(rs.getString(25));
			member.setPioOciCardNumber(rs.getString(26));
			member.setResidentInIndia(rs.getString(27));
			member.setDateFromWhichResidingInIndia(rs.getString(28));
			committeeList.add(member);
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
				+ "FROM FC_FC5_UPLOAD_DOC WHERE UNIQUE_FILENO=?");
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
			query = new StringBuffer("SELECT CHK_DETAIL, CHK_DESC "
					+ "FROM FC_FC5_CHECKLIST WHERE CHK_CODE=? AND CHK_CODE NOT IN ('05', '06')");
			statement = connection.prepareStatement(query.toString());
			
			for(int i=0; i<uploadedList.length(); i+=2) {
				String docId = uploadedList.substring(i, i+2);
				statement.setString(1, docId);
				rs = statement.executeQuery();
				String docName = null;
				String docDetails = null;
				String docPath = documentRoot.concat("/").concat(creationDate.substring(6, 10)).concat("/").concat(creationDate.substring(3, 5)).concat("/").concat(creationDate.substring(0, 2)).concat("/03/").concat(documentId).concat("/");
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
		StringBuffer query = new StringBuffer("SELECT CHK_CODE, CHK_DETAIL, CHK_DESC "
				+ "FROM FC_FC5_CHECKLIST WHERE CHK_CODE NOT IN ('05', '06') AND STATUS='Y'");
		PreparedStatement statement = connection.prepareStatement(query.toString());
		ResultSet rs = statement.executeQuery();
		String docName = null;
		String docDetails = null;
		String docId = null;
		
		deletedDocuments.clear();
		String otherDocId="07";
		String service = "/03/";
		String dirPath = docUploadFolder.concat(creationDate.substring(6, 10)).concat("/").concat(creationDate.substring(3, 5)).concat("/").concat(creationDate.substring(0, 2)).concat(service).concat(documentId).concat("/");
		File deletedDir = new File(dirPath.concat(deletedDocFolder));
		String deletedDocumentRoot = deletedDocRoot.concat("/").concat(creationDate.substring(6, 10)).concat("/").concat(creationDate.substring(3, 5)).concat("/").concat(creationDate.substring(0, 2)).concat(service).concat(documentId).concat("/");		
		while(rs.next()) {
			int j = 1;
			String docPath = documentRoot.concat("/").concat(creationDate.substring(6, 10)).concat("/").concat(creationDate.substring(3, 5)).concat("/").concat(creationDate.substring(0, 2)).concat("/03/").concat(documentId).concat("/");
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
		String service = "/03/";
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
		StringBuffer query = new StringBuffer("select FILENO,SL_NO,name,FATHER_HUSBAND_NAME,(select desig_name from TM_COMMITTEE_DESIGNATION where OFFICE_OF_ASSO=desig_code) from FC_FC5_COMMITTEE where UNIQUE_FILENO=?");
		PreparedStatement statement = connection.prepareStatement(query.toString());
		statement.setString(1, applicationId);
		ResultSet rs = statement.executeQuery();
		String afdvdfilenum =null;
		String afdvdsrnum = null;
		String afdvdname = null;
		String afdvdfname = null;
		String afdvdpost = null;
		String service = "/03/";
		while(rs.next()) {
			int j = 1;
			String docPath = documentRoot.concat("/").concat(creationDate.substring(6, 10)).concat("/").concat(creationDate.substring(3, 5)).concat("/").concat(creationDate.substring(0, 2)).concat(service).concat(documentId).concat("/").concat(documentId).concat("_EC/");
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
		retrieveFCRARegistrationDetails();
		retrieveFCRARecipientBankDetails();
		retrieveAnnualReturns();
		retrieveRecipientBankDetails();
		retrieveCommitteeMemberDetails();
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

	public AssociationDetails getFcraDetails() {
		return fcraDetails;
	}

	public void setFcraDetails(AssociationDetails fcraDetails) {
		this.fcraDetails = fcraDetails;
	}

	public String getMemberConvictedByCourt() {
		return memberConvictedByCourt;
	}

	public void setMemberConvictedByCourt(String memberConvictedByCourt) {
		this.memberConvictedByCourt = memberConvictedByCourt;
	}

	public String getMemberUnderProsecution() {
		return memberUnderProsecution;
	}

	public void setMemberUnderProsecution(String memberUnderProsecution) {
		this.memberUnderProsecution = memberUnderProsecution;
	}

	public String getMemberFoundGuiltyOfMisutilization() {
		return memberFoundGuiltyOfMisutilization;
	}

	public void setMemberFoundGuiltyOfMisutilization(
			String memberFoundGuiltyOfMisutilization) {
		this.memberFoundGuiltyOfMisutilization = memberFoundGuiltyOfMisutilization;
	}

	public String getMemberProhibitedFromAcceptingForeignContribution() {
		return memberProhibitedFromAcceptingForeignContribution;
	}

	public void setMemberProhibitedFromAcceptingForeignContribution(
			String memberProhibitedFromAcceptingForeignContribution) {
		this.memberProhibitedFromAcceptingForeignContribution = memberProhibitedFromAcceptingForeignContribution;
	}

	public String getMemberKeyFunctionaryOfOtherAssn() {
		return memberKeyFunctionaryOfOtherAssn;
	}

	public void setMemberKeyFunctionaryOfOtherAssn(
			String memberKeyFunctionaryOfOtherAssn) {
		this.memberKeyFunctionaryOfOtherAssn = memberKeyFunctionaryOfOtherAssn;
	}

	public String getMemberKeyFunctionaryOfOtherAssnSec1314() {
		return memberKeyFunctionaryOfOtherAssnSec1314;
	}

	public void setMemberKeyFunctionaryOfOtherAssnSec1314(
			String memberKeyFunctionaryOfOtherAssnSec1314) {
		this.memberKeyFunctionaryOfOtherAssnSec1314 = memberKeyFunctionaryOfOtherAssnSec1314;
	}

	public String getDetailsOfDiscrepancies() {
		return detailsOfDiscrepancies;
	}

	public void setDetailsOfDiscrepancies(String detailsOfDiscrepancies) {
		this.detailsOfDiscrepancies = detailsOfDiscrepancies;
	}

	public String getBranchOfForeignOrganisation() {
		return branchOfForeignOrganisation;
	}

	public void setBranchOfForeignOrganisation(String branchOfForeignOrganisation) {
		this.branchOfForeignOrganisation = branchOfForeignOrganisation;
	}

	public String getNameOfForeignOrganisation() {
		return nameOfForeignOrganisation;
	}

	public void setNameOfForeignOrganisation(String nameOfForeignOrganisation) {
		this.nameOfForeignOrganisation = nameOfForeignOrganisation;
	}

	public String getAddressOfForeignOrganisation() {
		return addressOfForeignOrganisation;
	}

	public void setAddressOfForeignOrganisation(String addressOfForeignOrganisation) {
		this.addressOfForeignOrganisation = addressOfForeignOrganisation;
	}

	public String getRegnOrPriorPermnNumberOfForeignOrganisation() {
		return regnOrPriorPermnNumberOfForeignOrganisation;
	}

	public void setRegnOrPriorPermnNumberOfForeignOrganisation(
			String regnOrPriorPermnNumberOfForeignOrganisation) {
		this.regnOrPriorPermnNumberOfForeignOrganisation = regnOrPriorPermnNumberOfForeignOrganisation;
	}

	public String getDateOfRegnOrPriorPermnForeignOrganisation() {
		return dateOfRegnOrPriorPermnForeignOrganisation;
	}

	public void setDateOfRegnOrPriorPermnForeignOrganisation(
			String dateOfRegnOrPriorPermnForeignOrganisation) {
		this.dateOfRegnOrPriorPermnForeignOrganisation = dateOfRegnOrPriorPermnForeignOrganisation;
	}

	public String getAttractsSection10() {
		return attractsSection10;
	}

	public void setAttractsSection10(String attractsSection10) {
		this.attractsSection10 = attractsSection10;
	}

	public String getOrderNumberUnderSection11_3() {
		return orderNumberUnderSection11_3;
	}

	public void setOrderNumberUnderSection11_3(String orderNumberUnderSection11_3) {
		this.orderNumberUnderSection11_3 = orderNumberUnderSection11_3;
	}

	public String getDateOfOrderUnderSection11_3() {
		return dateOfOrderUnderSection11_3;
	}

	public void setDateOfOrderUnderSection11_3(String dateOfOrderUnderSection11_3) {
		this.dateOfOrderUnderSection11_3 = dateOfOrderUnderSection11_3;
	}

	public String getDirectedToSeekPriorPermission() {
		return directedToSeekPriorPermission;
	}

	public void setDirectedToSeekPriorPermission(
			String directedToSeekPriorPermission) {
		this.directedToSeekPriorPermission = directedToSeekPriorPermission;
	}

	public String getOrderNumberSeekingPriorPermission() {
		return orderNumberSeekingPriorPermission;
	}

	public void setOrderNumberSeekingPriorPermission(
			String orderNumberSeekingPriorPermission) {
		this.orderNumberSeekingPriorPermission = orderNumberSeekingPriorPermission;
	}

	public String getDateOfOrderSeekingPriorPermission() {
		return dateOfOrderSeekingPriorPermission;
	}

	public void setDateOfOrderSeekingPriorPermission(
			String dateOfOrderSeekingPriorPermission) {
		this.dateOfOrderSeekingPriorPermission = dateOfOrderSeekingPriorPermission;
	}

	public String getProceededAgainstFCRA() {
		return proceededAgainstFCRA;
	}

	public void setProceededAgainstFCRA(String proceededAgainstFCRA) {
		this.proceededAgainstFCRA = proceededAgainstFCRA;
	}

	public String getProceededAgainstFCRADetails() {
		return proceededAgainstFCRADetails;
	}

	public void setProceededAgainstFCRADetails(String proceededAgainstFCRADetails) {
		this.proceededAgainstFCRADetails = proceededAgainstFCRADetails;
	}

	public List<DocumentDetails> getUploadedDocuments() {
		return uploadedDocuments;
	}

	public void setUploadedDocuments(List<DocumentDetails> uploadedDocuments) {
		this.uploadedDocuments = uploadedDocuments;
	}

	public List<AnnualReturns> getAnnualReturns() {
		return annualReturns;
	}

	public void setAnnualReturns(List<AnnualReturns> annualReturns) {
		this.annualReturns = annualReturns;
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
