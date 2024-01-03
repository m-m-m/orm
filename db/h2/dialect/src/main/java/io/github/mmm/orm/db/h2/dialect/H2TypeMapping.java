/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.db.h2.dialect;

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
 * {@link DbTypeMapping} for <a href="http://www.h2database.com/html/datatypes.html">H2 database</a>.
 *
 * @since 1.0.0
 */
public class H2TypeMapping extends DbTypeMapping {

  /**
   * The constructor.
   */
  public H2TypeMapping() {

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
    add(new DbTypeInstant2Timestamp("TIMESTAMP"));
    add(new DbTypeOffsetDateTime("TIMESTAMP WITH TIME ZONE"));
    add(new DbTypeZonedDateTime("TIMESTAMP WITH TIME ZONE"));
    add(new DbTypeLocalDate2Date("DATE"));
    add(new DbTypeLocalTime2Time("TIME"));
    add(new DbTypeOffsetTime("TIME WITH TIME ZONE"));
    add(new DbTypeLocalDateTime2Timestamp("DATETIME"));
    addBinary("BINARY", "BINARY(%s)");
    addString("VARCHAR", "VARCHAR(%s)", "CHAR(%s)");
  }

  @Override
  protected String getDeclarationDecimalFormat() {

    return "NUMERIC(%s, %s)";
  }

}
