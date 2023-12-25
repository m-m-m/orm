/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.orm.impl;

import io.github.mmm.bean.WritableBean;
import io.github.mmm.entity.bean.typemapping.TypeMapping;
import io.github.mmm.entity.id.Id;
import io.github.mmm.entity.id.IdMapper;
import io.github.mmm.orm.mapping.DbBeanMapper;
import io.github.mmm.orm.mapping.DbBeanMapperImpl;
import io.github.mmm.orm.mapping.DbPropertyMapper;
import io.github.mmm.orm.mapping.DbPropertyMapperImpl;
import io.github.mmm.orm.mapping.DbSegmentMapper;
import io.github.mmm.orm.mapping.Orm;
import io.github.mmm.orm.naming.DbNamingStrategy;
import io.github.mmm.orm.result.DbResultEntryObjectWithDeclaration;
import io.github.mmm.property.ReadableProperty;
import io.github.mmm.property.WritableProperty;
import io.github.mmm.property.criteria.ProjectionProperty;
import io.github.mmm.value.CriteriaObject;
import io.github.mmm.value.ReadablePath;
import io.github.mmm.value.converter.TypeMapper;

/**
 * Implementation of {@link Orm}.
 *
 * @since 1.0.0
 */
public class OrmImpl implements Orm {

  private final TypeMapping typeMapping;

  private final DbNamingStrategy namingStrategy;

  /**
   * The constructor.
   *
   * @param typeMapping the {@link TypeMapping}.
   * @param namingStrategy the {@link DbNamingStrategy}.
   */
  public OrmImpl(TypeMapping typeMapping, DbNamingStrategy namingStrategy) {

    super();
    this.typeMapping = typeMapping;
    this.namingStrategy = namingStrategy;
  }

  /**
   * @return the {@link TypeMapping}.
   */
  public TypeMapping getTypeMapping() {

    return this.typeMapping;
  }

  @Override
  public DbNamingStrategy getNamingStrategy() {

    return this.namingStrategy;
  }

  @Override
  @SuppressWarnings({ "rawtypes", "unchecked" })
  public <B extends WritableBean> DbBeanMapper<B> createBeanMapping(B bean,
      Iterable<? extends ReadableProperty<?>> properties) {

    DbBeanMapperImpl<B> beanMapper = new DbBeanMapperImpl<>(bean);
    for (ReadableProperty<?> property : properties) {
      if (!property.isTransient()) {
        DbSegmentMapper valueMapper = createSegmentMapper(property);
        DbPropertyMapper propertyMapper = new DbPropertyMapperImpl<>(property.getName(), valueMapper);
        beanMapper.add(propertyMapper);
      }
    }
    return beanMapper;
  }

  @SuppressWarnings({ "rawtypes", "unchecked" })
  @Override
  public <B extends WritableBean> DbBeanMapper<B> createBeanMappingProjection(B bean,
      Iterable<? extends ProjectionProperty<?>> properties) {

    DbBeanMapperImpl<B> beanMapper = new DbBeanMapperImpl<>(bean);
    for (ProjectionProperty<?> projectionProperty : properties) {
      WritableProperty property = (WritableProperty) projectionProperty.getProperty();
      String columnName = property.getName();
      DbSegmentMapper valueMapper = createSegmentMapper(projectionProperty.getSelection(), columnName,
          property.getValueClass(), property);
      ReadablePath parent = property.parentPath();
      if (parent != null) {
        parent = parent.parentPath();
        if (parent instanceof WritableProperty) {

        }
      }
      DbPropertyMapper propertyMapper = new DbPropertyMapperImpl<>(property.getName(), valueMapper);
      beanMapper.add(propertyMapper);
    }
    return beanMapper;
  }

  private <V> DbSegmentMapper<V, ?> createSegmentMapper(ReadableProperty<V> property) {

    String columnName = property.getName();
    return createSegmentMapper(property, columnName, property.getValueClass(), property);
  }

  @SuppressWarnings({ "rawtypes", "unchecked" })
  private <V> DbSegmentMapper<V, ?> createSegmentMapper(CriteriaObject<?> selection, String columnName,
      Class<V> valueClass, ReadableProperty<V> property) {

    TypeMapper<V, ?> typeMapper = this.typeMapping.getTypeMapper(valueClass);
    if ((typeMapper == null) && (property != null)) {
      typeMapper = property.getTypeMapper();
    }
    if ((typeMapper == null) && (Id.class.equals(valueClass))) {
      typeMapper = (TypeMapper) IdMapper.of();
    }
    if (typeMapper == null) {
      throw new IllegalStateException("No type mapping could be found for type " + valueClass + " and selection "
          + selection + "[" + selection.getClass().getSimpleName() + "]");
    }
    return createSegmentMapper(selection, columnName, typeMapper);
  }

  @SuppressWarnings({ "rawtypes", "unchecked" })
  private <V> DbSegmentMapper<V, ?> createSegmentMapper(CriteriaObject<?> selection, String columnName,
      TypeMapper<V, ?> typeMapper) {

    DbSegmentMapper<V, ?> nextSegment = null;
    TypeMapper<V, ?> next = typeMapper.next();
    if (next != null) {
      nextSegment = createSegmentMapper(selection, columnName, next);
    }
    String newColumnName = typeMapper.mapName(columnName);
    if (typeMapper.hasDeclaration()) {
      newColumnName = this.namingStrategy.getColumnName(newColumnName);
      DbResultEntryObjectWithDeclaration entry = new DbResultEntryObjectWithDeclaration<>(selection, null,
          newColumnName, typeMapper.getDeclaration());
      return new DbSegmentMapper<>(typeMapper, entry, nextSegment);
    } else {
      DbSegmentMapper child = createSegmentMapper(selection, newColumnName, typeMapper.getTargetType(), null);
      return new DbSegmentMapper<>(typeMapper, child, nextSegment);
    }
  }

  /**
   * @param newNamingStrategy the new {@link DbNamingStrategy} to use.
   * @return a {@link OrmImpl} with the given {@link DbNamingStrategy}.
   */
  public OrmImpl withNamingStrategy(DbNamingStrategy newNamingStrategy) {

    if (this.namingStrategy == newNamingStrategy) {
      return this;
    }
    return new OrmImpl(this.typeMapping, newNamingStrategy);
  }

}
