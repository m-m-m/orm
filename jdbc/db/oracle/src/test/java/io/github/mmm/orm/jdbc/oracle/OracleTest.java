package io.github.mmm.orm.jdbc.oracle;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;

/**
 * TODO hohwille This type ...
 *
 */
public class OracleTest {

  public static void main(String[] args) throws Exception {

    try (Connection connection = DriverManager.getConnection("jdbc:oracle:thin:@//localhost:1521/xepdb1", "SALOG",
        "SALOG")) {
      System.out.println("connected: " + connection.getSchema());
      DatabaseMetaData metaData = connection.getMetaData();

      try (ResultSet resultSet = metaData.getTables(null, "SALOG", null, null)) {
        while (resultSet.next()) {
          String tableName = resultSet.getString("TABLE_NAME");
          String remarks = resultSet.getString("REMARKS");
          String catalog = resultSet.getString("TABLE_CAT");
          String schema = resultSet.getString("TABLE_SCHEM");
          String tableType = resultSet.getString("TABLE_TYPE");
          System.err.println(catalog + "." + schema + "." + tableName + "@" + tableType + " - " + remarks);
          try (ResultSet colRs = metaData.getColumns(catalog, schema, tableName, "%")) {
            while (colRs.next()) {
              String colName = colRs.getString("COLUMN_NAME");
              int sqlType = colRs.getInt("DATA_TYPE");
              String typeName = colRs.getString("TYPE_NAME");
              int size = colRs.getInt("COLUMN_SIZE");
              int decimalDigits = colRs.getInt("DECIMAL_DIGITS");
              boolean nullable = colRs.getBoolean("IS_NULLABLE");
              System.out.println(colName + ":" + sqlType + "-" + typeName + "@" + size + "/" + decimalDigits
                  + " nullable:" + nullable);
            }
          }
        }
      }
    }
  }

}
