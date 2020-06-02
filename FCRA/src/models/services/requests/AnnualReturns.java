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
import models.services.DonorDetails;
import models.services.DonorWiseContribution;
import models.services.PurposeWiseContribution;

import org.owasp.esapi.ESAPI;

import utilities.ValidationException;

public class AnnualReturns extends AbstractRequest {
	private AssociationDetails associationDetails = new AssociationDetails();
	private String blockYear;
	private BigDecimal amountBroughtForward;
	private BigDecimal otherReceipt;
	private BigDecimal foreignContributionDirect;
	private BigDecimal foreignContributionTransfer;
	private BigDecimal totalForeignContribution;
	private List<PurposeWiseContribution> purposeWiseContribution = new ArrayList<PurposeWiseContribution>();
	private List<DonorWiseContribution> donorWiseContribution = new ArrayList<DonorWiseContribution>();
	private BigDecimal totalUtilizationForProjects;
	private BigDecimal totalAdministrativeExpenses;
	private BigDecimal totalInvestedInTermDeposits;
	private BigDecimal totalPurchase;
	private BigDecimal totalUtilization;
	private BigDecimal balance;
	private String totalNumberOfForeignersWorking;
	
	private BankDetails receipientBankDetails = new BankDetails();
	private List<BankDetails> utilizationBankDetails = new ArrayList<BankDetails>();
	
	
	private List<DocumentDetails> uploadedDocuments = new ArrayList<DocumentDetails>();
	private List<DocumentDetails> deletedDocuments = new ArrayList<DocumentDetails>();
	private List<AffiddavidDocument> affiddavidDocument = new ArrayList<AffiddavidDocument>();
	private List<DocumentDetails> deletedSupportDocuments = new ArrayList<DocumentDetails>();
	private List<DocumentDetails> deletedSupportDocumentsEc = new ArrayList<DocumentDetails>();
	
	public AnnualReturns(Connection connection) {
		super(connection);
	}	
	
