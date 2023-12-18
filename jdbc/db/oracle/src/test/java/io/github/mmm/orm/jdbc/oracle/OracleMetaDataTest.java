package io.github.mmm.orm.jdbc.oracle;

import io.github.mmm.orm.connection.DbConnection;
import io.github.mmm.orm.connection.DbConnectionData;
import io.github.mmm.orm.connection.DbConnectionPool;
import io.github.mmm.orm.metadata.DbColumn;
import io.github.mmm.orm.metadata.DbForeignKey;
import io.github.mmm.orm.metadata.DbIndex;
import io.github.mmm.orm.metadata.DbMetaData;
import io.github.mmm.orm.metadata.DbPrimaryKey;
import io.github.mmm.orm.metadata.DbTable;
import io.github.mmm.orm.metadata.DbTableData;
import io.github.mmm.orm.source.DbSource;

/**
 * TODO hohwille This type ...
 *
 */
public class OracleMetaDataTest {

  public static void main(String[] args) throws Exception {

    System.setProperty("db.default.url", "jdbc:oracle:thin:@//localhost:1521/xepdb1");
    System.setProperty("db.default.user", "SALOG");
    System.setProperty("db.default.password", "SALOG");
    DbConnectionData connectionData = DbConnectionData.of(DbSource.get());
    DbConnectionPool pool = connectionData.getPool();
    DbConnection connection = pool.acquire();
    try {
      DbMetaData metaData = connection.getMetaData();
      for (DbTableData tableData : metaData.getTables()) {
        DbTable table = metaData.getTable(tableData);
        System.out.println("**" + table + "**");
        for (DbColumn column : table.getColumns()) {
          System.out.println(column);
        }
        DbPrimaryKey pk = table.getPrimaryKey();
        if (pk != null) {
          System.out.println(pk);
        }
        for (DbForeignKey fk : table.getForeignKeys()) {
          System.out.println(fk);
        }
        for (DbIndex index : table.getIndexes()) {
          System.out.println(index);
        }
      }
    } finally {
      pool.release(connection);
    }
  }

}
