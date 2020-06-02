package models.master;

public class SocietyActs {


	
		// Fields

			private String actCode;
			private String actName;
			private Short displayOrder;
			private String recordStatus;
			private String createdBy;
			private String createdIp;
			private String createdDate;
			private String lastModifiedIp;
			private Integer lastModifiedDate;
			private String lastModifiedBy;
			

			// Constructors

			/** default constructor */
			public SocietyActs() {
			}
			public SocietyActs(String actCode) {
				this.actCode = actCode;
			}
			public SocietyActs(String actCode, String actName) {
				this.actCode = actCode;
				this.actName = actName;
			}
			public SocietyActs(String actCode, String actName,String createdDate) {
				this.actCode = actCode;
				this.actName = actName;
				this.createdDate=createdDate;
			}


			/** minimal constructor */
			public SocietyActs(String actCode, String actName,
					String recordStatus, String createdBy, String createdIp,
					String createdDate) {
				this.actCode = actCode;
				this.actName = actName;
				this.recordStatus = recordStatus;
				this.createdBy = createdBy;
				this.createdIp = createdIp;
				this.createdDate = createdDate;
			}

			/** full constructor */
			public SocietyActs(String actCode, String actName,
					Short displayOrder, String recordStatus, String createdBy, String createdIp,
					String createdDate) {
				this.actCode = actCode;
				this.actName = actName;
				this.displayOrder = displayOrder;
				this.recordStatus = recordStatus;
				this.createdBy = createdBy;
				this.createdIp = createdIp;
				this.createdDate = createdDate;
					
			}
			// Property accessors
			public String getActCode() {
				return actCode;
			}
			public void setActCode(String actCode) {
				this.actCode = actCode;
			}
			public String getActName() {
				return actName;
			}
			public void setActName(String actName) {
				this.actName = actName;
			}
			public Short getDisplayOrder() {
				return displayOrder;
			}
			public void setDisplayOrder(Short displayOrder) {
				this.displayOrder = displayOrder;
			}
			public String getRecordStatus() {
				return recordStatus;
			}
			public void setRecordStatus(String recordStatus) {
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
			public String getCreatedDate() {
				return createdDate;
			}
			public void setCreatedDate(String createdDate) {
				this.createdDate = createdDate;
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
			public String getLastModifiedBy() {
				return lastModifiedBy;
			}
			public void setLastModifiedBy(String lastModifiedBy) {
				this.lastModifiedBy = lastModifiedBy;
			}
			
}

