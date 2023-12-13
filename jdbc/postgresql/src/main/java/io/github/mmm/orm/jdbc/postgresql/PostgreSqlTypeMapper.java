/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.jdbc.postgresql;

import io.github.mmm.orm.type.DbTypeBigDecimal;
import io.github.mmm.orm.type.DbTypeBigInteger2Number;
import io.github.mmm.orm.type.DbTypeBoolean;
import io.github.mmm.orm.type.DbTypeByte;
import io.github.mmm.orm.type.DbTypeCharacter;
import io.github.mmm.orm.type.DbTypeDouble;
import io.github.mmm.orm.type.DbTypeDuration;
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
 * {@link DbTypeMapping} for <a href="https://www.postgresql.org/docs/9.5/datatype.html">PostgreSQL</a>.
 *
 * @since 1.0.0
 */
public class PostgreSqlTypeMapper extends DbTypeMapping {

  /**
   * The constructor.
   */
  public PostgreSqlTypeMapper() {

    super();
    add(new DbTypeLong("int8"));
    add(new DbTypeInteger("int4"));
    add(new DbTypeShort("int2"));
    add(new DbTypeByte("int2"));
    add(new DbTypeDouble("float8"));
    add(new DbTypeFloat("float4"));
    add(new DbTypeBigDecimal("numeric"));
    add(new DbTypeBigInteger2Number("numeric(1000)"));
    add(new DbTypeBoolean("bool"));
    add(new DbTypeCharacter("char(1)"));
    add(new DbTypeUuid("uuid"));
    add(new DbTypeInstant("timestamp"));
    add(new DbTypeOffsetDateTime("timestamp with time zone"));
    add(new DbTypeZonedDateTime("timestamp with time zone"));
    add(new DbTypeLocalDate("date"));
    add(new DbTypeLocalTime("time"));
    add(new DbTypeOffsetTime("time with time zone"));
    add(new DbTypeLocalDateTime("timestamp"));
    add(new DbTypeDuration("interval"));
    addBinary("bytea", "bytea");
    addString("text", "varchar(%s)", "char(%s)");
  }

  @Override
  protected String getDeclarationDecimalFormat() {

    return "numeric(%s, %s)";
  }

}
