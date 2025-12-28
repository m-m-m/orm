/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.statement.alter;

import org.junit.jupiter.api.Test;

import io.github.mmm.orm.ddl.DbColumnSpec;
import io.github.mmm.orm.ddl.constraint.DbConstraintDeferrable;
import io.github.mmm.orm.ddl.constraint.DbConstraintInitially;
import io.github.mmm.orm.ddl.constraint.DbConstraintRely;
import io.github.mmm.orm.ddl.constraint.DbConstraintState;
import io.github.mmm.orm.ddl.constraint.ForeignKeyConstraint;
import io.github.mmm.orm.statement.DbStatementTest;
import io.github.mmm.orm.statement.Person;
import io.github.mmm.orm.statement.Song;

/**
 * Test of {@link AlterTableClause} and {@link AlterTableStatement}.
 */
class AlterTableTest extends DbStatementTest {

  /** Test of {@link AlterTableClause} that adds a single column. */
  @Test
  void testAddColumn() {

    // arrange
    Song s = Song.of();
    // act
    AlterTableStatement<Song> alterTableStatement = new AlterTableClause<>(s).addColumn(s.Composer()).get();
    // assert
    check(alterTableStatement, """
        ALTER TABLE Song
        ADD Composer Link""", false);
  }

  /** Test of {@link AlterTableClause} that adds a single column with auto constraint. */
  @Test
  void testAddColumnAutoConstraint() {

    // arrange
    Song s = Song.of();
    // act
    AlterTableStatement<Song> alterTableStatement = new AlterTableClause<>(s).addColumn(s.Composer(), true).get();
    // assert
    check(alterTableStatement, """
        ALTER TABLE Song
        ADD Composer Link,
        ADD CONSTRAINT FK_Song_Composer FOREIGN KEY (Composer) REFERENCES Person(Id)""", false);
  }

  /** Test of {@link AlterTableClause} that adds a single column with a custom constraint. */
  @Test
  void testAddColumnCustomConstraint() {

    // arrange
    Song s = Song.of();
    Person p = Person.of();
    DbColumnSpec column = new DbColumnSpec(s.Composer());
    DbColumnSpec referenceColumn = new DbColumnSpec(p.Id());
    DbConstraintState state = DbConstraintState.of(DbConstraintDeferrable.DEFERRABLE,
        DbConstraintInitially.INITIALLY_IMMEDIATE, DbConstraintRely.RELY);
    // act
    AlterTableStatement<Song> alterTableStatement = new AlterTableClause<>(s).addColumn(s.Composer())
        .addConstraint(new ForeignKeyConstraint("MY_FK_CONSTRAINT", column, referenceColumn, state)).get();
    // assert
    check(alterTableStatement,
        """
            ALTER TABLE Song
            ADD Composer Link,
            ADD CONSTRAINT MY_FK_CONSTRAINT FOREIGN KEY (Composer) REFERENCES Person(Id) INITIALLY IMMEDIATE DEFERRABLE RELY""",
        false);
  }

}
