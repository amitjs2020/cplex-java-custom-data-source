package cplex.java.customdatasource.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SpendoptiModelRequest {

	@JsonProperty("scenarioRun")
	private List<ScenarioRun> scenarioRuns;
	
	//costData from DBread (db, "")(scenarioRunId);
	//Select scenariorun_id, spendopti_ilog_supply_lineitem_id,	supply_lineitem_id, carrier_id, ilog_demand_lineitem_id, totalprice, allocation from tt_spendopti_ilog_supply_lineitem where scenariorun_id = ? order by carrier_id
	@JsonProperty("costData")	
	private List<CostData> costData;
	
	//carrierLaneData from DBread (db, "")(scenarioRunId);
	//select distinct carrier_id, ilog_demand_lineitem_id from tt_spendopti_ilog_supply_lineitem where scenariorun_id = ? order by carrier_id
	@JsonProperty("carrierLaneData")	
	private List<CarrierLaneData> carrierLaneData;
	
	//groupData from DBread (db, "")(scenarioRunId);
	//select distinct ilog_group_id as groupID from tl_spendopti_ilog_group_demand_lineitem where scenariorun_id = ? 
	@JsonProperty("groupData")
	private List<GroupData> groupData;
	
	//aGrpData from DBread (db, " ")(scenarioRunId);
	//select distinct spendopti_ilog_carrier_amount_group_id as aGrpID from tt_spendopti_ilog_carrier_amount_group where scenariorun_id = ?
	@JsonProperty("aGrpData")
	private List<AmountGroupData> amountGroupData;
	
	//carrierData from DBread (db, "")(scenarioRunId);
	//Select distinct carrier_id  from tt_spendopti_ilog_supply_lineitem where scenariorun_id = ? 
	@JsonProperty("carrierData")
	private List<CarrierData> carrierData;
	
	//supplyLineItemData from DBread (db, " ")(scenarioRunId);  
	//Select distinct spendopti_ilog_supply_lineitem_id from tt_spendopti_ilog_supply_lineitem where scenariorun_id = ?
	@JsonProperty("supplyLineItemData")
	private List<SupplyLineItemData> supplyLineItemData;
	
	//laneinfoData from DBread (db, " ")(scenarioRunId);
	//Select scenariorun_id, spendopti_ilog_demand_lineitem_id, demand_lineitem_id, estvolume, penaltycost from tt_spendopti_ilog_demand_lineitem where scenariorun_id = ? 
	@JsonProperty("laneinfoData")
	private List<LaneInfoData> laneInfoData;
	
	//grouplaneData from DBread (db, "")(scenarioRunId);
	//select scenariorun_id, ilog_group_id, ilog_demand_lineitem_id from tl_spendopti_ilog_group_demand_lineitem where scenariorun_id = ? 
	@JsonProperty("grouplaneData")
	private List<GroupLaneData> groupLaneData;
	
	//carrierGroupData from DBread(db, " ")(scenarioRunId);
	//select distinct a.scenariorun_id as scenariorunId, a.carrier_id as carrier_id, b.ilog_group_id as ilog_group_id from tt_spendopti_ilog_supply_lineitem as a, tl_spendopti_ilog_group_demand_lineitem as b where a.ilog_demand_lineitem_id =b.ilog_demand_lineitem_id and a.scenariorun_id=b.scenariorun_id and a.scenariorun_id=? 
	@JsonProperty("carrierGroupData")
	private List<CarrierGroupData> carrierGroupData;
	
	//groupvolumeboundData from DBread (db, " ")(scenarioRunId);
	//select group_id,scenariorun_id,carrier_id,lowerbound_capacity,upperbound_capacity from tt_spendopti_ilog_group_capacity where scenariorun_id = ?
	@JsonProperty("groupvolumeboundData")
	private List<GroupVolumeBoundData> groupVolumeBoundData;
	
	//amtGroupBoundData from DBread (db, " ")(scenarioRunId);
	//select spendopti_ilog_carrier_amount_group_id, scenariorun_id, ilog_group_id, minimum_dollar_amount,  maximum_dollar_amount from tt_spendopti_ilog_carrier_amount_group where scenariorun_id = ?
	@JsonProperty("amtGroupBoundData")
	private List<AmountGroupBoundData> amountGroupBoundData;
	
	//amtGroupCarrierData from DBread (db, " ")(scenarioRunId);
	//select carrier_amount_group_id, scenariorun_id, carrier_id from tt_spendopti_ilog_carrier_group where scenariorun_id = ?
	@JsonProperty("amtGroupCarrierData")
	private List<AmountGroupCarrierData> amountGroupCarrierData;
	
	//groupcarrierboundData from DBread (db, " ")(scenarioRunId);
	//select minmax_carrier.group_id,	minmax_carrier.min_num,	minmax_carrier.max_num from tt_spendopti_ilog_group as ilog_group, tt_spendopti_ilog_minmax_carrier as minmax_carrier where ilog_group.spendopti_ilog_group_id = minmax_carrier.group_id and ilog_group.scenariorun_id = ?
	@JsonProperty("groupcarrierboundData")
	private List<GroupCarrierBoundData> groupCarrierBoundData;
	
	//parameterData from DBread (db, " ")(scenarioRunId);
	//select scenariorun_id, global_min_carrier, global_max_carrier, numberof_carrier_threshold, admin_cost_bellow_threshold, admin_cost_above_threshold, mode, result_parameter, max_run_time from tt_spendopti_ilog_globalparam where scenariorun_id = ?
	@JsonProperty("parameterData")
	private List<ParameterData> parameterData;
	
	//iCarrierLaneRawData from DBread (db, " ")(scenarioRunId);
	//select distinct carrier_id, spendopti_ilog_demand_lineitem_id from tt_spendopti_ilog_incumbent where max_volume > 0 and scenariorun_id=?
	@JsonProperty("iCarrierLaneRawData")
	private List<IncumbentCarrierLaneRawData> incumbentCarrierLaneRawData;
	
	//nICarrierLaneRawData from DBread (db, "") (scenarioRunId);
	//Select distinct a.carrier_id, a.ilog_demand_lineitem_id  from tt_spendopti_ilog_supply_lineitem as a where a.scenariorun_id = ? and not exists (select distinct b.carrier_id, b.spendopti_ilog_demand_lineitem_id from tt_spendopti_ilog_incumbent as b where b.carrier_id=a.carrier_id and b.spendopti_ilog_demand_lineitem_id = a.ilog_demand_lineitem_id and b.max_volume > 0 and b.scenariorun_id=a.scenariorun_id)
	@JsonProperty("nICarrierLaneRawData")
	private List<NoIncumbentCarrierLaneRawData> noIncumbentCarrierLaneRawData;
	
	//iCarrierLaneVolRawData from DBread(db, "")(scenarioRunId);
	//select carrier_id, spendopti_ilog_demand_lineitem_id, min_volume, max_volume  from tt_spendopti_ilog_incumbent where max_volume > 0 and scenariorun_id=?
	@JsonProperty("iCarrierLaneVolRawData")
	private List<IncumbentCarrierLaneVolumeRawData> incumbentCarrierLaneVolmeRawData;
	
	//sameProportionGrpData from DBread (db, " ")(scenarioRunId);
	//select distinct group_id  from tt_spendopti_ilog_carrier_levelling_proportional_group where scenariorun_id = ?
	@JsonProperty("sameProportion")
	private List<SameProportionGroupData> sameProportionGroupData;
	
	public List<ScenarioRun> getScenarioRuns() {
		return scenarioRuns;
	}

	public void setScenarioRuns(List<ScenarioRun> scenarioRuns) {
		this.scenarioRuns = scenarioRuns;
	}

	public List<CostData> getCostData() {
		return costData;
	}

	public void setCostData(List<CostData> costData) {
		this.costData = costData;
	}

	public List<CarrierLaneData> getCarrierLaneData() {
		return carrierLaneData;
	}

	public void setCarrierLaneData(List<CarrierLaneData> carrierLaneData) {
		this.carrierLaneData = carrierLaneData;
	}

	public List<GroupData> getGroupData() {
		return groupData;
	}

	public void setGroupData(List<GroupData> groupData) {
		this.groupData = groupData;
	}

	public List<AmountGroupData> getAmountGroupData() {
		return amountGroupData;
	}

	public void setAmountGroupData(List<AmountGroupData> amountGroupData) {
		this.amountGroupData = amountGroupData;
	}

	public List<CarrierData> getCarrierData() {
		return carrierData;
	}

	public void setCarrierData(List<CarrierData> carrierData) {
		this.carrierData = carrierData;
	}

	public List<SupplyLineItemData> getSupplyLineItemData() {
		return supplyLineItemData;
	}

	public void setSupplyLineItemData(List<SupplyLineItemData> supplyLineItemData) {
		this.supplyLineItemData = supplyLineItemData;
	}

	public List<LaneInfoData> getLaneInfoData() {
		return laneInfoData;
	}

	public void setLaneInfoData(List<LaneInfoData> laneInfoData) {
		this.laneInfoData = laneInfoData;
	}

	public List<GroupLaneData> getGroupLaneData() {
		return groupLaneData;
	}

	public void setGroupLaneData(List<GroupLaneData> groupLaneData) {
		this.groupLaneData = groupLaneData;
	}

	public List<CarrierGroupData> getCarrierGroupData() {
		return carrierGroupData;
	}

	public void setCarrierGroupData(List<CarrierGroupData> carrierGroupData) {
		this.carrierGroupData = carrierGroupData;
	}

	public List<GroupVolumeBoundData> getGroupVolumeBoundData() {
		return groupVolumeBoundData;
	}

	public void setGroupVolumeBoundData(List<GroupVolumeBoundData> groupVolumeBoundData) {
		this.groupVolumeBoundData = groupVolumeBoundData;
	}

	public List<AmountGroupBoundData> getAmountGroupBoundData() {
		return amountGroupBoundData;
	}

	public void setAmountGroupBoundData(List<AmountGroupBoundData> amountGroupBoundData) {
		this.amountGroupBoundData = amountGroupBoundData;
	}

	public List<AmountGroupCarrierData> getAmountGroupCarrierData() {
		return amountGroupCarrierData;
	}

	public void setAmountGroupCarrierData(List<AmountGroupCarrierData> amountGroupCarrierData) {
		this.amountGroupCarrierData = amountGroupCarrierData;
	}

	public List<GroupCarrierBoundData> getGroupCarrierBoundData() {
		return groupCarrierBoundData;
	}

	public void setGroupCarrierBoundData(List<GroupCarrierBoundData> groupCarrierBoundData) {
		this.groupCarrierBoundData = groupCarrierBoundData;
	}

	public List<ParameterData> getParameterData() {
		return parameterData;
	}

	public void setParameterData(List<ParameterData> parameterData) {
		this.parameterData = parameterData;
	}

	public List<IncumbentCarrierLaneRawData> getIncumbentCarrierLaneRawData() {
		return incumbentCarrierLaneRawData;
	}

	public void setIncumbentCarrierLaneRawData(List<IncumbentCarrierLaneRawData> incumbentCarrierLaneRawData) {
		this.incumbentCarrierLaneRawData = incumbentCarrierLaneRawData;
	}

	public List<NoIncumbentCarrierLaneRawData> getNoIncumbentCarrierLaneRawData() {
		return noIncumbentCarrierLaneRawData;
	}

	public void setNoIncumbentCarrierLaneRawData(List<NoIncumbentCarrierLaneRawData> noIncumbentCarrierLaneRawData) {
		this.noIncumbentCarrierLaneRawData = noIncumbentCarrierLaneRawData;
	}

	public List<IncumbentCarrierLaneVolumeRawData> getIncumbentCarrierLaneVolmeRawData() {
		return incumbentCarrierLaneVolmeRawData;
	}

	public void setIncumbentCarrierLaneVolRawData(List<IncumbentCarrierLaneVolumeRawData> incumbentCarrierLaneVolmeRawData) {
		this.incumbentCarrierLaneVolmeRawData = incumbentCarrierLaneVolmeRawData;
	}

	public List<SameProportionGroupData> getSameProportionGroupData() {
		return sameProportionGroupData;
	}

	public void setSameProportionGroupData(List<SameProportionGroupData> sameProportionGroupData) {
		this.sameProportionGroupData = sameProportionGroupData;
	}

}
