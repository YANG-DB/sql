/*
 * Copyright OpenSearch Contributors
 * SPDX-License-Identifier: Apache-2.0
 */

package org.opensearch.sql.spark.metrics;

public enum EmrMetrics {
  EMR_CANCEL_JOB_REQUEST_FAILURE_COUNT,
  EMR_GET_JOB_RESULT_FAILURE_COUNT,
  EMR_START_JOB_REQUEST_FAILURE_COUNT,
  EMR_INTERACTIVE_QUERY_JOBS_CREATION_COUNT,
  EMR_STREAMING_QUERY_JOBS_CREATION_COUNT,
  EMR_BATCH_QUERY_JOBS_CREATION_COUNT;
}
