package models.services.requests;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.owasp.esapi.ESAPI;





import dao.services.dashboard.ProjectDashboardDao;
import utilities.ValidationException;

public class ProjectRequest extends AbstractRequest {
	private String proposalType;
	private String proposalTypeDesc;
	private String projectProposal;
	private String functionalRequirement;
	private String splReqLocalConditions;
	private String splReqClimaticConditions;
	private String splReqSecurityConsideration;
	private String proposalAsPerNorms;
	private String specialRelaxationReqd;
	private String specialRelaxationDesc;
	private String changeInZoningLaws;
	private String changeInZoningLawStatus;
	private String changeInZoningLawStatusDesc;
	private Float approvedCost;
	private String approvedTimeOfCompletion;	
	private String assoAddress;
	private String assoNature;
	private String assoBank;
	private String assoBankAddress;
	private String lastRenewed;
	private String validUpTo;
	private String chiefFunctionary;
	private String historyStatus;
    private String assocType;
    private String exmpted;
	

	public ProjectRequest(Connection connection) {
		super(connection);
	}	
	
	public  ProjectRequest(Connection connection,String applicationId,String sectionFileNumber,String applicantName,String state,String district,String email,String mobile,
			String chief,int i){
		super(connection);
		this.applicationId=applicationId;
		this.sectionFileNo=sectionFileNumber;
		this.applicantName=applicantName;
		this.state=state;
		this.district=district;
		this.emailId=email;
		this.mobile=mobile;
		this.chiefFunctionary=chief;
	}
	
	public  ProjectRequest(Connection connection,String applicationId,String toOffice,String toOfficeInfo,String serviceId,String serviceName,
			String currentSubStage,String currentSubStageDesc,String submissionDate){
		super(connection);
		this.applicationId=applicationId;
		this.serviceId=serviceId;
		this.serviceName=serviceName;
		this.toOffice=toOffice;
		this.toOfficeInfo=toOfficeInfo;
		this.currentSubStage=currentSubStage;
		this.currentSubStageDesc=currentSubStageDesc;		
		this.submissionDate=submissionDate;
	}
	
	public  ProjectRequest(Connection connection,String applicationId,String serviceId,String serviceName,
			String currentStage,String currentStatus,String currentStageDesc,String submissionDate,String applicantName,String fileNo,String sectionFileNo,String lastAccessedBy,String lastAccessedOn,int i){
		super(connection);
		this.applicationId=applicationId;
		this.serviceId=serviceId;
		this.serviceName=serviceName;
		this.currentStage=currentStage;
		this.currentStatus=currentStatus;
		this.currentStageDesc=currentStageDesc;				
		this.submissionDate=submissionDate;
		this.applicantName=applicantName;
		this.tempFileNo=fileNo;
		this.sectionFileNo=sectionFileNo;
		this.lastAccessedBy=lastAccessedBy;
		this.lastAccessedOn=lastAccessedOn;
	}
	
	public  ProjectRequest(Connection connection,String assoRegnNumber,String applicantName,String sectionFileNo,String assoRegnStatus,String assoRegnStatusDesc
			,String assoAddress,String assoNature,String assoBank,String assoBankAddress,String regDate,String bankAcc,String lastRenewed,String validUpTo){
		super(connection);
		this.sectionFileNo=sectionFileNo;
		this.applicantName=applicantName;
		this.assoRegnNumber=assoRegnNumber;		
		this.assoRegnStatus=assoRegnStatus;
		this.assoRegnStatusDesc=assoRegnStatusDesc;
		this.assoAddress=assoAddress;
		this.assoNature=assoNature;
		this.assoBank=assoBank;
		this.assoBankAddress=assoBankAddress;
		this.assoRegnDate=regDate;
		this.assoAccNumber=bankAcc;
		this.lastRenewed=lastRenewed;
		this.validUpTo=validUpTo;
	}
	
