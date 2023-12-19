/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.jdbc.access;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import io.github.mmm.entity.bean.EntityBean;
import io.github.mmm.entity.id.Id;
import io.github.mmm.orm.access.AbstractDbAccess;
import io.github.mmm.orm.dialect.AbstractDbDialect;
import io.github.mmm.orm.jdbc.access.session.JdbcSession;
import io.github.mmm.orm.param.AbstractCriteriaParameters;

/**
 * Abstract implementation of {@link io.github.mmm.orm.access.DbAccess} using JDBC.
 */
public abstract class JdbcDbAccess extends AbstractDbAccess {

  @Override
  public void insert(EntityBean entity) {

    // TODO Auto-generated method stub

  }

  @Override
  public <E extends EntityBean> E selectById(Id<E> id, E prototype) {

    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void update(EntityBean entity) {

    // TODO Auto-generated method stub

  }

  @Override
  protected AbstractDbDialect<?> getDialect() {

    // TODO Auto-generated method stub
    return null;
  }

  @Override
  protected long executeSql(String sql, AbstractCriteriaParameters parameters) {

    try {
      JdbcSession session = null; // JdbcTransactionExecutor.getSession();
      Connection connection = session.getConnection();
      PreparedStatement statement = connection.prepareStatement(sql);
      parameters.apply(statement, connection);
      // TODO support batch updates...
      return statement.executeLargeUpdate();
    } catch (SQLException e) {
      // TODO proper custom runtinme exception class and error message including the SQL
      throw new IllegalStateException(e);
    }
  }

}
