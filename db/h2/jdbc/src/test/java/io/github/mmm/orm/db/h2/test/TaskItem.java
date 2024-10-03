/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.db.h2.test;

import io.github.mmm.bean.BeanFactory;
import io.github.mmm.entity.bean.EntityBean;
import io.github.mmm.entity.property.link.LinkProperty;
import io.github.mmm.property.booleans.BooleanProperty;
import io.github.mmm.property.string.StringProperty;

/**
 * {@link EntityBean} for an item of a {@link TaskList}.
 */
public interface TaskItem extends EntityBean {

  /**
   * @return name (title) of the item.
   */
  StringProperty Name();

  /**
   * @return {@code true} if completed, {@code false} otherwise (task is an open todo).
   */
  BooleanProperty Completed();

  /**
   * @return the {@link TaskList} owning this item.
   */
  LinkProperty<TaskList> TaskList();

  /**
   * @return a new instance of {@link TaskItem}.
   */
  static TaskItem of() {

    return BeanFactory.get().create(TaskItem.class);
  }

}
