package io.github.mmm.orm.memory;

import java.nio.file.Path;
import java.util.Locale;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import io.github.mmm.entity.id.PkId;
import io.github.mmm.orm.memory.example.Person;
import io.github.mmm.orm.memory.example.PersonMemoryRepository;

/**
 * Test of {@link SequenceMemoryRepository} with all its features using {@link PersonMemoryRepository} as example
 * implementation.
 */
class SequenceMemoryRepositoryTest extends Assertions {

  private PersonMemoryRepository repo;

  @Test
  void testAll() {

    this.repo = new PersonMemoryRepository();
    this.repo.read(Path.of("src/test/resources/persons.json"));
    Person peter = verifyPeterPan(null);
    Person tinker = verifyTinkerBell(null);
    verifyJohnDoe(null);
    verifyAliceBob(null);
    verifyMickeyMouse(null);
    Iterable<Person> foreverYoung = this.repo.findByAge(16);
    assertThat(foreverYoung).containsExactlyInAnyOrder(peter, tinker);
  }

  private Person verifyPeterPan(Person peter) {

    return verify(peter, 1L, "Peter Pan", 16, "PePa");
  }

  private Person verifyTinkerBell(Person tinker) {

    return verify(tinker, 2L, "Tinker Bell", 16, "Tinky");
  }

  private Person verifyJohnDoe(Person john) {

    return verify(john, 3L, "John Doe", 66, "JD", "Jonnyboy");
  }

  private Person verifyAliceBob(Person alice) {

    return verify(alice, 4L, "Alice Bob", 30, "Alice");
  }

  private Person verifyMickeyMouse(Person mickey) {

    return verify(mickey, 5L, "Mickey Mouse", 100, "Mickey");
  }

  private Person verify(Person person, long pk, String name, int age, String... aliases) {

    if (person == null) {
      person = this.repo.findById(PkId.of(Person.class, pk));
    }
    assertThat(person).as(name).isNotNull();
    assertThat(person.getId().getPk()).isEqualTo(pk);
    assertThat(person.Name().get()).isEqualTo(name);
    assertThat(person.Age().get()).isEqualTo(age);
    assertThat(person.Aliases().getAsSet()).containsExactlyInAnyOrder(aliases);
    verifyFindByNameOrAlias(person, name);
    for (String alias : aliases) {
      verifyFindByNameOrAlias(person, alias);
    }
    Iterable<Person> byAge = this.repo.findByAge(age);
    assertThat(byAge).contains(person);
    return person;
  }

  private void verifyFindByNameOrAlias(Person person, String name) {

    assertThat(this.repo.findByNameOrAlias(name)).isSameAs(person);
    assertThat(this.repo.findByNameOrAlias(name.toUpperCase(Locale.ROOT))).isSameAs(person);
    assertThat(this.repo.findByNameOrAlias(name.toLowerCase(Locale.ROOT))).isSameAs(person);
  }

}
