package io.github.mmm.orm.statement.insert;

import org.junit.jupiter.api.Test;

import io.github.mmm.entity.id.LongId;
import io.github.mmm.orm.statement.DbStatementTest;
import io.github.mmm.orm.statement.Person;

/**
 * Test of {@link Insert} and {@link InsertStatement}.
 */
public class InsertTest extends DbStatementTest {

  /** Test {@link Insert} with simple literal values. */
  @Test
  public void testInsertValues() {

    // given
    Person p = Person.of();
    // when
    InsertStatement<Person> insertStatement = new Insert().into(p).value(p.Name(), "John Doe").value(p.Single(), true)
        .get();
    // then
    check(insertStatement, "INSERT INTO Person(Name, Single) VALUES ('John Doe', TRUE)");
  }

  /** Test {@link Insert} with simple literal values. */
  @Test
  public void testInsertAllValues() {

    // given
    Person p = Person.of();
    p.Name().set("John Doe");
    p.Single().setValue(true);
    p.Id().set(LongId.of(4711L));
    // when
    InsertStatement<Person> insertStatement = new Insert().into(p).valuesAll().get();
    // then
    check(insertStatement, "INSERT INTO Person(Id, Name, Single) VALUES (4711, 'John Doe', TRUE)");
  }

}
