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
import io.github.mmm.orm.statement.DbPlainStatement;
import io.github.mmm.orm.statement.DbStatement;
import io.github.mmm.orm.statement.select.SelectStatement;

/**
 * Implementation of {@link IdSequence} backed by a database sequence using JDBC.
 */
public class JdbcSequence implements IdSequence {

  private final DbQualifiedName sequenceName;

  private final SelectStatement<Long> statement;

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
    this.statement = DbStatement.selectSeqNextVal(this.sequenceName);
  }

  @Override
  public long next(Id<?> template) {

    JdbcSession session = JdbcSession.get();
    DbDialect dialect = session.getConnectionData().getDialect();
    Connection connection = session.getConnection();
    DbPlainStatement plainStatement = dialect.createFormatter().formatStatement(this.statement).get();
    long id = -1;
    while (plainStatement != null) {
      String sql = plainStatement.getStatement();
      try {
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        if (id == -1) {
          ResultSet rs = preparedStatement.executeQuery();
          if (rs.next()) {
            id = rs.getLong(1);
            assert (id != -1);
          } else {
            throw new SQLException("ResultSet empty");
          }
        } else {
          preparedStatement.execute();
        }
      } catch (SQLException e) {
        throw new IllegalStateException("Failed to execute " + sql, e);
      }
      plainStatement = plainStatement.getNext();
    }
    assert (id != -1);
    return id;
  }

}
