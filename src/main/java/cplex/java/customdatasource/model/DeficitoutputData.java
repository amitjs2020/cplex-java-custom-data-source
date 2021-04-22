package cplex.java.customdatasource.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DeficitoutputData {

	@JsonProperty("scenario_id")
	private int scenarioId;

	@JsonProperty("ilog_demand_lineitem_id")
	private int ilogDemandLineitemId;

	@JsonProperty("Allocation")
	private float allocation;

	public DeficitoutputData() {

	}

	public int getScenarioId() {
		return scenarioId;
	}

	public void setScenarioId(int scenarioId) {
		this.scenarioId = scenarioId;
	}

	public int getIlogDemandLineitemId() {
		return ilogDemandLineitemId;
	}

	public void setIlogDemandLineitemId(int ilogDemandLineitemId) {
		this.ilogDemandLineitemId = ilogDemandLineitemId;
	}

	public float getAllocation() {
		return allocation;
	}

	public void setAllocation(float allocation) {
		this.allocation = allocation;
	}

}
