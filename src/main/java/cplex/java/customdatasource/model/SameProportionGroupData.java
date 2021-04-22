package cplex.java.customdatasource.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SameProportionGroupData {

	@JsonProperty("group_id")
	private int groupId;

	public SameProportionGroupData() {

	}

	public int getGroupId() {
		return groupId;
	}

	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}

}
