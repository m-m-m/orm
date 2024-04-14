package io.github.mmm.orm.db.h2.test;

import io.github.mmm.orm.spi.repository.AbstractDbRepository;

/**
 * {@link AbstractDbRepository Repository} for {@link Person}.
 */
public class PersonRepository extends AbstractDbRepository<Person> {

  /**
   * The constructor.
   */
  public PersonRepository() {

    super(Person.of());
  }

  @Override
  protected String getSequenceName() {

    return "PERSON_SEQ";
  }

}
