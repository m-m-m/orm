/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.dialect;

import java.util.Objects;

import io.github.mmm.entity.bean.EntityBean;
import io.github.mmm.orm.mapping.DbBeanMapper;
import io.github.mmm.orm.param.CriteriaParametersFactory;
import io.github.mmm.orm.param.CriteriaParametersIndexed;
import io.github.mmm.orm.result.DbResult;
import io.github.mmm.orm.result.DbResultValue;
import io.github.mmm.orm.statement.AbstractEntityClause;
import io.github.mmm.orm.statement.BasicDbStatementFormatter;
import io.github.mmm.orm.statement.select.SelectFrom;
import io.github.mmm.property.criteria.CriteriaFormatterFactory;

/**
 * {@link BasicDbStatementFormatter} for any real {@link DbDialect database dialect}.
 */
public class DbDialectStatementFormatter extends BasicDbStatementFormatter {

  /**
   * The constructor.
   *
   * @param dialect the owning {@link AbstractDbDialect dialect}.
   */
  public DbDialectStatementFormatter(AbstractDbDialect<?> dialect) {

    this(dialect, CriteriaParametersIndexed.FACTORY);
  }

  /**
   * The constructor.
   *
   * @param dialect the owning {@link AbstractDbDialect dialect}.
   * @param parametersFactory the {@link CriteriaFormatterFactory}.
   */
  public DbDialectStatementFormatter(AbstractDbDialect<?> dialect, CriteriaParametersFactory parametersFactory) {

    this(dialect, parametersFactory, INDENTATION);
  }

  /**
   * The constructor.
   *
   * @param dialect the owning {@link AbstractDbDialect dialect}.
   * @param parametersFactory the {@link CriteriaFormatterFactory}.
   * @param indentation the {@link #getIndentation() indentation}.
   */
  public DbDialectStatementFormatter(AbstractDbDialect<?> dialect, CriteriaParametersFactory parametersFactory,
      String indentation) {

    super(dialect, parametersFactory, indentation);
    Objects.requireNonNull(dialect);
  }

  @Override
  protected void formatEntityName(AbstractEntityClause<?, ?, ?> entityClause) {

    String table = this.dialect.getNamingStrategy().getTableName(entityClause);
    write(table);
  }

  @Override
  protected void formatSelectAll(SelectFrom<?, ?> selectFrom) {

    EntityBean entity = selectFrom.getEntity();
    DbBeanMapper<EntityBean> mapping = this.dialect.getOrm().createBeanMapper(entity);
    DbResult dbResult = mapping.java2db(entity);
    String s = "";
    for (DbResultValue<?> dbValue : dbResult) {
      write(s);
      String columnName = dbValue.getName();
      write(columnName);
      s = ", ";
    }
    write(" ");
  }

}
