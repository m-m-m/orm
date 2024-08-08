/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.memory.example;

import io.github.mmm.orm.repository.EntityRepository;

/**
 * {@link EntityRepository} for {@link Person}.
 */
public interface PersonRepository extends EntityRepository<Person> {

  Person findByNameOrAlias(String key);

  Iterable<Person> findByAge(int age);

}
