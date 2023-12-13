/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.metadata;

/**
 * Interface for any database metadata object such as a {@link DbTable}, {@link DbColumn}, {@link DbForeignKey}, etc.
 */
public interface DbObject {

  /**
   * @return the {@link DbName#get() name} of this object. May not be {@code null}.
   */
  DbName getName();

}
