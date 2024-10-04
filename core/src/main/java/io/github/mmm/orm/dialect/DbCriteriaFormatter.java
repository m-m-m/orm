/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.dialect;

import io.github.mmm.base.io.AppendableWriter;
import io.github.mmm.orm.naming.DbNamingStrategy;
import io.github.mmm.orm.param.CriteriaParametersIndexed;
import io.github.mmm.property.ReadableProperty;
import io.github.mmm.property.criteria.CriteriaExpression;
import io.github.mmm.property.criteria.CriteriaFormatter;
import io.github.mmm.property.criteria.CriteriaParameters;
import io.github.mmm.value.PropertyPath;

/**
 * {@link CriteriaFormatter} for (relational) database.
 *
 * @see DbDialectStatementFormatter
 */
public class DbCriteriaFormatter extends CriteriaFormatter {

  private final AbstractDbDialect<?> dialect;

  private DbCriteriaFormatter(AbstractDbDialect<?> dialect, AppendableWriter out) {

    this(dialect, out, new CriteriaParametersIndexed(dialect));
  }

  /**
   * The constructor.
   *
   * @param dialect the {@link AbstractDbDialect}.
   * @param out the {@link AppendableWriter}.
   * @param parameters the {@link CriteriaParameters}.
   */
  protected DbCriteriaFormatter(AbstractDbDialect<?> dialect, AppendableWriter out, CriteriaParameters<?> parameters) {

    super(parameters, out);
    this.dialect = dialect;
  }

  @Override
  public void onPropertyPath(PropertyPath<?> property, int i, CriteriaExpression<?> parent) {

    DbNamingStrategy namingStrategy = this.dialect.getNamingStrategy();
    String columnName;
    if (property instanceof ReadableProperty<?> p) {
      columnName = namingStrategy.getColumnName(p);
    } else {
      columnName = namingStrategy.getColumnName(property.getName());
    }
    write(columnName);
  }

  /**
   * @param parameters the {@link CriteriaParameters}.
   * @return the new {@link DbCriteriaFormatter}.
   */
  static DbCriteriaFormatter of(AbstractDbDialect<?> dialect) {

    return new DbCriteriaFormatter(dialect, new AppendableWriter());
  }

}
