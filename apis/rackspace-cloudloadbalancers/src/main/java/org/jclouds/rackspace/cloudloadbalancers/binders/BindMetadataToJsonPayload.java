/**
 * Licensed to jclouds, Inc. (jclouds) under one or more
 * contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  jclouds licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.jclouds.rackspace.cloudloadbalancers.binders;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.jclouds.http.HttpRequest;
import org.jclouds.json.Json;
import org.jclouds.rest.Binder;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;

/**
 * Binds the metadata to the request as a JSON payload.
 * 
 * @author Everett Toews
 */
@Singleton
public class BindMetadataToJsonPayload implements Binder {

   protected final Json jsonBinder;

   @Inject
   public BindMetadataToJsonPayload(Json jsonBinder) {
      this.jsonBinder = checkNotNull(jsonBinder, "jsonBinder");
   }

   @SuppressWarnings("unchecked")
   @Override
   public <R extends HttpRequest> R bindToRequest(R request, Object input) {
      checkArgument(checkNotNull(input, "input") instanceof Map, "This binder is only valid for Map<String, String>");
      checkNotNull(request, "request");

      Map<String, String> metadata = (Map<String, String>) input;
      List<Map<String, String>> clbMetadata = Lists.newArrayList();
      
      for (String key: metadata.keySet()) {
         clbMetadata.add(ImmutableMap.<String, String> of(
               "key", key,
               "value", metadata.get(key)));
      }
      
      String json = jsonBinder.toJson(ImmutableMap.of("metadata", clbMetadata));
      request.setPayload(json);
      request.getPayload().getContentMetadata().setContentType("application/json");
      return request;
   }
}
