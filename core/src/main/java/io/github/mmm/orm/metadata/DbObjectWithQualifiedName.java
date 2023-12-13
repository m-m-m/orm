/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.metadata;

/**
 * Interface for any database metadata object such as a {@link DbTable}, {@link DbColumn}, {@link DbForeignKey}, etc.
 */
public interface DbObjectWithQualifiedName extends DbObject {

  /**
   * @return the {@link DbQualifiedName qualified name} of this object.
   */
  DbQualifiedName getQualifiedName();

  @Override
  default DbName getName() {

    return getQualifiedName().getName();
  }

}
