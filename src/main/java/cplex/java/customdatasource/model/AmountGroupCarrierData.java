package cplex.java.customdatasource.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AmountGroupCarrierData {

	@JsonProperty("amtGroup_id")
	private int amoutGroupId;

	@JsonProperty("scenariorun_id")
	private int scenariorunId;

	@JsonProperty("carrier_id")
	private int carrierId;

	public AmountGroupCarrierData() {

	}

	public int getAmoutGroupId() {
		return amoutGroupId;
	}

	public void setAmoutGroupId(int amoutGroupId) {
		this.amoutGroupId = amoutGroupId;
	}

	public int getScenariorunId() {
		return scenariorunId;
	}

	public void setScenariorunId(int scenariorunId) {
		this.scenariorunId = scenariorunId;
	}

	public int getCarrierId() {
		return carrierId;
	}

	public void setCarrierId(int carrierId) {
		this.carrierId = carrierId;
	}

}
