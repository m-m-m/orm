/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.statement.select;

import org.junit.jupiter.api.Test;

import io.github.mmm.orm.dialect.AbstractDbDialect;
import io.github.mmm.orm.param.CriteriaParametersNamed;
import io.github.mmm.orm.statement.City;
import io.github.mmm.orm.statement.DbStatementFormatter;
import io.github.mmm.orm.statement.DbStatementTest;
import io.github.mmm.orm.statement.Person;
import io.github.mmm.orm.statement.Result;
import io.github.mmm.orm.statement.Song;
import io.github.mmm.orm.test.TestDialect;
import io.github.mmm.property.criteria.CriteriaFormatter;

/**
 * Test of {@link Select} and {@link SelectStatement}.
 */
public class SelectTest extends DbStatementTest {

  /** Test creation of {@link SelectStatement} and verifying resulting pseudo-SQL. */
  @Test
  public void testSelectSqlAndJson() {

    // given
    Person p = Person.of();
    String sql = "SELECT p FROM Person p WHERE p.Age >= 18 AND (p.Name LIKE 'John%' OR p.Single = TRUE) ORDER BY p.Name ASC";
    // when
    SelectStatement<Person> query = new SelectEntity<>(p).from().as("p")
        .where(p.Age().ge(18).and(p.Name().like("John*").or(p.Single().eq(true)))).orderBy(p.Name().asc()).get();
    // then
    check(query, sql, '"' + sql + '"');
    AbstractDbDialect<?> dialect = new TestDialect();
    // and when
    DbStatementFormatter sqlFormatter = new DbStatementFormatter(
        CriteriaFormatter.of(new CriteriaParametersNamed(dialect, true))) {

      @Override
      public boolean isUseAsBeforeAlias() {

        return true;
      }
    };

    assertThat(sqlFormatter.onStatement(query).toString()).isEqualTo(
        "SELECT p FROM Person AS p WHERE p.Age >= :Age AND (p.Name LIKE :Name OR p.Single = :Single) ORDER BY p.Name ASC");

    CriteriaParametersNamed parameters = sqlFormatter.getCriteriaFormatter().getParameters().cast();

    assertThat(parameters.getParameters()).containsEntry("Age", 18).containsEntry("Name", "John%")
        .containsEntry("Single", Boolean.TRUE).hasSize(3);
  }

  /** Test creation of {@link SelectStatement} and verifying resulting pseudo-SQL. */
  @Test
  public void testSelectAll() {

    // given
    Person p = Person.of();
    // when
    SelectStatement<Person> query = new SelectEntity<>(p).from().as("p").get();
    // then
    check(query, "SELECT p FROM Person p");
  }

  /** Test creation of {@link SelectStatement} and verifying resulting pseudo-SQL including auto-generated alias. */
  @Test
  public void testSelectAllWithAutoGeneratedAlias() {

    // given
    Person p = Person.of();
    Person p2 = Person.of();
    // when
    SelectStatement<Person> query = new SelectEntity<>(p).from().and(p2).where(p.Id().eq(p2.Id())).get();
    // then
    check(query, "SELECT p FROM Person p, Person pe WHERE p.Id = pe.Id");
  }

  /** Test creation of {@link SelectStatement} and verifying resulting pseudo-SQL. */
  @Test
  public void testSelectComplex() {

    // given
    Person p = Person.of();
    Song s = Song.of();
    Result r = Result.of();
    String sql = "SELECT new Result(song.Genre, COUNT(song.Id) Count, AVG(song.Duration) Duration) FROM Song song, Person p WHERE song.Composer = p.Id AND song.Duration <= 10800 ORDER BY song.Genre ASC";
    // when
    SelectStatement<Result> query = new SelectProjection<>(r, s.Genre(), r.Genre()).and(s.Id().count(), r.Count())
        .and(s.Duration().avg(), r.Duration()) //
        .from(s).as("song").and(p, "p") //
        .where(s.Composer().eq(p.Id()).and(s.Duration().le(3 * 60 * 60L))) //
        .groupBy(s.Genre()).orderBy(s.Genre().asc()).get();
    // then
    check(query, sql, '"' + sql + '"');
  }

  /** Test creation of {@link SelectStatement} and verifying resulting pseudo-SQL. */
  @Test
  public void testSelectEmbeddedProperties() {

    // given
    City c = City.of();
    // when
    SelectStatement<City> query = new SelectEntity<>(c).from().as("c")
        .where(c.GeoLocation().get().Latitude().eq(40.6892534).and(c.GeoLocation().get().Longitude().eq(-74.0466891)))
        .get();
    // then
    check(query,
        "SELECT c FROM City c WHERE c.GeoLocation.Latitude = 40.6892534 AND c.GeoLocation.Longitude = -74.0466891");
  }

}
