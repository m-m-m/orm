package io.github.mmm.orm.statement;

import io.github.mmm.base.exception.ApplicationException;

/**
 * {@link ApplicationException} thrown if a single result was expected but multiple results have been received.
 *
 * @since 1.0.0
 */
public final class NonUniqueResultException extends ApplicationException {

  /**
   * The constructor.
   *
   * @param count the actual number of results received.
   */
  public NonUniqueResultException(int count) {

    this(count, "query");
  }

  /**
   * The constructor.
   *
   * @param count the actual number of results received.
   * @param query the query that produced multiple results or the description of it.
   */
  public NonUniqueResultException(int count, String query) {

    super("Unique result was expected but received " + count + " results for " + query + ".");
    assert (count > 1);
  }

  @Override
  public String getCode() {

    return "DB-NUQ";
  }

}
