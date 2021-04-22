package cplex.java.customdatasource.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CarrierLaneData {

	@JsonProperty("carrier_id")
	private int carrierId;

	@JsonProperty("spendopti_ilog_demand_lineitem_id")
	private int ilogDemandLineitemId;

	public CarrierLaneData() {

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

}
