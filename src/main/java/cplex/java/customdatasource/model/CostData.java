package cplex.java.customdatasource.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CostData {
  
	@JsonProperty("scenariorun_id")
	private int scenariorunId;
	
	@JsonProperty("spendopti_ilog_supply_lineitem_id")
	private int spendoptiIlogSupplyLineitemId;
	
	@JsonProperty("supply_lineitem_id")
	private int supplyLineitemId;
	
	@JsonProperty("carrier_id")
	private int carrierId;
	
	@JsonProperty("ilog_demand_lineitem_id")
	private int ilogDemandLineitemId;
	
	@JsonProperty("totalprice")
	private float totalPrice;
	
	@JsonProperty("allocation")
	private float allocation;

	public CostData() {

	}

	public int getScenariorunId() {
		return scenariorunId;
	}

	public void setScenariorunId(int scenariorunId) {
		this.scenariorunId = scenariorunId;
	}

	public int getSpendoptiIlogSupplyLineitemId() {
		return spendoptiIlogSupplyLineitemId;
	}

	public void setSpendoptiIlogSupplyLineitemId(int spendoptiIlogSupplyLineitemId) {
		this.spendoptiIlogSupplyLineitemId = spendoptiIlogSupplyLineitemId;
	}

	public int getSupplyLineitemId() {
		return supplyLineitemId;
	}

	public void setSupplyLineitemId(int supplyLineitemId) {
		this.supplyLineitemId = supplyLineitemId;
	}

	public int getCarrierId() {
		return carrierId;
	}

	public void setCarrierId(int carrierId) {
		this.carrierId = carrierId;
	}

	public int getIlogDemandLineitemId() {
		return ilogDemandLineitemId;
	}

	public void setIlogDemandLineitemId(int ilogDemandLineitemId) {
		this.ilogDemandLineitemId = ilogDemandLineitemId;
	}

	public float getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(float totalPrice) {
		this.totalPrice = totalPrice;
	}

	public float getAllocation() {
		return allocation;
	}

	public void setAllocation(float allocation) {
		this.allocation = allocation;
	}

	@Override
	public String toString() {
		return "CostData [scenariorunId=" + scenariorunId + ", spendoptiIlogSupplyLineitemId="
				+ spendoptiIlogSupplyLineitemId + ", supplyLineitemId=" + supplyLineitemId + ", carrierId=" + carrierId
				+ ", ilogDemandLineitemId=" + ilogDemandLineitemId + ", totalPrice=" + totalPrice + ", allocation="
				+ allocation + "]";
	}
	
}
