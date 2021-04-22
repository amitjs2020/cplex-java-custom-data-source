package cplex.java.customdatasource.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import cplex.java.customdatasource.model.AmountGroupBoundData;
import cplex.java.customdatasource.model.AmountGroupCarrierData;
import cplex.java.customdatasource.model.AmountGroupData;
import cplex.java.customdatasource.model.CarrierData;
import cplex.java.customdatasource.model.CarrierGroupData;
import cplex.java.customdatasource.model.CarrierLaneData;
import cplex.java.customdatasource.model.CostData;
import cplex.java.customdatasource.model.DeficitoutputData;
import cplex.java.customdatasource.model.GroupCarrierBoundData;
import cplex.java.customdatasource.model.GroupData;
import cplex.java.customdatasource.model.GroupLaneData;
import cplex.java.customdatasource.model.GroupVolumeBoundData;
import cplex.java.customdatasource.model.IncumbentCarrierLaneRawData;
import cplex.java.customdatasource.model.IncumbentCarrierLaneVolumeRawData;
import cplex.java.customdatasource.model.LaneInfoData;
import cplex.java.customdatasource.model.ModeloutputData;
import cplex.java.customdatasource.model.NoIncumbentCarrierLaneRawData;
import cplex.java.customdatasource.model.ParameterData;
import cplex.java.customdatasource.model.SameProportionGroupData;
import cplex.java.customdatasource.model.SupplyLineItemData;

public class SpendOptiILogDAO {

	private static Logger LOG = Logger.getLogger(SpendOptiILogDAO.class.getName());

	private static final int batchSize = 10000;

	private DBConnection dbConnection;

	private static final String COST_DATA_SQL = "Select scenariorun_id, spendopti_ilog_supply_lineitem_id,	supply_lineitem_id, carrier_id, ilog_demand_lineitem_id, totalprice, allocation from tt_spendopti_ilog_supply_lineitem where scenariorun_id = ? order by carrier_id";

	private static final String CARRIER_LANE_DATA_SQL = "select distinct carrier_id, ilog_demand_lineitem_id from tt_spendopti_ilog_supply_lineitem where scenariorun_id = ? order by carrier_id";

	private static final String GROUP_DATA_SQL = "select distinct ilog_group_id as groupID from tl_spendopti_ilog_group_demand_lineitem where scenariorun_id = ?";

	private static final String AMOUNT_GROUP_DATA_SQL = "select distinct spendopti_ilog_carrier_amount_group_id as aGrpID from tt_spendopti_ilog_carrier_amount_group where scenariorun_id = ?";

	private static final String CARRIER_DATA_SQL = "Select distinct carrier_id  from tt_spendopti_ilog_supply_lineitem where scenariorun_id = ?";

	private static final String SUPPLY_LINEITEM_DATA_SQL = "Select distinct spendopti_ilog_supply_lineitem_id from tt_spendopti_ilog_supply_lineitem where scenariorun_id = ?";

	private static final String LANE_INFO_DATA_SQL = "Select scenariorun_id, spendopti_ilog_demand_lineitem_id, demand_lineitem_id, estvolume, penaltycost from tt_spendopti_ilog_demand_lineitem where scenariorun_id = ?";

	private static final String GROUP_LANE_DATA_SQL = "select scenariorun_id, ilog_group_id, ilog_demand_lineitem_id from tl_spendopti_ilog_group_demand_lineitem where scenariorun_id = ?";

	private static final String CARRIER_GROUP_DATA_SQL = "select distinct a.scenariorun_id as scenariorunId, a.carrier_id as carrier_id, b.ilog_group_id as ilog_group_id from tt_spendopti_ilog_supply_lineitem as a, tl_spendopti_ilog_group_demand_lineitem as b where a.ilog_demand_lineitem_id =b.ilog_demand_lineitem_id and a.scenariorun_id=b.scenariorun_id and a.scenariorun_id=?";

	private static final String GROUP_VOLUME_BOUND_DATA_SQL = "select group_id,scenariorun_id,carrier_id,lowerbound_capacity,upperbound_capacity from tt_spendopti_ilog_group_capacity where scenariorun_id = ?";

	private static final String AMOUNT_GROUP_BOUND_DATA_SQL = "select spendopti_ilog_carrier_amount_group_id, scenariorun_id, ilog_group_id, minimum_dollar_amount,  maximum_dollar_amount from tt_spendopti_ilog_carrier_amount_group where scenariorun_id = ?";

