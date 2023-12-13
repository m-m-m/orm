package io.github.mmm.orm.metadata.impl;

import io.github.mmm.orm.metadata.DbQualifiedName;
import io.github.mmm.orm.metadata.DbTableData;
import io.github.mmm.orm.metadata.DbTableType;

/**
 * Implementation of {@link DbTableData}.
 *
 * @param name the {@link DbTableImpl#getQualifiedName() qualified name}.
 * @param comment the {@link DbTableImpl#getComment() comment}
 * @param type the {@link DbTableImpl#getType() table type}.
 *
 * @since 1.0.0
 */
public record DbTableDataImpl(DbQualifiedName name, String comment, DbTableType type) implements DbTableData {

  @Override
  public DbQualifiedName getQualifiedName() {

    return name();
  }

  @Override
  public String getComment() {

    return comment();
  }

  @Override
  public DbTableType getType() {

    return type();
  }

}
