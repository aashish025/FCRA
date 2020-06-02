package models.master;

public class UserAudit {
	private String userName;
	private String roleName;
	private String roleId;
	private String timeStamp;
	private String createdIp;
	private String flag;
	
	/** default constructor */
	public UserAudit() {
	}
	
	/** full constructor */
	public UserAudit(String userName, String roleName, String timeStamp, String createdIp, String flag) {
		this.userName = userName;
		this.roleName = roleName;
		this.timeStamp = timeStamp;
		this.createdIp = createdIp;
		this.flag = flag;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}
	
	public String getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}

	public String getCreatedIp() {
		return createdIp;
	}

	public void setCreatedIp(String createdIp) {
		this.createdIp = createdIp;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}
	
	
	

}
