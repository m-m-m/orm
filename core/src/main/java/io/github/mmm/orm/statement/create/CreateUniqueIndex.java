/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.statement.create;

import io.github.mmm.orm.statement.StartClause;

/**
 * A {@link CreateUniqueIndex}-{@link StartClause} of an SQL {@link CreateIndexStatement}.
 *
 * @since 1.0.0
 */
public class CreateUniqueIndex extends CreateIndexClause {

  /** Name of {@link CreateUniqueIndex} for marshaling. */
  public static final String NAME_CREATE_UNIQUE_INDEX = "CREATE UNIQUE INDEX";

  /**
   * The constructor.
   *
   * @param name the {@link #getName() name of the index}.
   */
  public CreateUniqueIndex(String name) {

    super(name);
  }

  @Override
  public boolean isUnique() {

    return true;
  }

}