	private void retrieveRegistrationDetails() throws Exception {
		String fileNo = null;
		if(tempApplicationId != null && tempApplicationId.equals("") == false)
			fileNo = tempApplicationId;
		else
			fileNo = applicationId;
		
		StringBuffer query = new StringBuffer("SELECT ASSO_NAME, ASSO_ADDRESS, ASSO_TOWN_CITY, SUBSTR(A.STDIST, 1, 2), "
				+ "(SELECT SNAME FROM TM_STATE WHERE SCODE=SUBSTR(A.STDIST, 1, 2)), SUBSTR(A.STDIST, 3), (SELECT DISTNAME FROM TM_DISTRICT WHERE SCODE=SUBSTR(A.STDIST, 1, 2) AND DISTCODE=SUBSTR(A.STDIST, 3)), "
				+ "ASSO_PIN, ASSO_NATURE, "
				+ "(SELECT NATURE_DESC FROM TM_NATURE WHERE NATURE_CODE=ASSO_NATURE), ASSO_RELIGION, "
				+ "(SELECT RELIGION_DESC FROM TM_RELIGION WHERE RELIGION_CODE=ASSO_RELIGION), A.RCN, TO_CHAR(A.REG_DATE, 'dd-mm-yyyy'), "
				+ "TO_CHAR(FINAL_SUBMISSION_DATE, 'dd-mm-yyyy') "
				+ "FROM FC_INDIA A, FC_FC3_TALLY C WHERE A.RCN=C.RCN AND C.UNIQUE_FILENO=?");
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
	
	private void retrieveRegisteredRecipientBankDetails() throws Exception {
		String fileNo = null;
		if(tempApplicationId != null && tempApplicationId.equals("") == false)
			fileNo = tempApplicationId;
		else
			fileNo = applicationId;
		
		StringBuffer query = new StringBuffer("SELECT ACCOUNT_NO, BANK_NAME, (SELECT B.BANK_NAME FROM TM_BANKS B WHERE to_char(BANK_CODE)=A.BANK_NAME), "
				+ "BANK_ADDRESS, BANK_TOWN_CITY, SUBSTR(BANK_STDIST, 1, 2), (SELECT SNAME FROM TM_STATE WHERE SCODE=SUBSTR(BANK_STDIST, 1, 2)), SUBSTR(BANK_STDIST, 3), "
				+ "(SELECT DISTNAME FROM TM_DISTRICT WHERE SCODE=SUBSTR(BANK_STDIST, 1, 2) AND DISTCODE=SUBSTR(BANK_STDIST, 3)), "
				+ "BANK_PIN "
				+ "FROM FC_BANK A, FC_FC3_TALLY C WHERE A.RCN=C.RCN AND C.UNIQUE_FILENO=?");
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
			//bankDetails.setIfscCode(rs.getString(i++));
			associationDetails.setReceipientBankDetails(bankDetails);
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
		
		StringBuffer query = new StringBuffer("SELECT ACCOUNT_NO, BANK_NAME, (SELECT B.BANK_NAME FROM TM_BANKS B WHERE to_char(BANK_CODE)=A.BANK_NAME), "
				+ "BANK_ADDRESS, BANK_TOWN_CITY, BAK_STATE, (SELECT SNAME FROM TM_STATE WHERE SCODE=BAK_STATE), BANK_DISTRICT, "
				+ "(SELECT DISTNAME FROM TM_DISTRICT WHERE SCODE=BAK_STATE AND DISTCODE=BANK_DISTRICT), "
				+ "BANK_PIN, BANK_IFSC_CODE "
				+ "FROM FC_FC3_RECEIPT_BANK A, FC_FC3_TALLY C WHERE A.RCN=C.RCN AND A.BLKYEAR=C.BLKYEAR AND C.UNIQUE_FILENO=?");
		PreparedStatement statement = connection.prepareStatement(query.toString());
		statement.setString(1, applicationId);
		ResultSet rs = statement.executeQuery();
		if(rs.next()) {
			int i=1;
			// Bank details
			receipientBankDetails.setAccountNumber(rs.getString(i++));
			receipientBankDetails.setBankId(rs.getString(i++));
			receipientBankDetails.setBankName(rs.getString(i++));
			receipientBankDetails.setAddress(rs.getString(i++));
			receipientBankDetails.setTown(rs.getString(i++));
			receipientBankDetails.setState(rs.getString(i++));
			receipientBankDetails.setStateDesc(rs.getString(i++));
			receipientBankDetails.setDistrict(rs.getString(i++));
			receipientBankDetails.setDistrictDesc(rs.getString(i++));
			receipientBankDetails.setPincode(rs.getString(i++));
			receipientBankDetails.setIfscCode(rs.getString(i++));
		}
		rs.close();
		statement.close();
	}

	private void retrieveUtilizationBankDetails() throws Exception {
		String fileNo = null;
		if(tempApplicationId != null && tempApplicationId.equals("") == false)
			fileNo = tempApplicationId;
		else
			fileNo = applicationId;
		
		utilizationBankDetails.clear();
		StringBuffer query = new StringBuffer("SELECT ACCOUNT_NO, BANK_NAME, (SELECT B.BANK_NAME FROM TM_BANKS B WHERE to_char(BANK_CODE)=A.BANK_NAME), "
				+ "BANK_ADDRESS, BANK_TOWN_CITY, BAK_STATE, (SELECT SNAME FROM TM_STATE WHERE SCODE=BAK_STATE), BANK_DISTRICT, "
				+ "(SELECT DISTNAME FROM TM_DISTRICT WHERE SCODE=BAK_STATE AND DISTCODE=BANK_DISTRICT), "
				+ "BANK_PIN, BANK_IFSC_CODE "
				+ "FROM FC_FC3_UTILIZATION_BANK A, FC_FC3_TALLY C WHERE A.RCN=C.RCN AND A.BLKYEAR=C.BLKYEAR AND C.UNIQUE_FILENO=?");
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
			utilizationBankDetails.add(bankDetails);
		}
		rs.close();
		statement.close();
	}