	public  ProjectRequest(Connection connection,String assoRegnNumber,String applicantName,String sectionFileNo,String assoRegnStatus,String assoRegnStatusDesc
			,String assoAddress,String assoNature,String assoBank,String assoBankAddress,String regDate,String bankAcc,String lastRenewed,String validUpTo, String exmpted, String sno){
		super(connection);
		this.sectionFileNo=sectionFileNo;
		this.applicantName=applicantName;
		this.assoRegnNumber=assoRegnNumber;		
		this.assoRegnStatus=assoRegnStatus;
		this.assoRegnStatusDesc=assoRegnStatusDesc;
		this.assoAddress=assoAddress;
		this.assoNature=assoNature;
		this.assoBank=assoBank;
		this.assoBankAddress=assoBankAddress;
		this.assoRegnDate=regDate;
		this.assoAccNumber=bankAcc;
		this.lastRenewed=lastRenewed;
		this.validUpTo=validUpTo;
		this.exmpted=exmpted;
	}
	// change Association Type
	public  ProjectRequest(Connection connection,String assoRegnNumber,String applicantName,String sectionFileNo,String assoRegnStatus,String assoRegnStatusDesc
			,String assoAddress,String assoNature,String assoBank,String assoBankAddress,String regDate,String bankAcc,String lastRenewed,String validUpTo,String assocType,int a){
		super(connection);
		this.sectionFileNo=sectionFileNo;
		this.applicantName=applicantName;
		this.assoRegnNumber=assoRegnNumber;		
		this.assoRegnStatus=assoRegnStatus;
		this.assoRegnStatusDesc=assoRegnStatusDesc;
		this.assoAddress=assoAddress;
		this.assoNature=assoNature;
		this.assoBank=assoBank;
		this.assoBankAddress=assoBankAddress;
		this.assoRegnDate=regDate;
		this.assoAccNumber=bankAcc;
		this.lastRenewed=lastRenewed;
		this.validUpTo=validUpTo;
		this.assocType=assocType;
		
	}
	public ProjectRequest(Connection connection,String assoRegnNumber,String applicantName,String sectionFileNo,String assoRegnStatus,String assoRegnStatusDesc
			,String assoAddress,String assoNature,String assoBank,String assoBankAddress,String regDate,String bankAcc,String lastRenewed,String validUpTo, String history_status) {
		// TODO Auto-generated constructor stub
		super(connection);
		this.sectionFileNo=sectionFileNo;
		this.applicantName=applicantName;
		this.assoRegnNumber=assoRegnNumber;		
		this.assoRegnStatus=assoRegnStatus;
		this.assoRegnStatusDesc=assoRegnStatusDesc;
		this.assoAddress=assoAddress;
		this.assoNature=assoNature;
		this.assoBank=assoBank;
		this.assoBankAddress=assoBankAddress;
		this.assoRegnDate=regDate;
		this.assoAccNumber=bankAcc;
		this.lastRenewed=lastRenewed;
		this.validUpTo=validUpTo;
		this.historyStatus = history_status;
	}


	public  ProjectRequest(Connection connection,String applicationId,String serviceId,String serviceName,
			String currentStage,String currentStatus,String currentStageDesc,String submissionDate,String applicantName,String fileNo,String sectionFileNo,String state,String district){
		super(connection);
		this.applicationId=applicationId;
		this.serviceId=serviceId;
		this.serviceName=serviceName;
		this.currentStage=currentStage;
		this.currentStatus=currentStatus;
		this.currentStageDesc=currentStageDesc;				
		this.submissionDate=submissionDate;
		this.applicantName=applicantName;
		this.tempFileNo=fileNo;
		this.sectionFileNo=sectionFileNo;
		this.state=state;
		this.district=district;
		
	}
	
