package cplex.java.customdatasource;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import cplex.java.customdatasource.model.AmountGroupBoundData;
import cplex.java.customdatasource.model.AmountGroupCarrierData;
import cplex.java.customdatasource.model.AmountGroupData;
import cplex.java.customdatasource.model.CarrierData;
import cplex.java.customdatasource.model.CarrierGroupData;
import cplex.java.customdatasource.model.CarrierLaneData;
import cplex.java.customdatasource.model.CostData;
import cplex.java.customdatasource.model.GroupCarrierBoundData;
import cplex.java.customdatasource.model.GroupData;
import cplex.java.customdatasource.model.GroupLaneData;
import cplex.java.customdatasource.model.GroupVolumeBoundData;
import cplex.java.customdatasource.model.IncumbentCarrierLaneRawData;
import cplex.java.customdatasource.model.IncumbentCarrierLaneVolumeRawData;
import cplex.java.customdatasource.model.LaneInfoData;
import cplex.java.customdatasource.model.NoIncumbentCarrierLaneRawData;
import cplex.java.customdatasource.model.ParameterData;
import cplex.java.customdatasource.model.SameProportionGroupData;
import cplex.java.customdatasource.model.SpendoptiModelRequest;
import cplex.java.customdatasource.model.SupplyLineItemData;
import ilog.opl.IloCustomOplDataSource;
import ilog.opl.IloOplDataHandler;
import ilog.opl.IloOplElement;
import ilog.opl.IloOplElementDefinition;
import ilog.opl.IloOplElementDefinitionType.Type;
import ilog.opl.IloOplFactory;
import ilog.opl.IloOplModel;
import ilog.opl.IloOplModelDefinition;
import ilog.opl.IloOplTupleSchemaDefinition;
import ilog.opl_core.cppimpl.IloTupleSchema;

/**
 * An custom data source reading data using JDBC.
 *
 */
public class SpendOptiScenarioJSONCustomDataSource extends IloCustomOplDataSource {
	private SpendOptiJdbcConfiguration configuration;
	private IloOplModelDefinition modelDef;
	private SpendOptiObjectTransformer spendOptiObjectTransformer;

	/**
	 * Adds a custom data source to a model.
	 *
	 * @param xmlFile The xml configuration for the data source
	 * @param model   The OPL Model
	 * @return the custom datasource
	 */
	public static SpendOptiScenarioJSONCustomDataSource addDataSourceXMLConfig(String xmlFile, IloOplModel model,
			SpendOptiObjectTransformer spendOptiObjectTransformer) throws IOException {
		SpendOptiJdbcConfiguration config = new SpendOptiJdbcConfiguration();
		config.read(xmlFile);
		return addDataSource(config, model, spendOptiObjectTransformer);
	}

	/**
	 * Adds a custom data source to a model.
	 *
	 * @param config The JDBC configuration object
	 * @param model  The OPL Model
	 * @return the custom datasource
	 */
	public static SpendOptiScenarioJSONCustomDataSource addDataSource(SpendOptiJdbcConfiguration config,
			IloOplModel model, SpendOptiObjectTransformer spendOptiObjectTransformer) {
		IloOplFactory factory = IloOplFactory.getOplFactoryFrom(model);
		IloOplModelDefinition definition = model.getModelDefinition();
		SpendOptiScenarioJSONCustomDataSource source = new SpendOptiScenarioJSONCustomDataSource(config, factory,
				definition, spendOptiObjectTransformer);
		model.addDataSource(source);
		return source;
	}

	/**
	 * Creates a new JDBC custom data source, based on the specified configuration.
	 * 
	 * @param configuration The JDBC data source configuration.
	 * @param oplF          The OPL factory.
	 * @param def           The OPL Model definition.
	 */
	public SpendOptiScenarioJSONCustomDataSource(SpendOptiJdbcConfiguration configuration, IloOplFactory oplF,
			IloOplModelDefinition modelDef, SpendOptiObjectTransformer spendOptiObjectTransformer) {
		super(oplF);
		this.configuration = configuration;
		this.modelDef = modelDef;
		this.spendOptiObjectTransformer = spendOptiObjectTransformer;
	}

	void fillNamesAndTypes(IloTupleSchema schema, String[] names, Type[] types) {
		IloOplElementDefinition elementDefinition = modelDef.getElementDefinition(schema.getName());
		IloOplTupleSchemaDefinition tupleSchema = elementDefinition.asTupleSchema();
		for (int i = 0; i < schema.getSize(); i++) {
			String columnName = schema.getColumnName(i);
			types[i] = tupleSchema.getComponent(i).getElementDefinitionType();
			names[i] = columnName;
		}
	}

