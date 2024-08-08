/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.memory.example;

import io.github.mmm.bean.BeanFactory;
import io.github.mmm.entity.bean.EntityBean;
import io.github.mmm.property.number.integers.IntegerProperty;
import io.github.mmm.property.string.StringProperty;
import io.github.mmm.property.string.StringSetProperty;

/**
 * Example {@link EntityBean} for testing.
 */
public interface Person extends EntityBean {

  StringProperty Name();

  StringSetProperty Aliases();

  IntegerProperty Age();

  /**
   * @return a new instance of {@link Person}.
   */
  public static Person of() {

    return BeanFactory.get().create(Person.class);
  }

}
