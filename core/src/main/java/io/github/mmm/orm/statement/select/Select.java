/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.statement.select;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.github.mmm.bean.WritableBean;
import io.github.mmm.entity.bean.EntityBean;
import io.github.mmm.marshall.StructuredReader;
import io.github.mmm.marshall.StructuredState;
import io.github.mmm.marshall.StructuredWriter;
import io.github.mmm.orm.statement.AbstractDbClause;
import io.github.mmm.orm.statement.DbStatementMarshalling;
import io.github.mmm.orm.statement.StartClause;
import io.github.mmm.property.criteria.CriteriaExpression;
import io.github.mmm.property.criteria.CriteriaMarshalling;
import io.github.mmm.value.CriteriaObject;
import io.github.mmm.value.PropertyPath;

/**
 * {@link StartClause} of a {@link SelectStatement} to query data from the database. As {@link SelectStatement}s are
 * rather complex, this class is abstract to allow property type-safety and guidance of fluent API and code completion
 * via derived sub-classes for different scenarios. For simplest usage you can use the static methods provided by
 * {@link Select} to start your query (typing {@code Select.} and starting code completion). However, you are still free
 * to use regular instantiation like for other statements (typing {@code new Select} and starting code completion).
 *
 * <b>ATTENTION:</b> Please note that after {@link DbStatementMarshalling#readObject(StructuredReader) unmarshalling} or
 * parsing a {@link SelectStatement} the {@link SelectStatement#getSelect() select} clause may not be of any of the
 * expected sub-classes such as {@link SelectEntity}, {@link SelectSingle}, {@link SelectResult}, or
 * {@link SelectProjection}. Use {@code isSelect*} methods like {@link #isSelectEntity()} to determine the actual
 * selection type.
 *
 * @param <R> type of the result of the selection.
 * @since 1.0.0
 */
public abstract class Select<R> extends AbstractDbClause implements StartClause {

  /** Name of {@link Select} for marshaling. */
  public static final String NAME_SELECT = "select";

  /** Name of property {@link #isDistinct()} for marshaling. */
  public static final String NAME_DISTINCT = "distinct";

  /** Name of property {@link #getResultName()} for marshaling. */
  public static final String NAME_RESULT = "result";

  /** Value of property {@link #NAME_RESULT} for {@link SelectEntity}. */
  public static final String VALUE_RESULT_ENTITY = "entity";

  /** Value of property {@link #NAME_RESULT} for {@link SelectSingle}. */
  public static final String VALUE_RESULT_SINLGE = "1";

  /** Value of property {@link #NAME_RESULT} for {@link SelectResult}. */
  public static final String VALUE_RESULT_RESULT = "result";

  /** Name of property {@link #getSelections() selections} for marshaling. */
  public static final String NAME_SELECTIONS = "sel";

  private SelectStatement<R> statement;

  private final List<? extends CriteriaObject<?>> selections;

  private transient R resultBean;

  private String resultName;

  private boolean distinct;

  /**
   * The constructor.
   *
   * @param resultBean the {@link #getResultBean() result bean}.
   */
  protected Select(R resultBean) {

    super();
    this.selections = new ArrayList<>();
    setResultBean(resultBean);
  }

  @Override
  protected String getMarshallingName() {

    return NAME_SELECT;
  }

  /**
   * @return {@code true} for {@code DISTINCT} selection (filter out duplicates), {@code false} otherwise.
   */
  public boolean isDistinct() {

    return this.distinct;
  }

  /**
   * @return the {@link List} of selections. Only use for generic code. To build queries use fluent API methods.
   */
  public List<? extends CriteriaObject<?>> getSelections() {

    return this.selections;
  }

  /**
   * @return the owning {@link SelectStatement} or {@code null} if not initialized (until {@code from} method is
   *         called).
   */
  public SelectStatement<R> getStatement() {

    return this.statement;
  }

  void setStatement(SelectStatement<R> statement) {

    this.statement = statement;
  }

  /**
   * @return the result {@link io.github.mmm.bean.WritableBean bean} for {@link SelectProjection} or
   *         {@link SelectEntity}, otherwise {@code null}.
   */
  public R getResultBean() {

    return this.resultBean;
  }

  /**
   * @param resultBean the new value of {@link #getResultBean()}.
   */
  protected void setResultBean(R resultBean) {

    if ((resultBean instanceof WritableBean) && (this.resultName == null)) {
      this.resultName = ((WritableBean) resultBean).getType().getStableName();
    }
    this.resultBean = resultBean;
  }

  /**
   * @return the optional result name. Will be {@link #VALUE_RESULT_ENTITY} for {@link SelectEntity},
   *         {@link #VALUE_RESULT_SINLGE} for {@link SelectSingle}, and {@link #VALUE_RESULT_RESULT} for
   *         {@link SelectResult}.
   * @see #getResultBean()
   * @see io.github.mmm.orm.statement.AbstractEntityClause#getEntityName()
   */
  public String getResultName() {

    return this.resultName;
  }

  /**
   * @param resultName new value of {@link #getResultName()}.
   */
  protected void setResultName(String resultName) {

    this.resultName = resultName;
  }

  /**
   * @return {@code true} if the {@link SelectFrom#getEntity() primary entity} is selected, {@code false} otherwise.
   * @see SelectEntity
   */
  public boolean isSelectEntity() {

    return VALUE_RESULT_ENTITY.equals(this.resultName);
  }