	private void retrieveUtilizationDetails() throws Exception {
		StringBuffer query = new StringBuffer("SELECT TOTAL_UTILISATION_PROJECTS, TOTAL_ADMIN_EXPENSES, TOTAL_INVEST_TERMDEPOSIT, "
				+ "TOTAL_PURCHASE_ASSET, TOTAL_UTILISATION, BALANCE_UN_UTILZED, TOTAL_FOREIGNERS_WORKING "
				+ "FROM FC_FC3_UTILIZATION A, FC_FC3_TALLY C WHERE A.RCN=C.RCN AND A.BLKYEAR=C.BLKYEAR AND C.UNIQUE_FILENO=?");
		PreparedStatement statement = connection.prepareStatement(query.toString());
		statement.setString(1, applicationId);
		ResultSet rs = statement.executeQuery();
		if(rs.next()) {
			int i=1;
			String temp  = rs.getString(i++);
			if(temp != null && temp.equals("") == false)
				totalUtilizationForProjects = new BigDecimal(temp);
			temp  = rs.getString(i++);
			if(temp != null && temp.equals("") == false)
				totalAdministrativeExpenses = new BigDecimal(temp);
			temp  = rs.getString(i++);
			if(temp != null && temp.equals("") == false)
				totalInvestedInTermDeposits = new BigDecimal(temp);
			temp  = rs.getString(i++);
			if(temp != null && temp.equals("") == false)
				totalPurchase = new BigDecimal(temp);
			temp  = rs.getString(i++);
			if(temp != null && temp.equals("") == false)
				totalUtilization = new BigDecimal(temp);
			temp  = rs.getString(i++);
			if(temp != null && temp.equals("") == false)
				balance = new BigDecimal(temp);
			totalNumberOfForeignersWorking  = rs.getString(i++);
		}
		rs.close();
		statement.close();
	}
	/*
	private void retrieveDonorDetails() throws Exception {
		String fileNo = null;
		if(tempApplicationId != null && tempApplicationId.equals("") == false)
			fileNo = tempApplicationId;
		else
			fileNo = applicationId;
		
		List<DonorDetails> donorList = associationDetails.getDonorDetails();
		donorList.clear();
		StringBuffer query = new StringBuffer("SELECT NATUREANDVALUE_YES_NO, "
				+ "(SELECT CONTRIBUTION_TYPE_DESC FROM TM_NATURE_OF_CONTRIBUTION WHERE CONTRIBUTION_TYPE=NATUREANDVALUE_YES_NO), "
				+ "NATUREANDVALUE_VALUE, NATUREANDVALUE_CURRENCY, "
				+ "(SELECT CURR_NAME FROM TM_CURRENCY WHERE CURR_CODE=NATUREANDVALUE_CURRENCY), "
				+ "NATUREANDVALUE_CURRENCY_OTHER, OTHER_ACTIVITY, PURPOSE_FOREIGNCONT, "
				+ "(SELECT PURPOSE_NAME FROM TM_AMOUNT_PURPOSE WHERE PURPOSE_CODE=PURPOSE_FOREIGNCONT), "
				+ "IND_DONOR_YES_NO, (SELECT DONOR_TNAME FROM TM_DONOR_TYPE WHERE DONOR_ID=IND_DONOR_YES_NO), IND_DONOR_NAME, "
				+ "IND_DONOR_PERMANENTADDRESS, IND_DONOR_COUNTRY, (SELECT CTR_NAME FROM TM_COUNTRY WHERE CTR_CODE=IND_DONOR_COUNTRY), "
				+ "IND_DONOR_EMAIL, IND_FATHER_NAME, IND_DONOR_NATIONALITY, (SELECT CTR_NAME FROM TM_COUNTRY WHERE CTR_CODE=IND_DONOR_NATIONALITY), "
				+ "IND_PASSPORT_NO, IND_DONOR_PROFESSION, (SELECT OCC_NAME FROM TM_OCCUPATION WHERE OCC_CODE=IND_DONOR_PROFESSION), "
				+ "IND_DONOR_PROFESSION_OTHER "
				+ "FROM FC_FC1A_DONOR WHERE a.FILENO=?");
		PreparedStatement statement = connection.prepareStatement(query.toString());
		statement.setString(1, fileNo);
		ResultSet rs = statement.executeQuery();
		if(rs.next()) {
			int i=1;
			DonorDetails donor = new DonorDetails();
			
			natureOfContribution = rs.getString(i++);
			natureOfContributionDesc = rs.getString(i++);
			String temp = rs.getString(i++);
			if(temp != null && temp.equals("") == false)
				amount = new BigDecimal(temp); 
			
			currency = rs.getString(i++);
			currencyDesc = rs.getString(i++);
			currencyOther = rs.getString(i++);
			projectDetails = rs.getString(i++);
			purpose = rs.getString(i++);
			purposeDesc = rs.getString(i++);;

			donor.setDonorType(rs.getString(i++));
			donor.setDonorTypeDesc(rs.getString(i++));
			donor.setDonorName(rs.getString(i++));
			donor.setOfficeAddress(rs.getString(i++));
			donor.setDonorCountry(rs.getString(i++));
			donor.setDonorCountryDesc(rs.getString(i++));
			donor.setEmail(rs.getString(i++));
			
			donor.setNameOfFatherSpouse(rs.getString(i++));
			donor.setNationality(rs.getString(i++));
			donor.setNationalityDesc(rs.getString(i++));
			//nationalityOther
			donor.setPassportNumber(rs.getString(i++));
			donor.setOccupation(rs.getString(i++));
			donor.setOccupationDesc(rs.getString(i++));
			donor.setOccupationOther(rs.getString(i++));

			donorList.add(donor);
		}		
		rs.close();
		statement.close();				
	}
	*/

