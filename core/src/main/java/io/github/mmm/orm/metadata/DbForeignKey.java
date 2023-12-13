/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.metadata;

/**
 * Interface for a foreign key of a {@link DbTable}.
 */
public interface DbForeignKey extends DbObject {

  /**
   * @return the {@link DbObjectContainer container} with the foreign key {@link DbColumnReference columns}. Typically a
   *         foreign key is just referencing a single column.
   */
  DbObjectContainer<DbColumnReference> getColumns();

}