	/**
	 * Overrides the IloCustomOplDataSource method to read data when the model is
	 * generated.
	 */
	@Override
	public void customRead() {
		long startTime = System.currentTimeMillis();
		System.out.println("Reading elements from database");
		Map<String, String[]> dataMapping = configuration.getInputDataMapping();
		Set<Entry<String, String[]>> entrySet = dataMapping.entrySet();
		for (Entry<String, String[]> entry : entrySet) {
			String name = entry.getKey();
			String[] fields = entry.getValue();
			/*
			 * System.out.print("Processing data for " + name + " with fields ["); int i =
			 * 0; for (String s : fields) { System.out.print(s); if (i < fields.length - 1)
			 * { System.out.print(", "); i++; } } System.out.print("]");
			 * System.out.println();
			 */
			IloOplElementDefinition def = modelDef.getElementDefinition(name);
			System.out.println("Name: " + name + " Type: " + def.getElementDefinitionType() + " Leaf: "
					+ def.getLeaf().getElementDefinitionType());
			processData(name, fields, def);
		}
		long endTime = System.currentTimeMillis();
		System.out.println("Done (" + (endTime - startTime) / 1000.0 + " s)");
	}

	public void processData(String name, String[] fields, IloOplElementDefinition def) {
		SpendoptiModelRequest SpendoptiModelRequest = (SpendoptiModelRequest) spendOptiObjectTransformer
				.spendoptiJsonToJavaObjectCreator();
		switch (name) {
		case "costData":
			processDataForCostData(def, name, SpendoptiModelRequest.getCostData());
			break;
		case "carrierLaneData":
			processDataForCarrierLaneData(def, name, SpendoptiModelRequest.getCarrierLaneData());
			break;
		case "groupData":
			processDataForGroupData(def, name, SpendoptiModelRequest.getGroupData());
			break;
		case "aGrpData":
			processDataForAmountGroupData(def, name, SpendoptiModelRequest.getAmountGroupData());
			break;
		case "carrierData":
			processDataForCarrierData(def, name, SpendoptiModelRequest.getCarrierData());
			break;
		case "supplyLineItemData":
			processDataForSupplyLineItemData(def, name, SpendoptiModelRequest.getSupplyLineItemData());
			break;
		case "laneinfoData":
			processDataForLaneInfoData(def, name, SpendoptiModelRequest.getLaneInfoData());
			break;
		case "grouplaneData":
			processDataForGroupLaneData(def, name, SpendoptiModelRequest.getGroupLaneData());
			break;
		case "carrierGroupData":
			processDataForCarrierGroupData(def, name, SpendoptiModelRequest.getCarrierGroupData());
			break;
		case "groupvolumeboundData":
			processDataForGroupVolumeBoundData(def, name, SpendoptiModelRequest.getGroupVolumeBoundData());
			break;
		case "amtGroupBoundData":
			processDataForAmountGroupBoundData(def, name, SpendoptiModelRequest.getAmountGroupBoundData());
			break;
		case "amtGroupCarrierData":
			processDataForAmountGroupCarrierData(def, name, SpendoptiModelRequest.getAmountGroupCarrierData());
			break;
		case "groupcarrierboundData":
			processDataForGroupCarrierBoundData(def, name, SpendoptiModelRequest.getGroupCarrierBoundData());
			break;
		case "parameterData":
			processDataForParameterData(def, name, SpendoptiModelRequest.getParameterData());
			break;
		case "iCarrierLaneRawData":
			processDataForIncumbentCarrierLaneRawData(def, name,
					SpendoptiModelRequest.getIncumbentCarrierLaneRawData());
			break;
		case "nICarrierLaneRawData":
			processDataForNoIncumbentCarrierLaneRawData(def, name,
					SpendoptiModelRequest.getNoIncumbentCarrierLaneRawData());
			break;
		case "iCarrierLaneVolRawData":
			processDataForIncumbentCarrierLaneVolumeRawData(def, name,
					SpendoptiModelRequest.getIncumbentCarrierLaneVolmeRawData());
			break;
		case "sameProportionGrpData":
			processDataForSameProportionGroupData(def, name, SpendoptiModelRequest.getSameProportionGroupData());
			break;
		default:
			break;
		}
	}

