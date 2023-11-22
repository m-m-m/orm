/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.param;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Iterator;

import io.github.mmm.base.io.AppendableWriter;
import io.github.mmm.orm.type.DbType;
import io.github.mmm.property.criteria.CriteriaExpression;
import io.github.mmm.property.criteria.CriteriaParameters;
import io.github.mmm.property.criteria.Literal;

/**
 * {@link CriteriaParameters} using {@link CriteriaParameterImpl}.
 *
 * @since 1.0.0
 */
public abstract class AbstractCriteriaParameters implements CriteriaParameters<CriteriaParameterImpl<?>> {

  private CriteriaParameterImpl<?> first;

  private CriteriaParameterImpl<?> last;

  /**
   * The constructor.
   */
  public AbstractCriteriaParameters() {

    super();
  }

  @SuppressWarnings({ "unchecked", "rawtypes" })
  @Override
  public void onLiteral(Literal<?> literal, AppendableWriter out, CriteriaExpression<?> parent) {

    if (literal != null) {
      Object value = literal.get();
      if (value != null) {
        int index = 0;
        if (this.last != null) {
          index = this.last.getIndex() + 1;
        }
        DbType dbType = null;
        CriteriaParameterImpl<?> param = createParameter(index, value, dbType, parent);
        if (this.last == null) {
          this.first = param;
          this.last = param;
        } else {
          this.last.append(param);
          this.last = param;
        }
        out.append(param.getPlaceholder());
        return;
      }
    }
    out.append("null");
  }

  /**
   * @param <V> type of the {@code value}.
   * @param index the {@link CriteriaParameterImpl#getIndex() index}.
   * @param value the {@link CriteriaParameterImpl#getValue() value}.
   * @param dbType the the {@link CriteriaParameterImpl#getDbType() database type}.
   * @param parent the parent {@link CriteriaExpression} (see
   *        {@link #onLiteral(Literal, AppendableWriter, CriteriaExpression)}).
   * @return the new {@link CriteriaParameterImpl}.
   */
  protected abstract <V> CriteriaParameterImpl<V> createParameter(int index, V value, DbType<V, ?> dbType,
      CriteriaExpression<?> parent);

  @Override
  public Iterator<CriteriaParameterImpl<?>> iterator() {

    if (this.first == null) {
      return Collections.emptyIterator();
    }
    return new CriteriaParameterIterator(this.first);
  }

  /**
   * @param statement the {@link PreparedStatement}.
   * @param connection the JDBC {@link Connection}.
   * @throws SQLException on error.
   */
  @SuppressWarnings("exports")
  public void apply(PreparedStatement statement, Connection connection) throws SQLException {

    for (CriteriaParameterImpl<?> param : this) {
      param.apply(statement, connection);
    }
  }

}