	@Override
	public void getDetails() throws Exception {
		if(ESAPI.validator().isValidInput("applicationId", applicationId, "Word", 15, false) == false){
			throw new ValidationException("Invalid Application ID");
		}
		
		StringBuffer query = new StringBuffer("SELECT PR.APPLICATION_ID, PR.PROPOSAL_ID, PROPOSAL_DESC, REQD_CHANGE_IN_ZONING_LAW, "
				+ "STATUS_CHANGE_ZONING_LAW_REQ, PROJECT_PROPOSAL, FUNCTIONAL_REQ_OF_PROPOSAL, SPL_REQ_LOCAL_CONDNS, "
				+ "SPL_REQ_LOCAL_CLIMATIC_CONDNS, SPL_REQ_LOCAL_SECURITY_CONDNS, PROPOSAL_AS_PER_NORM_FLAG, "
				+ "SPL_RELAXATION_REQD, SPL_RELAXATION_REQD_DESC,"
				+ "APPROVED_COST_OF_PROJECT, APPROVED_TIME_OF_COMPLETION, AD.OFFICE_CODE "
				+ "FROM T_PROJECT_REQUEST PR, T_APPLICATION_DETAILS AD, TM_PROJECT_PROPOSAL_TYPE PPT "
				+ "WHERE PR.APPLICATION_ID=? AND PR.APPLICATION_ID=AD.APPLICATION_ID "
				+ "AND AD.CURRENT_STATUS!=3 AND PR.PROPOSAL_ID = PPT.PROPOSAL_ID");
		PreparedStatement statement = connection.prepareStatement(query.toString());
		statement.setString(1, applicationId);
		ResultSet rs = statement.executeQuery();
		if(rs.next()) {
			int i=1;
			applicationId = rs.getString(i++);
			proposalType = rs.getString(i++);
			proposalTypeDesc = rs.getString(i++);
			changeInZoningLaws = rs.getString(i++);
			changeInZoningLawStatus = rs.getString(i++);
			projectProposal = rs.getString(i++);
			functionalRequirement = rs.getString(i++);
			splReqLocalConditions = rs.getString(i++);
			splReqClimaticConditions = rs.getString(i++);
			splReqSecurityConsideration = rs.getString(i++);
			proposalAsPerNorms = rs.getString(i++);
			specialRelaxationReqd = rs.getString(i++);
			specialRelaxationDesc = rs.getString(i++);
			String temp = rs.getString(i++);
			if(temp == null || temp.equals(""))
				approvedCost = null;
			else
				approvedCost = Float.parseFloat(temp);
			approvedTimeOfCompletion = rs.getString(i++);
			officeCode = rs.getString(i++);			
			if(changeInZoningLawStatus == null || changeInZoningLawStatus.equals(""))
				changeInZoningLawStatusDesc = null;
			else {
				if(changeInZoningLawStatus.equals("A")) 
					changeInZoningLawStatusDesc = "Applied";
				else if(changeInZoningLawStatus.equals("N"))
					changeInZoningLawStatusDesc = "Not Applied";
				else if(changeInZoningLawStatus.equals("U"))
					changeInZoningLawStatusDesc = "Under Process";
			}				
		}
		else {
			rs.close();
			statement.close();
			throw new ValidationException("Invalid Application ID");
		}
		rs.close();
		statement.close();
	}

	@Override
	public void saveDetails() throws Exception {
		if(ESAPI.validator().isValidInput("applicationId", applicationId, "Word", 15, false) == false){
			throw new ValidationException("Invalid Application ID");
		}
				
		StringBuffer query = new StringBuffer("UPDATE T_PROJECT_REQUEST SET REQD_CHANGE_IN_ZONING_LAW=?, STATUS_CHANGE_ZONING_LAW_REQ=?, "
				+ "PROJECT_PROPOSAL=?, FUNCTIONAL_REQ_OF_PROPOSAL=?, SPL_REQ_LOCAL_CONDNS=?, SPL_REQ_LOCAL_CLIMATIC_CONDNS=?, "
				+ "SPL_REQ_LOCAL_SECURITY_CONDNS=?, PROPOSAL_AS_PER_NORM_FLAG=?, SPL_RELAXATION_REQD=?, SPL_RELAXATION_REQD_DESC=? "
				+ "WHERE APPLICATION_ID=?");
		PreparedStatement statement = connection.prepareStatement(query.toString());
		int i=1;
		statement.setString(i++, changeInZoningLaws);
		statement.setString(i++, changeInZoningLawStatus);
		statement.setString(i++, projectProposal);
		statement.setString(i++, functionalRequirement);
		statement.setString(i++, splReqLocalConditions);
		statement.setString(i++, splReqClimaticConditions);
		statement.setString(i++, splReqSecurityConsideration);
		statement.setString(i++, proposalAsPerNorms);
		statement.setString(i++, specialRelaxationReqd);
		statement.setString(i++, specialRelaxationDesc);
		statement.setString(i++, applicationId);
		
		int rows = statement.executeUpdate();
		statement.close();
	}
	
	@Override
	public void forward() throws Exception {
	}

	public String getProposalType() {
		return proposalType;
	}

	public void setProposalType(String proposalType) {
		this.proposalType = proposalType;
	}

	public String getProposalTypeDesc() {
		return proposalTypeDesc;
	}

	public void setProposalTypeDesc(String proposalTypeDesc) {
		this.proposalTypeDesc = proposalTypeDesc;
	}

	public String getProjectProposal() {
		return projectProposal;
	}

	public void setProjectProposal(String projectProposal) {
		this.projectProposal = projectProposal;
	}