	private void retrieveAnnualReturnDetails() throws Exception {
		String fileNo = null;
		if(tempApplicationId != null && tempApplicationId.equals("") == false)
			fileNo = tempApplicationId;
		else
			fileNo = applicationId;
		
		List<DonorDetails> donorList = associationDetails.getDonorDetails();
		donorList.clear();
		StringBuffer query = new StringBuffer("SELECT C.BLKYEAR, BROUGHT_FWD_BEGINIG, BK_INT, SOURCE_FOR_AMT, SOURCE_LOCAL_AMT, TOTAL_AMOUNT "
				+ "FROM FC_FC3_PART1_NEW A, FC_FC3_TALLY C WHERE A.RCN=C.RCN AND A.BLKYEAR=C.BLKYEAR AND C.UNIQUE_FILENO=?");
		PreparedStatement statement = connection.prepareStatement(query.toString());
		statement.setString(1, applicationId);
		ResultSet rs = statement.executeQuery();
		if(rs.next()) {
			int i=1;
			String temp = null;
			
			blockYear = rs.getString(i++);
			temp  = rs.getString(i++);
			if(temp != null && temp.equals("") == false)
				amountBroughtForward = new BigDecimal(temp);
			temp  = rs.getString(i++);
			if(temp != null && temp.equals("") == false)
				otherReceipt = new BigDecimal(temp);
			temp  = rs.getString(i++);
			if(temp != null && temp.equals("") == false)
				foreignContributionDirect = new BigDecimal(temp);
			temp  = rs.getString(i++);
			if(temp != null && temp.equals("") == false)
				foreignContributionTransfer = new BigDecimal(temp);
			temp  = rs.getString(i++);
			if(temp != null && temp.equals("") == false)
				totalForeignContribution = new BigDecimal(temp);
		}
		rs.close();
		statement.close();		
	}
	
	private void retrievePurposeWiseDetails() throws Exception {
		String fileNo = null;
		if(tempApplicationId != null && tempApplicationId.equals("") == false)
			fileNo = tempApplicationId;
		else
			fileNo = applicationId;
		
		
		purposeWiseContribution.clear();
		StringBuffer query = new StringBuffer("SELECT PURPOSE_CODE, (SELECT PURPOSE_NAME FROM TM_AMOUNT_PURPOSE B WHERE B.PURPOSE_CODE=A.PURPOSE_CODE), AMOUNT_RECEIVED "
				+ "FROM FC_FC3_PURPOSE_WISE A, FC_FC3_TALLY C WHERE A.RCN=C.RCN AND A.BLKYEAR=C.BLKYEAR AND C.UNIQUE_FILENO=?");
		PreparedStatement statement = connection.prepareStatement(query.toString());
		statement.setString(1, applicationId);
		ResultSet rs = statement.executeQuery();
		while(rs.next()) {
			int i=1;
			String temp = null;
			PurposeWiseContribution contribution = new PurposeWiseContribution();
			contribution.setPurpose(rs.getString(i++));
			contribution.setPurposeDesc(rs.getString(i++));
			temp  = rs.getString(i++);
			if(temp != null && temp.equals("") == false)
				contribution.setAmount(new BigDecimal(temp));
			purposeWiseContribution.add(contribution);
		}
		rs.close();
		statement.close();				
	}
	
