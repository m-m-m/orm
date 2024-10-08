/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.statement.select;

import org.junit.jupiter.api.Test;

import io.github.mmm.orm.dialect.AbstractDbDialect;
import io.github.mmm.orm.metadata.DbName;
import io.github.mmm.orm.param.CriteriaParametersNamed;
import io.github.mmm.orm.statement.BasicDbStatementFormatter;
import io.github.mmm.orm.statement.City;
import io.github.mmm.orm.statement.DbStatement;
import io.github.mmm.orm.statement.DbStatementTest;
import io.github.mmm.orm.statement.Person;
import io.github.mmm.orm.statement.Result;
import io.github.mmm.orm.statement.Song;
import io.github.mmm.orm.test.TestDialect;
import io.github.mmm.property.criteria.CriteriaAggregation;
import io.github.mmm.property.criteria.CriteriaFormatter;

/**
 * Test of {@link SelectClause} and {@link SelectStatement}.
 */
public class SelectTest extends DbStatementTest {

  /** Test creation of {@link SelectStatement} and verifying resulting pseudo-SQL. */
  @Test
  public void testSelectSqlAndJson() {

    // given
    Person p = Person.of();
    // when
    SelectStatement<Person> query = DbStatement.select(p).as("p")
        .where(p.Age().ge(18).and(p.Name().like("John*").or(p.Single().eq(true)))).orderBy(p.Name().asc()).get();
    // then
    check(query,
        "SELECT p FROM Person p WHERE p.Age >= 18 AND (p.Name LIKE 'John%' OR p.Single = TRUE) ORDER BY p.Name ASC",
        true);
    AbstractDbDialect<?> dialect = new TestDialect();
    // and when
    BasicDbStatementFormatter sqlFormatter = new BasicDbStatementFormatter(
        CriteriaFormatter.of(new CriteriaParametersNamed(dialect, true))) {

      @Override
      public boolean isUseAsBeforeAlias() {

        return true;
      }
    };

    assertThat(sqlFormatter.formatStatement(query).get()).isEqualTo(
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
    SelectStatement<Person> query = DbStatement.select(p).as("p").get();
    // then
    check(query, "SELECT p FROM Person p", true);
  }

  /** Test creation of {@link SelectStatement} and verifying resulting pseudo-SQL including auto-generated alias. */
  @Test
  public void testSelectAllWithAutoGeneratedAlias() {

    // given
    Person p = Person.of();
    Person p2 = Person.of();
    // when
    SelectStatement<Person> query = DbStatement.select(p).and(p2).where(p.Id().eq(p2.Id())).get();
    // then
    check(query, "SELECT p FROM Person p, Person pe WHERE p.Id = pe.Id", true);
  }

  /** Test creation of {@link SelectStatement} and verifying resulting pseudo-SQL. */
  @Test
  public void testSelectComplex() {

    // given
    Person p = Person.of();
    Song s = Song.of();
    Result r = Result.of();
    // when
    CriteriaAggregation<Integer> songCount = s.Id().count();
    SelectStatement<Result> query = DbStatement.selectProjection(r).and(s.Genre(), r.Genre()).and(songCount, r.Count())
        .and(s.Duration().avg(), r.Duration()) //
        .from(s).as("song").and(p, "p") //
        .where(s.Composer().eq(p.Id()).and(s.Duration().le(3 * 60 * 60L))) //
        .groupBy(s.Genre()).having(songCount.gt(1)).orderBy(s.Genre().asc()).get();
    // then
    check(query,
        "SELECT new Result(song.Genre, COUNT(song.Id) Count, AVG(song.Duration) Duration) FROM Song song, Person p WHERE song.Composer = p.Id AND song.Duration <= 10800 GROUP BY song.Genre HAVING COUNT(song.Id) > 1 ORDER BY song.Genre ASC",
        true);
  }

  /** Test creation of {@link SelectStatement} and verifying resulting pseudo-SQL. */
  @Test
  public void testSelectEmbeddedProperties() {

    // given
    City c = City.of();
    // when
    SelectStatement<City> query = DbStatement.select(c).as("c")
        .where(c.GeoLocation().get().Latitude().eq(40.6892534).and(c.GeoLocation().get().Longitude().eq(-74.0466891)))
        .get();
    // then
    check(query,
        "SELECT c FROM City c WHERE c.GeoLocation.Latitude = 40.6892534 AND c.GeoLocation.Longitude = -74.0466891",
        true);
  }

  /** Test creation of {@link SelectStatement} and verifying resulting pseudo-SQL. */
  @Test
  public void testSelectSequenceNextValue() {

    // given
    DbName sequenceName = DbName.of("MY_SEQUENCE");
    // when
    SelectStatement<Long> query = DbStatement.selectSeqNextVal(sequenceName);
    // then
    check(query, "SELECT NEXTVAL(MY_SEQUENCE)", true);
  }

}
