/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.dialect;

import java.io.IOException;

import io.github.mmm.base.exception.RuntimeIoException;
import io.github.mmm.orm.naming.DbNamingStrategy;
import io.github.mmm.orm.statement.DbStatementFormatter;

/**
 * Interface for an database dialect. It abstracts from the concrete syntax (e.g. specific SQL) of a database.
 */
public interface DbDialect {

  /**
   * @return the name of the {@link DbDialect} (e.g. "h2", "postgresql", etc.). Should be entirely lower-case to prevent
   *         case mismatching.
   */
  String getName();

  /**
   * @return the {@link DbNamingStrategy}.
   */
  DbNamingStrategy getNamingStrategy();

  /**
   * @param name the name to quote.
   * @param appendable the {@link Appendable} where to {@link Appendable#append(CharSequence) append} the quoted name
   *        to.
   */
  default void quoteIdentifier(String name, Appendable appendable) {

    try {
      appendable.append('"');
      appendable.append(name);
      appendable.append('"');
    } catch (IOException e) {
      throw new RuntimeIoException(e);
    }
  }

  /**
   * @return a new {@link DbStatementFormatter} using this SQL dialect.
   */
  DbStatementFormatter createFormatter();

}
