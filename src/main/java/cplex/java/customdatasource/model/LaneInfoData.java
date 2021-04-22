package cplex.java.customdatasource.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LaneInfoData {

	@JsonProperty("scenariorun_id")
	private int scenariorunId;

	@JsonProperty("spendopti_ilog_demand_lineitem_id")
	private int ilogDemandLineitemId;

	@JsonProperty("demand_lineitem_id")
	private int demandLineitemId;

	@JsonProperty("estvolume")
	private float estVolume;

	@JsonProperty("penaltycost")
	private float penaltyCost;

	public LaneInfoData() {

	}

	public int getScenariorunId() {
		return scenariorunId;
	}

	public void setScenariorunId(int scenariorunId) {
		this.scenariorunId = scenariorunId;
	}

	public int getIlogDemandLineitemId() {
		return ilogDemandLineitemId;
	}

	public void setIlogDemandLineitemId(int ilogDemandLineitemId) {
		this.ilogDemandLineitemId = ilogDemandLineitemId;
	}

	public int getDemandLineitemId() {
		return demandLineitemId;
	}

	public void setDemandLineitemId(int demandLineitemId) {
		this.demandLineitemId = demandLineitemId;
	}

	public float getEstVolume() {
		return estVolume;
	}

	public void setEstVolume(float estVolume) {
		this.estVolume = estVolume;
	}

	public float getPenaltyCost() {
		return penaltyCost;
	}

	public void setPenaltyCost(float penaltyCost) {
		this.penaltyCost = penaltyCost;
	}
}
