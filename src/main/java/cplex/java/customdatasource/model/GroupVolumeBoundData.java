package cplex.java.customdatasource.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GroupVolumeBoundData {

	@JsonProperty("group_id")
	private int groupId;
	
	@JsonProperty("scenariorun_id")
	private int scenariorunId;
	
	@JsonProperty("carrier_id")
	private int carrierId;
	
	@JsonProperty("lowerbound_capacity")
	private float lowerBoundCapacity;
	
	@JsonProperty("upperbound_capacity")
	private float upperBoundCapacity;
	
	public GroupVolumeBoundData() {
		
	}

	public int getGroupId() {
		return groupId;
	}

	public void setGroupId(int groupId) {
		this.groupId = groupId;
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

	public float getLowerBoundCapacity() {
		return lowerBoundCapacity;
	}

	public void setLowerBoundCapacity(float lowerBoundCapacity) {
		this.lowerBoundCapacity = lowerBoundCapacity;
	}

	public float getUpperBoundCapacity() {
		return upperBoundCapacity;
	}

	public void setUpperBoundCapacity(float upperBoundCapacity) {
		this.upperBoundCapacity = upperBoundCapacity;
	}
	
}
