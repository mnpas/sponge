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

import org.openksavi.sponge.remoteapi.model.response.SendEventResponse.SendEventResponseBody;

@ApiModel(value = "SendEventResponse", description = "A send event response")
public class SendEventResponse extends BodySpongeResponse<SendEventResponseBody> {

    public SendEventResponse(SendEventResponseBody body) {
        super(body);
    }

    public SendEventResponse() {
        this(new SendEventResponseBody());
    }

    public SendEventResponse(String eventId) {
        this(new SendEventResponseBody(eventId));
    }

    @Override
    public SendEventResponseBody createBody() {
        return new SendEventResponseBody();
    }

    @ApiModel(value = "SendEventResponseBody", description = "A send event response body")
    public static class SendEventResponseBody implements ResponseBody {

        private String eventId;

        public SendEventResponseBody() {
        }

        public SendEventResponseBody(String eventId) {
            this.eventId = eventId;
        }

        @ApiModelProperty(value = "The event id", required = true)
        public String getEventId() {
            return eventId;
        }

        public void setEventId(String eventId) {
            this.eventId = eventId;
        }
    }
}