	private void processDataForCostData(IloOplElementDefinition def, String name, List<CostData> costDatas) {
		Type type = def.getElementDefinitionType();
		Type leaf = def.getLeaf().getElementDefinitionType();
		if (type == Type.SET) {
			IloOplDataHandler handler = getDataHandler();
			IloOplElement elt = handler.getElement(name);
			ilog.opl_core.cppimpl.IloTupleSet tupleSet = (ilog.opl_core.cppimpl.IloTupleSet) elt.asTupleSet();
			IloTupleSchema schema = tupleSet.getSchema_cpp();
			int size = schema.getTotalColumnNumber();

			String[] oplFieldsName = new String[size];
			Type[] oplFieldsType = new Type[size];

			fillNamesAndTypes(schema, oplFieldsName, oplFieldsType);
			handler.startElement(name);
			handler.startSet();

			if (costDatas != null && !costDatas.isEmpty()) {
				for (CostData costData : costDatas) {
					if (leaf == Type.TUPLE) {
						handler.startTuple();
					}
					handler.addIntItem(costData.getScenariorunId());
					handler.addIntItem(costData.getSpendoptiIlogSupplyLineitemId());
					handler.addIntItem(costData.getSupplyLineitemId());
					handler.addIntItem(costData.getCarrierId());
					handler.addIntItem(costData.getIlogDemandLineitemId());
					handler.addNumItem(costData.getTotalPrice());
					handler.addNumItem(costData.getAllocation());
					if (leaf == Type.TUPLE) {
						handler.endTuple();
					}
				}
			}
			handler.endSet();
			handler.endElement();
		} else {
			throw new IllegalArgumentException("Cannot read element " + name + " of type " + type);
		}
	}

	private void processDataForCarrierLaneData(IloOplElementDefinition def, String name,
			List<CarrierLaneData> carrierLaneDatas) {
		Type type = def.getElementDefinitionType();
		Type leaf = def.getLeaf().getElementDefinitionType();
		if (type == Type.SET) {
			IloOplDataHandler handler = getDataHandler();
			IloOplElement elt = handler.getElement(name);
			ilog.opl_core.cppimpl.IloTupleSet tupleSet = (ilog.opl_core.cppimpl.IloTupleSet) elt.asTupleSet();
			IloTupleSchema schema = tupleSet.getSchema_cpp();
			int size = schema.getTotalColumnNumber();

			String[] oplFieldsName = new String[size];
			Type[] oplFieldsType = new Type[size];

			fillNamesAndTypes(schema, oplFieldsName, oplFieldsType);
			handler.startElement(name);
			handler.startSet();

			if (carrierLaneDatas != null && !carrierLaneDatas.isEmpty()) {
				for (CarrierLaneData carrierLaneData : carrierLaneDatas) {
					if (leaf == Type.TUPLE) {
						handler.startTuple();
					}
					handler.addIntItem(carrierLaneData.getCarrierId());
					handler.addIntItem(carrierLaneData.getIlogDemandLineitemId());
					if (leaf == Type.TUPLE) {
						handler.endTuple();
					}
				}
			}
			handler.endSet();
			handler.endElement();
		} else {
			throw new IllegalArgumentException("Cannot read element " + name + " of type " + type);
		}
	}

	private void processDataForGroupData(IloOplElementDefinition def, String name, List<GroupData> groupDatas) {
		Type type = def.getElementDefinitionType();
		Type leaf = def.getLeaf().getElementDefinitionType();
		if (type == Type.SET) {
			IloOplDataHandler handler = getDataHandler();
			IloOplElement elt = handler.getElement(name);
			ilog.opl_core.cppimpl.IloTupleSet tupleSet = (ilog.opl_core.cppimpl.IloTupleSet) elt.asTupleSet();
			IloTupleSchema schema = tupleSet.getSchema_cpp();
			int size = schema.getTotalColumnNumber();

			String[] oplFieldsName = new String[size];
			Type[] oplFieldsType = new Type[size];

			fillNamesAndTypes(schema, oplFieldsName, oplFieldsType);
			handler.startElement(name);
			handler.startSet();

			if (groupDatas != null && !groupDatas.isEmpty()) {
				for (GroupData groupData : groupDatas) {
					if (leaf == Type.TUPLE) {
						handler.startTuple();
					}
					handler.addIntItem(groupData.getIlogGroupId());
					if (leaf == Type.TUPLE) {
						handler.endTuple();
					}
				}
			}
			handler.endSet();
			handler.endElement();
		} else {
			throw new IllegalArgumentException("Cannot read element " + name + " of type " + type);
		}
	}

	private void processDataForAmountGroupData(IloOplElementDefinition def, String name,
			List<AmountGroupData> amountGroupDatas) {
		Type type = def.getElementDefinitionType();
		Type leaf = def.getLeaf().getElementDefinitionType();
		if (type == Type.SET) {
			IloOplDataHandler handler = getDataHandler();
			IloOplElement elt = handler.getElement(name);
			ilog.opl_core.cppimpl.IloTupleSet tupleSet = (ilog.opl_core.cppimpl.IloTupleSet) elt.asTupleSet();
			IloTupleSchema schema = tupleSet.getSchema_cpp();
			int size = schema.getTotalColumnNumber();

			String[] oplFieldsName = new String[size];
			Type[] oplFieldsType = new Type[size];

			fillNamesAndTypes(schema, oplFieldsName, oplFieldsType);
			handler.startElement(name);
			handler.startSet();

			if (amountGroupDatas != null && !amountGroupDatas.isEmpty()) {
				for (AmountGroupData amountGroupData : amountGroupDatas) {
					if (leaf == Type.TUPLE) {
						handler.startTuple();
					}
					handler.addIntItem(amountGroupData.getIlogCarrierAmountGroupId());
					if (leaf == Type.TUPLE) {
						handler.endTuple();
					}
				}
			}
			handler.endSet();
			handler.endElement();
		} else {
			throw new IllegalArgumentException("Cannot read element " + name + " of type " + type);
		}
	}

