/*
 * Copyright 2016-2020 The Sponge authors.
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

package org.openksavi.sponge.remoteapi.server.test.remote.delegate;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import org.openksavi.sponge.remoteapi.model.request.BodySpongeRequest;
import org.openksavi.sponge.remoteapi.model.request.RequestBody;

@ApiModel(value = "UpperCaseRequest", description = "An UpperCase request")
public class UpperCaseRequest extends BodySpongeRequest<UpperCaseRequest.UpperCaseRequestBody> {

    public UpperCaseRequest(String text) {
        super(new UpperCaseRequestBody());

        getBody().setText(text);
    }

    public UpperCaseRequest() {
        this(null);
    }

    @Override
    public UpperCaseRequest.UpperCaseRequestBody createBody() {
        return new UpperCaseRequestBody();
    }

    @ApiModel(value = "UpperCaseRequestBody", description = "An UpperCaseRequestBody request body")
    public static class UpperCaseRequestBody implements RequestBody {

        private String text;

        @ApiModelProperty(value = "Text to upper case", required = true)
        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }
}