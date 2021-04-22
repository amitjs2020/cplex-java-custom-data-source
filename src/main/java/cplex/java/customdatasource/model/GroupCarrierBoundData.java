package cplex.java.customdatasource.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GroupCarrierBoundData {

	@JsonProperty("group_id")
	private int groupId;

	@JsonProperty("min_num")
	private int minNum;

	@JsonProperty("max_num")
	private int maxNum;

	public GroupCarrierBoundData() {

	}

	public int getGroupId() {
		return groupId;
	}

	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}

	public int getMinNum() {
		return minNum;
	}

	public void setMinNum(int minNum) {
		this.minNum = minNum;
	}

	public int getMaxNum() {
		return maxNum;
	}

	public void setMaxNum(int maxNum) {
		this.maxNum = maxNum;
	}
}
