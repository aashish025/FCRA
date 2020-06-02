package models.master;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * GenderMaster entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "T_GENDER_MASTER", schema = "CIPPS")
public class Gender implements java.io.Serializable {

	// Fields

	private String genderCode;
	private String genderDesc;
	private Byte displayOrder;
	private Boolean recordStatus;
	private String createdBy;
	private String createdIp;
	private Date createdDate;
	private String rowIdentifier;

	// Constructors

	/** default constructor */
	public Gender() {
	}
	public Gender(String genderCode, String genderDesc) {
		this.genderCode = genderCode;
		this.genderDesc = genderDesc;
	}
	/** minimal constructor */
	public Gender(String genderCode,Boolean recordStatus, String genderDesc, String createdBy,
			String createdIp, Date createdDate, String rowIdentifier) {
		this.genderCode = genderCode;
		this.genderDesc = genderDesc;
		this.recordStatus = recordStatus;
		this.createdBy = createdBy;
		this.createdIp = createdIp;
		this.createdDate = createdDate;
		this.rowIdentifier = rowIdentifier;
		
	}

	/** full constructor */
	public Gender(String genderCode,Boolean recordStatus, String genderDesc, Byte displayOrder,
			String createdBy, String createdIp, Date createdDate, String rowIdentifier) {
		this.genderCode = genderCode;
		this.genderDesc = genderDesc;
		this.displayOrder = displayOrder;
		this.recordStatus = recordStatus;
		this.createdBy = createdBy;
		this.createdIp = createdIp;
		this.createdDate = createdDate;
		this.rowIdentifier = rowIdentifier;
	}

	public String getGenderCode() {
		return this.genderCode;
	}

	public void setGenderCode(String genderCode) {
		this.genderCode = genderCode;
	}

	public String getGenderDesc() {
		return this.genderDesc;
	}

	public void setGenderDesc(String genderDesc) {
		this.genderDesc = genderDesc;
	}


	public Boolean getRecordStatus() {
		return recordStatus;
	}

	public void setRecordStatus(Boolean recordStatus) {
		this.recordStatus = recordStatus;
	}
	

	public Byte getDisplayOrder() {
		return this.displayOrder;
	}

	public void setDisplayOrder(Byte displayOrder) {
		this.displayOrder = displayOrder;
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
	
	public String getRowIdentifier() {
		return rowIdentifier;
	}

	public void setRowIdentifier(String rowIdentifier) {
		this.rowIdentifier = rowIdentifier;
	}

	public String toString(){
		return genderDesc;
	}
}