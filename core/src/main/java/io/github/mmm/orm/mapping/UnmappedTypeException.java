/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.mapping;

import io.github.mmm.base.exception.ApplicationException;
import io.github.mmm.value.CriteriaObject;

/**
 * {@link ApplicationException} thrown if a {@link Class Java type} is not mapped and therefore unsupported by ORM. If
 * you hit this exception, you need to register an according {@link io.github.mmm.value.converter.TypeMapper} or
 * {@link io.github.mmm.orm.type.DbType}.
 *
 * @since 1.0.0
 */
public final class UnmappedTypeException extends ApplicationException {

  /**
   * The constructor.
   *
   * @param valueClass the {@link Class} reflecting the type that could not be mapped.
   */
  public UnmappedTypeException(Class<?> valueClass) {

    this(valueClass, null);
  }

  /**
   * The constructor.
   *
   * @param valueClass the {@link Class} reflecting the type that could not be mapped.
   * @param selection the {@link CriteriaObject} to select.
   */
  public UnmappedTypeException(Class<?> valueClass, CriteriaObject<?> selection) {

    super(createMessage(valueClass, selection));
  }

  private static String createMessage(Class<?> valueClass, CriteriaObject<?> selection) {

    StringBuilder sb = new StringBuilder("No type mapping could be found for type ");
    sb.append(valueClass.getName());
    if (selection != null) {
      sb.append(" and selection ");
      sb.append(selection);
      sb.append('[');
      sb.append(selection.getClass().getSimpleName());
      sb.append(']');
    }
    return sb.toString();
  }

  @Override
  public String getCode() {

    return "DB-UTYP";
  }

}
