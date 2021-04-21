package cplex.java.customdatasource;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * The class to store JDBC custom data source connection parameters.
 */
public class SpendOptiJdbcConfiguration {
	Properties readProperties = new Properties();

	public static class OutputParameters {

		String query = null;
		String outputTable = null;

		public OutputParameters(String query, String target) {
			this.query = query;
			this.outputTable = target;
		}
	};

	Map<String, OutputParameters> outputMapping = new HashMap<String, OutputParameters>();

	private final static String URL = "url";
	private final static String USER = "user";
	private final static String PASSWORD = "password";
	private final static String IS_MAPPING_NAME = "is_mapping_name";

	private final static String READ = "read";
	private final static String WRITE = "write";

	private final static String QUERY = "query";
	private final static String NAME = "name";
	private final static String TABLE = "table";
	private final static String TARGET = "target";

	private String url = null;
	private String user = null;
	private String password = null;
	private boolean isMappingName = false;

	/**
	 * Resolve <code>s</code> using environment variables. If <code>s</code> starts
	 * with "$" and is the name of an existing environment variable then return the
	 * value of that variable, otherwise return <code>s</code>.
	 * 
	 * @param s The string to resolve.
	 * @return The value of environment variable <code>s</code> if such a variable
	 *         exists, <code>s</code> otherwise.
	 */
	private static String resolveString(String s) {
		if (s == null || s.length() < 2 || s.charAt(0) != '$')
			return s;
		final String value = System.getenv(s.substring(1));
		if (value != null)
			return value;
		return s;
	}

	/**
	 * Creates a new JDBC configuration.
	 */
	public SpendOptiJdbcConfiguration() {
	}

	public boolean isMappingName() {
		return isMappingName;
	}

	public void setMappingName(boolean isMappingName) {
		this.isMappingName = isMappingName;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUser() {
		return resolveString(user);
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPassword() {
		return resolveString(password);
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Properties getReadQueries() {
		return readProperties;
	}

	/**
	 * Adds a read query to the datasource.
	 * 
	 * The specified query is used to populate the OPL data which name is specified.
	 * 
	 * @param name  The OPL data
	 * @param query The read query
	 */
	public void addReadQuery(String name, String query) {
		readProperties.setProperty(name, query);
	}

	public Map<String, OutputParameters> getOutputMapping() {
		return outputMapping;
	}

	/**
	 * Adds a write mapping to the datasource.
	 * 
	 * @param name   The OPL output name.
	 * @param target The database table to map the output to.
	 */
	public void addWriteMapping(String name, String target) {
		addOutputParameters(name, null, target);
	}

	public void addOutputParameters(String name, String query, String target) {
		outputMapping.put(name, new OutputParameters(query, target));
	}

	public void addInsertStatement(String name, String query) {
		outputMapping.put(name, new OutputParameters(query, null));
	}

	/**
	 * Reads the configuration from the specified file.
	 * 
	 * Supported files are .properties and .XML files. See also
	 * {@link #readXML(InputStream)} and {@link #readProperties(InputStream)}
	 * 
	 * @param filename The configuration file name.
	 * @throws IOException
	 */
	public void read(String filename) throws IOException {
		InputStream input = new FileInputStream(filename);
		try {
			if (filename.toLowerCase().endsWith(".properties"))
				this.readProperties(input);
			else if (filename.toLowerCase().endsWith(".xml"))
				this.readXML(input);
		} finally {
			if (input != null) {
				input.close();
			}
		}
	}

	/**
	 * 
	 * @param input
	 * @throws IOException
	 */
	public void readProperties(InputStream input) throws IOException {
		Properties properties = new Properties();
		properties.load(input);

		this.url = properties.getProperty(URL);
		this.user = properties.getProperty(USER);
		this.password = properties.getProperty(PASSWORD);
		String v = properties.getProperty(IS_MAPPING_NAME);
		if (v != null) {
			this.isMappingName = Boolean.valueOf(v);
		}

		// iterate properties to find read and write
		Enumeration<?> propertyNames = properties.propertyNames();
		String read = READ + ".";
		String write = WRITE + ".";
		while (propertyNames.hasMoreElements()) {
			String name = (String) propertyNames.nextElement();
			if (name.startsWith(read)) {
				int pos = read.length();
				String element = name.substring(pos);
				readProperties.setProperty(element, (String) properties.getProperty(name));
			} else if (name.startsWith(write)) {
				int pos = write.length();
				String element = name.substring(pos);
				addWriteMapping(element, (String) properties.getProperty(name));
			}
		}
	}

	/**
	 * 
	 * @param input
	 * @throws IOException
	 */
	public void readXML(InputStream input) throws IOException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(input);
			// connection parameters
			url = doc.getElementsByTagName(URL).item(0).getTextContent();
			user = doc.getElementsByTagName(USER).item(0).getTextContent();
			password = doc.getElementsByTagName(PASSWORD).item(0).getTextContent();

			NodeList nl = doc.getElementsByTagName(IS_MAPPING_NAME);
			if (nl.getLength() > 0) {
				String v = nl.item(0).getTextContent();
				isMappingName = Boolean.valueOf(v);
			}

			// input parameters
			Node readNode = doc.getElementsByTagName(READ).item(0);
			if (readNode != null && readNode.getNodeType() == Node.ELEMENT_NODE) {
				NodeList queries = ((Element) readNode).getElementsByTagName(QUERY);
				for (int iquery = 0; iquery < queries.getLength(); iquery++) {
					Node qNode = queries.item(iquery);
					if (qNode.getNodeType() == Node.ELEMENT_NODE) {
						Element qElement = (Element) qNode;
						String name = qElement.getAttribute(NAME);
						String query = qElement.getTextContent();
						readProperties.setProperty(name, query);
					}
				}
			}

			// write parameters
			Node writeNode = doc.getElementsByTagName(WRITE).item(0);
			if (writeNode != null && writeNode.getNodeType() == Node.ELEMENT_NODE) {
				NodeList tables = ((Element) writeNode).getElementsByTagName(TABLE);
				for (int itable = 0; itable < tables.getLength(); itable++) {
					Node tNode = tables.item(itable);
					if (tNode.getNodeType() == Node.ELEMENT_NODE) {
						Element tElement = (Element) tNode;
						String name = tElement.getAttribute(NAME);
						String target = tElement.getAttribute(TARGET);
						addWriteMapping(name, target);
					}
				}
			}
		} catch (ParserConfigurationException | SAXException e) {
			throw new RuntimeException("Could not read XML configuration");
		}
	}
}