	private void processDataForCarrierData(IloOplElementDefinition def, String name, List<CarrierData> carrierDatas) {
		Type type = def.getElementDefinitionType();
		Type leaf = def.getLeaf().getElementDefinitionType();
		if (type == Type.SET) {
			IloOplDataHandler handler = getDataHandler();
			IloOplElement elt = handler.getElement(name);
			ilog.opl_core.cppimpl.IloTupleSet tupleSet = (ilog.opl_core.cppimpl.IloTupleSet) elt.asTupleSet();
			IloTupleSchema schema = tupleSet.getSchema_cpp();
			int size = schema.getTotalColumnNumber();

			String[] oplFieldsName = new String[size];
			Type[] oplFieldsType = new Type[size];

			fillNamesAndTypes(schema, oplFieldsName, oplFieldsType);
			handler.startElement(name);
			handler.startSet();

			if (carrierDatas != null && !carrierDatas.isEmpty()) {
				for (CarrierData carrierData : carrierDatas) {
					if (leaf == Type.TUPLE) {
						handler.startTuple();
					}
					handler.addIntItem(carrierData.getCarrierId());
					if (leaf == Type.TUPLE) {
						handler.endTuple();
					}
				}
			}
			handler.endSet();
			handler.endElement();
		} else {
			throw new IllegalArgumentException("Cannot read element " + name + " of type " + type);
		}
	}

	private void processDataForSupplyLineItemData(IloOplElementDefinition def, String name,
			List<SupplyLineItemData> supplyLineItemDatas) {
		Type type = def.getElementDefinitionType();
		Type leaf = def.getLeaf().getElementDefinitionType();
		if (type == Type.SET) {
			IloOplDataHandler handler = getDataHandler();
			IloOplElement elt = handler.getElement(name);
			ilog.opl_core.cppimpl.IloTupleSet tupleSet = (ilog.opl_core.cppimpl.IloTupleSet) elt.asTupleSet();
			IloTupleSchema schema = tupleSet.getSchema_cpp();
			int size = schema.getTotalColumnNumber();

			String[] oplFieldsName = new String[size];
			Type[] oplFieldsType = new Type[size];

			fillNamesAndTypes(schema, oplFieldsName, oplFieldsType);
			handler.startElement(name);
			handler.startSet();

			if (supplyLineItemDatas != null && !supplyLineItemDatas.isEmpty()) {
				for (SupplyLineItemData supplyLineItemData : supplyLineItemDatas) {
					if (leaf == Type.TUPLE) {
						handler.startTuple();
					}
					handler.addIntItem(supplyLineItemData.getSupplyLineitemId());
					if (leaf == Type.TUPLE) {
						handler.endTuple();
					}
				}
			}
			handler.endSet();
			handler.endElement();
		} else {
			throw new IllegalArgumentException("Cannot read element " + name + " of type " + type);
		}
	}

	private void processDataForLaneInfoData(IloOplElementDefinition def, String name,
			List<LaneInfoData> laneInfoDatas) {
		Type type = def.getElementDefinitionType();
		Type leaf = def.getLeaf().getElementDefinitionType();
		if (type == Type.SET) {
			IloOplDataHandler handler = getDataHandler();
			IloOplElement elt = handler.getElement(name);
			ilog.opl_core.cppimpl.IloTupleSet tupleSet = (ilog.opl_core.cppimpl.IloTupleSet) elt.asTupleSet();
			IloTupleSchema schema = tupleSet.getSchema_cpp();
			int size = schema.getTotalColumnNumber();

			String[] oplFieldsName = new String[size];
			Type[] oplFieldsType = new Type[size];

			fillNamesAndTypes(schema, oplFieldsName, oplFieldsType);
			handler.startElement(name);
			handler.startSet();

			if (laneInfoDatas != null && !laneInfoDatas.isEmpty()) {
				for (LaneInfoData laneInfoData : laneInfoDatas) {
					if (leaf == Type.TUPLE) {
						handler.startTuple();
					}
					handler.addIntItem(laneInfoData.getScenariorunId());
					handler.addIntItem(laneInfoData.getIlogDemandLineitemId());
					handler.addIntItem(laneInfoData.getDemandLineitemId());
					handler.addNumItem(laneInfoData.getEstVolume());
					handler.addNumItem(laneInfoData.getPenaltyCost());
					if (leaf == Type.TUPLE) {
						handler.endTuple();
					}
				}
			}
			handler.endSet();
			handler.endElement();
		} else {
			throw new IllegalArgumentException("Cannot read element " + name + " of type " + type);
		}
	}

