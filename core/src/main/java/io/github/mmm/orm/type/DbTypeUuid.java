/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.type;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.UUID;

/**
 * Implementation of {@link DbTypeSimple} for a {@link UUID} if directly supported by DB.
 */
public class DbTypeUuid extends DbTypeSimple<UUID> {

  /**
   * The constructor.
   */
  public DbTypeUuid() {

    this("UUID");
  }

  /**
   * The constructor.
   *
   * @param declaration the database type {@link #getDeclaration() declaration}.
   */
  public DbTypeUuid(String declaration) {

    this(declaration, -1);
  }

  /**
   * The constructor.
   *
   * @param declaration the database type {@link #getDeclaration() declaration}.
   * @param sqlType the {@link #getSqlType() SQL type}.
   */
  public DbTypeUuid(String declaration, int sqlType) {

    super(declaration, sqlType);
  }

  @Override
  public Class<UUID> getSourceType() {

    return UUID.class;
  }

  @SuppressWarnings("exports")
  @Override
  public void setDbParameter(PreparedStatement statement, int index, UUID value, Connection connection)
      throws SQLException {

    if (value == null) {
      int type = this.sqlType;
      if (type < 0) {
        type = Types.NULL;
      }
      statement.setNull(index, type);
    } else {
      statement.setObject(index, value);
    }
  }

}
