package cplex.java.customdatasource.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class NoIncumbentCarrierLaneRawData {

	@JsonProperty("nICarrier_id")
	private int noIincumbentCarrierId;
	
	@JsonProperty("ilog_demand_lineitem_id")
	private int ilogDemandLineitemId;
	
	public NoIncumbentCarrierLaneRawData() {
		
	}

	public int getNoIncumbentCarrierId() {
		return noIincumbentCarrierId;
	}

	public void setNoIncumbentCarrierId(int noIincumbentCarrierId) {
		this.noIincumbentCarrierId = noIincumbentCarrierId;
	}

	public int getIlogDemandLineitemId() {
		return ilogDemandLineitemId;
	}

	public void setIlogDemandLineitemId(int ilogDemandLineitemId) {
		this.ilogDemandLineitemId = ilogDemandLineitemId;
	}
}