	private void processDataForGroupLaneData(IloOplElementDefinition def, String name,
			List<GroupLaneData> groupLaneDatas) {
		Type type = def.getElementDefinitionType();
		Type leaf = def.getLeaf().getElementDefinitionType();
		if (type == Type.SET) {
			IloOplDataHandler handler = getDataHandler();
			IloOplElement elt = handler.getElement(name);
			ilog.opl_core.cppimpl.IloTupleSet tupleSet = (ilog.opl_core.cppimpl.IloTupleSet) elt.asTupleSet();
			IloTupleSchema schema = tupleSet.getSchema_cpp();
			int size = schema.getTotalColumnNumber();

			String[] oplFieldsName = new String[size];
			Type[] oplFieldsType = new Type[size];

			fillNamesAndTypes(schema, oplFieldsName, oplFieldsType);
			handler.startElement(name);
			handler.startSet();

			if (groupLaneDatas != null && !groupLaneDatas.isEmpty()) {
				for (GroupLaneData groupLaneData : groupLaneDatas) {
					if (leaf == Type.TUPLE) {
						handler.startTuple();
					}
					handler.addIntItem(groupLaneData.getScenariorunId());
					handler.addIntItem(groupLaneData.getIlogGroupId());
					handler.addIntItem(groupLaneData.getIlogDemandLineitemId());
					if (leaf == Type.TUPLE) {
						handler.endTuple();
					}
				}
			}
			handler.endSet();
			handler.endElement();
		} else {
			throw new IllegalArgumentException("Cannot read element " + name + " of type " + type);
		}
	}

	private void processDataForCarrierGroupData(IloOplElementDefinition def, String name,
			List<CarrierGroupData> carrierGroupDatas) {
		Type type = def.getElementDefinitionType();
		Type leaf = def.getLeaf().getElementDefinitionType();
		if (type == Type.SET) {
			IloOplDataHandler handler = getDataHandler();
			IloOplElement elt = handler.getElement(name);
			ilog.opl_core.cppimpl.IloTupleSet tupleSet = (ilog.opl_core.cppimpl.IloTupleSet) elt.asTupleSet();
			IloTupleSchema schema = tupleSet.getSchema_cpp();
			int size = schema.getTotalColumnNumber();

			String[] oplFieldsName = new String[size];
			Type[] oplFieldsType = new Type[size];

			fillNamesAndTypes(schema, oplFieldsName, oplFieldsType);
			handler.startElement(name);
			handler.startSet();

			if (carrierGroupDatas != null && !carrierGroupDatas.isEmpty()) {
				for (CarrierGroupData carrierGroupData : carrierGroupDatas) {
					if (leaf == Type.TUPLE) {
						handler.startTuple();
					}
					handler.addIntItem(carrierGroupData.getScenariorunId());
					handler.addIntItem(carrierGroupData.getCarrierId());
					handler.addIntItem(carrierGroupData.getGroupId());
					if (leaf == Type.TUPLE) {
						handler.endTuple();
					}
				}
			}
			handler.endSet();
			handler.endElement();
		} else {
			throw new IllegalArgumentException("Cannot read element " + name + " of type " + type);
		}
	}

	private void processDataForGroupVolumeBoundData(IloOplElementDefinition def, String name,
			List<GroupVolumeBoundData> groupVolumeBoundDatas) {
		Type type = def.getElementDefinitionType();
		Type leaf = def.getLeaf().getElementDefinitionType();
		if (type == Type.SET) {
			IloOplDataHandler handler = getDataHandler();
			IloOplElement elt = handler.getElement(name);
			ilog.opl_core.cppimpl.IloTupleSet tupleSet = (ilog.opl_core.cppimpl.IloTupleSet) elt.asTupleSet();
			IloTupleSchema schema = tupleSet.getSchema_cpp();
			int size = schema.getTotalColumnNumber();

			String[] oplFieldsName = new String[size];
			Type[] oplFieldsType = new Type[size];

			fillNamesAndTypes(schema, oplFieldsName, oplFieldsType);
			handler.startElement(name);
			handler.startSet();

			if (groupVolumeBoundDatas != null && !groupVolumeBoundDatas.isEmpty()) {
				for (GroupVolumeBoundData groupVolumeBoundData : groupVolumeBoundDatas) {
					if (leaf == Type.TUPLE) {
						handler.startTuple();
					}
					handler.addIntItem(groupVolumeBoundData.getGroupId());
					handler.addIntItem(groupVolumeBoundData.getScenariorunId());
					handler.addIntItem(groupVolumeBoundData.getCarrierId());
					handler.addNumItem(groupVolumeBoundData.getLowerBoundCapacity());
					handler.addNumItem(groupVolumeBoundData.getUpperBoundCapacity());
					if (leaf == Type.TUPLE) {
						handler.endTuple();
					}
				}
			}
			handler.endSet();
			handler.endElement();
		} else {
			throw new IllegalArgumentException("Cannot read element " + name + " of type " + type);
		}
	}

