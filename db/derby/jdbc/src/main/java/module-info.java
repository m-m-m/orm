/*
 * Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0
 */
/**
 * Provides ORM support for derby database via JDBC.
 */
module io.github.mmm.orm.db.derby.jdbc {

  requires io.github.mmm.orm.db.derby.dialect;

  requires io.github.mmm.orm.jdbc;

  requires org.apache.derby.engine;

}
