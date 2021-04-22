package cplex.java.customdatasource.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AmountGroupBoundData {

	@JsonProperty("amtGroup_id")
	private int amountGroupId;

	@JsonProperty("scenariorun_id")
	private int scenariorunId;

	@JsonProperty("group_id")
	private int groupId;

	@JsonProperty("amtLowerbound")
	private float amountLowerBound;

	@JsonProperty("amtUpperbound")
	private float amoutUpperBound;

	public AmountGroupBoundData() {

	}

	public int getAmountGroupId() {
		return amountGroupId;
	}

	public void setAmountGroupId(int amountGroupId) {
		this.amountGroupId = amountGroupId;
	}

	public int getScenariorunId() {
		return scenariorunId;
	}

	public void setScenariorunId(int scenariorunId) {
		this.scenariorunId = scenariorunId;
	}

	public int getGroupId() {
		return groupId;
	}

	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}

	public float getAmountLowerBound() {
		return amountLowerBound;
	}

	public void setAmountLowerBound(float amountLowerBound) {
		this.amountLowerBound = amountLowerBound;
	}

	public float getAmoutUpperBound() {
		return amoutUpperBound;
	}

	public void setAmoutUpperBound(float amoutUpperBound) {
		this.amoutUpperBound = amoutUpperBound;
	}

}
