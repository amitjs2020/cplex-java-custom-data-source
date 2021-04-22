package cplex.java.customdatasource;

import java.io.FileOutputStream;

import ilog.concert.IloException;
import ilog.opl.IloOplDataSource;
import ilog.opl.IloOplErrorHandler;
import ilog.opl.IloOplException;
import ilog.opl.IloOplFactory;
import ilog.opl.IloOplModel;
import ilog.opl.IloOplModelDefinition;
import ilog.opl.IloOplRunConfiguration;

/**
 * This program demonstrates the use of OPL's data source API to customize a
 * data source using JDBC. It also shows how one can write results using JDBC.
 *
 */
public class SpendoptiScenarioRunModel {
	static public void main(String[] args) throws Exception {
		int status = 127;
		try {
			CommandLine cl = new CommandLine(args);
			IloOplFactory.setDebugMode(true);
			IloOplFactory oplF = new IloOplFactory();
			IloOplErrorHandler errHandler = oplF.createOplErrorHandler(System.out);

			IloOplRunConfiguration rc = null;
			if (cl.getDataFileNames().size() == 0) {
				rc = oplF.createOplRunConfiguration(cl.getModelFileName());
			} else {
				String[] dataFiles = cl.getDataFileNames().toArray(new String[cl.getDataFileNames().size()]);
				rc = oplF.createOplRunConfiguration(cl.getModelFileName(), dataFiles);
			}
			rc.setErrorHandler(errHandler);
			IloOplModel opl = rc.getOplModel();

			IloOplModelDefinition def = opl.getModelDefinition();

			//
			// Reads the JDBC configuration, initialize a JDBC custom data
			// source
			// and sets the source in OPL.
			//
			SpendOptiObjectTransformer spendOptiObjectTransformer = new SpendOptiObjectTransformer();
			SpendOptiJdbcConfiguration jdbcProperties = null;
			String jdbcConfigurationFile = cl.getPropertiesFileName();
			if (jdbcConfigurationFile != null) {
				jdbcProperties = new SpendOptiJdbcConfiguration();
				jdbcProperties.read(jdbcConfigurationFile);
				// Create the custom JDBC data source
				//IloOplDataSource jdbcDataSource = new SpendOptiScenarioCustomDataSource(jdbcProperties, oplF, def);
				IloOplDataSource jdbcDataSource = new SpendOptiScenarioJSONCustomDataSource(jdbcProperties, oplF, def, spendOptiObjectTransformer);
				// Pass it to the model.
				opl.addDataSource(jdbcDataSource);
			}

			opl.generate();

			if (cl.getExternalDataName() != null) {
				FileOutputStream ofs = new FileOutputStream(cl.getExternalDataName());
				opl.printExternalData(ofs);
				ofs.close();
			}
			System.out.println(opl.toString());
			boolean success = false;
			if (opl.hasCplex()) {
				if (opl.getCplex().solve()) {
					success = true;
				}
			} else {
				if (opl.getCP().solve()) {
					success = true;
				}
			}
			if (success == true) {
				opl.postProcess();
				// write results
				if (jdbcProperties != null) {
					//SpendOptiScenarioJdbcWriter writer = new SpendOptiScenarioJdbcWriter(jdbcProperties, def, opl);
					//writer.customWrite();
					SpendOptiScenarioJSONObjectWriter writer = new SpendOptiScenarioJSONObjectWriter(jdbcProperties, def, opl, spendOptiObjectTransformer);
					writer.jsonWriter();
				}
			}
			oplF.end();
			status = 0;
		} catch (IloOplException ex) {
			System.err.println("### OPL exception: " + ex.getMessage());
			ex.printStackTrace();
			status = 2;
		} catch (IloException ex) {
			System.err.println("### CONCERT exception: " + ex.getMessage());
			ex.printStackTrace();
			status = 3;
		} catch (Exception ex) {
			System.err.println("### UNEXPECTED UNKNOWN ERROR ...");
			ex.printStackTrace();
			status = 4;
		}
		System.exit(status);
	}
}
