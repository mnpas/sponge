/*
 * Copyright 2016-2018 The Sponge authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.openksavi.sponge.remoteapi.model.request;

import io.swagger.annotations.ApiModel;

import org.openksavi.sponge.remoteapi.RemoteApiConstants;

@ApiModel(value = "GetKnowledgeBasesRequest", description = "A get knowledge bases request")
public class GetKnowledgeBasesRequest extends BaseRequest<BaseRequestParams> {

    public GetKnowledgeBasesRequest(BaseRequestParams body) {
        super(RemoteApiConstants.OPERATION_KNOWLEDGE_BASES, body);
    }

    public GetKnowledgeBasesRequest() {
        this(new BaseRequestParams());
    }

    @Override
    public BaseRequestParams createParams() {
        return new BaseRequestParams();
    }
}
