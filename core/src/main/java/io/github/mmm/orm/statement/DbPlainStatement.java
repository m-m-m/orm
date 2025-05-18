package io.github.mmm.orm.statement;

import io.github.mmm.property.criteria.AttributeReadParameters;
import io.github.mmm.property.criteria.CriteriaParameters;

/**
 * Simple container for a plain database statement (SQL).
 */
public class DbPlainStatement implements AttributeReadParameters {

  private final String statement;

  private final CriteriaParameters<?> parameters;

  private final DbPlainStatement next;

  /**
   * The constructor.
   *
   * @param statement the {@link #getStatement() raw statement}.
   * @param parameters the {@link #getParameters() parameters}.
   * @param next the {@link #getNext() next}.
   */
  public DbPlainStatement(String statement, CriteriaParameters<?> parameters, DbPlainStatement next) {

    super();
    this.statement = statement;
    this.parameters = parameters;
    this.next = next;
  }

  /**
   * @return the SQL of this statement.
   */
  public String getStatement() {

    return this.statement;
  }

  @Override
  public CriteriaParameters<?> getParameters() {

    return this.parameters;
  }

  /**
   * @return the next {@link DbPlainStatement} or {@code null} if this is the last one.
   */
  public DbPlainStatement getNext() {

    return this.next;
  }

  @Override
  public String toString() {

    if (this.next == null) {
      return this.statement;
    }
    StringBuilder sb = new StringBuilder(this.statement.length() + this.next.statement.length() + 2);
    toString(sb);
    return sb.toString();
  }

  private void toString(StringBuilder sb) {

    sb.append(this.statement);
    if (this.next != null) {
      sb.append(";\n");
      this.next.toString(sb);
    }
  }

}
