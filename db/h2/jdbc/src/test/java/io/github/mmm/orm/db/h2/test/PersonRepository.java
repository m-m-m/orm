package io.github.mmm.orm.db.h2.test;

import io.github.mmm.entity.id.LongIdGenerator;
import io.github.mmm.entity.id.LongIdMemorySequence;
import io.github.mmm.orm.spi.access.DbAccess;
import io.github.mmm.orm.spi.repository.AbstractDbRepository;

/**
 * TODO hohwille This type ...
 *
 */
public class PersonRepository extends AbstractDbRepository<Person> {

  /**
   * The constructor.
   *
   * @param dbAccess
   */
  public PersonRepository(DbAccess dbAccess) {

    super(Person.of(), new LongIdGenerator(new LongIdMemorySequence()), dbAccess);
  }

}
