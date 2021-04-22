package cplex.java.customdatasource.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SupplyLineItemData {

	@JsonProperty("supplyLineItemID")
	private int supplyLineitemId;
	
	public SupplyLineItemData() {
		
	}
	
	public int getSupplyLineitemId() {
		return supplyLineitemId;
	}
	
	public void setSupplyLineitemId(int supplyLineitemId) {
		this.supplyLineitemId = supplyLineitemId;
	}
}
