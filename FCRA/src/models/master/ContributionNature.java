package models.master;

public class ContributionNature {

             // Fields

				private String contributionType;
				private String contributionName;
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
				public ContributionNature() {
				}
				public ContributionNature(String contributionType) {
					this.contributionType = contributionType;
				}
				public ContributionNature(String contributionType, String contributionName) {
					this.contributionType = contributionType;
					this.contributionName = contributionName;
				}
				public ContributionNature(String contributionType, String contributionName,String createdDate) {
					this.contributionType = contributionType;
					this.contributionName = contributionName;
					this.createdDate=createdDate;
				}


				/** minimal constructor */
				public ContributionNature(String contributionType, String contributionName,
						String recordStatus, String createdBy, String createdIp,
						String createdDate) {
					this.contributionType = contributionType;
					this.contributionName = contributionName;
					this.recordStatus = recordStatus;
					this.createdBy = createdBy;
					this.createdIp = createdIp;
					this.createdDate = createdDate;
				}

				/** full constructor */
				public ContributionNature(String contributionType, String contributionName,
						Short displayOrder, String recordStatus, String createdBy, String createdIp,
						String createdDate) {
					this.contributionType = contributionType;
					this.contributionName = contributionName;
					this.displayOrder = displayOrder;
					this.recordStatus = recordStatus;
					this.createdBy = createdBy;
					this.createdIp = createdIp;
					this.createdDate = createdDate;
						
				}
				// Property accessors
				public String getContributionType() {
					return contributionType;
				}
				public void setContributionType(String contributionType) {
					this.contributionType = contributionType;
				}
				public String getContributionName() {
					return contributionName;
				}
				public void setContributionName(String contributionName) {
					this.contributionName = contributionName;
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
