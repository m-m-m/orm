/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.jdbc.connection;

import java.sql.Connection;

import io.github.mmm.orm.connection.DbConnection;
import io.github.mmm.orm.dialect.DbDialect;
import io.github.mmm.orm.jdbc.impl.metadata.JdbcMetaData;
import io.github.mmm.orm.metadata.DbMetaData;
import io.github.mmm.orm.source.DbSource;
import io.github.mmm.orm.tx.DbTransaction;

/**
 * Database session data for a single transaction.
 */
public class JdbcConnection implements DbConnection, DbTransaction {

  final Connection connection;

  final DbSource source;

  private final DbDialect dialect;

  private JdbcMetaData metaData;

  private boolean open;

  /**
   * The constructor.
   *
   * @param connection the JDBC {@link Connection}.
   * @param source the {@link #getSource() source}-
   * @param dialect the {@link DbDialect}.
   */
  public JdbcConnection(Connection connection, DbSource source, DbDialect dialect) {

    super();
    this.connection = connection;
    this.source = source;
    this.dialect = dialect;
    this.open = true;
  }

  @Override
  public DbSource getSource() {

    return this.source;
  }

  @Override
  public boolean isOpen() {

    return this.open;
  }

  /**
   * @return the raw JDBC {@link Connection}.
   */
  public Connection getConnection() {

    return this.connection;
  }

  @Override
  public DbMetaData getMetaData() {

    if (this.metaData == null) {
      this.metaData = new JdbcMetaData(this.connection, this.dialect);
    }
    return this.metaData;
  }

  void close() {

    this.open = false;
  }

}
