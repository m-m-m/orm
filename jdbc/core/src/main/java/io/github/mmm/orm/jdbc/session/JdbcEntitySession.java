/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.jdbc.session;

import java.sql.Connection;

import io.github.mmm.entity.bean.EntityBean;
import io.github.mmm.orm.session.DbEntitySession;

/**
 * {@link DbEntitySession} for JDBC.
 *
 * @param <E> type of the managed {@link EntityBean}.
 */
public class JdbcEntitySession<E extends EntityBean> extends DbEntitySession<E> {

  private Connection connection;

  /**
   * The constructor.
   *
   * @param connection the JDBC {@link Connection}.
   */
  public JdbcEntitySession(Connection connection) {

    super();
    this.connection = connection;
    // this.connection.getMetaData().getColumns(null, null, null, "%");
  }

  /**
   * @return the JDBC {@link Connection}.
   */
  public Connection getConnection() {

    return this.connection;
  }

}
