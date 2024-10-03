package io.github.mmm.orm.db.h2.test;

import java.sql.SQLException;
import java.time.LocalDate;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import io.github.mmm.entity.id.Id;
import io.github.mmm.entity.link.Link;
import io.github.mmm.orm.tx.DbTransaction;
import io.github.mmm.orm.tx.DbTransactionExecutor;

/**
 * Test of H2 database support.
 */
public class H2JdbcTest extends Assertions {

  /** Test that H2 integration works. */
  @Test
  public void testInsertAndUpdatePerson() {

    try {
      DbTransactionExecutor executor = DbTransactionExecutor.get();
      DbTransaction tx = executor.doInTx(() -> doInTxInsertAndUpdatePerson(executor));
      assertThat(tx.isOpen()).isFalse();
    } catch (Throwable t) {
      // JUnit is buggy and eats up exceptions
      t.printStackTrace();
      throw t;
    }
  }

  private DbTransaction doInTxInsertAndUpdatePerson(DbTransactionExecutor executor) throws SQLException {

    DbTransaction tx = executor.getTransaction();
    assertThat(tx.isOpen()).isTrue();
    Person person = Person.of();
    PersonRepository repository = new PersonRepository();
    repository.createTable();
    repository.createSequence();
    person.Name().set("John Doe");
    person.Birthday().set(LocalDate.of(1999, 12, 31));
    repository.save(person);
    assertThat(person.getId().getPk()).isEqualTo(1000000000000L);
    assertThat(person.getId().getRevision()).isEqualTo(1L);

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

  /** Test that entity with relation can be persisted (inserted). */
  @Test
  public void testInsertEntityWithRelation() {

    try {
      DbTransactionExecutor executor = DbTransactionExecutor.get();
      DbTransaction tx = executor.doInTx(() -> doInTxInsertEntityWithRelation(executor));
      assertThat(tx.isOpen()).isFalse();
    } catch (Throwable t) {
      // JUnit is buggy and eats up exceptions
      t.printStackTrace();
      throw t;
    }
  }

  private DbTransaction doInTxInsertEntityWithRelation(DbTransactionExecutor executor) throws SQLException {

    DbTransaction tx = executor.getTransaction();
    assertThat(tx.isOpen()).isTrue();
    TaskListRepository listRepository = new TaskListRepository();
    listRepository.createTable();
    listRepository.createSequence();
    TaskItemRepository itemRepository = new TaskItemRepository();
    itemRepository.createTable();
    TaskList taskList = TaskList.of();
    taskList.Name().set("Shopping");
    listRepository.save(taskList);
    assertThat(taskList.getId().getPk()).isEqualTo(1000000000000L);
    assertThat(taskList.getId().getRevision()).isEqualTo(1L);

    TaskItem item1 = TaskItem.of();
    item1.Name().set("Milk");
    item1.TaskList().set(Link.of(taskList));
    itemRepository.save(item1);

    TaskItem item2 = TaskItem.of();
    item2.Name().set("Bread");
    item2.TaskList().set(Link.of(taskList));
    itemRepository.save(item2);

    item1 = itemRepository.findById(Id.from(item1));
    item2 = itemRepository.findById(Id.from(item2));

    Iterable<TaskItem> items = itemRepository.findByTaskList(Id.from(taskList));
    assertThat(items).containsExactlyInAnyOrder(item1, item2);
    return tx;
  }

}
