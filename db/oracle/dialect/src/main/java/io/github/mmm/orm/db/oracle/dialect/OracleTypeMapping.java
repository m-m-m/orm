/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.db.oracle.dialect;

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
 * {@link DbTypeMapping} for Oracle database.
 *
 * @since 1.0.0
 */
public class OracleTypeMapping extends DbTypeMapping {

  /**
   * The constructor.
   */
  public OracleTypeMapping() {

    super();
    add(new DbTypeLong("NUMBER(19)"));
    add(new DbTypeInteger("NUMBER(10)"));
    add(new DbTypeShort("NUMBER(5)"));
    add(new DbTypeByte("NUMBER(3)"));
    add(new DbTypeDouble("NUMBER(19,4)"));
    add(new DbTypeFloat("NUMBER(19,4)"));
    add(new DbTypeBigDecimal("NUMBER(38,5)"));
    add(new DbTypeBigInteger2Number("NUMBER(38)"));
    add(new DbTypeBoolean("BOOLEAN"));
    add(new DbTypeCharacter("CHAR"));
    add(new DbTypeUuid("UUID"));
    add(new DbTypeInstant("TIMESTAMP"));
    add(new DbTypeOffsetDateTime("TIMESTAMP"));
    add(new DbTypeZonedDateTime("TIMESTAMP"));
    add(new DbTypeLocalDate("DATE"));
    add(new DbTypeLocalTime("DATE"));
    add(new DbTypeOffsetTime("DATE"));
    add(new DbTypeLocalDateTime("DATE"));
    add(new DbTypeBoolean("BOOLEAN")); // requires Oracle DB >= 21
    add(new DbTypeCharacter("CHAR"));
    add(new DbTypeUuid("UUID")); // ???
    addBinary("BINARY", "BINARY(%s)");
    addString("VARCHAR2", "VARCHAR2(%s CHAR)", "VARCHAR2(%s CHAR)");
  }

  @Override
  protected String getDeclarationDecimalFormat() {

    return "NUMERIC(%s, %s)";
  }

}
