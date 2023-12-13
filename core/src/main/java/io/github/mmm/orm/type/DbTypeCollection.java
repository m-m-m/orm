/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.type;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDate;
import java.util.Collection;

/**
 * Implementation of {@link DbTypeSimple} for a regular {@link LocalDate}.
 */
@SuppressWarnings("rawtypes")
public class DbTypeCollection extends DbTypeSimple<Collection> {

  private final DbType<?, ?> elementType;

  /**
   * The constructor.
   *
   * @param elementType the {@link DbType} of the elements contained in the {@link Collection}.
   */
  public DbTypeCollection(DbType<?, ?> elementType) {

    this(elementType, "ARRAY");
  }

  /**
   * The constructor.
   *
   * @param elementType the {@link DbType} of the elements contained in the {@link Collection}.
   * @param declaration the database type {@link #getDeclaration() declaration}.
   */
  public DbTypeCollection(DbType<?, ?> elementType, String declaration) {

    this(elementType, declaration, Types.ARRAY);
  }

  /**
   * The constructor.
   *
   * @param elementType the {@link DbType} of the elements contained in the {@link Collection}.
   * @param declaration the database type {@link #getDeclaration() declaration}.
   * @param sqlType the {@link #getSqlType() SQL type}.
   */
  public DbTypeCollection(DbType<?, ?> elementType, String declaration, int sqlType) {

    super(declaration, sqlType);
    this.elementType = elementType;
  }

  @Override
  public Class<Collection> getSourceType() {

    return Collection.class;
  }

  @SuppressWarnings("exports")
  @Override
  public void setDbParameter(PreparedStatement statement, int index, Collection value, Connection connection)
      throws SQLException {

    if (value == null) {
      statement.setNull(index, this.sqlType);
    } else {
      int size = value.size();
      Object[] elements = (Object[]) java.lang.reflect.Array.newInstance(this.elementType.getTargetType(), size);
      elements = value.toArray(elements);
      java.sql.Array array = connection.createArrayOf(this.elementType.getDeclaration(), elements);
      statement.setArray(index, array);
    }
  }

}
