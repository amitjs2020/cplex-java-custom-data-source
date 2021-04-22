package cplex.java.customdatasource.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class IncumbentCarrierLaneVolumeRawData {

	@JsonProperty("carrier_id")
	private int carrierId; 
	
	@JsonProperty("spendopti_ilog_demand_lineitem_id")
	private int ilogDemandLineitemId; 
	
	@JsonProperty("min_volume")
	private float minVolume; 
	
	@JsonProperty("max_volume")
	private float maxVolume;
	
	public IncumbentCarrierLaneVolumeRawData() {
		
	}

	public int getCarrierId() {
		return carrierId;
	}

	public void setCarrierId(int carrierId) {
		this.carrierId = carrierId;
	}

	public int getIlogDemandLineitemId() {
		return ilogDemandLineitemId;
	}

	public void setIlogDemandLineitemId(int ilogDemandLineitemId) {
		this.ilogDemandLineitemId = ilogDemandLineitemId;
	}

	public float getMinVolume() {
		return minVolume;
	}

	public void setMinVolume(float minVolume) {
		this.minVolume = minVolume;
	}

	public float getMaxVolume() {
		return maxVolume;
	}

	public void setMaxVolume(float maxVolume) {
		this.maxVolume = maxVolume;
	}

}
