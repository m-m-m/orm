/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.mapping;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Iterator;

import org.junit.jupiter.api.Test;

import io.github.mmm.entity.id.PkId;
import io.github.mmm.entity.id.RevisionedIdVersion;
import io.github.mmm.orm.impl.OrmImpl;
import io.github.mmm.orm.naming.DbNamingStrategy;
import io.github.mmm.orm.result.DbResult;
import io.github.mmm.orm.result.DbResultValue;
import io.github.mmm.orm.statement.City;
import io.github.mmm.orm.statement.GeoLocation;
import io.github.mmm.orm.test.TestTypeMapping;

/**
 * Test of {@link Orm} and {@link DbBeanMapper}.
 */
class OrmTest {

  private Orm createOrm() {

    return new OrmImpl(new TestTypeMapping(), DbNamingStrategy.ofRdbms());
  }

  /** Test {@link DbBeanMapper} on {@link City}. */
  @Test
  void testMapCity() {

    // arrange
    long id = 4711L;
    long revision = 1L;
    String name = "Frankfurt";
    int inhabitants = 773070;
    double longitude = 50.110924;
    double latitude = 8.682127;

    City city = City.of();
    city.Id().set(new RevisionedIdVersion<>(PkId.of(City.class, id), revision));
    city.Name().set(name);
    city.Inhabitants().setValue(inhabitants);
    GeoLocation geoLocation = city.GeoLocation().get();
    geoLocation.Longitude().setValue(longitude);
    geoLocation.Latitude().setValue(latitude);
    Orm orm = createOrm();

    // act
    DbBeanMapper<City> mapping = orm.createBeanMapper(city);
    DbResult dbResult = mapping.java2db(city);
    City city2 = mapping.db2java(dbResult);

    // assert
    Iterator<DbResultValue<?>> dbValueIterator = dbResult.iterator();
    checkCell(dbValueIterator, "GEO_LOCATION$LATITUDE", latitude, "DOUBLE PRECISION");
    checkCell(dbValueIterator, "GEO_LOCATION$LONGITUDE", longitude, "DOUBLE PRECISION");
    checkCell(dbValueIterator, "ID", id, "BIGINT");
    checkCell(dbValueIterator, "REV", revision, "BIGINT");
    checkCell(dbValueIterator, "INHABITANTS", inhabitants, "INTEGER");
    checkCell(dbValueIterator, "NAME", name, "VARCHAR");
    assertThat(dbValueIterator.hasNext()).isFalse();
    assertThat(city.isEqual(city2)).isTrue();
  }

  private void checkCell(Iterator<DbResultValue<?>> dbValueIterator, String dbName, Object value, String declaration) {

    assertThat(dbValueIterator).hasNext();
    DbResultValue<?> dbValue = dbValueIterator.next();
    assertThat(dbValue.getName()).isEqualTo(dbName);
    assertThat(dbValue.getValue()).isEqualTo(value);
    assertThat(dbValue.getDeclaration()).isEqualTo(declaration);
  }

}
