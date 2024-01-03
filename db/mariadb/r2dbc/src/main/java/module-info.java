
/*
 * Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0
 */

/**
 * Provides ORM support for MariaDB database via R2DBC.
 */
module io.github.mmm.orm.db.mariadb.r2dbc {

  requires transitive io.github.mmm.orm.r2dbc;

  requires r2dbc.mariadb;

}
