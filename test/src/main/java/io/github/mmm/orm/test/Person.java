/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.test;

import io.github.mmm.bean.BeanFactory;
import io.github.mmm.entity.bean.EntityBean;
import io.github.mmm.property.string.StringProperty;
import io.github.mmm.property.time.localdate.LocalDateProperty;

/**
 * {@link EntityBean} for a human person used for testing.
 */
public interface Person extends EntityBean {

  /**
   * @return full name of the person including first and last name.
   */
  StringProperty Name();

  /**
   * @return date of birth.
   */
  LocalDateProperty Birthday();

  /**
   * @return a new instance of {@link Person}.
   */
  static Person of() {

    return BeanFactory.get().create(Person.class);
  }

}
