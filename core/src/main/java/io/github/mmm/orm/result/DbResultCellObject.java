/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.result;

import io.github.mmm.entity.bean.EntityBean;
import io.github.mmm.property.ReadableProperty;
import io.github.mmm.property.criteria.CriteriaExpression;
import io.github.mmm.value.CriteriaObject;
import io.github.mmm.value.PropertyPath;

/**
 * Implementation of {@link DbResultCell} as simple object.
 *
 * @param <V> type of the {@link #getValue() value}.
 * @since 1.0.0
 */
public class DbResultCellObject<V> implements DbResultCell<V> {

  /** @see #getSelection() */
  protected final CriteriaObject<?> selection;

  /** @see #getDbName() */
  protected final String dbName;

  final V value;

  /**
   * The constructor.
   *
   * @param selection the {@link #getSelection() selection}.
   * @param value the result {@link #getValue() value} from the DB.
   */
  public DbResultCellObject(CriteriaObject<?> selection, V value) {

    this(selection, value, computeDbName(selection));
  }

  /**
   * The constructor.
   *
   * @param selection the {@link #getSelection() selection}.
   * @param value the result {@link #getValue() value} from the DB.
   * @param dbName the {@link #getDbName() database name}.
   */
  public DbResultCellObject(CriteriaObject<?> selection, V value, String dbName) {

    super();
    this.selection = selection;
    this.value = value;
    this.dbName = dbName;
  }

  @Override
  public CriteriaObject<?> getSelection() {

    return this.selection;
  }

  @Override
  public String getDbName() {

    return this.dbName;
  }

  private static String computeDbName(CriteriaObject<?> selection) {

    StringBuilder sb = new StringBuilder();
    computeDbName(selection, sb);
    return sb.toString();
  }

  private static void computeDbName(CriteriaObject<?> selection, StringBuilder sb) {

    if (selection instanceof ReadableProperty<?> p) {
      String column = p.getMetadata().getMetaInfo().get(EntityBean.META_KEY_COLUMN);
      if (column == null) {
        column = toDbName(p.getName());
      }
      sb.append(column);
    } else if (selection instanceof PropertyPath<?> pp) {
      sb.append(toDbName(pp.getName()));
    } else if (selection instanceof CriteriaExpression) {
      CriteriaExpression<?> expression = (CriteriaExpression<?>) selection;
      String op = expression.getOperator().getName();
      int count = expression.getArgCount();
      if (count == 0) {
        sb.append(op);
      } else if (count == 1) {
        sb.append(op);
        sb.append('_');
        computeDbName(expression.getFirstArg(), sb);
      } else {
        String infix = "";
        for (CriteriaObject<?> arg : expression.getArgs()) {
          sb.append(infix);
          computeDbName(arg, sb);
          if (infix.isEmpty()) {
            infix = "_" + op + "_";
          }
        }
      }
    } else {
      sb.append(selection.toString());
    }
  }

  private static String toDbName(String javaName) {

    // TODO use naming strategy, therefore non static, refactor all
    return javaName;
  }

  @Override
  public V getValue() {

    return this.value;
  }

  @Override
  public DbResultCellObject<V> withValue(V newValue) {

    if (this.value == newValue) {
      return this;
    }
    return new DbResultCellObject<>(this.selection, newValue, this.dbName);
  }

  @Override
  public String toString() {

    return this.dbName + "[" + getDeclaration() + "]=" + this.value;
  }

}