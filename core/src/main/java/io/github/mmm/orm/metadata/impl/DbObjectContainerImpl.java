/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.metadata.impl;

import java.util.Iterator;

import io.github.mmm.orm.metadata.DbObject;
import io.github.mmm.orm.metadata.DbObjectContainer;

/**
 * Implementation of {@link DbObjectContainer}.
 *
 * @param <O> type of the contained {@link DbObject}s.
 * @since 1.0.0
 */
public abstract class DbObjectContainerImpl<O extends DbObject> extends DbObjectImpl implements DbObjectContainer<O> {

  @Override
  public void toString(StringBuilder sb, boolean detailed) {

    sb.append('{');
    boolean first = true;
    Iterator<O> iterator = iterator();
    while (iterator.hasNext()) {
      O child = iterator.next();
      if (first) {
        first = false;
      } else {
        sb.append(',');
      }
      ((DbObjectImpl) child).toString(sb, false);
    }
    sb.append('}');
  }

}
