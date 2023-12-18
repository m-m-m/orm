/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.jdbc.oracle;

import java.sql.SQLException;

import javax.sql.DataSource;

import io.github.mmm.base.metainfo.MetaInfo;
import io.github.mmm.orm.jdbc.connection.JdbcConnectionPoolProvider;
import io.github.mmm.orm.source.DbSource;
import oracle.jdbc.pool.OracleDataSource;
import oracle.ucp.jdbc.PoolDataSource;
import oracle.ucp.jdbc.PoolDataSourceFactory;

/**
 * Implementation of {@link JdbcConnectionPoolProvider} for Oracle UCP.
 *
 * @since 1.0.0
 */
public class OracleConnectionPoolProvider extends JdbcConnectionPoolProvider {

  @Override
  public String getId() {

    return "oracle";
  }

  @Override
  public boolean isResponsible(String url) {

    return url.startsWith("jdbc:oracle:");
  }

  @Override
  protected DataSource createDataSource(MetaInfo config) {

    try {
      PoolDataSource poolDataSource = PoolDataSourceFactory.getPoolDataSource();
      poolDataSource.setConnectionFactoryClassName(OracleDataSource.class.getName());
      poolDataSource.setURL(config.getRequired(DbSource.KEY_URL));
      poolDataSource.setUser(config.getRequired(DbSource.KEY_USER));
      poolDataSource.setPassword(config.getRequired(DbSource.KEY_PASSWORD));
      return poolDataSource;
    } catch (SQLException e) {
      throw new IllegalStateException(e);
    }
  }

}