	private void processDataForAmountGroupBoundData(IloOplElementDefinition def, String name,
			List<AmountGroupBoundData> amountGroupBoundDatas) {
		Type type = def.getElementDefinitionType();
		Type leaf = def.getLeaf().getElementDefinitionType();
		if (type == Type.SET) {
			IloOplDataHandler handler = getDataHandler();
			IloOplElement elt = handler.getElement(name);
			ilog.opl_core.cppimpl.IloTupleSet tupleSet = (ilog.opl_core.cppimpl.IloTupleSet) elt.asTupleSet();
			IloTupleSchema schema = tupleSet.getSchema_cpp();
			int size = schema.getTotalColumnNumber();

			String[] oplFieldsName = new String[size];
			Type[] oplFieldsType = new Type[size];

			fillNamesAndTypes(schema, oplFieldsName, oplFieldsType);
			handler.startElement(name);
			handler.startSet();

			if (amountGroupBoundDatas != null && !amountGroupBoundDatas.isEmpty()) {
				for (AmountGroupBoundData amountGroupBoundData : amountGroupBoundDatas) {
					if (leaf == Type.TUPLE) {
						handler.startTuple();
					}
					handler.addIntItem(amountGroupBoundData.getAmountGroupId());
					handler.addIntItem(amountGroupBoundData.getScenariorunId());
					handler.addIntItem(amountGroupBoundData.getGroupId());
					handler.addNumItem(amountGroupBoundData.getAmountLowerBound());
					handler.addNumItem(amountGroupBoundData.getAmoutUpperBound());
					if (leaf == Type.TUPLE) {
						handler.endTuple();
					}
				}
			}
			handler.endSet();
			handler.endElement();
		} else {
			throw new IllegalArgumentException("Cannot read element " + name + " of type " + type);
		}
	}

	private void processDataForAmountGroupCarrierData(IloOplElementDefinition def, String name,
			List<AmountGroupCarrierData> amountGroupCarrierDatas) {
		Type type = def.getElementDefinitionType();
		Type leaf = def.getLeaf().getElementDefinitionType();
		if (type == Type.SET) {
			IloOplDataHandler handler = getDataHandler();
			IloOplElement elt = handler.getElement(name);
			ilog.opl_core.cppimpl.IloTupleSet tupleSet = (ilog.opl_core.cppimpl.IloTupleSet) elt.asTupleSet();
			IloTupleSchema schema = tupleSet.getSchema_cpp();
			int size = schema.getTotalColumnNumber();

			String[] oplFieldsName = new String[size];
			Type[] oplFieldsType = new Type[size];

			fillNamesAndTypes(schema, oplFieldsName, oplFieldsType);
			handler.startElement(name);
			handler.startSet();

			if (amountGroupCarrierDatas != null && !amountGroupCarrierDatas.isEmpty()) {
				for (AmountGroupCarrierData amountGroupCarrierData : amountGroupCarrierDatas) {
					if (leaf == Type.TUPLE) {
						handler.startTuple();
					}
					handler.addIntItem(amountGroupCarrierData.getAmoutGroupId());
					handler.addIntItem(amountGroupCarrierData.getScenariorunId());
					handler.addIntItem(amountGroupCarrierData.getCarrierId());
					if (leaf == Type.TUPLE) {
						handler.endTuple();
					}
				}
			}
			handler.endSet();
			handler.endElement();
		} else {
			throw new IllegalArgumentException("Cannot read element " + name + " of type " + type);
		}
	}

