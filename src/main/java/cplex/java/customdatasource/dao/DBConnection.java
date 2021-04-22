package cplex.java.customdatasource.dao;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {

	public static String JDBC_URL = "jdbc:sqlserver://localhost:1433;databaseName=ftedb;user=srvgtnxfterw;password=@lhh$tb*rw";

	public Connection getDbConnection() {
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			Connection connObj = DriverManager.getConnection(JDBC_URL);
			if (connObj != null) {
				//DatabaseMetaData metaObj = (DatabaseMetaData) connObj.getMetaData();
				//System.out.println("Driver Name?= " + metaObj.getDriverName() + ", Driver Version?= "
				//		+ metaObj.getDriverVersion() + ", Product Name?= " + metaObj.getDatabaseProductName()
				//		+ ", Product Version?= " + metaObj.getDatabaseProductVersion());
			}
			return connObj;
		} catch (Exception sqlException) {
			sqlException.printStackTrace();
		}
		return null;
	}

	public static void main(String[] args) {
		DBConnection connection = new DBConnection();
		connection.getDbConnection();
	}

}
