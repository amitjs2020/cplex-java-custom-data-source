package cplex.java.customdatasource;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import cplex.java.customdatasource.model.DeficitoutputData;
import cplex.java.customdatasource.model.ModeloutputData;
import ilog.concert.IloTuple;
import ilog.opl.IloOplElement;
import ilog.opl.IloOplElementDefinition;
import ilog.opl.IloOplElementDefinitionType.Type;
import ilog.opl.IloOplModel;
import ilog.opl.IloOplModelDefinition;
import ilog.opl.IloOplTupleSchemaDefinition;
import ilog.opl_core.cppimpl.IloTupleSchema;
import ilog.opl_core.cppimpl.IloTupleSet;

/**
 * The class to write data using JDBC.
 *
 */
public class SpendOptiScenarioJSONObjectWriter {
	private SpendOptiJdbcConfiguration configuration;
	private IloOplModelDefinition def;
	private IloOplModel model;
	private SpendOptiObjectTransformer spendOptiObjectTransformer;

	/**
	 * Convenience method to write the output of a model to a database.
	 * 
	 * @param config The database connection configuration.
	 * @param model  The OPL model.
	 */
	public static void writeOutput(SpendOptiJdbcConfiguration config, IloOplModel model,
			SpendOptiObjectTransformer spendOptiObjectTransformer) {
		IloOplModelDefinition definition = model.getModelDefinition();
		SpendOptiScenarioJSONObjectWriter writer = new SpendOptiScenarioJSONObjectWriter(config, definition, model,
				spendOptiObjectTransformer);
		writer.jsonWriter();
	}

	public SpendOptiScenarioJSONObjectWriter(SpendOptiJdbcConfiguration configuration, IloOplModelDefinition def,
			IloOplModel model, SpendOptiObjectTransformer spendOptiObjectTransformer) {
		this.configuration = configuration;
		this.def = def;
		this.model = model;
		this.spendOptiObjectTransformer = spendOptiObjectTransformer;
	}

	public void jsonWriter() {
		long startTime = System.currentTimeMillis();
		System.out.println("Writing elements from JSON");
		Map<String, String[]> dataMapping = configuration.getOutputDataMapping();
		Set<Entry<String, String[]>> entrySet = dataMapping.entrySet();
		for (Entry<String, String[]> entry : entrySet) {
			String name = entry.getKey();
			String[] fields = entry.getValue();
			System.out.println("Writing " + name);
			jsonWriter(name, fields);
		}
		long endTime = System.currentTimeMillis();
		System.out.println("Done (" + (endTime - startTime) / 1000.0 + " s)");
	}

	void jsonWriter(String name, String[] fields) {
		IloOplElement elt = model.getElement(name);
		ilog.opl_core.cppimpl.IloTupleSet tupleSet = (ilog.opl_core.cppimpl.IloTupleSet) elt.asTupleSet();
		IloTupleSchema schema = tupleSet.getSchema_cpp();
		IloOplElementDefinition tupleDef = def.getElementDefinition(schema.getName());
		IloOplTupleSchemaDefinition tupleSchemaDef = tupleDef.asTupleSchema();
		final Type[] columnType = new Type[schema.getSize()];
		for (int i = 0; i < columnType.length; ++i) {
			columnType[i] = tupleSchemaDef.getComponent(i).getElementDefinitionType();
		}

		if ("modeloutputData".equals(name)) {
			createJsonForModeloutputData(name, tupleSet);
		} else if ("deficitoutputData".equals(name)) {
			createJsonForDeficitoutputData(name, tupleSet);
		}
	}

	private void createJsonForModeloutputData(String name, IloTupleSet tupleSet) {
		if (tupleSet != null) {
			List<ModeloutputData> modeloutputDatas = new ArrayList<>();
			for (Iterator<IloTuple> it1 = tupleSet.iterator(); it1.hasNext();) {
				IloTuple tuple = it1.next();
				ModeloutputData modeloutputData = new ModeloutputData();
				modeloutputData.setAllocation((float) tuple.getNumValue(0));
				modeloutputData.setIlogSupplyLineitemId(tuple.getIntValue(1));
				modeloutputDatas.add(modeloutputData);
			}
			spendOptiObjectTransformer.spendoptiJavaToJsonObjectCreator(name + ".json", modeloutputDatas);
		}
	}

	private void createJsonForDeficitoutputData(String name, IloTupleSet tupleSet) {
		if (tupleSet != null) {
			List<DeficitoutputData> deficitoutputDatas = new ArrayList<>();
			for (Iterator<IloTuple> it1 = tupleSet.iterator(); it1.hasNext();) {
				IloTuple tuple = it1.next();
				DeficitoutputData deficitoutputData = new DeficitoutputData();
				deficitoutputData.setScenarioId(tuple.getIntValue(0));
				deficitoutputData.setIlogDemandLineitemId(tuple.getIntValue(1));
				deficitoutputData.setAllocation((float) tuple.getNumValue(2));
				deficitoutputDatas.add(deficitoutputData);
			}
			spendOptiObjectTransformer.spendoptiJavaToJsonObjectCreator(name + ".json", deficitoutputDatas);
		}
	}
}
