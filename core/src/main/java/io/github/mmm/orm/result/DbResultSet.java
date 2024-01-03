/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.result;

import java.io.Closeable;

/**
 * A {@link DbResultSet} represents the result of a {@link io.github.mmm.orm.statement.DbStatement}. It allows to
 * {@link #iterator() iterate} the {@link DbResultRow}s received from the database. It is an abstraction of a JDBC
 * {@link java.sql.ResultSet}.<br>
 * <b>ATTENTION:</b> A {@link DbResultSet} should be iterated only a single time. When calling {@link #iterator()}
 * method again the result is unspecified and it may even lead to errors.
 *
 * @since 1.0.0
 */
public interface DbResultSet extends Iterable<DbResultRow>, Closeable {

  @Override
  void close();

}
