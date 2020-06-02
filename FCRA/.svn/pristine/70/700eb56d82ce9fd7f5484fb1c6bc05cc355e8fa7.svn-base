package models.master;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

public class User {

	// Fields

	private String userid;
	private Designation designation;
	private String designationId;
	private String designationName;
	private UserStatus userStatus;
	private String statusId;
	private String emailId;
	private String statusName;
	private String officeCode;
	private Office office;
	private String genderCode;
	private String genderDesc;
	private Gender gender;
	private String userpwd;
	private Integer userLevel;
	private String userName;
	private Date pwdDate;
	private String passwd1;
	private Date pwd1Date;
	private String passwd2;
	private Date pwd2Date;
	private Boolean loginFlag;
	private String activeFlag;
	private Date activeDate;
	private String actionId;
	private Date actionDate;
	private String actionRemark;
	private String actionBy;
	private String actionIp;
	private String createdBy;
	private String createdIp;
	private Date createdDate;
	private Short wrongPwdCount;
	private String rowIdentifier;
    private String sectioncode; 
	// Constructors

	/** default constructor */
	public User() {
	}
	public User(String userid, String userName) {
		this.userid = userid;
		this.userName = userName;
	}
	/** minimal constructor */
	public User(String userid, Designation designation,
			UserStatus userStatus, Office office,
			Gender gender, String userpwd, Integer userLevel,
			String userName, Date pwdDate, Boolean loginFlag,
			String activeFlag, String createdBy, String createdIp,
			Date createdDate, Short wrongPwdCount) {
		this.userid = userid;
		this.designation = designation;
		this.userStatus = userStatus;
		this.office = office;
		this.gender = gender;
		this.userpwd = userpwd;
		this.userLevel = userLevel;
		this.userName = userName;
		this.pwdDate = pwdDate;
		this.loginFlag = loginFlag;
		this.activeFlag = activeFlag;
		this.createdBy = createdBy;
		this.createdIp = createdIp;
		this.createdDate = createdDate;
		this.wrongPwdCount = wrongPwdCount;
	}
	
	

	public User(String userid,String userName,String emailId,String genderCode,String genderDesc,String designationId,String designationName, 
			String statusName) {		
		this.userid = userid;
		this.userName = userName;
		this.emailId = emailId;
		this.designationName = designationName;
		this.designationId = designationId;
		this.statusName = statusName;	
		this.genderCode = genderCode;
		this.genderDesc = genderDesc;
		
	}
	
	
	
	public User(String userName, String emailId,
			 String genderCode, String designationId) {		
		this.designationId = designationId;
		this.userName = userName;		
		this.genderCode = genderCode;
		this.emailId = emailId;
	}
	
	
	/** full constructor */
	public User(String userid, Designation designation,
			UserStatus userStatus, Office office, Gender gender,
			String userpwd, Integer userLevel, String userName, Date pwdDate,
			String passwd1, Date pwd1Date, String passwd2, Date pwd2Date,
			Boolean loginFlag, String activeFlag, Date activeDate,
			String actionId,Date actionDate, String actionRemark, String actionBy,
			String actionIp, String createdBy, String createdIp,
			Date createdDate, Short wrongPwdCount,String rowIdentifier) {
		super();
		this.userid = userid;
		this.designation = designation;
		this.userStatus = userStatus;
		this.office = office;
		this.gender = gender;
		this.userpwd = userpwd;
		this.userLevel = userLevel;
		this.userName = userName;
		this.pwdDate = pwdDate;
		this.passwd1 = passwd1;
		this.pwd1Date = pwd1Date;
		this.passwd2 = passwd2;
		this.pwd2Date = pwd2Date;
		this.loginFlag = loginFlag;
		this.activeFlag = activeFlag;
		this.activeDate = activeDate;
		this.actionId=actionId;
		this.actionDate = actionDate;
		this.actionRemark = actionRemark;
		this.actionBy = actionBy;
		this.actionIp = actionIp;
		this.createdBy = createdBy;
		this.createdIp = createdIp;
		this.createdDate = createdDate;
		this.wrongPwdCount = wrongPwdCount;
		this.rowIdentifier=rowIdentifier;
	}
	// Property accessors
	@Id
	@Column(name = "USERID", unique = true, nullable = false, length = 10)
	public String getUserid() {
		return this.userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "DESIGNATION_ID", nullable = false)
	public Designation getDesignation() {
		return this.designation;
	}

