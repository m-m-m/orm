/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.statement;

import io.github.mmm.orm.statement.select.SelectClause;

/**
 * A {@link StartClause} is the entry {@link DbClause} that defines the characteristic of the operation in the database
 * such as {@link SelectClause}, Delete.
 *
 * @since 1.0.0
 */
public interface StartClause extends DbClause {

}
