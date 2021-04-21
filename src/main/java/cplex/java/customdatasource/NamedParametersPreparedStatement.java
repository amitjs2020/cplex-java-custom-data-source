package cplex.java.customdatasource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class allows to set parameters of a PreparedStatement by name instead of by index.
 * Code like this:
 *
 */
public class NamedParametersPreparedStatement {
  // maps keys into index. One key can be in multiple place, so indices are lists of int.
  private Map<String, int[]> index;
  private PreparedStatement statement;
  
  /**
   * Creates a NamedParametersPreparedStatement.
   * 
   * 
   * @param connection The database connection
   * @param query The named parameters query
   * @throws SQLException If the statement could not be created.
   */
  public NamedParametersPreparedStatement(Connection connection, String query) throws SQLException {
    this.index = new HashMap<String, int[]>();
    String parsedQuery = parseQuery(query, index);
    this.statement = connection.prepareStatement(parsedQuery);
  }
  
  /**
   * Parses the query with named parameters.
   * 
   * Creates the index and prepared statement
   * @param query
   */
  public static String parseQuery(String query, Map<String, int[]> index) {
    if (query == null)
      return null;
    StringBuffer pq = new StringBuffer(query.length());
    LookAheadStringIterator it = new LookAheadStringIterator(query);
    Map<String, List<Integer> > index_map = new HashMap<String, List<Integer> >();
    int icount = 1;
    while (it.available()) {
      int c = it.currentChar();
      if (c == '\'' || c == '"') {
        // starting a quoted or double quoted string. Just swallow everything as is until ending quote.
        pq.append((char)c);
        it.next();
        it.swallow(pq, (char)c);
      } else if (c == ':' && Character.isJavaIdentifierStart(it.nextChar())) {
        // get parameter name
        it.next();
        String name = it.extractIdentifierName().toLowerCase();
        // store index
        List<Integer> a = index_map.get(name);
        if (a == null) {
          a = new ArrayList<Integer>();
          index_map.put(name,  a);
        }
        a.add(icount++);
        pq.append("?");
      } else {
        pq.append((char)c);
      }
      it.next();
    }
    // now generate index from indexMap with int[]
    for (String k: index_map.keySet()) {
      List<Integer> l = index_map.get(k);
      int kl = l.size();
      int[] a = new int[kl];
      for (int i=0; i < kl; i++)
        a[i] = l.get(i);
      index.put(k, a);
    }
    return pq.toString();
  }
  
  /**
   * @return true if this statement has named parameters
   */
  public boolean hasNamedParameters() {
    return index.size() > 0;
  }
  
  /**
   * Closes the underlying PreparedStatement.
   * @throws SQLException If the PreparedStatement could not be closed.
   */
  public void close() throws SQLException {
    statement.close();
  }
  
  /**
   * Returns the underlying statement.
   * @return
   */
  public PreparedStatement getStatement() {
    return statement;
  }
  
  public boolean execute() throws SQLException {
    return statement.execute();
  }
  
  public ResultSet executeQuery() throws SQLException {
    return statement.executeQuery();
  }
  
  public int executeUpdate() throws SQLException {
    return statement.executeUpdate();
  }
  
  public void addBatch() throws SQLException {
    statement.addBatch();
  }
  
  public int[] executeBatch() throws SQLException {
    return statement.executeBatch();
  }
  
  /**
   * Sets a parameter.
   * @param name The name of the parameter.
   * @param value The value of the parameter.
   * @throws SQLException If an error occurred.
   * @throws IllegalArgumentExcetpion If the parameter does not exist.
   */
  public void setObject(String name, Object value) throws SQLException {
    int[] indexes = index.get(name.toLowerCase());
    if (indexes == null) {
      throw new IllegalArgumentException("Parameter not found: " + name);
    }
    for (int i: indexes) {
      statement.setObject(i, value);
    }
  }
  
  /**
   * Sets a parameter.
   * @param name The name of the parameter.
   * @param value The value of the parameter.
   * @throws SQLException If an error occurred.
   * @throws IllegalArgumentExcetpion If the parameter does not exist.
   */
  public void setString(String name, String value) throws SQLException {
    int[] indexes = index.get(name.toLowerCase());
    if (indexes == null) {
      throw new IllegalArgumentException("Parameter not found: " + name);
    }
    for (int i: indexes) {
      statement.setString(i, value);
    }
  }
  
  /**
   * Sets a parameter.
   * @param name The name of the parameter.
   * @param value The value of the parameter.
   * @throws SQLException If an error occurred.
   * @throws IllegalArgumentExcetpion If the parameter does not exist.
   */
  public void setInt(String name, int value) throws SQLException {
    int[] indexes = index.get(name.toLowerCase());
    if (indexes == null) {
      throw new IllegalArgumentException("Parameter not found: " + name);
    }
    for (int i: indexes) {
      statement.setInt(i, value);
    }
  }
  
  /**
   * Sets a parameter.
   * @param name The name of the parameter.
   * @param value The value of the parameter.
   * @throws SQLException If an error occurred.
   * @throws IllegalArgumentExcetpion If the parameter does not exist.
   */
  public void setLong(String name, long value) throws SQLException {
    int[] indexes = index.get(name.toLowerCase());
    if (indexes == null) {
      throw new IllegalArgumentException("Parameter not found: " + name);
    }
    for (int i: indexes) {
      statement.setLong(i, value);
    }
  }
  
  /**
   * Sets a parameter.
   * @param name The name of the parameter.
   * @param value The value of the parameter.
   * @throws SQLException If an error occurred.
   * @throws IllegalArgumentExcetpion If the parameter does not exist.
   */
  public void setDouble(String name, double value) throws SQLException {
    int[] indexes = index.get(name.toLowerCase());
    if (indexes == null) {
      throw new IllegalArgumentException("Parameter not found: " + name);
    }
    for (int i: indexes) {
      statement.setDouble(i, value);
    }
  }
}

