package models.master;

import javax.persistence.Column;
import javax.persistence.Embeddable;

public class RoleMenuId implements java.io.Serializable {
	// Fields
	private Short roleId;
	private Short smenuId;

	// Constructors
	/** default constructor */
	public RoleMenuId() {
	}

	/** full constructor */
	public RoleMenuId(Short roleId, Short smenuId) {
		this.roleId = roleId;
		this.smenuId = smenuId;
	}

	public Short getRoleId() {
		return this.roleId;
	}

	public void setRoleId(Short roleId) {
		this.roleId = roleId;
	}


	public Short getSmenuId() {
		return this.smenuId;
	}

	public void setSmenuId(Short smenuId) {
		this.smenuId = smenuId;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof RoleMenuId))
			return false;
		RoleMenuId castOther = (RoleMenuId) other;

		return ((this.getRoleId() == castOther.getRoleId()) || (this
				.getRoleId() != null
				&& castOther.getRoleId() != null && this.getRoleId().equals(
				castOther.getRoleId())))
				&& ((this.getSmenuId() == castOther.getSmenuId()) || (this
						.getSmenuId() != null
						&& castOther.getSmenuId() != null && this.getSmenuId()
						.equals(castOther.getSmenuId())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result
				+ (getRoleId() == null ? 0 : this.getRoleId().hashCode());
		result = 37 * result
				+ (getSmenuId() == null ? 0 : this.getSmenuId().hashCode());
		return result;
	}

}