/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.param;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Iterator;

import io.github.mmm.base.exception.ObjectMismatchException;
import io.github.mmm.base.io.AppendableWriter;
import io.github.mmm.entity.bean.typemapping.TypeMapping;
import io.github.mmm.orm.dialect.AbstractDbDialect;
import io.github.mmm.orm.mapping.UnmappedTypeException;
import io.github.mmm.orm.type.DbType;
import io.github.mmm.property.criteria.CriteriaExpression;
import io.github.mmm.property.criteria.CriteriaParameters;
import io.github.mmm.property.criteria.Literal;
import io.github.mmm.value.converter.TypeMapper;

/**
 * {@link CriteriaParameters} using {@link CriteriaParameterImpl}.
 *
 * @since 1.0.0
 */
public abstract class AbstractCriteriaParameters implements CriteriaParameters<CriteriaParameterImpl<?>> {

  private final AbstractDbDialect<?> dialect;

  private CriteriaParameterImpl<?> first;

  private CriteriaParameterImpl<?> last;

  /**
   * The constructor.
   *
   * @param dialect the {@link AbstractDbDialect}.
   */
  public AbstractCriteriaParameters(AbstractDbDialect<?> dialect) {

    super();
    this.dialect = dialect;
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
        TypeMapping typeMapping = this.dialect.getOrm().getTypeMapping();
        DbType dbType = null;
        while (dbType == null) {
          Class<?> valueClass = value.getClass();
          TypeMapper typeMapper = typeMapping.getTypeMapper(valueClass);
          if (typeMapper == null) {
            throw new UnmappedTypeException(valueClass);
          } else if (typeMapper instanceof DbType type) {
            dbType = type;
          } else if (typeMapper.next() == null) {
            value = typeMapper.toTarget(value);
          } else {
            throw new ObjectMismatchException(valueClass, "atomic type");
          }
        }
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
