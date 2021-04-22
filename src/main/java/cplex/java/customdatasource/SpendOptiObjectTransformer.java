package cplex.java.customdatasource;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import cplex.java.customdatasource.dao.SpendOptiILogDAO;
import cplex.java.customdatasource.model.CostData;
import cplex.java.customdatasource.model.ScenarioRun;
import cplex.java.customdatasource.model.SpendoptiModelRequest;

/**
 * This controller creates a single job request for a problem instance to be
 * processed by the <code>truck.mod</code> shipment problem model. Once the
 * problem is solved, the results are mapped to an instance of a
 * {@link Solution} class and displayed in the console.
 */
public class SpendOptiObjectTransformer {

	public static Logger LOG = Logger.getLogger(SpendOptiObjectTransformer.class.getName());

	// Create mapper instance for Java --> JSON serialization
	ObjectMapper mapper = new ObjectMapper();

	/**
	 * Constructor.
	 * 
	 * @param baseURL
	 * @param apiKeyClientId
	 */
	public SpendOptiObjectTransformer() {

	}

	public void createSpendoptiModelRequest(String fileName) {
		int scenarioRunId = 33;
		SpendoptiModelRequest spendoptiModelRequest = new SpendoptiModelRequest();
		SpendOptiILogDAO spendOptiDao = new SpendOptiILogDAO();
		List<ScenarioRun> scenarioRuns = new ArrayList<>();
		ScenarioRun scenarioRun = new ScenarioRun();
		scenarioRun.setScenarioRunId(31);
		scenarioRuns.add(scenarioRun);
		spendoptiModelRequest.setScenarioRuns(scenarioRuns);
		spendoptiModelRequest.setCostData(spendOptiDao.getCostData(scenarioRunId));
		spendoptiModelRequest.setCarrierLaneData(spendOptiDao.getCarrierLaneData(scenarioRunId));
		spendoptiModelRequest.setGroupData(spendOptiDao.getGroupData(scenarioRunId));
		spendoptiModelRequest.setAmountGroupData(spendOptiDao.getAmountGroupData(scenarioRunId));
		spendoptiModelRequest.setCarrierData(spendOptiDao.getCarrierData(scenarioRunId));
		spendoptiModelRequest.setSupplyLineItemData(spendOptiDao.getSupplyLineItemData(scenarioRunId));
		spendoptiModelRequest.setLaneInfoData(spendOptiDao.getLaneInfoData(scenarioRunId));
		spendoptiModelRequest.setGroupLaneData(spendOptiDao.getGroupLaneData(scenarioRunId));
		spendoptiModelRequest.setCarrierGroupData(spendOptiDao.getCarrierGroupData(scenarioRunId));
		spendoptiModelRequest.setGroupVolumeBoundData(spendOptiDao.getGroupVolumeBoundData(scenarioRunId));
		spendoptiModelRequest.setAmountGroupBoundData(spendOptiDao.getAmountGroupBoundData(scenarioRunId));
		spendoptiModelRequest.setAmountGroupCarrierData(spendOptiDao.getAmountGroupCarrierData(scenarioRunId));
		spendoptiModelRequest.setGroupCarrierBoundData(spendOptiDao.getGroupCarrierBoundData(scenarioRunId));
		spendoptiModelRequest.setParameterData(spendOptiDao.getParameterData(scenarioRunId));
		spendoptiModelRequest
				.setIncumbentCarrierLaneRawData(spendOptiDao.getIncumbentCarrierLaneRawData(scenarioRunId));
		spendoptiModelRequest
				.setNoIncumbentCarrierLaneRawData(spendOptiDao.getNoIncumbentCarrierLaneRawData(scenarioRunId));
		spendoptiModelRequest
				.setIncumbentCarrierLaneVolRawData(spendOptiDao.getIncumbentCarrierLaneVolumeRawData(scenarioRunId));
		spendoptiModelRequest.setSameProportionGroupData(spendOptiDao.getSameProportionGroupData(scenarioRunId));
		
		spendoptiJavaToJsonObjectCreator(fileName, spendoptiModelRequest);
	}
	
	public void spendoptiJavaToJsonObjectCreator(String fileName, Object spendoptiModelRequest) {
		try {
			mapper.writeValue(new File(fileName), spendoptiModelRequest);
			System.out.println(mapper.writeValueAsString(spendoptiModelRequest));
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Object spendoptiJsonToJavaObjectCreator() {
		SpendoptiModelRequest spendoptiModelRequest = null;
		try {
			spendoptiModelRequest = mapper.readValue(new File("spendoptiModelRequest.json"), SpendoptiModelRequest.class);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return spendoptiModelRequest;
	}
	
	public <T> T spendoptiJsonToJavaObjectCreator(String name, TypeReference<T> typeReference) {
		T t = null;
		try {
			t = mapper.readValue(new File(name), typeReference);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return t;
	}
	
	public static void main(String[] args) {
		SpendOptiObjectTransformer objectTransformer = new SpendOptiObjectTransformer();
		String inputFileName = "spendoptiModelRequest.json";
		objectTransformer.createSpendoptiModelRequest(inputFileName);
		SpendoptiModelRequest spendoptiModelRequest = (SpendoptiModelRequest) objectTransformer.spendoptiJsonToJavaObjectCreator();
		System.out.println("Scenario Run ID: " + spendoptiModelRequest.getScenarioRuns().get(0).getScenarioRunId());
		
		System.out.println("Cost Delta");
		for(CostData costDelta : spendoptiModelRequest.getCostData()) {
			System.out.println(costDelta);
		}
	}
}
