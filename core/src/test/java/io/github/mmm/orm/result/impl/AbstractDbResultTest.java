package io.github.mmm.orm.result.impl;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Iterator;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import io.github.mmm.base.exception.ObjectNotFoundException;
import io.github.mmm.orm.result.DbResult;
import io.github.mmm.orm.result.DbResultValue;

/**
 * Abstract test for {@link DbResult}.
 */
public abstract class AbstractDbResultTest extends Assertions {

  /** Dummy cell 0. */
  protected static final DbResultValue<String> CELL0 = new DbResultValueObject<>("foo", "foo1", "VARCHAR");

  /** Dummy cell 1. */
  protected static final DbResultValue<Integer> CELL1 = new DbResultValueObject<>("bar", Integer.valueOf(42), "NUMBER");

  /** Dummy cell 2. */
  protected static final DbResultValue<Boolean> CELL2 = new DbResultValueObject<>("some", Boolean.TRUE, "BOOLEAN");

  /**
   * Test the basic functionality of {@link DbResult}.
   */
  @Test
  void testBasics() {

    // arrange
    // act
    DbResult result = create(CELL0, CELL1, CELL2);

    // assert
    if (result instanceof AbstractDbResult adr) {
      assertThat(adr.get(0)).isSameAs(CELL0);
      assertThat(adr.get(1)).isSameAs(CELL1);
      assertThat(adr.get(2)).isSameAs(CELL2);
    }
    verifyIterator(result, CELL0, CELL1, CELL2);
    assertThat(result.indexOf("foo")).isEqualTo(0);
    assertThat(result.indexOf("bar")).isEqualTo(1);
    assertThat(result.indexOf("some")).isEqualTo(2);
    assertThat(result.indexOf("Foo")).isEqualTo(0);
    assertThat(result.indexOf("foO")).isEqualTo(0);
    assertThat(result.indexOf("FOO")).isEqualTo(0);
    assertThat(result.indexOfRequired("FOO")).isEqualTo(0);
    ObjectNotFoundException error = assertThrows(ObjectNotFoundException.class, () -> {
      result.indexOfRequired("id");
    });
    assertThat(error.getLocalizedMessage()).isEqualTo("Could not find DB-column for key 'id' in foo, bar, some");
  }

  /**
   * @param cells the {@link DbResultValue}s.
   * @return the {@link DbResult} for the given {@code cells}.
   */
  protected abstract DbResult create(DbResultValue<?>... cells);

  /**
   * Verifies that {@link DbResult#iterator()} iterates exactly the given {@link DbResultValue}s in order.
   *
   * @param result the {@link DbResult}.
   * @param cells the expected {@link DbResultValue}s in order.
   */
  protected void verifyIterator(DbResult result, DbResultValue<?>... cells) {

    Iterator<DbResultValue<?>> iterator = result.iterator();
    for (DbResultValue<?> cell : cells) {
      assertThat(iterator.hasNext()).isTrue();
      DbResultValue<?> actual = iterator.next();
      assertThat(actual.getName()).isEqualTo(cell.getName());
      assertThat(actual.getValue()).isEqualTo(cell.getValue());
      assertThat(actual.getDeclaration()).isEqualTo(cell.getDeclaration());
    }
    assertThat(iterator.hasNext()).isFalse();
  }

}
