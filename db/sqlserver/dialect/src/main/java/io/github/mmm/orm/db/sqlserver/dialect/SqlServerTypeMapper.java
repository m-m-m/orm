/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.db.sqlserver.dialect;

import io.github.mmm.orm.type.DbTypeBigDecimal;
import io.github.mmm.orm.type.DbTypeBigInteger2Number;
import io.github.mmm.orm.type.DbTypeBoolean;
import io.github.mmm.orm.type.DbTypeByte;
import io.github.mmm.orm.type.DbTypeCharacter;
import io.github.mmm.orm.type.DbTypeDouble;
import io.github.mmm.orm.type.DbTypeFloat;
import io.github.mmm.orm.type.DbTypeInstant;
import io.github.mmm.orm.type.DbTypeInteger;
import io.github.mmm.orm.type.DbTypeLocalDate;
import io.github.mmm.orm.type.DbTypeLocalDateTime;
import io.github.mmm.orm.type.DbTypeLocalTime;
import io.github.mmm.orm.type.DbTypeLong;
import io.github.mmm.orm.type.DbTypeOffsetDateTime;
import io.github.mmm.orm.type.DbTypeOffsetTime;
import io.github.mmm.orm.type.DbTypeShort;
import io.github.mmm.orm.type.DbTypeUuid;
import io.github.mmm.orm.type.DbTypeZonedDateTime;
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
    add(new DbTypeLong("int8"));
    add(new DbTypeInteger("int4"));
    add(new DbTypeShort("int2"));
    add(new DbTypeByte("int2"));
    add(new DbTypeDouble("real"));
    add(new DbTypeFloat("float"));
    add(new DbTypeBigDecimal("decimal"));
    add(new DbTypeBigInteger2Number("decimal(38)"));
    add(new DbTypeBoolean("bool"));
    add(new DbTypeCharacter("char(1)"));
    add(new DbTypeUuid("uuid"));
    // TODO
    add(new DbTypeInstant("datetime2"));
    add(new DbTypeOffsetDateTime("datetimeoffset(7)"));
    add(new DbTypeZonedDateTime("datetimeoffset(7)"));
    add(new DbTypeLocalDate("date"));
    add(new DbTypeLocalTime("time(7)"));
    add(new DbTypeOffsetTime("datetimeoffset(7)"));
    add(new DbTypeLocalDateTime("datetime2"));
    addBinary("varbinary(max)", "varbinary(%s)");
    addString("varchar(max)", "varchar(%s)", "char(%s)");
  }

  @Override
  protected String getDeclarationDecimalFormat() {

    return "decimal(%s, %s)";
  }

}
