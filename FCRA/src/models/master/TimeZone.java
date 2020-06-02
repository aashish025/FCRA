package models.master;

import java.sql.Timestamp;
import java.util.Date;

public class TimeZone {
	// Fields	
    private Short zoneId;
	private Country country;
	private String countryName;
	private String zoneName;
	private Short displayOrder;
	private Boolean recordStatus;
	private String createdBy;
	private String createdIp;
	private Date createdDate;
	private String lastModifiedBy;
	private String lastModifiedIp;
	private Timestamp lastModifiedDate;	

	private String rowIdentifier;
	
	/** default constructor */
	public TimeZone() {
	}
	
	public TimeZone(Short zoneId,String zoneName) {
		this.zoneId = zoneId;
		this.zoneName = zoneName;
	}
	/** minimal constructor */
	public TimeZone(Short zoneId,Country country,Boolean recordStatus, 
			String createdBy, String createdIp,Date createdDate) {
		this.zoneId = zoneId;
		this.country = country;
		this.recordStatus = recordStatus;
		this.createdBy = createdBy;
		this.createdIp = createdIp;
		this.createdDate = createdDate;
	}
	
	/** full constructor */
	public TimeZone(Short zoneId,Country country, String zoneName,Boolean recordStatus, 
			String createdBy, String createdIp,Date createdDate) {
		this.zoneId = zoneId;
		this.country = country;
		this.zoneName = zoneName;
		this.recordStatus = recordStatus;
		this.createdBy = createdBy;
		this.createdIp = createdIp;
		this.createdDate = createdDate;
	}

	public Short getZoneId() {
		return zoneId;
	}

	public void setZoneId(Short zoneId) {
		this.zoneId = zoneId;
	}

	public Country getCountry() {
		return country;
	}

	public void setCountry(Country country) {
		this.country = country;
	}

	public String getZoneName() {
		return zoneName;
	}
	
	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	public void setZoneName(String zoneName) {
		this.zoneName = zoneName;
	}

	public Short getDisplayOrder() {
		return displayOrder;
	}

	public void setDisplayOrder(Short displayOrder) {
		this.displayOrder = displayOrder;
	}

	public Boolean getRecordStatus() {
		return recordStatus;
	}

	public void setRecordStatus(Boolean recordStatus) {
		this.recordStatus = recordStatus;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getCreatedIp() {
		return createdIp;
	}

	public void setCreatedIp(String createdIp) {
		this.createdIp = createdIp;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	
	public String getLastModifiedBy() {
		return lastModifiedBy;
	}

	public void setLastModifiedBy(String lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}

	public String getLastModifiedIp() {
		return lastModifiedIp;
	}

	public void setLastModifiedIp(String lastModifiedIp) {
		this.lastModifiedIp = lastModifiedIp;
	}

	public Timestamp getLastModifiedDate() {
		return lastModifiedDate;
	}

	public void setLastModifiedDate(Timestamp lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	public String getRowIdentifier() {
		return rowIdentifier;
	}

	public void setRowIdentifier(String rowIdentifier) {
		this.rowIdentifier = rowIdentifier;
	}
	
	@Override
	  public String toString()
	  {
		  return zoneName;
	  }
}
