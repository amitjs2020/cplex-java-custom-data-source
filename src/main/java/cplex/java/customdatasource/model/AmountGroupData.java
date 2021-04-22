package cplex.java.customdatasource.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AmountGroupData {
	
	@JsonProperty("aGroupID")
	private int ilogCarrierAmountGroupId;
	
	public AmountGroupData() {
		
	}
	
	public int getIlogCarrierAmountGroupId() {
		return ilogCarrierAmountGroupId;
	}

	public void setIlogCarrierAmountGroupId(int ilogCarrierAmountGroupId) {
		this.ilogCarrierAmountGroupId = ilogCarrierAmountGroupId;
	}
	
}