	public void setDesignation(Designation designation) {
		this.designation = designation;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ACTION_ID", nullable = false)
	public UserStatus getUserStatus() {
		return this.userStatus;
	}

	public void setUserStatus(UserStatus userStatus) {
		this.userStatus = userStatus;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ADMIN_UID", nullable = false)
	public Office getOffice() {
		return this.office;
	}

	public void setOffice(Office office) {
		this.office = office;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "GENDER_CODE")
	public Gender getGender() {
		return this.gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

	@Column(name = "USERPWD", nullable = false, length = 40)
	public String getUserpwd() {
		return this.userpwd;
	}

	public void setUserpwd(String userpwd) {
		this.userpwd = userpwd;
	}

	

	@Column(name = "USER_NAME", nullable = false, length = 50)
	public String getUserName() {
		return this.userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "PWD_DATE", nullable = false, length = 7)
	public Date getPwdDate() {
		return this.pwdDate;
	}

	public void setPwdDate(Date pwdDate) {
		this.pwdDate = pwdDate;
	}

	@Column(name = "PASSWD1", length = 40)
	public String getPasswd1() {
		return this.passwd1;
	}

	public void setPasswd1(String passwd1) {
		this.passwd1 = passwd1;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "PWD1_DATE", length = 7)
	public Date getPwd1Date() {
		return this.pwd1Date;
	}

	public void setPwd1Date(Date pwd1Date) {
		this.pwd1Date = pwd1Date;
	}

	@Column(name = "PASSWD2", length = 40)
	public String getPasswd2() {
		return this.passwd2;
	}

	public void setPasswd2(String passwd2) {
		this.passwd2 = passwd2;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "PWD2_DATE", length = 7)
	public Date getPwd2Date() {
		return this.pwd2Date;
	}

	public void setPwd2Date(Date pwd2Date) {
		this.pwd2Date = pwd2Date;
	}

	@Column(name = "LOGIN_FLAG", nullable = false, precision = 1, scale = 0)
	public Boolean getLoginFlag() {
		return this.loginFlag;
	}

	public void setLoginFlag(Boolean loginFlag) {
		this.loginFlag = loginFlag;
	}

	@Column(name = "ACTIVE_FLAG", nullable = false, length = 1)
	public String getActiveFlag() {
		return this.activeFlag;
	}

	public void setActiveFlag(String activeFlag) {
		this.activeFlag = activeFlag;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "ACTIVE_DATE", length = 7)
	public Date getActiveDate() {
		return this.activeDate;
	}

	public void setActiveDate(Date activeDate) {
		this.activeDate = activeDate;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "ACTION_DATE", length = 7)
	public Date getActionDate() {
		return this.actionDate;
	}

	public void setActionDate(Date actionDate) {
		this.actionDate = actionDate;
	}

	@Column(name = "ACTION_REMARK", length = 100)
	public String getActionRemark() {
		return this.actionRemark;
	}

	public void setActionRemark(String actionRemark) {
		this.actionRemark = actionRemark;
	}

	@Column(name = "ACTION_BY", length = 10)
	public String getActionBy() {
		return this.actionBy;
	}

	public void setActionBy(String actionBy) {
		this.actionBy = actionBy;
	}

	@Column(name = "ACTION_IP", length = 30)
	public String getActionIp() {
		return this.actionIp;
	}

	public void setActionIp(String actionIp) {
		this.actionIp = actionIp;
	}

	@Column(name = "CREATED_BY", nullable = false, length = 10)
	public String getCreatedBy() {
		return this.createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	@Column(name = "CREATED_IP", nullable = false, length = 30)
	public String getCreatedIp() {
		return this.createdIp;
	}

	public void setCreatedIp(String createdIp) {
		this.createdIp = createdIp;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "CREATED_DATE", nullable = false, length = 7)
	public Date getCreatedDate() {
		return this.createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	@Column(name = "WRONG_PWD_COUNT", nullable = false, precision = 1, scale = 0)
	public Short getWrongPwdCount() {
		return this.wrongPwdCount;
	}

	public void setWrongPwdCount(Short wrongPwdCount) {
		this.wrongPwdCount = wrongPwdCount;
	}
	
	@Column(name = "ROWID", insertable = false, updatable = false)	
	public String getRowIdentifier() {
		return rowIdentifier;
	}

	public void setRowIdentifier(String rowIdentifier) {
		this.rowIdentifier = rowIdentifier;
	}
	
	public String getActionId() {
		return actionId;
	}
	public void setActionId(String actionId) {
		this.actionId = actionId;
	}	
	
	@Override
	public String toString()
	{
		if(designation.getShortDesignationName() == null)
			return userid + " ("+ userName +")";
		else
			return userid + " ("+ userName +") [" + designation.getShortDesignationName() + "]";
	}
	public String getDesignationId() {
		return designationId;
	}
	public void setDesignationId(String designationId) {
		this.designationId = designationId;
	}
	public String getDesignationName() {
		return designationName;
	}
	public void setDesignationName(String designationName) {
		this.designationName = designationName;
	}
	public String getStatusId() {
		return statusId;
	}
	public void setStatusId(String statusId) {
		this.statusId = statusId;
	}
	public String getStatusName() {
		return statusName;
	}
	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}
	public String getGenderCode() {
		return genderCode;
	}
	public void setGenderCode(String genderCode) {
		this.genderCode = genderCode;
	}
	public String getGenderDesc() {
		return genderDesc;
	}
	public void setGenderDesc(String genderDesc) {
		this.genderDesc = genderDesc;
	}
	public String getEmailId() {
		return emailId;
	}
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}
	public String getOfficeCode() {
		return officeCode;
	}
	public void setOfficeCode(String officeCode) {
		this.officeCode = officeCode;
	}
	public void setUserLevel(Integer userLevel) {
		this.userLevel = userLevel;
	}
	public Integer getUserLevel() {
		return userLevel;
	}
	public String getSectioncode() {
		return sectioncode;
	}
	public void setSectioncode(String sectioncode) {
		this.sectioncode = sectioncode;
	}
}