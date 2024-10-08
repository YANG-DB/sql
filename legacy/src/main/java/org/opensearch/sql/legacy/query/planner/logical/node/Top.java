/*
 * Copyright OpenSearch Contributors
 * SPDX-License-Identifier: Apache-2.0
 */

package org.opensearch.sql.legacy.query.planner.logical.node;

import java.util.Map;
import org.opensearch.sql.legacy.query.planner.core.PlanNode;
import org.opensearch.sql.legacy.query.planner.logical.LogicalOperator;
import org.opensearch.sql.legacy.query.planner.physical.PhysicalOperator;
import org.opensearch.sql.legacy.query.planner.physical.Row;
import org.opensearch.sql.legacy.query.planner.physical.estimation.Cost;

/**
 * Operator that keep only Top N rows and stop iteration.
 *
 * @param <T> data row object
 */
public class Top<T> implements LogicalOperator, PhysicalOperator<T> {

  private final PlanNode next;

  /** Number of rows to return in total */
  private int count;

  @SuppressWarnings("unchecked")
  public Top(PlanNode next, int count) {
    this.next = next;
    this.count = count;
  }

  @Override
  public PlanNode[] children() {
    return new PlanNode[] {next};
  }

  @SuppressWarnings("unchecked")
  @Override
  public boolean hasNext() {
    return count > 0 && ((PhysicalOperator<T>) next).hasNext();
  }

  @SuppressWarnings("unchecked")
  @Override
  public Row<T> next() {
    count--;
    return ((PhysicalOperator<T>) next).next();
  }

  @Override
  public <U> PhysicalOperator[] toPhysical(Map<LogicalOperator, PhysicalOperator<U>> optimalOps) {
    if (!(next instanceof LogicalOperator)) {
      throw new IllegalStateException(
          "Only logical operator can perform this toPhysical() operation");
    }
    return new PhysicalOperator[] {new Top<>(optimalOps.get(next), count)};
  }

  @Override
  public Cost estimate() {
    return new Cost();
  }

  @Override
  public String toString() {
    return "Top [ " + "count=" + count + " ]";
  }
}
