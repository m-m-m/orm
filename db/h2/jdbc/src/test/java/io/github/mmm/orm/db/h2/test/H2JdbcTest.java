package io.github.mmm.orm.db.h2.test;

import java.sql.SQLException;
import java.time.LocalDate;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import io.github.mmm.entity.id.Id;
import io.github.mmm.orm.jdbc.access.JdbcAccess;
import io.github.mmm.orm.statement.create.CreateSequenceClause;
import io.github.mmm.orm.statement.create.CreateSequenceStatement;
import io.github.mmm.orm.tx.DbTransaction;
import io.github.mmm.orm.tx.DbTransactionExecutor;

/**
 * Test of H2 database support.
 */
public class H2JdbcTest extends Assertions {

  /** Test that H2 integration works. */
  @Test
  public void test() {

    try {
      DbTransactionExecutor executor = DbTransactionExecutor.get();
      DbTransaction tx = executor.doInTx(() -> doTransactionalTask(executor));
      assertThat(tx.isOpen()).isFalse();
    } catch (Throwable t) {
      // JUnit is buggy and eats up exceptions
      t.printStackTrace();
      throw t;
    }
  }

  private DbTransaction doTransactionalTask(DbTransactionExecutor executor) throws SQLException {

    DbTransaction tx = executor.getTransaction();
    assertThat(tx.isOpen()).isTrue();
    JdbcAccess access = new JdbcAccess();
    Person person = Person.of();
    access.createTable(person);
    PersonRepository repository = new PersonRepository(access);
    CreateSequenceStatement createSequenceStatement = new CreateSequenceClause(repository.getSequenceName())
        .incrementBy(10).startWith(1000000000000L).minValue(1000000000000L).maxValue(9123456789123456789L).nocycle()
        .get();
    access.createSequence(createSequenceStatement);
    person.Name().set("John Doe");
    person.Birthday().set(LocalDate.of(1999, 12, 31));
    repository.save(person);
    person.Name().set("Joe Doe");
    repository.save(person);
    Person person2 = repository.findById(Id.from(person));
    assertThat(person.isEqual(person2)).isTrue();
    assertThat(person.getId().get()).isEqualTo(1000000000000L);
    assertThat(person.getId().getRevision()).isEqualTo(2L);
    return tx;
  }

}
