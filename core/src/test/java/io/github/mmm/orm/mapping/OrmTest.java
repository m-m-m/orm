/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.mapping;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Iterator;

import org.junit.jupiter.api.Test;

import io.github.mmm.entity.id.LongId;
import io.github.mmm.orm.impl.OrmImpl;
import io.github.mmm.orm.naming.DbNamingStrategy;
import io.github.mmm.orm.result.DbResultCell;
import io.github.mmm.orm.result.DbResultRow;
import io.github.mmm.orm.statement.City;
import io.github.mmm.orm.statement.GeoLocation;
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
 * Test of {@link Orm} and {@link DbBeanMapper}.
 */
public class OrmTest {

  private Orm createOrm() {

    return new OrmImpl(new TestTypeMapping(), DbNamingStrategy.ofRdbms());
  }

  /** Test {@link DbBeanMapper} on {@link City}. */
  @Test
  public void testMapCity() {

    // arrange
    long id = 4711L;
    String name = "Frankfurt";
    int inhabitants = 773070;
    double longitude = 50.110924;
    double latitude = 8.682127;

    City city = City.of();
    city.Id().set(LongId.of(id).updateRevision());
    city.Name().set(name);
    city.Inhabitants().setValue(inhabitants);
    GeoLocation geoLocation = city.GeoLocation().get();
    geoLocation.Longitude().setValue(longitude);
    geoLocation.Latitude().setValue(latitude);
    Orm orm = createOrm();

    // act
    DbBeanMapper<City> mapping = orm.createBeanMapping(city);
    DbResultRow row = mapping.java2db(city);
    City city2 = mapping.db2java(row);

    // assert
    Iterator<DbResultCell<?>> cellIterator = row.getCells().iterator();
    checkCell(cellIterator, "GEO_LOCATION_LATITUDE", latitude, "DOUBLE PRECISION");
    checkCell(cellIterator, "GEO_LOCATION_LONGITUDE", longitude, "DOUBLE PRECISION");
    checkCell(cellIterator, "ID", id, "BIGINT");
    checkCell(cellIterator, "REV", 1L, "BIGINT");
    checkCell(cellIterator, "INHABITANTS", inhabitants, "INTEGER");
    checkCell(cellIterator, "NAME", name, "VARCHAR");
    System.out.println(cellIterator.next());
    System.out.println(cellIterator.next());

    assertThat(cellIterator.hasNext()).isFalse();
    assertThat(city.isEqual(city2)).isTrue();
  }

  private void checkCell(Iterator<DbResultCell<?>> cellIterator, String dbName, Object value, String declaration) {

    assertThat(cellIterator).hasNext();
    DbResultCell<?> cell = cellIterator.next();
    assertThat(cell.getDbName()).isEqualTo(dbName);
    assertThat(cell.getValue()).isEqualTo(value);
    assertThat(cell.getDeclaration()).isEqualTo(declaration);
  }

  private static class TestTypeMapping extends DbTypeMapping {

    /**
     * The constructor.
     */
    public TestTypeMapping() {

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

  }

}