	private void retrieveDonorWiseDetails() throws Exception {
		String fileNo = null;
		if(tempApplicationId != null && tempApplicationId.equals("") == false)
			fileNo = tempApplicationId;
		else
			fileNo = applicationId;
		
		
		donorWiseContribution.clear();
		StringBuffer query = new StringBuffer("SELECT DONOR_TYPE, (SELECT DONOR_TNAME FROM TM_DONOR_TYPE B WHERE B.DONOR_ID=D.DONOR_TYPE), "
				+ "DONOR_NAME, DONOR_ADDRESS, DONOR_COUNTRY, (SELECT CTR_NAME FROM TM_COUNTRY WHERE CTR_CODE=DONOR_COUNTRY), "
				+ "DONOR_EMAILID, DONOR_WEBSITE, "
				+ "PCODE, (SELECT PURPOSE_NAME FROM TM_AMOUNT_PURPOSE B WHERE B.PURPOSE_CODE=A.PCODE), AMOUNT "
				+ "FROM FC_FC3_DONOR_WISE A, FC_FC3_TALLY C, FC_FC3_DONOR D WHERE A.RCN=C.RCN AND A.BLKYEAR=C.BLKYEAR AND A.DONOR_CODE=D.DONOR_CODE AND C.UNIQUE_FILENO=?");
		PreparedStatement statement = connection.prepareStatement(query.toString());
		statement.setString(1, applicationId);
		ResultSet rs = statement.executeQuery();
		while(rs.next()) {
			int i=1;
			String temp = null;
			DonorWiseContribution contribution = new DonorWiseContribution();
			DonorDetails donor = new DonorDetails();
			donor.setDonorType(rs.getString(i++));
			donor.setDonorTypeDesc(rs.getString(i++));
			donor.setDonorName(rs.getString(i++));
			donor.setOfficeAddress(rs.getString(i++));
			donor.setDonorCountry(rs.getString(i++));
			donor.setDonorCountryDesc(rs.getString(i++));
			donor.setEmail(rs.getString(i++));
			donor.setWebsite(rs.getString(i++));
			contribution.setDonor(donor);
			contribution.setPurpose(rs.getString(i++));
			contribution.setPurposeDesc(rs.getString(i++));
			temp  = rs.getString(i++);
			if(temp != null && temp.equals("") == false)
				contribution.setAmount(new BigDecimal(temp));
			donorWiseContribution.add(contribution);
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
				+ "FROM FC_FC3_UPLOADED_DOC A, FC_FC3_TALLY C WHERE A.RCN=C.RCN AND A.BLKYR=C.BLKYEAR AND C.UNIQUE_FILENO=?");
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
					+ "FROM FC_FC3_CHECKLIST WHERE CHK_CODE=? AND CHK_CODE NOT IN ('05', '06')");
			statement = connection.prepareStatement(query.toString());
			
