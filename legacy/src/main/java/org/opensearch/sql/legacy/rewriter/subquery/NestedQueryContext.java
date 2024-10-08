/*
 * Copyright OpenSearch Contributors
 * SPDX-License-Identifier: Apache-2.0
 */

package org.opensearch.sql.legacy.rewriter.subquery;

import com.alibaba.druid.sql.ast.statement.SQLExprTableSource;
import com.alibaba.druid.sql.ast.statement.SQLJoinTableSource;
import com.alibaba.druid.sql.ast.statement.SQLTableSource;
import com.google.common.base.Strings;
import java.util.HashMap;
import java.util.Map;

/**
 * {@link NestedQueryContext} build the context with Query to detected the specified table is nested
 * or not. Todo current implementation doesn't rely on the index mapping which should be added after
 * the semantics is builded.
 */
public class NestedQueryContext {
  private static final String SEPARATOR = ".";
  private static final String EMPTY = "";
  // <tableAlias, parentTableAlias>, if parentTable not exist, parentTableAlias = "";
  private final Map<String, String> aliasParents = new HashMap<>();

  /** Is the table refer to the nested field of the parent table. */
  public boolean isNested(SQLExprTableSource table) {
    String parent = parent(table);
    if (Strings.isNullOrEmpty(parent)) {
      return !Strings.isNullOrEmpty(aliasParents.get(alias(table)));
    } else {
      return aliasParents.containsKey(parent);
    }
  }

  /** add table to the context. */
  public void add(SQLTableSource table) {
    if (table instanceof SQLExprTableSource) {
      process((SQLExprTableSource) table);
    } else if (table instanceof SQLJoinTableSource) {
      add(((SQLJoinTableSource) table).getLeft());
      add(((SQLJoinTableSource) table).getRight());
    } else {
      throw new IllegalStateException("unsupported table source");
    }
  }

  private void process(SQLExprTableSource table) {
    String alias = alias(table);
    String parent = parent(table);
    if (!Strings.isNullOrEmpty(alias)) {
      aliasParents.putIfAbsent(alias, parent);
    }
  }

  /**
   * Extract the parent alias from the tableName. For example SELECT * FROM employee e, e.project as
   * p, For expr: employee, the parent alias is "". For expr: e.project, the parent alias is e.
   */
  private String parent(SQLExprTableSource table) {
    String tableName = table.getExpr().toString();
    int index = tableName.indexOf(SEPARATOR);
    return index == -1 ? EMPTY : tableName.substring(0, index);
  }

  private String alias(SQLExprTableSource table) {
    if (Strings.isNullOrEmpty(table.getAlias())) {
      return table.getExpr().toString();
    }
    return table.getAlias();
  }
}
