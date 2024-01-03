package io.github.mmm.orm.statement;

import io.github.mmm.base.exception.ObjectMismatchException;
import io.github.mmm.bean.ReadableBean;
import io.github.mmm.bean.WritableBean;
import io.github.mmm.bean.property.BeanProperty;
import io.github.mmm.entity.bean.EntityBean;
import io.github.mmm.property.ReadableProperty;
import io.github.mmm.property.criteria.PropertyPathParser;
import io.github.mmm.scanner.CharStreamScanner;
import io.github.mmm.value.PropertyPath;
import io.github.mmm.value.ReadablePath;

/**
 * Implementation of {@link PropertyPathParser} that resolves {@link PropertyPath paths} directly against a given
 * {@link EntityBean} without any alias.
 *
 * @since 1.0.0
 */
class EntityPathParser implements PropertyPathParser {

  private final ReadableBean entity;

  EntityPathParser(ReadableBean entity) {

    super();
    this.entity = entity;
  }

  @Override
  public PropertyPath<?> parse(CharStreamScanner scanner, String segment) {

    return parsePath(scanner, this.entity, segment);
  }

  static ReadableProperty<?> resolvePath(ReadableBean bean, ReadablePath path) {

    return resolvePath(bean, path, false);
  }

  static ReadableProperty<?> resolvePath(ReadableBean bean, ReadablePath path, boolean withoutAlias) {

    ReadablePath parent = path.parentPath();
    if (parent == null) {
      if (withoutAlias) {
        return bean.getRequiredProperty(path.pathSegment());
      } else {
        return null;
      }
    } else {
      String segment = path.pathSegment();
      ReadableProperty<?> property = resolvePath(bean, parent, withoutAlias);
      if (property == null) {
        assert (!withoutAlias);
        return bean.getRequiredProperty(segment);
      } else {
        return traverseProperty(property, segment);
      }
    }
  }

  static ReadableProperty<?> parsePath(CharStreamScanner scanner, ReadableBean bean) {

    return parsePath(scanner, bean, null);
  }

  static ReadableProperty<?> parsePath(CharStreamScanner scanner, ReadableBean bean, String segment) {

    ReadableProperty<?> p = null;
    do {
      if (segment == null) {
        segment = PropertyPathParser.parseSegment(scanner);
      }
      if (p == null) {
        p = bean.getProperty(segment);
        if (p == null) {
          parsePathAlias(scanner, bean, bean, segment);
        }
      } else {
        p = traverseProperty(p, segment);
      }
      segment = null;
    } while (scanner.expectOne('.'));
    return p;
  }

  private static void parsePathAlias(CharStreamScanner scanner, ReadablePath path, ReadableBean bean, String segment) {

    if (path == null) {
      bean.getRequiredProperty(segment); // force property not found exception
    } else {
      ReadablePath parentPath = path.parentPath();
      if (parentPath != null) {
        parsePathAlias(scanner, parentPath, bean, segment);
        scanner.requireOne('.');
        segment = PropertyPathParser.parseSegment(scanner);
      }
      if (!segment.equals(path.pathSegment())) {
        throw new ObjectMismatchException(segment, path.pathSegment());
      }
    }
  }

  static ReadableProperty<?> traverseProperty(ReadableProperty<?> property, String segment) {

    WritableBean childBean = null;
    if (property instanceof BeanProperty) {
      childBean = ((BeanProperty<?>) property).get();
    }
    if (childBean == null) {
      throw new IllegalArgumentException("Cannot traverse from property " + property.path() + " to " + segment);
    }
    return childBean.getRequiredProperty(segment);
  }

}
