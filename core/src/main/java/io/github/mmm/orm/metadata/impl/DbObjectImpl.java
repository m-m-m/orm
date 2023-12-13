/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.metadata.impl;

/**
 * Abstract base class for {@link io.github.mmm.orm.metadata.DbObject} and
 * {@link io.github.mmm.orm.metadata.DbObjectContainer}.
 *
 * @since 1.0.0
 */
public abstract class DbObjectImpl {

  @Override
  public final String toString() {

    StringBuilder sb = new StringBuilder();
    toString(sb, true);
    return sb.toString();
  }

  /**
   * @param sb the {@link StringBuilder}.
   * @param detailed {@code true} to include details (extra properties), {@code false} otherwise (only name/ref).
   */
  public abstract void toString(StringBuilder sb, boolean detailed);

}
