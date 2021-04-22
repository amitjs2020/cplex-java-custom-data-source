package cplex.java.customdatasource.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GroupLaneData {

	@JsonProperty("scenariorun_id")
	private int scenariorunId;
	
	@JsonProperty("ilog_group_id")
	private int ilogGroupId;
	
	@JsonProperty("ilog_demand_lineitem_id")
	private int ilogDemandLineitemId;
	
	public GroupLaneData() {
		
	}

	public int getScenariorunId() {
		return scenariorunId;
	}

	public void setScenariorunId(int scenariorunId) {
		this.scenariorunId = scenariorunId;
	}

	public int getIlogGroupId() {
		return ilogGroupId;
	}

	public void setIlogGroupId(int ilogGroupId) {
		this.ilogGroupId = ilogGroupId;
	}

	public int getIlogDemandLineitemId() {
		return ilogDemandLineitemId;
	}

	public void setIlogDemandLineitemId(int ilogDemandLineitemId) {
		this.ilogDemandLineitemId = ilogDemandLineitemId;
	}
	
}