	private static final String AMOUNT_GROUP_CARRIER_DATA_SQL = "select carrier_amount_group_id, scenariorun_id, carrier_id from tt_spendopti_ilog_carrier_group where scenariorun_id = ?";

	private static final String GROUP_CARRIER_BOUND_DATA_SQL = "select minmax_carrier.group_id as group_id, minmax_carrier.min_num as min_num, minmax_carrier.max_num as max_num from tt_spendopti_ilog_group as ilog_group, tt_spendopti_ilog_minmax_carrier as minmax_carrier where ilog_group.spendopti_ilog_group_id = minmax_carrier.group_id and ilog_group.scenariorun_id = ?";

	private static final String PARAMETER_DATA_SQL = "select scenariorun_id, global_min_carrier, global_max_carrier, numberof_carrier_threshold, admin_cost_bellow_threshold, admin_cost_above_threshold, mode, result_parameter, max_run_time from tt_spendopti_ilog_globalparam where scenariorun_id = ?";

	private static final String INCUMBENT_CARRIER_LANE_RAW_DATA_SQL = "select distinct carrier_id, spendopti_ilog_demand_lineitem_id from tt_spendopti_ilog_incumbent where max_volume > 0 and scenariorun_id=?";

	private static final String NO_INCUMBERNT_CARRIER_LANE_RAW_DATA_SQL = "Select distinct a.carrier_id as carrier_id, a.ilog_demand_lineitem_id as ilog_demand_lineitem_id from tt_spendopti_ilog_supply_lineitem as a where a.scenariorun_id = ? and not exists (select distinct b.carrier_id, b.spendopti_ilog_demand_lineitem_id from tt_spendopti_ilog_incumbent as b where b.carrier_id=a.carrier_id and b.spendopti_ilog_demand_lineitem_id = a.ilog_demand_lineitem_id and b.max_volume > 0 and b.scenariorun_id=a.scenariorun_id)";

	private static final String INCUMBENT_CARRIER_LANE_VOL_RAW_DATA_SQL = "select carrier_id, spendopti_ilog_demand_lineitem_id, min_volume, max_volume  from tt_spendopti_ilog_incumbent where max_volume > 0 and scenariorun_id=?";

	private static final String SAME_PROPORTION_GROUP_DATA_SQL = "select distinct group_id  from tt_spendopti_ilog_carrier_levelling_proportional_group where scenariorun_id = ?";

	private static final String UPDATE_MODELOUTPUTDATA = "update tt_spendopti_ilog_supply_lineitem set allocation =? where spendopti_ilog_supply_lineitem_id=?";

	private static final String INSERT_DEFICITOUTPUTDATA = "insert into tt_spendopti_ilog_deficit_allocation(scenariorun_id, ilog_demand_lineitem_id, Allocation) Values(?,?,?)";

	public SpendOptiILogDAO() {
		this.dbConnection = new DBConnection();
	}

