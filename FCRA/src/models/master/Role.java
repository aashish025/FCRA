package models.master;

import java.util.Date;

public class Role{
	// Fields
	private Short roleId;
	private String roleName;
	private String assignedUserLevels;
	private Short displayOrder;
	private String recordStatus;
	private String createdBy;
	private String createdIp;
	private String createdDate;
	private String rowIdentifier;
	private String lastModifiedIp;
	private Integer lastModifiedDate;
	private String lastModifiedBy;
    private String assignedRoles;
    String subMenuId;
	// Constructors

	/** default constructor */
	public Role() {
	}
	
	/** minimest constructor */
	public Role(Short roleId) {
		this.roleId = roleId;
	}

	public Role(Short roleId, String roleName,String createdDate) {
		this.roleId = roleId;
		this.roleName = roleName;
		this.createdDate = createdDate;
	}
		
	
	/** minimal constructor */
	public Role(Short roleId, String roleName, String recordStatus, String createdBy,
			String createdIp, String createdDate) {
		this.roleId = roleId;
		this.roleName = roleName;
		this.recordStatus = recordStatus;
		this.createdBy = createdBy;
		this.createdIp = createdIp;
		this.createdDate = createdDate;
	}

	/** full constructor */
	public Role(Short roleId, String roleName, String assignedUserLevels, Short displayOrder, String recordStatus,
			String createdBy, String createdIp, String createdDate) {
		this.roleId = roleId;
		this.roleName = roleName;
		this.assignedUserLevels = assignedUserLevels;
		this.displayOrder = displayOrder;
		this.recordStatus = recordStatus;
		this.createdBy = createdBy;
		this.createdIp = createdIp;
		this.createdDate = createdDate;
	}


	public Short getRoleId() {
		return this.roleId;
	}

	public void setRoleId(Short roleId) {
		this.roleId = roleId;
	}


	public String getRoleName() {
		return this.roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}


	public String getAssignedUserLevels() {
		return this.assignedUserLevels;
	}

	public void setAssignedUserLevels(String assignedUserLevels) {
		this.assignedUserLevels = assignedUserLevels;
	}


	public Short getDisplayOrder() {
		return this.displayOrder;
	}

	public void setDisplayOrder(Short displayOrder) {
		this.displayOrder = displayOrder;
	}


	public String getRecordStatus() {
		return this.recordStatus;
	}

	public void setRecordStatus(String recordStatus) {
		this.recordStatus = recordStatus;
	}


	public String getCreatedBy() {
		return this.createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}


	public String getCreatedIp() {
		return this.createdIp;
	}

	public void setCreatedIp(String createdIp) {
		this.createdIp = createdIp;
	}

	public String getCreatedDate() {
		return this.createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	
	public String getRowIdentifier() {
		return rowIdentifier;
	}

	public void setRowIdentifier(String rowIdentifier) {
		this.rowIdentifier = rowIdentifier;
	}

	public String getLastModifiedIp() {
		return lastModifiedIp;
	}

	public void setLastModifiedIp(String lastModifiedIp) {
		this.lastModifiedIp = lastModifiedIp;
	}

	public Integer getLastModifiedDate() {
		return lastModifiedDate;
	}

	public void setLastModifiedDate(Integer lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	public String getAssignedRoles() {
		return assignedRoles;
	}

	public void setAssignedRoles(String assignedRoles) {
		this.assignedRoles = assignedRoles;
	}

	public String getSubMenuId() {
		return subMenuId;
	}

	public void setSubMenuId(String subMenuId) {
		this.subMenuId = subMenuId;
	}

	public String getLastModifiedBy() {
		return lastModifiedBy;
	}

	public void setLastModifiedBy(String lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}


	
}