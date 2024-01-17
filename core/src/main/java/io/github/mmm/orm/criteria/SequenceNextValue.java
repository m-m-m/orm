package io.github.mmm.orm.criteria;

import java.util.Objects;

import io.github.mmm.orm.metadata.DbQualifiedName;
import io.github.mmm.value.CriteriaObject;

/**
 * {@link CriteriaObject} representing the next value of a database sequence.
 */
public final class SequenceNextValue implements CriteriaObject<Long> {

  /** The keyword {@value}. */
  public static final String NEXT_VALUE = "NEXTVAL";

  /** Start sequence. */
  public static final String START = NEXT_VALUE + "(";

  private final DbQualifiedName sequenceName;

  /**
   * The constructor.
   *
   * @param sequenceName the {@link DbQualifiedName} of the database sequence.
   */
  public SequenceNextValue(DbQualifiedName sequenceName) {

    super();
    Objects.requireNonNull(sequenceName);
    this.sequenceName = sequenceName;
  }

  /**
   * @return sequenceName
   */
  public DbQualifiedName getSequenceName() {

    return this.sequenceName;
  }

  @Override
  public String toString() {

    return START + this.sequenceName + ")";
  }

}
