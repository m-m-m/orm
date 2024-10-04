/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.statement;

import io.github.mmm.base.io.AppendableWriter;
import io.github.mmm.orm.dialect.AbstractDbDialect;
import io.github.mmm.orm.dialect.DbCriteriaFormatter;
import io.github.mmm.orm.dialect.DbDialectStatementFormatter;
import io.github.mmm.orm.mapping.Orm;
import io.github.mmm.orm.type.DbTypeBigDecimal;
import io.github.mmm.orm.type.DbTypeBigInteger2Number;
import io.github.mmm.orm.type.DbTypeBoolean;
import io.github.mmm.orm.type.DbTypeByte;
import io.github.mmm.orm.type.DbTypeCharacter;
import io.github.mmm.orm.type.DbTypeDouble;
import io.github.mmm.orm.type.DbTypeFloat;
import io.github.mmm.orm.type.DbTypeInstant2Timestamp;
import io.github.mmm.orm.type.DbTypeInteger;
import io.github.mmm.orm.type.DbTypeLocalDate2Date;
import io.github.mmm.orm.type.DbTypeLocalDateTime2Timestamp;
import io.github.mmm.orm.type.DbTypeLocalTime2Time;
import io.github.mmm.orm.type.DbTypeLong;
import io.github.mmm.orm.type.DbTypeOffsetDateTime;
import io.github.mmm.orm.type.DbTypeOffsetTime;
import io.github.mmm.orm.type.DbTypeShort;
import io.github.mmm.orm.type.DbTypeUuid;
import io.github.mmm.orm.type.DbTypeZonedDateTime;
import io.github.mmm.orm.typemapping.DbTypeMapping;

/**
 * Implementation of {@link AbstractDbDialect} for testing SQL database emulation.
 */
public class SqlDialect extends AbstractDbDialect<SqlDialect> {

  /**
   * The constructor.
   */
  public SqlDialect() {

    super(new SqlTypeMapping());
  }

  @Override
  public String getId() {

    return "sql";
  }

  @Override
  public DbDialectStatementFormatter createFormatter() {

    return new DbDialectStatementFormatter(this, new SqlCriteriaFormatter(this), "  ");
  }

  @Override
  protected SqlDialect withOrm(Orm newOrm) {

    throw new UnsupportedOperationException();
  }

  private static class SqlTypeMapping extends DbTypeMapping {
    private SqlTypeMapping() {

      super();
      add(new DbTypeLong("Long"));
      add(new DbTypeInteger("Integer"));
      add(new DbTypeShort("Short"));
      add(new DbTypeByte("Byte"));
      add(new DbTypeDouble("Double"));
      add(new DbTypeFloat("Float"));
      add(new DbTypeBigDecimal("BigDecimal"));
      add(new DbTypeBigInteger2Number("BigInteger"));
      add(new DbTypeBoolean("Boolean"));
      add(new DbTypeCharacter("Character"));
      add(new DbTypeUuid("Uuid"));
      add(new DbTypeInstant2Timestamp("Instant"));
      add(new DbTypeOffsetDateTime("OffsetDateTime"));
      add(new DbTypeZonedDateTime("ZonedDateTime"));
      add(new DbTypeLocalDate2Date("LocalDate"));
      add(new DbTypeLocalTime2Time("LocalTime"));
      add(new DbTypeOffsetTime("OffsetTime"));
      add(new DbTypeLocalDateTime2Timestamp("LocalDateTime"));
      addString("String", "String(%s)", "String(%s)");
    }
  }

  private static final class SqlCriteriaFormatter extends DbCriteriaFormatter {

    private SqlCriteriaFormatter(SqlDialect dialect) {

      super(dialect, new AppendableWriter(), null);
    }

  }
}
