/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.statement.alter;

import org.junit.jupiter.api.Test;

import io.github.mmm.orm.statement.DbStatementTest;
import io.github.mmm.orm.statement.Song;

/**
 * Test of {@link AlterTableClause} and {@link AlterTableStatement}.
 */
public class AlterTableTest extends DbStatementTest {

  /** Test of {@link AlterTableClause} that adds a single column. */
  @Test
  public void testAddColumn() {

    // given
    Song s = Song.of();
    // when
    AlterTableStatement<Song> alterTableStatement = new AlterTableClause<>(s).addColumn(s.Composer()).get();
    // then
    check(alterTableStatement, "ALTER TABLE Song\n" //
        + "ADD Composer Link");
  }

  /** Test of {@link AlterTableClause} that adds a single column with auto constraint. */
  @Test
  public void testAddColumnAutoConstraint() {

    // given
    Song s = Song.of();
    // when
    AlterTableStatement<Song> alterTableStatement = new AlterTableClause<>(s).addColumn(s.Composer(), true).get();
    // then
    check(alterTableStatement, "ALTER TABLE Song\n" //
        + "ADD Composer Link,\n" //
        + "ADD CONSTRAINT FK_Song_Composer FOREIGN KEY (Composer) REFERENCES Person(Id)");
  }

}
