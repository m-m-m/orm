/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.statement.delete;

import org.junit.jupiter.api.Test;

import io.github.mmm.orm.statement.DbStatementTest;
import io.github.mmm.orm.statement.Person;

/**
 * Test of {@link DeleteClause} and {@link DeleteStatement}.
 */
public class DeleteTest extends DbStatementTest {

  /** Test of {@link DeleteClause} for entire table. */
  @Test
  public void testDeleteAll() {

    // arrange
    Person p = Person.of();
    // act
    DeleteStatement<Person> deleteStatement = new DeleteClause().from(p).get();
    // assert
    check(deleteStatement, "DELETE FROM Person p", true);
  }

  /** Test of {@link DeleteClause} with where clause. */
  @Test
  public void testDeleteWhere() {

    // arrange
    Person p = Person.of();
    // act
    DeleteStatement<Person> deleteStatement = new DeleteClause().from(p).as("p").where(p.Single().eq(Boolean.TRUE))
        .get();
    // assert
    check(deleteStatement, "DELETE FROM Person p WHERE p.Single = TRUE", true);
  }

}
