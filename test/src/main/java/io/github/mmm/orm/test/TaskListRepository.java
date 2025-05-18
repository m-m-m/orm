package io.github.mmm.orm.test;

import io.github.mmm.orm.spi.repository.AbstractDbRepository;

/**
 * {@link AbstractDbRepository Repository} for {@link TaskList}.
 */
public class TaskListRepository extends AbstractDbRepository<TaskList> {

  /**
   * The constructor.
   */
  public TaskListRepository() {

    super(TaskList.of());
  }

}