  /**
   * @return {@code true} if only a {@link SelectSingle single} {@link #getSelections() selection} is selected as raw
   *         value, {@code false} otherwise.
   * @see SelectSingle
   */
  public boolean isSelectSingle() {

    return VALUE_RESULT_SINLGE.equals(this.resultName);
  }

  /**
   * @return {@code true} if only if the {@link SelectFrom#getEntity() main entity} is selected, {@code false}
   *         otherwise.
   * @see SelectEntity
   */
  public boolean isSelectResult() {

    return VALUE_RESULT_SINLGE.equals(this.resultName);
  }

  /**
   * Sets {@link #isDistinct() DISTINCT} selection (filter out duplicates).
   *
   * @return this {@link Select} for fluent API calls.
   */
  public Select<R> distinct() {

    this.distinct = true;
    return this;
  }

  @SuppressWarnings({ "unchecked", "rawtypes" })
  void add(CriteriaObject<?> selection) {

    ((List) this.selections).add(selection);
  }

  /**
   * @param expression the {@link CriteriaExpression} to add to the selection.
   * @return this {@link Select} for fluent API calls.
   */
  protected Select<R> and(CriteriaExpression<?> expression) {

    add(expression);
    return this;
  }

  /**
   * @param expressions the {@link CriteriaExpression}s to add to the selection.
   * @return this {@link Select} for fluent API calls.
   */
  protected Select<R> and(CriteriaExpression<?>... expressions) {

    for (CriteriaExpression<?> aggregation : expressions) {
      add(aggregation);
    }
    return this;
  }

  /**
   * @param property the {@link PropertyPath property} to add to the selection.
   * @return this {@link Select} for fluent API calls.
   */
  protected Select<R> and(PropertyPath<?> property) {

    add(property);
    return this;
  }

  /**
   * @param properties the {@link PropertyPath properties} to add to the selection.
   * @return this {@link Select} for fluent API calls.
   */
  protected Select<R> and(PropertyPath<?>... properties) {

    for (PropertyPath<?> property : properties) {
      add(property);
    }
    return this;
  }

  /**
   * @param <E> type of the {@link EntityBean}.
   * @param entity the {@link EntityBean entity} to select from.
   * @return the {@link SelectFrom} for fluent API calls.
   */
  protected <E extends EntityBean> SelectFrom<R, E> from(E entity) {

    return new SelectFrom<>(this, entity);
  }

  @Override
  protected void writeProperties(StructuredWriter writer) {

    if (!Objects.equals(VALUE_RESULT_ENTITY, this.resultName)) {
      writer.writeName(NAME_RESULT);
      if (isSelectEntity()) {
        writer.writeValueAsString(VALUE_RESULT_ENTITY);
      } else if (isSelectSingle()) {
        writer.writeValueAsString(VALUE_RESULT_SINLGE);
      } else if (isSelectResult()) {
        writer.writeValueAsString(VALUE_RESULT_RESULT);
      } else {
        writer.writeValueAsString(this.resultName);
      }
    }
    if (isDistinct()) {
      writer.writeName(NAME_DISTINCT);
      writer.writeValueAsBoolean(Boolean.TRUE);
    }
    if (!this.selections.isEmpty()) {
      writer.writeName(NAME_SELECTIONS);
      writer.writeStartArray();
      CriteriaMarshalling marshalling = CriteriaMarshalling.get();
      for (CriteriaObject<?> selection : this.selections) {
        marshalling.writeArg(writer, selection);
      }
      writer.writeEnd();
    }
    super.writeProperties(writer);
  }

  @Override
  protected void readProperty(StructuredReader reader, String name) {

    if (reader.isNameMatching(name, NAME_DISTINCT)) {
      this.distinct = Boolean.TRUE.equals(reader.readValueAsBoolean());
    } else if (reader.isNameMatching(name, NAME_RESULT)) {
      this.resultName = reader.readValueAsString();
    } else if (reader.isNameMatching(name, NAME_SELECTIONS)) {
      reader.require(StructuredState.START_ARRAY, true);
      CriteriaMarshalling marshalling = CriteriaMarshalling.get();
      while (!reader.readEnd()) {
        CriteriaObject<?> selection = marshalling.readArg(reader);
        add(selection);
      }
    } else {
      super.readProperty(reader, name);
    }
  }

  /**
   * Alternative for {@code new SelectSingle(selection)}.
   *
   * @param <R> type of the result of the selection.
   * @param selection the single {@link CriteriaObject} to select.
   * @return the new {@link SelectSingle} clause.
   */
  public static <R> SelectSingle<R> single(CriteriaObject<R> selection) {

    return new SelectSingle<>(selection);
  }

  /**
   * Alternative for {@code new SelectEntity(entity)}.
   *
   * @param <R> type of the {@link EntityBean} to select.
   * @param entity the {@link EntityBean} to select.
   * @return the new {@link SelectEntity} clause.
   */
  public static <R extends EntityBean> SelectEntity<R> entity(R entity) {

    return new SelectEntity<>(entity);
  }

  /**
   * Alternative for {@code new SelectProjection(bean)}.
   *
   * @param <R> type of the {@link WritableBean} to select.
   * @param bean the {@link WritableBean} to select.
   * @return the new {@link SelectProjection} clause.
   */
  public static <R extends WritableBean> SelectProjection<R> projection(R bean) {

    return new SelectProjection<>(bean);
  }

  /**
   * Alternative for {@code new SelectResult()}.
   *
   * @return the new {@link SelectResult} clause.
   */
  public static SelectResult result() {

    return new SelectResult();
  }

}