	public String getFunctionalRequirement() {
		return functionalRequirement;
	}

	public void setFunctionalRequirement(String functionalRequirement) {
		this.functionalRequirement = functionalRequirement;
	}

	public String getSplReqLocalConditions() {
		return splReqLocalConditions;
	}

	public void setSplReqLocalConditions(String splReqLocalConditions) {
		this.splReqLocalConditions = splReqLocalConditions;
	}

	public String getSplReqClimaticConditions() {
		return splReqClimaticConditions;
	}

	public void setSplReqClimaticConditions(String splReqClimaticConditions) {
		this.splReqClimaticConditions = splReqClimaticConditions;
	}

	public String getSplReqSecurityConsideration() {
		return splReqSecurityConsideration;
	}

	public void setSplReqSecurityConsideration(String splReqSecurityConsideration) {
		this.splReqSecurityConsideration = splReqSecurityConsideration;
	}

	public String getProposalAsPerNorms() {
		return proposalAsPerNorms;
	}

	public void setProposalAsPerNorms(String proposalAsPerNorms) {
		this.proposalAsPerNorms = proposalAsPerNorms;
	}

	public String getSpecialRelaxationReqd() {
		return specialRelaxationReqd;
	}

	public void setSpecialRelaxationReqd(String specialRelaxationReqd) {
		this.specialRelaxationReqd = specialRelaxationReqd;
	}

	public String getSpecialRelaxationDesc() {
		return specialRelaxationDesc;
	}

	public void setSpecialRelaxationDesc(String specialRelaxationDesc) {
		this.specialRelaxationDesc = specialRelaxationDesc;
	}

	public String getChangeInZoningLaws() {
		return changeInZoningLaws;
	}

	public void setChangeInZoningLaws(String changeInZoningLaws) {
		this.changeInZoningLaws = changeInZoningLaws;
	}

	public String getChangeInZoningLawStatus() {
		return changeInZoningLawStatus;
	}

	public void setChangeInZoningLawStatus(String changeInZoningLawStatus) {
		this.changeInZoningLawStatus = changeInZoningLawStatus;
	}

	public String getChangeInZoningLawStatusDesc() {
		return changeInZoningLawStatusDesc;
	}

	public void setChangeInZoningLawStatusDesc(String changeInZoningLawStatusDesc) {
		this.changeInZoningLawStatusDesc = changeInZoningLawStatusDesc;
	}

	public Float getApprovedCost() {
		return approvedCost;
	}

	public void setApprovedCost(Float approvedCost) {
		this.approvedCost = approvedCost;
	}

	public String getApprovedTimeOfCompletion() {
		return approvedTimeOfCompletion;
	}

	public void setApprovedTimeOfCompletion(String approvedTimeOfCompletion) {
		this.approvedTimeOfCompletion = approvedTimeOfCompletion;
	}

	public String getAssoAddress() {
		return assoAddress;
	}

	public void setAssoAddress(String assoAddress) {
		this.assoAddress = assoAddress;
	}

	public String getAssoNature() {
		return assoNature;
	}

	public void setAssoNature(String assoNature) {
		this.assoNature = assoNature;
	}

	public String getAssoBank() {
		return assoBank;
	}

	public void setAssoBank(String assoBank) {
		this.assoBank = assoBank;
	}

	public String getAssoBankAddress() {
		return assoBankAddress;
	}

	public void setAssoBankAddress(String assoBankAddress) {
		this.assoBankAddress = assoBankAddress;
	}

	public String getLastRenewed() {
		return lastRenewed;
	}

	public void setLastRenewed(String lastRenewed) {
		this.lastRenewed = lastRenewed;
	}

	public String getValidUpTo() {
		return validUpTo;
	}

	public void setValidUpTo(String validUpTo) {
		this.validUpTo = validUpTo;
	}

	public String getChiefFunctionary() {
		return chiefFunctionary;
	}

	public void setChiefFunctionary(String chiefFunctionary) {
		this.chiefFunctionary = chiefFunctionary;
	}

	public String getHistoryStatus() {
		return historyStatus;
	}

	public void setHistoryStatus(String historyStatus) {
		this.historyStatus = historyStatus;
	}

	public String getExmpted() {
		return exmpted;
	}

	public void setExmpted(String exmpted) {
		this.exmpted = exmpted;
	}

	public String getAssocType() {
		return assocType;
	}

	public void setAssocType(String assocType) {
		this.assocType = assocType;
	}

	

	

}
