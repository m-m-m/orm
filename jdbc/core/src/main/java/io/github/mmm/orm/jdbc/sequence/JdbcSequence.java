/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.jdbc.sequence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import io.github.mmm.entity.id.Id;
import io.github.mmm.entity.id.sequence.IdSequence;
import io.github.mmm.orm.dialect.DbDialect;
import io.github.mmm.orm.jdbc.session.JdbcSession;
import io.github.mmm.orm.metadata.DbName;
import io.github.mmm.orm.metadata.DbQualifiedName;
import io.github.mmm.orm.statement.DbStatement;
import io.github.mmm.orm.statement.select.SelectStatement;

/**
 * Implementation of {@link IdSequence} backed by a database sequence using JDBC.
 */
public class JdbcSequence implements IdSequence {

  private final DbQualifiedName sequenceName;

  /**
   * The constructor.
   *
   * @param sequenceName the name of the sequence.
   */
  public JdbcSequence(DbName sequenceName) {

    this(new DbQualifiedName(null, null, sequenceName));
  }

  /**
   * The constructor.
   *
   * @param sequenceName the name of the sequence.
   */
  public JdbcSequence(DbQualifiedName sequenceName) {

    super();
    this.sequenceName = sequenceName;
  }

  @Override
  public long next(Id<?> template) {

    JdbcSession session = JdbcSession.get();
    DbDialect dialect = session.getConnectionData().getDialect();
    SelectStatement<Long> statement = DbStatement.selectSeqNextVal(this.sequenceName);
    try {
      Connection connection = session.getConnection();
      String sql = dialect.createFormatter().formatStatement(statement).get();
      PreparedStatement preparedStatement = connection.prepareStatement(sql);
      ResultSet rs = preparedStatement.executeQuery();
      while (rs.next()) {
        return rs.getLong(1);
      }
      throw new SQLException("ResultSet empty");
    } catch (SQLException e) {
      throw new IllegalStateException("Failed to execute " + statement, e);
    }
  }

}