	public List<CostData> getCostData(int scenarioRunId) {
		List<CostData> costDatas = new ArrayList<>();
		Connection connection = dbConnection.getDbConnection();
		PreparedStatement prepareStmt = null;
		ResultSet rs = null;
		try {
			prepareStmt = connection.prepareStatement(COST_DATA_SQL);
			prepareStmt.setInt(1, scenarioRunId);
			rs = prepareStmt.executeQuery();
			// Iterate through the data in the result set and display it.
			while (rs.next()) {
				CostData costData = new CostData();
				costData.setScenariorunId(rs.getInt("scenariorun_id"));
				costData.setSpendoptiIlogSupplyLineitemId(rs.getInt("spendopti_ilog_supply_lineitem_id"));
				costData.setSupplyLineitemId(rs.getInt("supply_lineitem_id"));
				costData.setCarrierId(rs.getInt("carrier_id"));
				costData.setIlogDemandLineitemId(rs.getInt("ilog_demand_lineitem_id"));
				costData.setTotalPrice(rs.getFloat("totalprice"));
				costData.setAllocation(rs.getFloat("allocation"));
				costDatas.add(costData);
			}
			LOG.log(Level.INFO, "getCostData():: SQL: " + COST_DATA_SQL + " scenarioRunId " + scenarioRunId);
		} catch (Exception sqlException) {
			sqlException.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (prepareStmt != null) {
					prepareStmt.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (Exception sqlException) {
				sqlException.printStackTrace();
			}
		}
		return costDatas;
	}

	public List<CarrierLaneData> getCarrierLaneData(int scenarioRunId) {
		List<CarrierLaneData> carrierLaneDatas = new ArrayList<>();
		Connection connection = dbConnection.getDbConnection();
		PreparedStatement prepareStmt = null;
		ResultSet rs = null;
		try {
			prepareStmt = connection.prepareStatement(CARRIER_LANE_DATA_SQL);
			prepareStmt.setInt(1, scenarioRunId);
			rs = prepareStmt.executeQuery();
			// Iterate through the data in the result set and display it.
			while (rs.next()) {
				CarrierLaneData carrierLaneData = new CarrierLaneData();
				carrierLaneData.setCarrierId(rs.getInt("carrier_id"));
				carrierLaneData.setIlogDemandLineitemId(rs.getInt("ilog_demand_lineitem_id"));
				carrierLaneDatas.add(carrierLaneData);
			}
			LOG.log(Level.INFO,
					"getCarrierLaneData():: SQL: " + CARRIER_LANE_DATA_SQL + " scenarioRunId " + scenarioRunId);
		} catch (Exception sqlException) {
			sqlException.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (prepareStmt != null) {
					prepareStmt.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (Exception sqlException) {
				sqlException.printStackTrace();
			}
		}
		return carrierLaneDatas;
	}

	public List<GroupData> getGroupData(int scenarioRunId) {
		List<GroupData> groupDatas = new ArrayList<>();
		Connection connection = dbConnection.getDbConnection();
		PreparedStatement prepareStmt = null;
		ResultSet rs = null;
		try {
			prepareStmt = connection.prepareStatement(GROUP_DATA_SQL);
			prepareStmt.setInt(1, scenarioRunId);
			rs = prepareStmt.executeQuery();

			// Iterate through the data in the result set and display it.
			while (rs.next()) {
				GroupData groupData = new GroupData();
				groupData.setIlogGroupId(rs.getInt("groupID"));
				groupDatas.add(groupData);
			}
			LOG.log(Level.INFO, "getGroupData():: SQL: " + GROUP_DATA_SQL + " scenarioRunId " + scenarioRunId);
		} catch (Exception sqlException) {
			sqlException.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (prepareStmt != null) {
					prepareStmt.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (Exception sqlException) {
				sqlException.printStackTrace();
			}
		}
		return groupDatas;
	}

	public List<AmountGroupData> getAmountGroupData(int scenarioRunId) {
		List<AmountGroupData> amountGroupDatas = new ArrayList<>();
		Connection connection = dbConnection.getDbConnection();

		PreparedStatement prepareStmt = null;
		ResultSet rs = null;
		try {
			prepareStmt = connection.prepareStatement(AMOUNT_GROUP_DATA_SQL);
			prepareStmt.setInt(1, scenarioRunId);
			rs = prepareStmt.executeQuery();

			// Iterate through the data in the result set and display it.
			while (rs.next()) {
				AmountGroupData amountGroupData = new AmountGroupData();
				amountGroupData.setIlogCarrierAmountGroupId(rs.getInt("aGrpID"));
				amountGroupDatas.add(amountGroupData);
			}
			LOG.log(Level.INFO,
					"getAmountGroupData():: SQL: " + AMOUNT_GROUP_DATA_SQL + " scenarioRunId " + scenarioRunId);
		} catch (Exception sqlException) {
			sqlException.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (prepareStmt != null) {
					prepareStmt.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (Exception sqlException) {
				sqlException.printStackTrace();
			}
		}
		return amountGroupDatas;
	}

	public List<CarrierData> getCarrierData(int scenarioRunId) {
		List<CarrierData> carrierDatas = new ArrayList<>();
		Connection connection = dbConnection.getDbConnection();

		PreparedStatement prepareStmt = null;
		ResultSet rs = null;
		try {
			prepareStmt = connection.prepareStatement(CARRIER_DATA_SQL);
			prepareStmt.setInt(1, scenarioRunId);
			rs = prepareStmt.executeQuery();

			// Iterate through the data in the result set and display it.
			while (rs.next()) {
				CarrierData carrierData = new CarrierData();
				carrierData.setCarrierId(rs.getInt("carrier_id"));
				carrierDatas.add(carrierData);
			}
			LOG.log(Level.INFO, "getCarrierData():: SQL: " + CARRIER_DATA_SQL + " scenarioRunId " + scenarioRunId);
		} catch (Exception sqlException) {
			sqlException.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (prepareStmt != null) {
					prepareStmt.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (Exception sqlException) {
				sqlException.printStackTrace();
			}
		}
		return carrierDatas;
	}

	public List<SupplyLineItemData> getSupplyLineItemData(int scenarioRunId) {
		List<SupplyLineItemData> supplyLineItemDatas = new ArrayList<>();
		Connection connection = dbConnection.getDbConnection();

		PreparedStatement prepareStmt = null;
		ResultSet rs = null;
		try {
			prepareStmt = connection.prepareStatement(SUPPLY_LINEITEM_DATA_SQL);
			prepareStmt.setInt(1, scenarioRunId);
			rs = prepareStmt.executeQuery();

			// Iterate through the data in the result set and display it.
			while (rs.next()) {
				SupplyLineItemData supplyLineItemData = new SupplyLineItemData();
				supplyLineItemData.setSupplyLineitemId(rs.getInt("spendopti_ilog_supply_lineitem_id"));
				supplyLineItemDatas.add(supplyLineItemData);
			}
			LOG.log(Level.INFO,
					"getSupplyLineItemData():: SQL: " + SUPPLY_LINEITEM_DATA_SQL + " scenarioRunId " + scenarioRunId);
		} catch (Exception sqlException) {
			sqlException.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (prepareStmt != null) {
					prepareStmt.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (Exception sqlException) {
				sqlException.printStackTrace();
			}
		}
		return supplyLineItemDatas;
	}

	public List<LaneInfoData> getLaneInfoData(int scenarioRunId) {
		List<LaneInfoData> laneInfoDatas = new ArrayList<>();
		Connection connection = dbConnection.getDbConnection();

		PreparedStatement prepareStmt = null;
		ResultSet rs = null;
		try {
			prepareStmt = connection.prepareStatement(LANE_INFO_DATA_SQL);
			prepareStmt.setInt(1, scenarioRunId);
			rs = prepareStmt.executeQuery();

			// Iterate through the data in the result set and display it.
			while (rs.next()) {
				LaneInfoData laneInfoData = new LaneInfoData();
				laneInfoData.setDemandLineitemId(rs.getInt("demand_lineitem_id"));
				laneInfoData.setEstVolume(rs.getFloat("estvolume"));
				laneInfoData.setIlogDemandLineitemId(rs.getInt("spendopti_ilog_demand_lineitem_id"));
				laneInfoData.setPenaltyCost(rs.getFloat("penaltycost"));
				laneInfoData.setScenariorunId(rs.getInt("scenariorun_id"));
				laneInfoDatas.add(laneInfoData);
			}
			LOG.log(Level.INFO, "getLaneInfoData():: SQL: " + LANE_INFO_DATA_SQL + " scenarioRunId " + scenarioRunId);
		} catch (Exception sqlException) {
			sqlException.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (prepareStmt != null) {
					prepareStmt.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (Exception sqlException) {
				sqlException.printStackTrace();
			}
		}
		return laneInfoDatas;
	}

	public List<GroupLaneData> getGroupLaneData(int scenarioRunId) {
		List<GroupLaneData> groupLaneDatas = new ArrayList<>();
		Connection connection = dbConnection.getDbConnection();
		PreparedStatement prepareStmt = null;
		ResultSet rs = null;
		try {
			prepareStmt = connection.prepareStatement(GROUP_LANE_DATA_SQL);
			prepareStmt.setInt(1, scenarioRunId);
			rs = prepareStmt.executeQuery();

			// Iterate through the data in the result set and display it.
			while (rs.next()) {
				GroupLaneData groupLaneData = new GroupLaneData();
				groupLaneData.setIlogDemandLineitemId(rs.getInt("ilog_demand_lineitem_id"));
				groupLaneData.setIlogGroupId(rs.getInt("ilog_group_id"));
				groupLaneData.setScenariorunId(rs.getInt("scenariorun_id"));
				groupLaneDatas.add(groupLaneData);
			}
			LOG.log(Level.INFO, "getGroupLaneData():: SQL: " + GROUP_LANE_DATA_SQL + " scenarioRunId " + scenarioRunId);
		} catch (Exception sqlException) {
			sqlException.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (prepareStmt != null) {
					prepareStmt.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (Exception sqlException) {
				sqlException.printStackTrace();
			}
		}
		return groupLaneDatas;
	}

	public List<CarrierGroupData> getCarrierGroupData(int scenarioRunId) {
		List<CarrierGroupData> carrierGroupDatas = new ArrayList<>();
		Connection connection = dbConnection.getDbConnection();
		PreparedStatement prepareStmt = null;
		ResultSet rs = null;
		try {
			prepareStmt = connection.prepareStatement(CARRIER_GROUP_DATA_SQL);
			prepareStmt.setInt(1, scenarioRunId);
			rs = prepareStmt.executeQuery();

			// Iterate through the data in the result set and display it.
			while (rs.next()) {
				CarrierGroupData carrierGroupData = new CarrierGroupData();
				carrierGroupData.setCarrierId(rs.getInt("carrier_id"));
				carrierGroupData.setGroupId(rs.getInt("ilog_group_id"));
				carrierGroupData.setScenariorunId(rs.getInt("scenariorunId"));
				carrierGroupDatas.add(carrierGroupData);
			}
			LOG.log(Level.INFO,
					"getCarrierGroupData():: SQL: " + CARRIER_GROUP_DATA_SQL + " scenarioRunId " + scenarioRunId);
		} catch (Exception sqlException) {
			sqlException.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (prepareStmt != null) {
					prepareStmt.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (Exception sqlException) {
				sqlException.printStackTrace();
			}
		}
		return carrierGroupDatas;
	}

	public List<GroupVolumeBoundData> getGroupVolumeBoundData(int scenarioRunId) {
		List<GroupVolumeBoundData> groupVolumeBoundDatas = new ArrayList<>();
		Connection connection = dbConnection.getDbConnection();
		PreparedStatement prepareStmt = null;
		ResultSet rs = null;
		try {
			prepareStmt = connection.prepareStatement(GROUP_VOLUME_BOUND_DATA_SQL);
			prepareStmt.setInt(1, scenarioRunId);
			rs = prepareStmt.executeQuery();

			// Iterate through the data in the result set and display it.
			while (rs.next()) {
				GroupVolumeBoundData groupVolumeBoundData = new GroupVolumeBoundData();
				groupVolumeBoundData.setCarrierId(rs.getInt("carrier_id"));
				groupVolumeBoundData.setGroupId(rs.getInt("group_id"));
				groupVolumeBoundData.setLowerBoundCapacity(rs.getFloat("lowerbound_capacity"));
				groupVolumeBoundData.setScenariorunId(rs.getInt("scenariorun_id"));
				groupVolumeBoundData.setUpperBoundCapacity(rs.getFloat("upperbound_capacity"));
				groupVolumeBoundDatas.add(groupVolumeBoundData);
			}
			LOG.log(Level.INFO, "getGroupVolumeBoundData():: SQL: " + GROUP_VOLUME_BOUND_DATA_SQL + " scenarioRunId "
					+ scenarioRunId);
		} catch (Exception sqlException) {
			sqlException.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (prepareStmt != null) {
					prepareStmt.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (Exception sqlException) {
				sqlException.printStackTrace();
			}
		}
		return groupVolumeBoundDatas;
	}

	public List<AmountGroupBoundData> getAmountGroupBoundData(int scenarioRunId) {
		List<AmountGroupBoundData> amountGroupBoundDatas = new ArrayList<>();
		Connection connection = dbConnection.getDbConnection();
		PreparedStatement prepareStmt = null;
		ResultSet rs = null;
		try {
			prepareStmt = connection.prepareStatement(AMOUNT_GROUP_BOUND_DATA_SQL);
			prepareStmt.setInt(1, scenarioRunId);
			rs = prepareStmt.executeQuery();

			// Iterate through the data in the result set and display it.
			while (rs.next()) {
				AmountGroupBoundData amountGroupBoundData = new AmountGroupBoundData();
				amountGroupBoundData.setAmountGroupId(rs.getInt("spendopti_ilog_carrier_amount_group_id"));
				amountGroupBoundData.setAmountLowerBound(rs.getFloat("minimum_dollar_amount"));
				amountGroupBoundData.setAmoutUpperBound(rs.getFloat("maximum_dollar_amount"));
				amountGroupBoundData.setGroupId(rs.getInt("ilog_group_id"));
				amountGroupBoundData.setScenariorunId(rs.getInt("scenariorun_id"));
				amountGroupBoundDatas.add(amountGroupBoundData);
			}
			LOG.log(Level.INFO, "getAmountGroupBoundData():: SQL: " + AMOUNT_GROUP_BOUND_DATA_SQL + " scenarioRunId "
					+ scenarioRunId);
		} catch (Exception sqlException) {
			sqlException.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (prepareStmt != null) {
					prepareStmt.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (Exception sqlException) {
				sqlException.printStackTrace();
			}
		}
		return amountGroupBoundDatas;
	}

	public List<AmountGroupCarrierData> getAmountGroupCarrierData(int scenarioRunId) {
		List<AmountGroupCarrierData> amountGroupCarrierDatas = new ArrayList<>();
		Connection connection = dbConnection.getDbConnection();
		PreparedStatement prepareStmt = null;
		ResultSet rs = null;
		try {
			prepareStmt = connection.prepareStatement(AMOUNT_GROUP_CARRIER_DATA_SQL);
			prepareStmt.setInt(1, scenarioRunId);
			rs = prepareStmt.executeQuery();

			// Iterate through the data in the result set and display it.
			while (rs.next()) {
				AmountGroupCarrierData amountGroupCarrierData = new AmountGroupCarrierData();
				amountGroupCarrierData.setAmoutGroupId(rs.getInt("carrier_amount_group_id"));
				amountGroupCarrierData.setCarrierId(rs.getInt("carrier_id"));
				amountGroupCarrierData.setScenariorunId(rs.getInt("scenariorun_id"));
				amountGroupCarrierDatas.add(amountGroupCarrierData);
			}
			LOG.log(Level.INFO, "getAmountGroupCarrierData():: SQL: " + AMOUNT_GROUP_CARRIER_DATA_SQL
					+ " scenarioRunId " + scenarioRunId);
		} catch (Exception sqlException) {
			sqlException.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (prepareStmt != null) {
					prepareStmt.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (Exception sqlException) {
				sqlException.printStackTrace();
			}
		}
		return amountGroupCarrierDatas;
	}

	public List<GroupCarrierBoundData> getGroupCarrierBoundData(int scenarioRunId) {
		List<GroupCarrierBoundData> groupCarrierBoundDatas = new ArrayList<>();
		Connection connection = dbConnection.getDbConnection();
		PreparedStatement prepareStmt = null;
		ResultSet rs = null;
		try {
			prepareStmt = connection.prepareStatement(GROUP_CARRIER_BOUND_DATA_SQL);
			prepareStmt.setInt(1, scenarioRunId);
			rs = prepareStmt.executeQuery();

			// Iterate through the data in the result set and display it.
			while (rs.next()) {
				GroupCarrierBoundData groupCarrierBoundData = new GroupCarrierBoundData();
				groupCarrierBoundData.setGroupId(rs.getInt("group_id"));
				groupCarrierBoundData.setMaxNum(rs.getInt("min_num"));
				groupCarrierBoundData.setMinNum(rs.getInt("max_num"));
				groupCarrierBoundDatas.add(groupCarrierBoundData);
			}
			LOG.log(Level.INFO, "getGroupCarrierBoundData():: SQL: " + GROUP_CARRIER_BOUND_DATA_SQL + " scenarioRunId "
					+ scenarioRunId);
		} catch (Exception sqlException) {
			sqlException.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (prepareStmt != null) {
					prepareStmt.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (Exception sqlException) {
				sqlException.printStackTrace();
			}
		}
		return groupCarrierBoundDatas;
	}

	public List<ParameterData> getParameterData(int scenarioRunId) {
		List<ParameterData> parameterDatas = new ArrayList<>();
		Connection connection = dbConnection.getDbConnection();

		PreparedStatement prepareStmt = null;
		ResultSet rs = null;
		try {
			prepareStmt = connection.prepareStatement(PARAMETER_DATA_SQL);
			prepareStmt.setInt(1, scenarioRunId);
			rs = prepareStmt.executeQuery();

			// Iterate through the data in the result set and display it.
			while (rs.next()) {
				ParameterData parameterData = new ParameterData();
				parameterData.setAdminCostAboveThreshold(rs.getFloat("admin_cost_above_threshold"));
				parameterData.setAdminCostBellowThreshold(rs.getInt("admin_cost_bellow_threshold"));
				parameterData.setGlobalMaxCarrier(rs.getInt("global_max_carrier"));
				parameterData.setGlobalMinCarrier(rs.getInt("global_min_carrier"));
				parameterData.setMaxRunTime(rs.getInt("max_run_time"));
				parameterData.setMode(rs.getInt("mode"));
				parameterData.setNumberofCarrierThreshold(rs.getInt("numberof_carrier_threshold"));
				parameterData.setResultParameter(rs.getInt("result_parameter"));
				parameterData.setScenariorunId(rs.getInt("scenariorun_id"));
				parameterDatas.add(parameterData);
			}
			LOG.log(Level.INFO, "getParameterData():: SQL: " + PARAMETER_DATA_SQL + " scenarioRunId " + scenarioRunId);
		} catch (Exception sqlException) {
			sqlException.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (prepareStmt != null) {
					prepareStmt.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (Exception sqlException) {
				sqlException.printStackTrace();
			}
		}
		return parameterDatas;
	}

	public List<IncumbentCarrierLaneRawData> getIncumbentCarrierLaneRawData(int scenarioRunId) {
		List<IncumbentCarrierLaneRawData> incumbentCarrierLaneRawDatas = new ArrayList<>();
		Connection connection = dbConnection.getDbConnection();
		PreparedStatement prepareStmt = null;
		ResultSet rs = null;
		try {
			prepareStmt = connection.prepareStatement(INCUMBENT_CARRIER_LANE_RAW_DATA_SQL);
			prepareStmt.setInt(1, scenarioRunId);
			rs = prepareStmt.executeQuery();

			// Iterate through the data in the result set and display it.
			while (rs.next()) {
				IncumbentCarrierLaneRawData incumbentCarrierLaneRawData = new IncumbentCarrierLaneRawData();
				incumbentCarrierLaneRawData.setIlogDemandLineitemId(rs.getInt("spendopti_ilog_demand_lineitem_id"));
				incumbentCarrierLaneRawData.setIncumbentCarrierId(rs.getInt("carrier_id"));
				incumbentCarrierLaneRawDatas.add(incumbentCarrierLaneRawData);
			}
			LOG.log(Level.INFO, "getIncumbentCarrierLaneRawData():: SQL: " + INCUMBENT_CARRIER_LANE_RAW_DATA_SQL
					+ " scenarioRunId " + scenarioRunId);
		} catch (Exception sqlException) {
			sqlException.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (prepareStmt != null) {
					prepareStmt.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (Exception sqlException) {
				sqlException.printStackTrace();
			}
		}
		return incumbentCarrierLaneRawDatas;
	}

	public List<NoIncumbentCarrierLaneRawData> getNoIncumbentCarrierLaneRawData(int scenarioRunId) {
		List<NoIncumbentCarrierLaneRawData> noIncumbentCarrierLaneRawDatas = new ArrayList<>();
		Connection connection = dbConnection.getDbConnection();
		PreparedStatement prepareStmt = null;
		ResultSet rs = null;
		try {
			prepareStmt = connection.prepareStatement(NO_INCUMBERNT_CARRIER_LANE_RAW_DATA_SQL);
			prepareStmt.setInt(1, scenarioRunId);
			rs = prepareStmt.executeQuery();

			// Iterate through the data in the result set and display it.
			while (rs.next()) {
				NoIncumbentCarrierLaneRawData noIncumbentCarrierLaneRawData = new NoIncumbentCarrierLaneRawData();
				noIncumbentCarrierLaneRawData.setIlogDemandLineitemId(rs.getInt("ilog_demand_lineitem_id"));
				noIncumbentCarrierLaneRawData.setNoIncumbentCarrierId(rs.getInt("carrier_id"));
				noIncumbentCarrierLaneRawDatas.add(noIncumbentCarrierLaneRawData);
			}
			LOG.log(Level.INFO, "getNoIncumbentCarrierLaneRawData():: SQL: " + NO_INCUMBERNT_CARRIER_LANE_RAW_DATA_SQL
					+ " scenarioRunId " + scenarioRunId);
		} catch (Exception sqlException) {
			sqlException.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (prepareStmt != null) {
					prepareStmt.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (Exception sqlException) {
				sqlException.printStackTrace();
			}
		}
		return noIncumbentCarrierLaneRawDatas;
	}

	public List<IncumbentCarrierLaneVolumeRawData> getIncumbentCarrierLaneVolumeRawData(int scenarioRunId) {
		List<IncumbentCarrierLaneVolumeRawData> incumbentCarrierLaneVolumeRawDatas = new ArrayList<>();
		Connection connection = dbConnection.getDbConnection();
		PreparedStatement prepareStmt = null;
		ResultSet rs = null;
		try {
			prepareStmt = connection.prepareStatement(INCUMBENT_CARRIER_LANE_VOL_RAW_DATA_SQL);
			prepareStmt.setInt(1, scenarioRunId);
			rs = prepareStmt.executeQuery();

			// Iterate through the data in the result set and display it.
			while (rs.next()) {
				IncumbentCarrierLaneVolumeRawData incumbentCarrierLaneVolumeRawData = new IncumbentCarrierLaneVolumeRawData();
				incumbentCarrierLaneVolumeRawData.setCarrierId(rs.getInt("carrier_id"));
				incumbentCarrierLaneVolumeRawData
						.setIlogDemandLineitemId(rs.getInt("spendopti_ilog_demand_lineitem_id"));
				incumbentCarrierLaneVolumeRawData.setMaxVolume(rs.getFloat("max_volume"));
				incumbentCarrierLaneVolumeRawData.setMinVolume(rs.getFloat("min_volume"));
				incumbentCarrierLaneVolumeRawDatas.add(incumbentCarrierLaneVolumeRawData);
			}
			LOG.log(Level.INFO, "getIncumbentCarrierLaneVolumeRawData():: SQL: "
					+ INCUMBENT_CARRIER_LANE_VOL_RAW_DATA_SQL + " scenarioRunId " + scenarioRunId);
		} catch (Exception sqlException) {
			sqlException.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (prepareStmt != null) {
					prepareStmt.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (Exception sqlException) {
				sqlException.printStackTrace();
			}
		}
		return incumbentCarrierLaneVolumeRawDatas;
	}

	public List<SameProportionGroupData> getSameProportionGroupData(int scenarioRunId) {
		List<SameProportionGroupData> sameProportionGroupDatas = new ArrayList<>();
		Connection connection = dbConnection.getDbConnection();
		PreparedStatement prepareStmt = null;
		ResultSet rs = null;
		try {
			prepareStmt = connection.prepareStatement(SAME_PROPORTION_GROUP_DATA_SQL);
			prepareStmt.setInt(1, scenarioRunId);
			rs = prepareStmt.executeQuery();

			// Iterate through the data in the result set and display it.
			while (rs.next()) {
				SameProportionGroupData sameProportionGroupData = new SameProportionGroupData();
				sameProportionGroupData.setGroupId(rs.getInt("group_id"));
				sameProportionGroupDatas.add(sameProportionGroupData);
			}
			LOG.log(Level.INFO, "getSameProportionGroupData():: SQL: " + SAME_PROPORTION_GROUP_DATA_SQL
					+ " scenarioRunId " + scenarioRunId);
		} catch (Exception sqlException) {
			sqlException.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (prepareStmt != null) {
					prepareStmt.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (Exception sqlException) {
				sqlException.printStackTrace();
			}
		}
		return sameProportionGroupDatas;
	}

	public void updateModeloutputData(List<ModeloutputData> modeloutputDatas) {
		Connection connection = dbConnection.getDbConnection();
		PreparedStatement prepareStmt = null;
		ResultSet rs = null;
		try {
			connection.setAutoCommit(false);
			prepareStmt = connection.prepareStatement(UPDATE_MODELOUTPUTDATA);
			long count = 1;
			for (ModeloutputData modeloutputData : modeloutputDatas) {
				int parameterIndex = 1;
				prepareStmt.setFloat(parameterIndex++, modeloutputData.getAllocation());
				prepareStmt.setInt(parameterIndex++, modeloutputData.getIlogSupplyLineitemId());
				prepareStmt.addBatch();
				if ((count % batchSize) == 0) {
					prepareStmt.executeBatch();
				}
				count++;
			}
			// flush batches if any
			if (batchSize != 0) {
				prepareStmt.executeBatch();
			}
			connection.commit();
			prepareStmt.execute();
			LOG.log(Level.INFO, "updateModeloutputData():: SQL: " + UPDATE_MODELOUTPUTDATA);
		} catch (Exception sqlException) {
			sqlException.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (prepareStmt != null) {
					prepareStmt.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (Exception sqlException) {
				sqlException.printStackTrace();
			}
		}
	}

	public void insertModeloutputData(List<DeficitoutputData> deficitoutputDatas) {
		Connection connection = dbConnection.getDbConnection();
		PreparedStatement prepareStmt = null;
		ResultSet rs = null;
		try {
			connection.setAutoCommit(false);
			prepareStmt = connection.prepareStatement(INSERT_DEFICITOUTPUTDATA);
			long count = 1;
			for (DeficitoutputData deficitoutputData : deficitoutputDatas) {
				int parameterIndex = 1;
				prepareStmt.setInt(parameterIndex++, deficitoutputData.getScenarioId());
				prepareStmt.setInt(parameterIndex++, deficitoutputData.getIlogDemandLineitemId());
				prepareStmt.setFloat(parameterIndex++, deficitoutputData.getAllocation());
				prepareStmt.addBatch();
				if ((count % batchSize) == 0) {
					prepareStmt.executeBatch();
				}
				count++;
			}
			// flush batches if any
			if (batchSize != 0) {
				prepareStmt.executeBatch();
			}
			connection.commit();
			LOG.log(Level.INFO, "insertModeloutputData():: SQL: " + UPDATE_MODELOUTPUTDATA);
		} catch (Exception sqlException) {
			sqlException.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (prepareStmt != null) {
					prepareStmt.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (Exception sqlException) {
				sqlException.printStackTrace();
			}
		}
	}

}
