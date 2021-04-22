package cplex.java.customdatasource.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SpendoptiModelResponse {

	@JsonProperty("modeloutputData")
	private List<ModeloutputData> modeloutputData;

	@JsonProperty("deficitoutputData")
	private List<DeficitoutputData> deficitoutputData;

	public SpendoptiModelResponse() {

	}

	public List<ModeloutputData> getModeloutputData() {
		return modeloutputData;
	}

	public void setModeloutputData(List<ModeloutputData> modeloutputData) {
		this.modeloutputData = modeloutputData;
	}

	public List<DeficitoutputData> getDeficitoutputData() {
		return deficitoutputData;
	}

	public void setDeficitoutputData(List<DeficitoutputData> deficitoutputData) {
		this.deficitoutputData = deficitoutputData;
	}

}