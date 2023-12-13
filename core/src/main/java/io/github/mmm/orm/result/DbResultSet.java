/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.result;

import io.github.mmm.orm.statement.select.SelectStatement;

/**
 * A {@link DbResultSet} list or set of {@link DbResult}s of a {@link SelectStatement} from a database. It is an
 * abstraction of a JDBC {@link java.sql.ResultSet}.
 *
 * @since 1.0.0
 */
public interface DbResultSet extends Iterable<DbResult> {

}