			for(int i=0; i<uploadedList.length(); i+=2) {
				String docId = uploadedList.substring(i, i+2);
				statement.setString(1, docId);
				rs = statement.executeQuery();
				String docName = null;
				String docDetails = null;
				String docPath = documentRoot.concat("/").concat(creationDate.substring(6, 10)).concat("/").concat(creationDate.substring(3, 5)).concat("/").concat(creationDate.substring(0, 2)).concat("/04/").concat(documentId).concat("/");
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
		StringBuffer query = new StringBuffer("SELECT CHK_CODE, CHK_NAME, CHK_NAME "
					+ "FROM FC_FC3_CHECKLIST WHERE CHK_CODE NOT IN ('05', '06') AND CHK_STATUS='Y'");
		PreparedStatement statement = connection.prepareStatement(query.toString());
		ResultSet rs = statement.executeQuery();
		String docName = null;
		String docDetails = null;
		String docId = null;

		deletedDocuments.clear();
		String otherDocId="07";
		String service = "/04/";
		String dirPath = docUploadFolder.concat(creationDate.substring(6, 10)).concat("/").concat(creationDate.substring(3, 5)).concat("/").concat(creationDate.substring(0, 2)).concat(service).concat(documentId).concat("/");
		File deletedDir = new File(dirPath.concat(deletedDocFolder));
		String deletedDocumentRoot = deletedDocRoot.concat("/").concat(creationDate.substring(6, 10)).concat("/").concat(creationDate.substring(3, 5)).concat("/").concat(creationDate.substring(0, 2)).concat(service).concat(documentId).concat("/");
		while(rs.next()) {
			int j = 1;
			String docPath = documentRoot.concat("/").concat(creationDate.substring(6, 10)).concat("/").concat(creationDate.substring(3, 5)).concat("/").concat(creationDate.substring(0, 2)).concat("/04/").concat(documentId).concat("/");
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
		String service = "/04/";
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
		String service = "/04/";
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
		retrieveRegisteredRecipientBankDetails();
		retrieveRecipientBankDetails();
		retrieveUtilizationBankDetails();
		retrieveAnnualReturnDetails();
		retrievePurposeWiseDetails();
		retrieveDonorWiseDetails();
		retrieveUtilizationDetails();
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

	public String getBlockYear() {
		return blockYear;
	}

	public void setBlockYear(String blockYear) {
		this.blockYear = blockYear;
	}

	public BigDecimal getAmountBroughtForward() {
		return amountBroughtForward;
	}

	public void setAmountBroughtForward(BigDecimal amountBroughtForward) {
		this.amountBroughtForward = amountBroughtForward;
	}

	public BigDecimal getOtherReceipt() {
		return otherReceipt;
	}

	public void setOtherReceipt(BigDecimal otherReceipt) {
		this.otherReceipt = otherReceipt;
	}

	public BigDecimal getForeignContributionDirect() {
		return foreignContributionDirect;
	}

	public void setForeignContributionDirect(BigDecimal foreignContributionDirect) {
		this.foreignContributionDirect = foreignContributionDirect;
	}

	public BigDecimal getForeignContributionTransfer() {
		return foreignContributionTransfer;
	}

	public void setForeignContributionTransfer(
			BigDecimal foreignContributionTransfer) {
		this.foreignContributionTransfer = foreignContributionTransfer;
	}

	public BigDecimal getTotalForeignContribution() {
		return totalForeignContribution;
	}

	public void setTotalForeignContribution(BigDecimal totalForeignContribution) {
		this.totalForeignContribution = totalForeignContribution;
	}

	public List<PurposeWiseContribution> getPurposeWiseContribution() {
		return purposeWiseContribution;
	}

	public void setPurposeWiseContribution(
			List<PurposeWiseContribution> purposeWiseContribution) {
		this.purposeWiseContribution = purposeWiseContribution;
	}

	public List<DonorWiseContribution> getDonorWiseContribution() {
		return donorWiseContribution;
	}

	public void setDonorWiseContribution(
			List<DonorWiseContribution> donorWiseContribution) {
		this.donorWiseContribution = donorWiseContribution;
	}

	public BigDecimal getTotalUtilizationForProjects() {
		return totalUtilizationForProjects;
	}

	public void setTotalUtilizationForProjects(
			BigDecimal totalUtilizationForProjects) {
		this.totalUtilizationForProjects = totalUtilizationForProjects;
	}

	public BigDecimal getTotalAdministrativeExpenses() {
		return totalAdministrativeExpenses;
	}

	public void setTotalAdministrativeExpenses(
			BigDecimal totalAdministrativeExpenses) {
		this.totalAdministrativeExpenses = totalAdministrativeExpenses;
	}

	public BigDecimal getTotalInvestedInTermDeposits() {
		return totalInvestedInTermDeposits;
	}

	public void setTotalInvestedInTermDeposits(
			BigDecimal totalInvestedInTermDeposits) {
		this.totalInvestedInTermDeposits = totalInvestedInTermDeposits;
	}

	public BigDecimal getTotalPurchase() {
		return totalPurchase;
	}

	public void setTotalPurchase(BigDecimal totalPurchase) {
		this.totalPurchase = totalPurchase;
	}

	public BigDecimal getTotalUtilization() {
		return totalUtilization;
	}

	public void setTotalUtilization(BigDecimal totalUtilization) {
		this.totalUtilization = totalUtilization;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	public String getTotalNumberOfForeignersWorking() {
		return totalNumberOfForeignersWorking;
	}

	public void setTotalNumberOfForeignersWorking(
			String totalNumberOfForeignersWorking) {
		this.totalNumberOfForeignersWorking = totalNumberOfForeignersWorking;
	}

	public BankDetails getReceipientBankDetails() {
		return receipientBankDetails;
	}

	public void setReceipientBankDetails(BankDetails receipientBankDetails) {
		this.receipientBankDetails = receipientBankDetails;
	}

	public List<BankDetails> getUtilizationBankDetails() {
		return utilizationBankDetails;
	}

	public void setUtilizationBankDetails(List<BankDetails> utilizationBankDetails) {
		this.utilizationBankDetails = utilizationBankDetails;
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
