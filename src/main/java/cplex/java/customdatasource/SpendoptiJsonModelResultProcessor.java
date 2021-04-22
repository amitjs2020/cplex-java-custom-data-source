package cplex.java.customdatasource;

import java.util.List;

import org.codehaus.jackson.type.TypeReference;

import cplex.java.customdatasource.dao.SpendOptiILogDAO;
import cplex.java.customdatasource.model.DeficitoutputData;
import cplex.java.customdatasource.model.ModeloutputData;

public class SpendoptiJsonModelResultProcessor {

	private SpendOptiObjectTransformer spendOptiObjectTransformer;
	
	public SpendoptiJsonModelResultProcessor(SpendOptiObjectTransformer spendOptiObjectTransformer) {
		this.spendOptiObjectTransformer = spendOptiObjectTransformer;
	}
	
	private void processResult() {
		SpendOptiILogDAO spendOptiDao = new SpendOptiILogDAO();
		List<ModeloutputData> modeloutputDatas = spendOptiObjectTransformer.spendoptiJsonToJavaObjectCreator("modeloutputData.json", new TypeReference<List<ModeloutputData>>(){});
		if(modeloutputDatas != null && !modeloutputDatas.isEmpty()) {
			spendOptiDao.updateModeloutputData(modeloutputDatas);			
		}
		List<DeficitoutputData> deficitoutputDatas = spendOptiObjectTransformer.spendoptiJsonToJavaObjectCreator("deficitoutputData.json", new TypeReference<List<DeficitoutputData>>(){});
		if(deficitoutputDatas != null && !deficitoutputDatas.isEmpty()) {
			spendOptiDao.insertModeloutputData(deficitoutputDatas);			
		}
	}
	
	public static void main(String[] args) {
		SpendOptiObjectTransformer spendOptiObjectTransformer = new SpendOptiObjectTransformer();
		SpendoptiJsonModelResultProcessor processor = new SpendoptiJsonModelResultProcessor(spendOptiObjectTransformer);
		processor.processResult();
	}

}
