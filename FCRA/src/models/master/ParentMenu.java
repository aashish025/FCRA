package models.master;

import java.sql.Date;
import java.util.List;

public class ParentMenu  {
	


		private Short pmenuId;
		private String pmenuName;
		private Short displayOrder;
		private Boolean recordStatus;
		private String createdBy;
		private String createdIp;
		private String createdDate;
		private String lastModifiedBy;
		private String lastModifiedIp;
		private String lastModifiedDate;
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

		public String getLastModifiedDate() {
			return lastModifiedDate;
		}

		public void setLastModifiedDate(String lastModifiedDate) {
			this.lastModifiedDate = lastModifiedDate;
		}

		private String rowIdentifier;
		private List<SubMenu> subMenuList;
		
		// Constructors

		/** default constructor */
		public ParentMenu() {
		}

		/** full constructor */
		public ParentMenu(Short  pmenuId, String pmenuName, Short displayOrder,Boolean recordStatus, 
				String createdBy, String createdIp, String createdDate) {
			this.pmenuId = pmenuId;
			this.pmenuName = pmenuName;
			this.displayOrder = displayOrder;
			this.recordStatus = recordStatus;
			this.createdBy = createdBy;
			this.createdIp = createdIp;
			this.createdDate = createdDate;
			
		}
		
		/** full constructor */
		public ParentMenu(Short  pmenuId, String pmenuName, List<SubMenu> subMenuList) {
			this.pmenuId = pmenuId;
			this.pmenuName = pmenuName;
			this.subMenuList=subMenuList;
		}
		
		/** full constructor */
		public ParentMenu(Short  pmenuId, String pmenuName) {
			this.pmenuId = pmenuId;
			this.pmenuName = pmenuName;
		}
		
		
		
		public ParentMenu(Short pmenuId, String pmenuName, String createdDate) {
			
			
				this.pmenuId=pmenuId;
				this.pmenuName=pmenuName;
				this.createdDate=createdDate;
			
			
		}
	
		public Short getPmenuId() {
			return pmenuId;
		}

		public void setPmenuId(Short pmenuId) {
			this.pmenuId = pmenuId;
		}

		public String getPmenuName() {
			return this.pmenuName;
		}

		public void setPmenuName(String pmenuName) {
			this.pmenuName = pmenuName;
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
	
		public String getCreatedDate() {
			return createdDate;
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
		
		  public List<SubMenu> getSubMenuList() {
			return subMenuList;
		}

		public void setSubMenuList(List<SubMenu> subMenuList) {
			this.subMenuList = subMenuList;
		}


}
