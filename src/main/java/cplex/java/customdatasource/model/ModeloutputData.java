package cplex.java.customdatasource.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ModeloutputData {

	@JsonProperty("allocation")
	private float allocation;
	
	@JsonProperty("spendopti_ilog_supply_lineitem_id")
	private int ilogSupplyLineitemId;
	
	public ModeloutputData() {
		
	}

	public float getAllocation() {
		return allocation;
	}

	public void setAllocation(float allocation) {
		this.allocation = allocation;
	}

	public int getIlogSupplyLineitemId() {
		return ilogSupplyLineitemId;
	}

	public void setIlogSupplyLineitemId(int ilogSupplyLineitemId) {
		this.ilogSupplyLineitemId = ilogSupplyLineitemId;
	}
	
}
