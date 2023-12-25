/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.dialect;

import java.util.Objects;

import io.github.mmm.entity.bean.EntityBean;
import io.github.mmm.orm.mapping.DbBeanMapper;
import io.github.mmm.orm.param.CriteriaParametersIndexed;
import io.github.mmm.orm.result.DbResult;
import io.github.mmm.orm.result.DbResultEntry;
import io.github.mmm.orm.statement.AbstractEntityClause;
import io.github.mmm.orm.statement.DbStatementFormatter;
import io.github.mmm.orm.statement.select.SelectFrom;
import io.github.mmm.property.criteria.CriteriaFormatter;

/**
 * Abstract base class of {@link DbStatementFormatter} for any real {@link DbDialect database dialect}.
 */
public abstract class DbDialectStatementFormatter extends DbStatementFormatter {

  /**
   * The constructor.
   *
   * @param dialect the owning {@link AbstractDbDialect dialect}.
   */
  public DbDialectStatementFormatter(AbstractDbDialect<?> dialect) {

    this(dialect, CriteriaFormatter.of(new CriteriaParametersIndexed()), INDENTATION);
  }

  /**
   * The constructor.
   *
   * @param dialect the owning {@link AbstractDbDialect dialect}.
   * @param criteriaFormatter the {@link CriteriaFormatter} used to format criteria fragments to database syntax (e.g.
   *        SQL).
   * @param indentation the {@link #getIndentation() indentation}.
   */
  public DbDialectStatementFormatter(AbstractDbDialect<?> dialect, CriteriaFormatter criteriaFormatter,
      String indentation) {

    super(dialect, criteriaFormatter, indentation);
    Objects.requireNonNull(dialect);
  }

  @Override
  protected void writeEntityName(AbstractEntityClause<?, ?, ?> entityClause) {

    String table = this.dialect.getNamingStrategy().getTableName(entityClause);
    write(table);
  }

  @Override
  protected void onSelectAll(SelectFrom<?, ?> selectFrom) {

    EntityBean entity = selectFrom.getEntity();
    DbBeanMapper<EntityBean> mapping = this.dialect.getOrm().createBeanMapping(entity, entity.getProperties());
    DbResult result = mapping.java2db(entity);
    String s = "";
    for (DbResultEntry<?> entry : result) {
      write(s);
      String columnName = entry.getDbName();
      write(columnName);
      s = ", ";
    }
    super.onSelectAll(selectFrom);
  }

}
