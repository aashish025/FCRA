package models.master;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * DesignationMaster entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "T_DESIGNATION_MASTER", schema = "FRRO")
public class Designation implements java.io.Serializable {

	// Fields

	private Short designationId;
	private String designationName;
	private String shortDesignationName;
	private String officeIds;
	private Short displayOrder;
	private String createdBy;
	private Boolean recordStatus;
	private String createdIp;
	private Date createdDate;
	private String rowIdentifier;

	// Constructors

	/** default constructor */
	public Designation() {
	}

	/** minimal constructor */
	public Designation(Short designationId, String designationName,
			String createdBy,Boolean recordStatus, String createdIp, Date createdDate) {
		this.designationId = designationId;
		this.designationName = designationName;
		this.recordStatus = recordStatus;
		this.createdBy = createdBy;
		this.createdIp = createdIp;
		this.createdDate = createdDate;
	}

	/** custom constructor */
	public Designation(Short designationId, String designationName,
			Short displayOrder, String createdBy, String createdIp,
			Date createdDate) {
		this.designationId = designationId;
		this.designationName = designationName;
		this.displayOrder = displayOrder;
		this.createdBy = createdBy;
		this.createdIp = createdIp;
		this.createdDate = createdDate;
	}

	/** full constructor */
	public Designation(Short designationId, String designationName, String shortDesignationName, String officeIds, 
			Short displayOrder, String createdBy, String createdIp,
			Date createdDate) {
		this.designationId = designationId;
		this.designationName = designationName;
		this.shortDesignationName = shortDesignationName;
		this.officeIds = officeIds;
		this.displayOrder = displayOrder;
		this.createdBy = createdBy;
		this.createdIp = createdIp;
		this.createdDate = createdDate;
	}
	/** full constructor */
	public Designation(Short designationId, String designationName, String shortDesignationName, String officeIds, 
			Short displayOrder, String createdBy, String createdIp,
			Date createdDate,Boolean recordStatus) {
		this.designationId = designationId;
		this.designationName = designationName;
		this.shortDesignationName = shortDesignationName;
		this.officeIds = officeIds;
		this.displayOrder = displayOrder;
		this.createdBy = createdBy;
		this.createdIp = createdIp;
		this.createdDate = createdDate;
		this.recordStatus=recordStatus;
	}

	// Property accessors
	@Id
	@Column(name = "DESIGNATION_ID", unique = true, nullable = false, precision = 3, scale = 0)
	public Short getDesignationId() {
		return this.designationId;
	}

	public void setDesignationId(Short designationId) {
		this.designationId = designationId;
	}

	@Column(name = "DESIGNATION_NAME", nullable = false, length = 50)
	public String getDesignationName() {
		return this.designationName;
	}

	public void setDesignationName(String designationName) {
		this.designationName = designationName;
	}

	@Column(name = "SHORT_DESIGNATION", nullable = true, length = 10)
	public String getShortDesignationName() {
		return this.shortDesignationName;
	}

	public void setShortDesignationName(String shortDesignationName) {
		this.shortDesignationName = shortDesignationName;
	}
	
	@Column(name = "OFFICE_ID", nullable = true, length = 50)
	public String getOfficeIds() {
		return officeIds;
	}

	public void setOfficeIds(String officeIds) {
		this.officeIds = officeIds;
	}

	@Column(name = "RECORD_STATUS", nullable = false, precision = 1, scale = 0)
	public Boolean getRecordStatus() {
		return this.recordStatus;
	}

	public void setRecordStatus(Boolean recordStatus) {
		this.recordStatus = recordStatus;
	}
	@Column(name = "DISPLAY_ORDER", precision = 3, scale = 0)
	public Short getDisplayOrder() {
		return this.displayOrder;
	}

	public void setDisplayOrder(Short displayOrder) {
		this.displayOrder = displayOrder;
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

	@Column(name = "ROWID", insertable = false, updatable = false)	
	public String getRowIdentifier() {
		return rowIdentifier;
	}

	public void setRowIdentifier(String rowIdentifier) {
		this.rowIdentifier = rowIdentifier;
	}	

	  @Override
	  public String toString()
	  {
	    return designationName;
	  }
}