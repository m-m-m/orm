package io.github.mmm.orm.db.h2.test;

import java.sql.SQLException;
import java.time.LocalDate;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import io.github.mmm.entity.id.Id;
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
    Person person = Person.of();
    PersonRepository repository = new PersonRepository();
    repository.createTable();
    repository.createSequence();
    person.Name().set("John Doe");
    person.Birthday().set(LocalDate.of(1999, 12, 31));
    repository.save(person);
    person.Name().set("Joe Doe");
    // to force OptimisticLockException...
    // LongVersionId<Person> id = (LongVersionId<Person>) Id.from(person);
    // person.Id().set(id.withRevision(0L));
    repository.save(person);
    Person person2 = repository.findById(Id.from(person));
    assertThat(person.isEqual(person2)).isTrue();
    assertThat(person.getId().getPk()).isEqualTo(1000000000000L);
    assertThat(person.getId().getRevision()).isEqualTo(2L);
    return tx;
  }

}
