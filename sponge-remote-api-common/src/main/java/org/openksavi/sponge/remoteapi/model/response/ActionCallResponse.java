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

package org.openksavi.sponge.remoteapi.model.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import org.openksavi.sponge.remoteapi.model.response.ActionCallResponse.ActionCallResponseBody;

@ApiModel(value = "ActionCallResponse", description = "An action call response")
public class ActionCallResponse extends BodySpongeResponse<ActionCallResponseBody> {

    public ActionCallResponse(ActionCallResponseBody body) {
        super(body);
    }

    public ActionCallResponse() {
        this(new ActionCallResponseBody());
    }

    public ActionCallResponse(Object result) {
        this(new ActionCallResponseBody(result));
    }

    @Override
    public ActionCallResponseBody createBody() {
        return new ActionCallResponseBody();
    }

    @ApiModel(value = "ActionCallResponseBody", description = "An action call response body")
    public static class ActionCallResponseBody implements ResponseBody {

        private Object result;

        public ActionCallResponseBody() {
        }

        public ActionCallResponseBody(Object result) {
            this.result = result;
        }

        @ApiModelProperty(value = "The action result", required = true)
        public Object getResult() {
            return result;
        }

        public void setResult(Object result) {
            this.result = result;
        }
    }
}
