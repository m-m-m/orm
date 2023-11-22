/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.jdbc.sqlserver;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZonedDateTime;
import java.util.UUID;

import io.github.mmm.orm.typemapping.DbTypeMapping;

/**
 * {@link DbTypeMapping} for <a href="https://docs.microsoft.com/en-us/sql/t-sql/data-types/data-types-transact-sql">MS
 * SQL Server</a>.
 *
 * @since 1.0.0
 */
public class SqlServerTypeMapper extends DbTypeMapping {

  /**
   * The constructor.
   */
  public SqlServerTypeMapper() {

    super();
    add(Long.class, "int8");
    add(Integer.class, "int4");
    add(Short.class, "int2");
    add(Byte.class, "int2");
    add(Double.class, "real");
    add(Float.class, "float");
    add(BigDecimal.class, "decimal");
    add(BigInteger.class, "decimal(38)");
    add(Boolean.class, "bool");
    add(Character.class, "char(1)");
    add(UUID.class, "uuid");
    add(Instant.class, "datetime2");
    add(OffsetDateTime.class, "datetimeoffset(7)");
    add(ZonedDateTime.class, "datetimeoffset(7)");
    add(LocalDate.class, "date");
    add(LocalTime.class, "time(7)");
    add(LocalDateTime.class, "datetime2");
    addBinary("varbinary(max)", "varbinary(%s)");
    addString("varchar(max)", "varchar(%s)", "char(%s)");
  }

  @Override
  protected String getDeclarationDecimalFormat() {

    return "decimal(%s, %s)";
  }

}
