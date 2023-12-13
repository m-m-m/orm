/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.jdbc.impl.metadata;

import io.github.mmm.orm.metadata.DbColumnReference;
import io.github.mmm.orm.metadata.DbForeignKey;
import io.github.mmm.orm.metadata.DbName;
import io.github.mmm.orm.metadata.impl.DbForeignKeyImpl;

/**
 * {@link JdbcContainerBuilder} to build a {@link DbForeignKey}.
 */
public class JdbcFkBuilder extends JdbcContainerBuilder<DbColumnReference> {

  private final DbName name;

  /**
   * The constructor.
   *
   * @param name the name of the foreign key.
   */
  public JdbcFkBuilder(DbName name) {

    super();
    this.name = name;
  }

  /**
   * @return the new {@link DbForeignKey} created from this builder.
   */
  public DbForeignKey buildFk() {

    return new DbForeignKeyImpl(this.name, build());
  }

}
