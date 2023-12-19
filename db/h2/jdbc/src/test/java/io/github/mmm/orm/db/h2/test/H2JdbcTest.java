package io.github.mmm.orm.db.h2.test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import io.github.mmm.orm.jdbc.connection.JdbcConnection;
import io.github.mmm.orm.metadata.DbMetaData;
import io.github.mmm.orm.metadata.DbName;
import io.github.mmm.orm.metadata.DbQualifiedName;
import io.github.mmm.orm.metadata.DbTable;
import io.github.mmm.orm.metadata.DbTableData;
import io.github.mmm.orm.tx.DbTransaction;
import io.github.mmm.orm.tx.DbTransactionExecutor;

/**
 * Test of H2 database support.
 */
public class H2JdbcTest extends Assertions {

  /** Test that H2 integration works. */
  @Test
  public void test() {

    try {
      DbTransactionExecutor executor = DbTransactionExecutor.get();
      DbTransaction tx = executor.doInTx(() -> doTransactionalTask(executor));
      assertThat(tx.isOpen()).isFalse();
    } catch (Throwable t) {
      // JUnit is buggy and eats up exceptions
      t.printStackTrace();
      throw t;
    }
  }

  private DbTransaction doTransactionalTask(DbTransactionExecutor executor) throws SQLException {

    DbTransaction tx = executor.getTransaction();
    assertThat(tx.isOpen()).isTrue();
    // TODO just a temporary hack, need to replace with real test...
    JdbcConnection jdbcConnection = (JdbcConnection) tx;
    Connection connection = jdbcConnection.getConnection();
    Statement statement = connection.createStatement();
    statement.execute("CREATE TABLE FOO (ID BIGINT PRIMARY KEY, NAME VARCHAR);");
    statement = connection.createStatement();
    DbMetaData metaData = jdbcConnection.getMetaData();
    System.out.println(metaData.getCurrentCatalog());
    System.out.println(metaData.getCurrentSchema());
    for (DbTableData table : metaData.getTables()) {
      System.out.println(table);
    }
    DbTable table = metaData.getTable(new DbQualifiedName(null, null, DbName.of("FOO")));
    System.out.println(table);
    System.out.println(table.getType());
    System.out.println(table.getType().isTable());
    statement.execute("INSERT INTO FOO (ID, NAME) VALUES (1, 'NAME');");
    statement = connection.createStatement();
    ResultSet resultSet = statement.executeQuery("SELECT * FROM FOO;");
    while (resultSet.next()) {
      System.out.println(resultSet.getLong(1));
      System.out.println(resultSet.getString(2));
    }
    return tx;
  }

}
