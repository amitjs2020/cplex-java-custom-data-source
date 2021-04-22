package cplex.java.customdatasource.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GroupData {

	@JsonProperty("groupID")
	private int ilogGroupId;
	
	public GroupData() {
		
	}
	
	public int getIlogGroupId() {
		return ilogGroupId;
	}
	
	public void setIlogGroupId(int ilogGroupId) {
		this.ilogGroupId = ilogGroupId;
	}
	
}
