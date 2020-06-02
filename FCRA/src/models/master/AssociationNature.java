package models.master;

public class AssociationNature {
	
		// Fields

			private String natureCode;
			private String natureName;
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
			public AssociationNature() {
			}
			public AssociationNature(String natureCode) {
				this.natureCode = natureCode;
			}
			public AssociationNature(String natureCode, String natureName) {
				this.natureCode = natureCode;
				this.natureName = natureName;
			}
			public AssociationNature(String natureCode, String natureName,String createdDate) {
				this.natureCode = natureCode;
				this.natureName = natureName;
				this.createdDate=createdDate;
			}


			/** minimal constructor */
			public AssociationNature(String natureCode, String natureName,
					String recordStatus, String createdBy, String createdIp,
					String createdDate) {
				this.natureCode = natureCode;
				this.natureName = natureName;
				this.recordStatus = recordStatus;
				this.createdBy = createdBy;
				this.createdIp = createdIp;
				this.createdDate = createdDate;
			}

			/** full constructor */
			public AssociationNature(String natureCode, String natureName,
					Short displayOrder, String recordStatus, String createdBy, String createdIp,
					String createdDate) {
				this.natureCode = natureCode;
				this.natureName = natureName;
				this.displayOrder = displayOrder;
				this.recordStatus = recordStatus;
				this.createdBy = createdBy;
				this.createdIp = createdIp;
				this.createdDate = createdDate;
					
			}
			// Property accessors
			public String getNatureCode() {
				return natureCode;
			}
			public void setNatureCode(String natureCode) {
				this.natureCode = natureCode;
			}
			public String getNatureName() {
				return natureName;
			}
			public void setNatureName(String natureName) {
				this.natureName = natureName;
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