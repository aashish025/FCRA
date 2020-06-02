package models.master;

import java.util.Date;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;



public class RoleMenu implements java.io.Serializable {
	// Fields
	private RoleMenuId id;
	private Role role;
	private SubMenu subMenu;
	private Short displayOrder;
	private Boolean recordStatus;
	private String createdBy;
	private String createdIp;
	private Date createdDate;

	// Constructors
	/** default constructor */
	public RoleMenu() {
	}

	/** minimal constructor */
	public RoleMenu(RoleMenuId id, Role role,
			SubMenu subMenu, Boolean recordStatus,
			String createdBy, String createdIp, Date createdDate) {
		this.id = id;
		this.role = role;
		this.subMenu = subMenu;
		this.recordStatus = recordStatus;
		this.createdBy = createdBy;
		this.createdIp = createdIp;
		this.createdDate = createdDate;
	}

	/** full constructor */
	public RoleMenu(RoleMenuId id, Role role,
			SubMenu subMenu, Short displayOrder,
			Boolean recordStatus, String createdBy, String createdIp,
			Date createdDate) {
		this.id = id;
		this.role = role;
		this.subMenu = subMenu;
		this.displayOrder = displayOrder;
		this.recordStatus = recordStatus;
		this.createdBy = createdBy;
		this.createdIp = createdIp;
		this.createdDate = createdDate;
	}

	
	public RoleMenuId getId() {
		return this.id;
	}

	public void setId(RoleMenuId id) {
		this.id = id;
	}

	
	public Role getRole() {
		return this.role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	
	public SubMenu getSubMenu() {
		return this.subMenu;
	}

	public void setSubMenu(SubMenu subMenu) {
		this.subMenu = subMenu;
	}

	
	public Short getDisplayOrder() {
		return this.displayOrder;
	}

	public void setDisplayOrder(Short displayOrder) {
		this.displayOrder = displayOrder;
	}

	
	public Boolean getRecordStatus() {
		return this.recordStatus;
	}

	public void setRecordStatus(Boolean recordStatus) {
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

	
	public Date getCreatedDate() {
		return this.createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

}