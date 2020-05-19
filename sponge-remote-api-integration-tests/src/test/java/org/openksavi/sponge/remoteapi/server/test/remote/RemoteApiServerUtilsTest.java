/*
 * Copyright 2016-2019 The Sponge authors.
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

package org.openksavi.sponge.remoteapi.server.test.remote;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import org.openksavi.sponge.remoteapi.model.RemoteActionMeta;
import org.openksavi.sponge.remoteapi.model.RemoteCategoryMeta;
import org.openksavi.sponge.remoteapi.model.RemoteKnowledgeBaseMeta;
import org.openksavi.sponge.remoteapi.server.util.RemoteApiServerUtils;

public class RemoteApiServerUtilsTest {

    private RemoteActionMeta createActionMeta(String actionName, Integer categorySeq, Integer kbSeq, String actionLabel) {
        RemoteActionMeta action = mock(RemoteActionMeta.class);
        when(action.getCategory()).thenReturn(new RemoteCategoryMeta("c", "c", "c", new HashMap<>(), categorySeq));
        when(action.getKnowledgeBase()).thenReturn(new RemoteKnowledgeBaseMeta("k", "k", "k", null, kbSeq));
        when(action.getLabel()).thenReturn(actionLabel);
        when(action.getName()).thenReturn(actionName);

        return action;
    }

    private List<String> sortActions(RemoteActionMeta... actions) {
        List<RemoteActionMeta> actionList = Arrays.asList(actions);
        actionList.sort(RemoteApiServerUtils.createActionsOrderComparator());

        return actionList.stream().map(meta -> meta.getName()).collect(Collectors.toList());
    }

    private void assertSort(List<String> expected, RemoteActionMeta action1, RemoteActionMeta action2) {
        assertEquals(expected, sortActions(action1, action2));
        assertEquals(expected, sortActions(action2, action1));
    }

    @Test
    public void testActionsOrderComparator() {
        assertSort(Arrays.asList("A1", "A2"), createActionMeta("A1", 0, 0, "A1"), createActionMeta("A2", 0, 0, "A2"));
        assertSort(Arrays.asList("A2", "A1"), createActionMeta("A1", 1, 0, "A1"), createActionMeta("A2", 0, 0, "A2"));
        assertSort(Arrays.asList("A2", "A1"), createActionMeta("A1", 0, 1, "A1"), createActionMeta("A2", 0, 0, "A2"));
        assertSort(Arrays.asList("A2", "A1"), createActionMeta("A1", null, 0, "A1"), createActionMeta("A2", 0, 0, "A2"));
        assertSort(Arrays.asList("A1", "A2"), createActionMeta("A1", null, 0, "A1"), createActionMeta("A2", null, 0, "A2"));
        assertSort(Arrays.asList("A2", "A1"), createActionMeta("A1", null, null, "A1"), createActionMeta("A2", null, 0, "A2"));
        assertSort(Arrays.asList("A1", "A2"), createActionMeta("A1", null, null, "A1"), createActionMeta("A2", null, null, "A2"));
    }
}
