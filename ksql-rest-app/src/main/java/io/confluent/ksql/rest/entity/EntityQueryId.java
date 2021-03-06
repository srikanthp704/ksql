/*
 * Copyright 2018 Confluent Inc.
 *
 * Licensed under the Confluent Community License; you may not use this file
 * except in compliance with the License.  You may obtain a copy of the License at
 *
 * http://www.confluent.io/confluent-community-license
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OF ANY KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations under the License.
 */

package io.confluent.ksql.rest.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonValue;
import io.confluent.ksql.query.QueryId;
import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public class EntityQueryId {
  private final String id;

  public EntityQueryId(final QueryId queryId) {
    this.id = queryId.getId();
  }

  @JsonCreator
  public EntityQueryId(final String id) {
    this.id = id;
  }

  @JsonValue
  public String getId() {
    return id;
  }

  @Override
  public boolean equals(final Object o) {
    return o instanceof EntityQueryId
        && Objects.equals(((EntityQueryId) o).id, id);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }

  @Override
  public String toString() {
    return id;
  }
}
