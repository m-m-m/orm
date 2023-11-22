/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.access;

/**
 * Interface providing generic database access with support for all kind of
 * {@link io.github.mmm.orm.statement.DbStatement statements}.
 */
public interface DbAccess extends DbCreateAccess, DbDeleteAccess, DbInsertAccess, DbMergeAccess, DbSelectAccess,
    DbUpdateAccess, DbUpsertAccess {

}
