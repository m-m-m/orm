package io.github.mmm.orm.statement.insert;

import org.junit.jupiter.api.Test;

import io.github.mmm.entity.id.PkId;
import io.github.mmm.entity.id.RevisionedIdVersion;
import io.github.mmm.orm.statement.DbStatementTest;
import io.github.mmm.orm.statement.Person;

/**
 * Test of {@link InsertClause} and {@link InsertStatement}.
 */
public class InsertTest extends DbStatementTest {

  /** Test {@link InsertClause} with simple literal values. */
  @Test
  public void testInsertValues() {

    // given
    Person p = Person.of();
    // when
    InsertStatement<Person> insertStatement = new InsertClause().into(p).value(p.Name(), "John Doe")
        .value(p.Single(), true).get();
    // then
    check(insertStatement, "INSERT INTO Person(Name, Single) VALUES ('John Doe', TRUE)");
  }

  /** Test {@link InsertClause} with simple literal values. */
  @Test
  public void testInsertAllValues() {

    // given
    Person p = Person.of();
    p.Name().set("John Doe");
    p.Single().setValue(true);
    p.Id().set(new RevisionedIdVersion<>(PkId.of(Person.class, 4711L), 1L));
    // when
    InsertStatement<Person> insertStatement = new InsertClause().into(p).valuesAll().get();
    // then
    check(insertStatement, "INSERT INTO Person(Id, Rev, Name, Single) VALUES (4711, 1, 'John Doe', TRUE)");
  }

}
