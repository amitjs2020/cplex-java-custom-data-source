package cplex.java.customdatasource.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CarrierData {
	
	@JsonProperty("carrierID")
	private int carrierId;
	
	public CarrierData() {
		
	}
	
	public int getCarrierId() {
		return carrierId;
	}
	
	public void setCarrierId(int carrierId) {
		this.carrierId = carrierId;
	}

}
