/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.jdbc.mysql;

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
 * {@link DbTypeMapping} for <a href="http://www.h2database.com/html/datatypes.html">H2 database</a>.
 *
 * @since 1.0.0
 */
public class MySqlTypeMapping extends DbTypeMapping {

  /**
   * The constructor.
   */
  public MySqlTypeMapping() {

    super();
    add(new DbTypeLong("BIGINT"));
    add(new DbTypeInteger("INTEGER"));
    add(new DbTypeShort("SMALLINT"));
    add(new DbTypeByte("TINYINT"));
    add(new DbTypeDouble("DOUBLE PRECISION"));
    add(new DbTypeFloat("REAL"));
    add(new DbTypeBigDecimal("NUMERIC"));
    add(new DbTypeBigInteger2Number("NUMERIC(100000)"));
    add(new DbTypeBoolean("BOOLEAN"));
    add(new DbTypeCharacter("CHAR"));
    add(new DbTypeUuid("UUID"));
    add(new DbTypeInstant("TIMESTAMP"));
    add(new DbTypeOffsetDateTime("TIMESTAMP WITH TIME ZONE"));
    add(new DbTypeZonedDateTime("TIMESTAMP WITH TIME ZONE"));
    add(new DbTypeLocalDate("DATE"));
    add(new DbTypeLocalTime("TIME"));
    add(new DbTypeOffsetTime("TIME WITH TIME ZONE"));
    add(new DbTypeLocalDateTime("DATETIME"));
    addBinary("BINARY", "BINARY(%s)");
    addString("VARCHAR", "VARCHAR(%s)", "CHAR(%s)");
  }

  @Override
  protected String getDeclarationDecimalFormat() {

    return "NUMERIC(%s, %s)";
  }

}
