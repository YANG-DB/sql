/*
 * Copyright OpenSearch Contributors
 * SPDX-License-Identifier: Apache-2.0
 */

package org.opensearch.sql.ppl;

import java.io.IOException;
import org.opensearch.rest.RestRequest;

/** Run LikeQueryIT tests using http GET request */
public class LikeQueryWithGetIT extends LikeQueryIT {
  @Override
  public void init() throws IOException {
    super.init();
    this.method = RestRequest.Method.GET;
  }
}
