package cplex.java.customdatasource.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ParameterData {

	@JsonProperty("scenariorun_id")
	private int scenariorunId;

	@JsonProperty("global_min_carrier")
	private int globalMinCarrier;

	@JsonProperty("global_max_carrier")
	private int globalMaxCarrier;

	@JsonProperty("numberof_carrier_threshold")
	private int numberofCarrierThreshold;

	@JsonProperty("admin_cost_bellow_threshold")
	float adminCostBellowThreshold;

	@JsonProperty("admin_cost_above_threshold")
	float adminCostAboveThreshold;

	@JsonProperty("mode")
	private int mode;

	@JsonProperty("result_parameter")
	private int resultParameter;

	@JsonProperty("max_run_time")
	private int maxRunTime;

	public ParameterData() {

	}

	public int getScenariorunId() {
		return scenariorunId;
	}

	public void setScenariorunId(int scenariorunId) {
		this.scenariorunId = scenariorunId;
	}

	public int getGlobalMinCarrier() {
		return globalMinCarrier;
	}

	public void setGlobalMinCarrier(int globalMinCarrier) {
		this.globalMinCarrier = globalMinCarrier;
	}

	public int getGlobalMaxCarrier() {
		return globalMaxCarrier;
	}

	public void setGlobalMaxCarrier(int globalMaxCarrier) {
		this.globalMaxCarrier = globalMaxCarrier;
	}

	public int getNumberofCarrierThreshold() {
		return numberofCarrierThreshold;
	}

	public void setNumberofCarrierThreshold(int numberofCarrierThreshold) {
		this.numberofCarrierThreshold = numberofCarrierThreshold;
	}

	public float getAdminCostBellowThreshold() {
		return adminCostBellowThreshold;
	}

	public void setAdminCostBellowThreshold(float adminCostBellowThreshold) {
		this.adminCostBellowThreshold = adminCostBellowThreshold;
	}

	public float getAdminCostAboveThreshold() {
		return adminCostAboveThreshold;
	}

	public void setAdminCostAboveThreshold(float adminCostAboveThreshold) {
		this.adminCostAboveThreshold = adminCostAboveThreshold;
	}

	public int getMode() {
		return mode;
	}

	public void setMode(int mode) {
		this.mode = mode;
	}

	public int getResultParameter() {
		return resultParameter;
	}

	public void setResultParameter(int resultParameter) {
		this.resultParameter = resultParameter;
	}

	public int getMaxRunTime() {
		return maxRunTime;
	}

	public void setMaxRunTime(int maxRunTime) {
		this.maxRunTime = maxRunTime;
	}

}
