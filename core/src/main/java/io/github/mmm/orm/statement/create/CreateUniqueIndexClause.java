/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.statement.create;

import io.github.mmm.orm.statement.StartClause;

/**
 * A {@link CreateUniqueIndexClause}-{@link StartClause} of an SQL {@link CreateIndexStatement}.
 *
 * @since 1.0.0
 * @see io.github.mmm.orm.statement.DbStatement#createUniqueIndex()
 */
public class CreateUniqueIndexClause extends CreateIndexClause {

  /** Name of {@link CreateUniqueIndexClause} for marshaling. */
  public static final String NAME_CREATE_UNIQUE_INDEX = "CREATE UNIQUE INDEX";

  /**
   * The constructor to use an auto-generated {@link #getName() name}.
   */
  public CreateUniqueIndexClause() {

    super();
  }

  /**
   * The constructor.
   *
   * @param name the {@link #getName() name of the index}.
   */
  public CreateUniqueIndexClause(String name) {

    super(name);
  }

  @Override
  public boolean isUnique() {

    return true;
  }

}
