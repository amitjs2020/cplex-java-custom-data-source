package cplex.java.customdatasource.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class IncumbentCarrierLaneRawData {

	@JsonProperty("iCarrier_id")
	private int incumbentCarrierId;
	
	@JsonProperty("spendopti_ilog_demand_lineitem_id")
	private int ilogDemandLineitemId;
	
	public IncumbentCarrierLaneRawData() {
		
	}

	public int getIncumbentCarrierId() {
		return incumbentCarrierId;
	}

	public void setIncumbentCarrierId(int incumbentCarrierId) {
		this.incumbentCarrierId = incumbentCarrierId;
	}

	public int getIlogDemandLineitemId() {
		return ilogDemandLineitemId;
	}

	public void setIlogDemandLineitemId(int ilogDemandLineitemId) {
		this.ilogDemandLineitemId = ilogDemandLineitemId;
	}
	
}
