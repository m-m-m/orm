/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.memory.example;

import java.util.Collection;
import java.util.List;

import io.github.mmm.orm.memory.SequenceMemoryRepository;
import io.github.mmm.orm.memory.index.MemoryIndex;
import io.github.mmm.orm.memory.index.MemoryNonUniqueIndex;
import io.github.mmm.orm.memory.index.MemoryUniqueIndex;

/**
 * Implementation of {@link PersonRepository} using {@link SequenceMemoryRepository}.
 */
public class PersonMemoryRepository extends SequenceMemoryRepository<Person> implements PersonRepository {

  private final MemoryUniqueIndex<String, Person> nameOrAliasIndex;

  private final MemoryNonUniqueIndex<Integer, Person> ageIndex;

  private final List<MemoryIndex<?, ? super Person>> indices;

  /**
   * The constructor.
   */
  public PersonMemoryRepository() {

    super(Person.of());
    Person person = getPrototype();
    this.nameOrAliasIndex = new MemoryUniqueIndex<>(this, StringNormalizer.get(), person.Name().getName(),
        person.Aliases().getName());
    this.ageIndex = new MemoryNonUniqueIndex<>(this, person.Age().getName());
    this.indices = List.of(this.nameOrAliasIndex, this.ageIndex);
  }

  @Override
  protected Collection<MemoryIndex<?, ? super Person>> getIndices() {

    return this.indices;
  }

  @Override
  public Iterable<Person> findByAge(int age) {

    return this.ageIndex.find(Integer.valueOf(age));
  }

  @Override
  public Person findByNameOrAlias(String key) {

    return this.nameOrAliasIndex.find(key);
  }

}
