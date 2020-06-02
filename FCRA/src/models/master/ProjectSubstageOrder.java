package models.master;

public class ProjectSubstageOrder {
	private String substageId;
	private String substageDesc;
    private String proposalId;
    private String processOrder;
	private String proposalDesc;
	private String rowIdentifier;
	
  public ProjectSubstageOrder()
	{
		
	}
  public ProjectSubstageOrder(String substageId , String substageDesc , String proposalId ,String proposalDesc , String processOrder, String rid)
  {
	  this.substageId=substageId;
	  this.substageDesc=substageDesc;
	  this.proposalId=proposalId;
	  this.proposalDesc=proposalDesc;
	  this.processOrder=processOrder;
	  this.rowIdentifier=rid;
	  
  }
	
	
	
	public String getSubstageId() {
		return substageId;
	}
	public void setSubstageId(String substageId) {
		this.substageId = substageId;
	}
	public String getSubstageDesc() {
		return substageDesc;
	}
	public void setSubstageDesc(String substageDesc) {
		this.substageDesc = substageDesc;
	}
	
	public String getProcessOrder() {
		return processOrder;
	}
	public void setProcessOrder(String processOrder) {
		this.processOrder = processOrder;
	}
	public String getProposalDesc() {
		return proposalDesc;
	}
	public void setProposalDesc(String proposalDesc) {
		this.proposalDesc = proposalDesc;
	}
	public String getProposalId() {
		return proposalId;
	}
	public void setProposalId(String proposalId) {
		this.proposalId = proposalId;
	}
	public String getRowIdentifier() {
		return rowIdentifier;
	}
	public void setRowIdentifier(String rowIdentifier) {
		this.rowIdentifier = rowIdentifier;
	}
	

}