	private void processDataForGroupCarrierBoundData(IloOplElementDefinition def, String name,
			List<GroupCarrierBoundData> groupCarrierBoundDatas) {
		Type type = def.getElementDefinitionType();
		Type leaf = def.getLeaf().getElementDefinitionType();
		if (type == Type.SET) {
			IloOplDataHandler handler = getDataHandler();
			IloOplElement elt = handler.getElement(name);
			ilog.opl_core.cppimpl.IloTupleSet tupleSet = (ilog.opl_core.cppimpl.IloTupleSet) elt.asTupleSet();
			IloTupleSchema schema = tupleSet.getSchema_cpp();
			int size = schema.getTotalColumnNumber();

			String[] oplFieldsName = new String[size];
			Type[] oplFieldsType = new Type[size];

			fillNamesAndTypes(schema, oplFieldsName, oplFieldsType);
			handler.startElement(name);
			handler.startSet();

			if (groupCarrierBoundDatas != null && !groupCarrierBoundDatas.isEmpty()) {
				for (GroupCarrierBoundData groupCarrierBoundData : groupCarrierBoundDatas) {
					if (leaf == Type.TUPLE) {
						handler.startTuple();
					}
					handler.addIntItem(groupCarrierBoundData.getGroupId());
					handler.addIntItem(groupCarrierBoundData.getMinNum());
					handler.addIntItem(groupCarrierBoundData.getMaxNum());
					if (leaf == Type.TUPLE) {
						handler.endTuple();
					}
				}
			}
			handler.endSet();
			handler.endElement();
		} else {
			throw new IllegalArgumentException("Cannot read element " + name + " of type " + type);
		}
	}

	private void processDataForParameterData(IloOplElementDefinition def, String name,
			List<ParameterData> parameterDatas) {
		Type type = def.getElementDefinitionType();
		Type leaf = def.getLeaf().getElementDefinitionType();
		if (type == Type.SET) {
			IloOplDataHandler handler = getDataHandler();
			IloOplElement elt = handler.getElement(name);
			ilog.opl_core.cppimpl.IloTupleSet tupleSet = (ilog.opl_core.cppimpl.IloTupleSet) elt.asTupleSet();
			IloTupleSchema schema = tupleSet.getSchema_cpp();
			int size = schema.getTotalColumnNumber();

			String[] oplFieldsName = new String[size];
			Type[] oplFieldsType = new Type[size];

			fillNamesAndTypes(schema, oplFieldsName, oplFieldsType);
			handler.startElement(name);
			handler.startSet();

			if (parameterDatas != null && !parameterDatas.isEmpty()) {
				for (ParameterData parameterData : parameterDatas) {
					if (leaf == Type.TUPLE) {
						handler.startTuple();
					}
					handler.addIntItem(parameterData.getScenariorunId());
					handler.addIntItem(parameterData.getGlobalMinCarrier());
					handler.addIntItem(parameterData.getGlobalMaxCarrier());
					handler.addIntItem(parameterData.getNumberofCarrierThreshold());
					handler.addNumItem(parameterData.getAdminCostBellowThreshold());
					handler.addNumItem(parameterData.getAdminCostAboveThreshold());
					handler.addIntItem(parameterData.getMode());
					handler.addIntItem(parameterData.getResultParameter());
					handler.addIntItem(parameterData.getMaxRunTime());
					if (leaf == Type.TUPLE) {
						handler.endTuple();
					}
				}
			}
			handler.endSet();
			handler.endElement();
		} else {
			throw new IllegalArgumentException("Cannot read element " + name + " of type " + type);
		}
	}

	private void processDataForIncumbentCarrierLaneRawData(IloOplElementDefinition def, String name,
			List<IncumbentCarrierLaneRawData> incumbentCarrierLaneRawDatas) {
		Type type = def.getElementDefinitionType();
		Type leaf = def.getLeaf().getElementDefinitionType();
		if (type == Type.SET) {
			IloOplDataHandler handler = getDataHandler();
			IloOplElement elt = handler.getElement(name);
			ilog.opl_core.cppimpl.IloTupleSet tupleSet = (ilog.opl_core.cppimpl.IloTupleSet) elt.asTupleSet();
			IloTupleSchema schema = tupleSet.getSchema_cpp();
			int size = schema.getTotalColumnNumber();

			String[] oplFieldsName = new String[size];
			Type[] oplFieldsType = new Type[size];

			fillNamesAndTypes(schema, oplFieldsName, oplFieldsType);
			handler.startElement(name);
			handler.startSet();

			if (incumbentCarrierLaneRawDatas != null && !incumbentCarrierLaneRawDatas.isEmpty()) {
				for (IncumbentCarrierLaneRawData incumbentCarrierLaneRawData : incumbentCarrierLaneRawDatas) {
					if (leaf == Type.TUPLE) {
						handler.startTuple();
					}
					handler.addIntItem(incumbentCarrierLaneRawData.getIncumbentCarrierId());
					handler.addIntItem(incumbentCarrierLaneRawData.getIlogDemandLineitemId());
					if (leaf == Type.TUPLE) {
						handler.endTuple();
					}
				}
			}
			handler.endSet();
			handler.endElement();
		} else {
			throw new IllegalArgumentException("Cannot read element " + name + " of type " + type);
		}
	}

