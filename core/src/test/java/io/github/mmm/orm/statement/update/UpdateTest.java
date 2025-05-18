/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.statement.update;

import org.junit.jupiter.api.Test;

import io.github.mmm.entity.id.PkId;
import io.github.mmm.orm.statement.DbStatementTest;
import io.github.mmm.orm.statement.Person;
import io.github.mmm.orm.statement.Song;

/**
 * Test of {@link UpdateClause} and {@link UpdateStatement}.
 */
public class UpdateTest extends DbStatementTest {

  /** Test of {@link UpdateClause} for entire table. */
  @Test
  public void testUpdateAll() {

    // given
    Person p = Person.of();
    // when
    UpdateStatement<Person> updateStatement = new UpdateClause<>(p).as("p").set(p.Single(), Boolean.TRUE).get();
    // then
    check(updateStatement, "UPDATE Person p SET p.Single = TRUE", true);
  }

  /** Test of {@link UpdateClause} for with {@link UpdateWhere} clause. */
  @Test
  public void testUpdateWhere() {

    // given
    Person p = Person.of();
    // when
    UpdateStatement<Person> updateStatement = new UpdateClause<>(p).as("p").set(p.Single(), Boolean.FALSE)
        .where(p.Id().eq(PkId.of(Person.class, 4711L))).get();
    // then
    check(updateStatement, "UPDATE Person p SET p.Single = FALSE WHERE p.Id = 4711", true);
  }

  /** Test of {@link UpdateClause} for with data from other entity. */
  @Test
  public void testUpdateFromJoinTable() {

    // given
    Song s = Song.of();
    Person p = Person.of();
    // when
    UpdateStatement<Song> updateStatement = new UpdateClause<>(s).as("s").and(p).as("p").set(s.Title(), p.Name())
        .where(s.Composer().eq(p.Id())).get();
    // then
    check(updateStatement, "UPDATE Song s, Person p SET s.Title = p.Name WHERE s.Composer = p.Id", true);
  }

}
