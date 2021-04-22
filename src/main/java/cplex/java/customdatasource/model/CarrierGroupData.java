package cplex.java.customdatasource.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CarrierGroupData {

	@JsonProperty("scenariorunid")
	private int scenariorunId;
	
	@JsonProperty("carrier_id")
	private int carrierId;
	
	@JsonProperty("group_id")
	private int groupId;
	
	public CarrierGroupData() {
		
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

	public int getGroupId() {
		return groupId;
	}

	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}
	
}