	private void processDataForNoIncumbentCarrierLaneRawData(IloOplElementDefinition def, String name,
			List<NoIncumbentCarrierLaneRawData> noIncumbentCarrierLaneRawDatas) {
		Type type = def.getElementDefinitionType();
		Type leaf = def.getLeaf().getElementDefinitionType();
		if (type == Type.SET) {
			IloOplDataHandler handler = getDataHandler();
			IloOplElement elt = handler.getElement(name);
			ilog.opl_core.cppimpl.IloTupleSet tupleSet = (ilog.opl_core.cppimpl.IloTupleSet) elt.asTupleSet();
			IloTupleSchema schema = tupleSet.getSchema_cpp();
			int size = schema.getTotalColumnNumber();

			String[] oplFieldsName = new String[size];
			Type[] oplFieldsType = new Type[size];

			fillNamesAndTypes(schema, oplFieldsName, oplFieldsType);
			handler.startElement(name);
			handler.startSet();

			if (noIncumbentCarrierLaneRawDatas != null && !noIncumbentCarrierLaneRawDatas.isEmpty()) {
				for (NoIncumbentCarrierLaneRawData noIncumbentCarrierLaneRawData : noIncumbentCarrierLaneRawDatas) {
					if (leaf == Type.TUPLE) {
						handler.startTuple();
					}
					handler.addIntItem(noIncumbentCarrierLaneRawData.getNoIncumbentCarrierId());
					handler.addIntItem(noIncumbentCarrierLaneRawData.getIlogDemandLineitemId());
					if (leaf == Type.TUPLE) {
						handler.endTuple();
					}
				}
			}
			handler.endSet();
			handler.endElement();
		} else {
			throw new IllegalArgumentException("Cannot read element " + name + " of type " + type);
		}
	}

	private void processDataForIncumbentCarrierLaneVolumeRawData(IloOplElementDefinition def, String name,
			List<IncumbentCarrierLaneVolumeRawData> incumbentCarrierLaneVolumeRawDatas) {
		Type type = def.getElementDefinitionType();
		Type leaf = def.getLeaf().getElementDefinitionType();
		if (type == Type.SET) {
			IloOplDataHandler handler = getDataHandler();
			IloOplElement elt = handler.getElement(name);
			ilog.opl_core.cppimpl.IloTupleSet tupleSet = (ilog.opl_core.cppimpl.IloTupleSet) elt.asTupleSet();
			IloTupleSchema schema = tupleSet.getSchema_cpp();
			int size = schema.getTotalColumnNumber();

			String[] oplFieldsName = new String[size];
			Type[] oplFieldsType = new Type[size];

			fillNamesAndTypes(schema, oplFieldsName, oplFieldsType);
			handler.startElement(name);
			handler.startSet();

			if (incumbentCarrierLaneVolumeRawDatas != null && !incumbentCarrierLaneVolumeRawDatas.isEmpty()) {
				for (IncumbentCarrierLaneVolumeRawData incumbentCarrierLaneVolumeRawData : incumbentCarrierLaneVolumeRawDatas) {
					if (leaf == Type.TUPLE) {
						handler.startTuple();
					}
					handler.addIntItem(incumbentCarrierLaneVolumeRawData.getCarrierId());
					handler.addIntItem(incumbentCarrierLaneVolumeRawData.getIlogDemandLineitemId());
					handler.addNumItem(incumbentCarrierLaneVolumeRawData.getMinVolume());
					handler.addNumItem(incumbentCarrierLaneVolumeRawData.getMaxVolume());
					if (leaf == Type.TUPLE) {
						handler.endTuple();
					}
				}
			}
			handler.endSet();
			handler.endElement();
		} else {
			throw new IllegalArgumentException("Cannot read element " + name + " of type " + type);
		}
	}

	private void processDataForSameProportionGroupData(IloOplElementDefinition def, String name,
			List<SameProportionGroupData> sameProportionGroupDatas) {
		Type type = def.getElementDefinitionType();
		Type leaf = def.getLeaf().getElementDefinitionType();
		if (type == Type.SET) {
			IloOplDataHandler handler = getDataHandler();
			handler.startElement(name);
			handler.startSet();

			if (sameProportionGroupDatas != null && !sameProportionGroupDatas.isEmpty()) {
				for (SameProportionGroupData sameProportionGroupData : sameProportionGroupDatas) {
					if (leaf == Type.TUPLE) {
						handler.startTuple();
					}
					handler.addIntItem(sameProportionGroupData.getGroupId());
					if (leaf == Type.TUPLE) {
						handler.endTuple();
					}
				}
			}
			handler.endSet();
			handler.endElement();
		} else {
			throw new IllegalArgumentException("Cannot read element " + name + " of type " + type);
		}
	}

}
