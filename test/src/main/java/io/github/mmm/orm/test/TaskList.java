/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.test;

import io.github.mmm.bean.BeanFactory;
import io.github.mmm.entity.bean.EntityBean;
import io.github.mmm.entity.link.Link;
import io.github.mmm.property.container.list.ListProperty;
import io.github.mmm.property.string.StringProperty;

/**
 * {@link EntityBean} for a list of {@link TaskItem}s used for testing.
 */
public interface TaskList extends EntityBean {

  /**
   * @return full name of the person including first and last name.
   */
  StringProperty Name();

  /**
   * @return the list of {@link TaskItem}s owned by this task-list.
   */
  ListProperty<Link<TaskItem>> Items();

  /**
   * @return a new instance of {@link TaskList}.
   */
  static TaskList of() {

    return BeanFactory.get().create(TaskList.class);
  }

}
